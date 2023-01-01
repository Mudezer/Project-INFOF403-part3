import java.util.ArrayList;

public class ASTGeneration {

    /**
     * This method generates the abstract syntax tree of the program from a parse tree of a program
     * [1] Program           → BEGIN [ProgName] <Code> END
     * @param parseTree : the parse tree whose root is program
     * @return the abstract syntax tree of the program, representing the program
     */
    public static AbstractSyntaxTree ProgramNode(ParseTree parseTree){
        Symbol label = parseTree.getLabel(); //Program being the root of the whole tree
        ArrayList<AbstractSyntaxTree> chldn = new ArrayList<>();
        chldn.add(CodeNode(parseTree.getChildren().get(2)));
        return new AbstractSyntaxTree(label, chldn);
    }

    /**
     * This method generates the abstract syntax tree of the code from a parse tree of the code
     * [2] <Code>              → <Instruction> <CodeF>
     * [3]                     → ε
     * @param parseTree : the parse tree whose root is code 
     * @return the abstract syntax tree of the code, representing the code
     */
    private static AbstractSyntaxTree CodeNode(ParseTree parseTree) {
        Symbol label = parseTree.getLabel(); //Code
        ArrayList<AbstractSyntaxTree> instructions = new ArrayList<>(); //instructions
        while(parseTree != null){
            //skip the Instruction node and get the actual instruction directly to the code node (code, (Assign, If, While, Print, Read)))
            ParseTree child = parseTree.getChildren().get(0).getChildren().get(0);  
            switch(child.getLabel().getValue().toString()){
                case "Assign": instructions.add(AssignNode(child)); break; //sends the Assign variable : Assign -> [VarName] := ExprArith
                case "If": instructions.add(IfNode(child)); break; //sends the If variable : If -> 
                case "While": instructions.add(WhileNode(child)); break;
                case "Print": instructions.add(PrintNode(child)); break;
                case "Read": instructions.add(ReadNode(child)); break;
            }
            parseTree = CodeF(parseTree.getChildren().get(1)); //Code -> Instruction(0) CodeF(1)
        }

        
        return new AbstractSyntaxTree(label, instructions);
    }

    /**
     * [4] CodeF               → , Code
     * [5]                     → ε
     * This method checks if the parse tree of Code F or not, if it isn't, it returns the parse tree of Code
     * @param parseTree : the parse tree whose root is CodeF
     * @return the parse tree of Code if the parse tree of CodeF is not null, otherwise it returns the parse tree of CodeF
     */
    private static ParseTree CodeF(ParseTree parseTree) { //parseTree = CodeF
        if(parseTree == null)
            return null;
        else
            return parseTree.getChildren().get(1); //CodeF -> , Code(1)
    }

    /**
     * 
     * [11] Assign           → [VarName] := ExprArith
     * This method generates the abstract syntax tree of the assign from a parse tree of the assign instruction
     * @param parseTree : the parse tree whose root is Assign
     * @return the abstract syntax tree of the assign, representing the assign instruction
     */
    private static AbstractSyntaxTree AssignNode(ParseTree parseTree) { //parseTree = (Assign,(VarName, :=, ExprArith)
        Symbol label = parseTree.getLabel(); //Assign
        ArrayList<AbstractSyntaxTree> chldn = new ArrayList<>();
        chldn.add(new AbstractSyntaxTree(parseTree.getChildren().get(0).getLabel())); //VarName
        chldn.add(ExprArithProcessing(parseTree.getChildren().get(2))); //ExprArith
        return new AbstractSyntaxTree(label, chldn);
    }

