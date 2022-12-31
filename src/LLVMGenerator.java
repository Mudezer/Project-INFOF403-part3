import java.util.ArrayList;
import java.util.List;

public class LLVMGenerator {
    
    private static String basicFunctions = 
            "@.strP = private unnamed_addr constant [4 x i8] c\"%d\\0A\\00\", align 1\n" +
            "\n" +
            "; Function Attrs: nounwind uwtable\n" +
            "define void @println(i32 %x) #0 {\n" +
            "\t%1 = alloca i32, align 4\n" +
            "\tstore i32 %x, i32* %1, align 4\n" +
            "\t%2 = load i32, i32* %1, align 4\n" +
            "\t%3 = call i32 (i8*, ...) @printf(i8* getelementptr inbounds ([4 x i8], [4 x i8]* @.strP, i32 0, i32 0), i32 %2)\n" +
            "\tret void\n" +
            "}\n" +
            "\n" +
            "declare i32 @printf(i8*, ...) #1\n" +
            "\n" +
            "declare i32 @getchar()\n" +
            "\n" +
            "define i32 @readInt() {\n" +
            "\tentry:\t; create variables\n" +
            "\t\t%res   = alloca i32\n" +
            "\t\t%digit = alloca i32\n" +
            "\t\tstore i32 0, i32* %res\n" +
            "\t\tbr label %read\n\n" +
            "\tread:\t; read a digit\n" +
            "\t\t%0 = call i32 @getchar()\n" +
            "\t\t%1 = sub i32 %0, 48\n" +
            "\t\tstore i32 %1, i32* %digit\n" +
            "\t\t%2 = icmp ne i32 %0, 10\t; is the char entered '\\n'?\n" +
            "\t\tbr i1 %2, label %check, label %exit\n\n" +
            "\tcheck:\t; is the char entered a number?\n" +
            "\t\t%3 = icmp sle i32 %1, 9\n" +
            "\t\t%4 = icmp sge i32 %1, 0\n" +
            "\t\t%5 = and i1 %3, %4\n" +
            "\t\tbr i1 %5, label %save, label %exit\n\n" +
            "\tsave:\t; res<-res*10+digit\n" +
            "\t\t%6 = load i32, i32* %res\n" +
            "\t\t%7 = load i32, i32* %digit\n" +
            "\t\t%8 = mul i32 %6, 10\n" +
            "\t\t%9 = add i32 %8, %7\n" +
            "\t\tstore i32 %9, i32* %res\n" +
            "\t\tbr label %read\n\n" +
            "\texit:\t; return res\n" +
            "\t\t%10 = load i32, i32* %res\n" +
            "\t\tret i32 %10\n" +
            "}\n\n";


    private List<String> variables = new ArrayList<>();
    private List<SubFunctions> subFunctions = new ArrayList<>();
    private SubFunctions currentSubFunctions = null;

    private Integer whileNumb = 0;
    private Integer ifNumb = 0;
    private Integer nonPersisVariableNumb = 0;
    


    public String GenerateLLVMCode(AbstractSyntaxTree ast){
        reset();
        StringBuilder code = new StringBuilder();
        code.append(basicFunctions);
        code.append("define i32 @main() {\n");
        ProcessAST(ast);
        for(String var : this.variables){
            code.append("\t%").append(var).append(" = alloca i32\n");
        }
        code.append("\t label %entry\n\n");
        for(SubFunctions sub : this.subFunctions){
            code.append(sub.toString()).append("\n");
        }
        code.append("}\n");
        return code.toString();
    }

    private void reset(){
        this.variables = new ArrayList<>();
        this.subFunctions = new ArrayList<>();
        this.currentSubFunctions = null;
        this.whileNumb = 0;
        this.ifNumb = 0;
        this.nonPersisVariableNumb = 0;
    }

    private SubFunctions newSubFunctions(String label){
        SubFunctions sub = new SubFunctions(label);
        subFunctions.add(sub);
        return sub;
    }

    private String newVariable(){
        this.nonPersisVariableNumb++;
        return this.nonPersisVariableNumb.toString();
    }

    private String newWhile(){
        this.whileNumb++;
        return this.whileNumb.toString();
    }

    private String newIf(){
        this.ifNumb++;
        return this.ifNumb.toString();
    }



    private void ProcessAST(AbstractSyntaxTree ast) { //ast = (program,child)
        this.currentSubFunctions = newSubFunctions("entry");
        ProcessCodeAST(ast.getChildren().get(0));
        this.currentSubFunctions.addContent("ret i32 0");
    }

    private void ProcessCodeAST(AbstractSyntaxTree ast) { //ast = (code,child)
        for(AbstractSyntaxTree child: ast.getChildren()){
            Symbol childSymbol = child.getLabel();
            switch(childSymbol.getValue().toString()){
                case "Assign":
                    ProcessAssignAST(child);
                    break;
                case "While":
                    ProcessWhileNode(child);
                    break;
                case "If":
                    ProcessIfNode(child);
                    break;
                case "Read":
                    ProcessReadNode(child);
                    break;
                case "Print":
                    ProcessPrintNode(child);
                    break;
                default:
                    System.err.println("Error: Unexpected symbol in code node");
                    System.exit(1);
            }
        }

    }

