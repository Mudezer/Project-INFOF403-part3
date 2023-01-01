import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 *
 * Project Part 2: Parser
 *
 * @author Bermudez Loïc, Nabbout Fadi
 * Lexical Analyzer based on solution
 *
 */

public class Main{
    /**
     *
     * The scanner
     *
     * @param args  The argument(s) given to the program
     * @throws IOException java.io.IOException if an I/O-Error occurs
     * @throws FileNotFoundException java.io.FileNotFoundException if the specified file does not exist
     *
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, SecurityException{
        // Display the usage when the number of arguments is wrong (should be at least one)
        if(args.length < 1 ){
            System.out.println("Usage:  java -jar part3.jar file.fs\n"
                    + "or\tjava "+Main.class.getSimpleName()+" file.fs");
            System.exit(0);
        }

        FileReader source;
        String fortress = null;

        if(args.length == 1){
            fortress = args[0];
        }
        // Open the file given in argument
        source = new FileReader(fortress);
        final Parser parser = new Parser(source);

        //launch the parsing
        ParseTree parseTree = parser.Program();

        //launch the AST generation based on the parse tree
        AbstractSyntaxTree ast = ASTGeneration.ProgramNode(parseTree);

        //launch the LLVM code generation based on the AST
        LLVMGenerator llvm = new LLVMGenerator();
        String code = llvm.GenerateLLVMCode(ast);
        System.out.println(code);


    }


}