    /**
     * This method generate the abstract syntax binary tree of an arithmetic expression 
     * It handles any addition and substraction operations
     * Process the ExprArith and the ExprArithF at the same time 
     * @param parseTree : the parse tree whose root is ExprArith
     * @return  the abstract syntax tree of the ExprArith, representing the ExprArith
     */
    private static AbstractSyntaxTree ExprArithProcessing(ParseTree parseTree) { // exprArith -> Prod ExprArithF
        AbstractSyntaxTree currentNode;
        // je donne prod à la fonction traitant les variables prod
        AbstractSyntaxTree lastNode = ProdProcessing(parseTree.getChildren().get(0)); // <Prod>
        parseTree = parseTree.getChildren().get(1); // <ExprArithF>

        while(parseTree != null){ // while loop processing the ExprArithF variable
            currentNode = new AbstractSyntaxTree(parseTree.getChildren().get(0).getLabel()); // + or -
            currentNode.addChild(lastNode);
            currentNode.addChild(ProdProcessing(parseTree.getChildren().get(1))); // <Prod>

            lastNode = currentNode;
            parseTree = parseTree.getChildren().get(2); // <ExprArithF>
        }

        return lastNode;
    }

    /**
     * This method generate the abstract syntax binary tree of a product expression
     * It handles any multiplication and division operations
     * Process the Prod and the ProdF at the same time 
     * @param parseTree : the parse tree whose root is Prod
     * @return the abstract syntax tree of the Prod, representing the Prod
     */
    private static AbstractSyntaxTree ProdProcessing(ParseTree parseTree) { // Prod -> Atom ProdF
        AbstractSyntaxTree currentNode;
        AbstractSyntaxTree lastNode = AtomProcessing(parseTree.getChildren().get(0)); // <Atom>
        parseTree = parseTree.getChildren().get(1); // <ProdF>
        // System.out.println("line 112 " + parseTree.getLabel().getValue().toString());
         //while loop processing the ProdF variable
        while(parseTree != null){
            // System.out.println("line 115 " + parseTree.getChildren().get(0).getLabel().getValue().toString());
            currentNode = new AbstractSyntaxTree(parseTree.getChildren().get(0).getLabel()); // * or /
            currentNode.addChild(lastNode);
            currentNode.addChild(AtomProcessing(parseTree.getChildren().get(1))); // <Atom>

            lastNode = currentNode;
            parseTree = parseTree.getChildren().get(2); // <ProdF>
            // System.out.println("line 122 " + parseTree.getLabel().getValue().toString());

        }

        return lastNode;
    }

    /**
     * This method generate the abstract syntax tree of an atom
     * It handles any negation, parenthesis, variable and number (constants)
     * @param parseTree : the parse tree whose root is Atom
     * @return the abstract syntax tree of the Atom, representing the Atom
     */
    private static AbstractSyntaxTree AtomProcessing(ParseTree parseTree) { // Atom -> - Atom | ( ExprArith ) | VarName | Number
        // System.out.println(parseTree.getChildren().get(0).getLabel().getValue().toString());
        switch(parseTree.getChildren().get(0).getLabel().getType()){
            case MINUS:
                Symbol neg = new Symbol(LexicalUnit.NUMBER, "-1");
                Symbol op = new Symbol(LexicalUnit.TIMES, "*");
                AbstractSyntaxTree negNode = new AbstractSyntaxTree(op);
                negNode.addChild(AtomProcessing(parseTree.getChildren().get(1)));
                negNode.addChild(new AbstractSyntaxTree(neg));
                return negNode;
                // break;
            case NUMBER:
            case VARNAME:
                return new AbstractSyntaxTree(parseTree.getChildren().get(0).getLabel());
                // break;
            case LPAREN:
                return ExprArithProcessing(parseTree.getChildren().get(1));
                // break;
            default:
                break;
        }
        return null;
    }

