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
    

    public static String GenerateLLVMCode(AbstractSyntaxTree ast){

        StringBuilder code = new StringBuilder();

        code.append(basicFunctions);

        code.append("define i32 @main() {\n");

        




        return code.toString();
    }
    
}