    private void ProcessPrintNode(AbstractSyntaxTree ast) {
        Symbol childSymbol = ast.getChildren().get(0).getLabel();
        String varName = childSymbol.getValue().toString(); 
        // will I keep this?
        // if(!this.variables.contains(varName)){
        //     System.err.println("Error at line " + childSymbol.getLine() + " column" + childSymbol.getColumn() + " = \n"+
        //     "\t variable\"" + varName + "\" used before assignment");
        //     System.exit(1);
        // }

        String nonPersisVariable = newVariable();
        this.currentSubFunctions.addContent("%" + nonPersisVariable + " = load i32, i32* %" + varName);
        this.currentSubFunctions.addContent("call void @printInt(i32 %" + nonPersisVariable + ")");
    }

    private void ProcessReadNode(AbstractSyntaxTree ast) {
        String varName = ast.getChildren().get(0).getLabel().getValue().toString();
        if(!this.variables.contains(varName)){
            this.variables.add(varName);
        }

        String nonPersisVariable = newVariable();
        this.currentSubFunctions.addContent("%" + nonPersisVariable + " = call i32 @readInt()");
        this.currentSubFunctions.addContent("store i32 %" + nonPersisVariable + ", i32* %" + varName);
    }

    private void ProcessIfNode(AbstractSyntaxTree ast) {
        String ifNumber = newIf();
        String trueLabel = "if_true_" + ifNumber;
        String falseLabel = "if_false_" + ifNumber;
        String endLabel = "if_end_" + ifNumber;

        ProcessConditionAST(ast.getChildren().get(0), trueLabel, falseLabel);

        this.currentSubFunctions = newSubFunctions(trueLabel);
        ProcessCodeAST(ast.getChildren().get(1));
        this.currentSubFunctions.addContent("br label %" + endLabel);

        this.currentSubFunctions = newSubFunctions(falseLabel);
        if(ast.getChildren().size() == 3){
            ProcessCodeAST(ast.getChildren().get(2));
        }
        this.currentSubFunctions.addContent("br label %" + endLabel);

        this.currentSubFunctions = newSubFunctions(endLabel);
    }

    private void ProcessWhileNode(AbstractSyntaxTree ast) {
        String whileNumber = newWhile();
        String condition = "while_cond_" + whileNumber;
        String loop = "while_loop_" + whileNumber;
        String end = "while_end_" + whileNumber;

        this.currentSubFunctions.addContent("br label %" + condition);

        this.currentSubFunctions = newSubFunctions(condition);
        ProcessConditionAST(ast.getChildren().get(0), loop, end);

        this.currentSubFunctions = newSubFunctions(loop);
        ProcessCodeAST(ast.getChildren().get(1));

        this.currentSubFunctions.addContent("br label %" + condition);

        this.currentSubFunctions = newSubFunctions(end);
    }

    

    private void ProcessAssignAST(AbstractSyntaxTree ast) {
        String varName = ast.getChildren().get(0).getLabel().getValue().toString();
        if(!this.variables.contains(varName)){
            this.variables.add(varName);
        }
        String expression = ProcessExpressionAST(ast.getChildren().get(1));
        this.currentSubFunctions.addContent("store i32 %" + expression + ", i32* %" + varName);

    }

    private void ProcessConditionAST(AbstractSyntaxTree ast, String loop, String end) {
        String left = ProcessExpressionAST(ast.getChildren().get(0));
        String right = ProcessExpressionAST(ast.getChildren().get(1));

        String op = "eq";

        switch(ast.getLabel().getType()){
            case EQUAL:
                op = "eq";
                break;
            case GREATER:
                op = "sgt";
                break;
            case SMALLER:
                op = "slt";
                break;
            default:
                System.err.println("Error: Unexpected symbol in condition node");
                System.exit(1);
        }

        String nonPersisVariable = newVariable();
        this.currentSubFunctions.addContent("%" + nonPersisVariable + " = icmp " + op + " i32 %" + left + ", %" + right);
        this.currentSubFunctions.addContent("br i1 %" + nonPersisVariable + ", label %" + loop + ", label %" + end);
    }

    private String ProcessExpressionAST(AbstractSyntaxTree ast) {
        return NodeProccessing(ast);
    }

    private String NodeProccessing(AbstractSyntaxTree ast) {
        if(ast.getChildren().isEmpty()){
            return ProcessLeaf(ast);
        }else{
            String left = NodeProccessing(ast.getChildren().get(0));
            String right = NodeProccessing(ast.getChildren().get(1));
            String op = "add";
            String result = newVariable();

            switch(ast.getLabel().getType()){
                case PLUS:
                    op = "add";
                    break;
                case MINUS:
                    op = "sub";
                    break;
                case TIMES:
                    op = "mul";
                    break;
                case DIVIDE:
                    op = "sdiv";
                    break;
                // default:
                //     System.err.println("Error: Unexpected symbol in expression node");
                //     System.exit(1);
            }
            this.currentSubFunctions.addContent("%" + result + " = " + op + " i32 %" + left + ", %" + right);
            return result;
        }
    }

    private String ProcessLeaf(AbstractSyntaxTree ast) {
        String variable = newVariable();
        switch(ast.getLabel().getType()){
            case NUMBER:
                this.currentSubFunctions.addContent("%" + variable + " = add i32 0, " + ast.getLabel().getValue().toString());
                return variable;
            case VARNAME:
                String varName = ast.getLabel().getValue().toString();
                if(!this.variables.contains(varName)){
                    System.err.println("Error at line " + ast.getLabel().getLine() + " column" + ast.getLabel().getColumn() + " = \n"+
                    "\t variable\"" + varName + "\" used before assignment");
                    System.exit(1);
                }
                this.currentSubFunctions.addContent("%" + variable + " = load i32, i32* %" + varName);
                break;
        }
        return variable;
    }
    
}