    /**
     * This method generates the abstract syntax tree of the If from a parse tree of the If instruction
     *  [12] While           → while [Condition] do [Code] end
     * @param parseTree : the parse tree whose root is While
     * @return the abstract syntax tree of the While, representing the While instruction
     */
    private static AbstractSyntaxTree IfNode(ParseTree parseTree) { // IF
        Symbol label = parseTree.getLabel(); //If symbol
        ArrayList<AbstractSyntaxTree> chldn = new ArrayList<>();
        chldn.add(ConditionNode(parseTree.getChildren().get(2))); //Condition
        chldn.add(CodeNode(parseTree.getChildren().get(5))); // Code if true
        parseTree = parseTree.getChildren().get(6); //IfSeq
        if(parseTree.getChildren().get(0).getLabel().getType() == LexicalUnit.ELSE){
            chldn.add(CodeNode(parseTree.getChildren().get(1)));
        }else{
            chldn.add(null);
        }
        return new AbstractSyntaxTree(label, chldn);
    }

    /**
     * This method generates the abstract syntax tree of the While from a parse tree of the While instruction
     * It handles the different conditions and the code to execute if the condition is validated or not
     *  [16] <Cond>             → <ExprArith> <Comp>
     *  [17] <Comp>             → = <ExprArith>
     *  [18]                    → > <ExprArith>
     *  [19]                    → < <ExprArith>
     * 
     * @param parseTree : the parse tree whose root is While
     * @return the abstract syntax tree of the While, representing the While instruction
     */
    private static AbstractSyntaxTree ConditionNode(ParseTree parseTree) { // cond
        ArrayList<AbstractSyntaxTree> chldn = new ArrayList<>();
        chldn.add(ExprArithProcessing(parseTree.getChildren().get(0))); // ExprArith
        parseTree = parseTree.getChildren().get(1); // Comp
        Symbol compOp = parseTree.getChildren().get(0).getLabel();
        chldn.add(ExprArithProcessing(parseTree.getChildren().get(1))); // ExprArith
        return new AbstractSyntaxTree(compOp, chldn);
    }


    /**
     * This method generates the abstract syntax tree of the While from a parse tree of the While instruction
     * It handles the different conditions and the code to execute if the condition is validated or not
     * @param parseTree : the parse tree whose root is While
     * @return the abstract syntax tree of the While, representing the While instruction
     */
    private static AbstractSyntaxTree WhileNode(ParseTree parseTree) { //while variable
        Symbol label = parseTree.getLabel(); //While symbol
        ArrayList<AbstractSyntaxTree> chldn = new ArrayList<>();
        chldn.add(ConditionNode(parseTree.getChildren().get(2))); //Condition
        chldn.add(CodeNode(parseTree.getChildren().get(5))); // Code to execute while true
        return new AbstractSyntaxTree(label, chldn);
    }

    /**
     * This method generates the abstract syntax tree of the Print from a parse tree of the Print instruction
     * It handles the different conditions and the code to execute if the condition is validated or not
     * @param parseTree : the parse tree whose root is While
     * @return the abstract syntax tree of the While, representing the While instruction
     */
    private static AbstractSyntaxTree PrintNode(ParseTree parseTree) { //print variable
        Symbol label = parseTree.getLabel(); //Print symbol
        ArrayList<AbstractSyntaxTree> chldn = new ArrayList<>();
        AbstractSyntaxTree variable = new AbstractSyntaxTree(parseTree.getChildren().get(2).getLabel());
        chldn.add(variable);
        return new AbstractSyntaxTree(label, chldn);
    }


    /**
     * This method generates the abstract syntax tree of the Read from a parse tree of the Read instruction
     *  [13] Read             → read [VarName]
     * @param parseTree : the parse tree whose root is While
     * @return  the abstract syntax tree of the While, representing the While instruction
     */
    private static AbstractSyntaxTree ReadNode(ParseTree parseTree) { //read variable
        Symbol label = parseTree.getLabel(); //Read symbol
        ArrayList<AbstractSyntaxTree> chldn = new ArrayList<>();
        AbstractSyntaxTree variable = new AbstractSyntaxTree(parseTree.getChildren().get(2).getLabel());
        chldn.add(variable);
        return new AbstractSyntaxTree(label, chldn);
    }

    
}
