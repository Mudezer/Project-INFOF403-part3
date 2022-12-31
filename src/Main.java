import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
            System.out.println("Usage:  java -jar part2.jar -wt file.tex file.fs\n"
                    + "or\tjava "+Main.class.getSimpleName()+" -wt file.tex  file.fs");
            System.exit(0);
        }

        FileReader source;
        FileWriter output;
        String latex = null;
        String fortress = null;


        /*
            checks if there are sufficient arguments and which one are missing
         */
        for(int i = 0; i< args.length;i++){
            if(args[i].equals("-wt")){
                if(i+1 < args.length && i+2 < args.length){
                    latex= args[i+1];
                    fortress = args[i +2];
                }else if(i + 1 == args.length){
                    System.err.println("source file expected, please enter one in fs or co format");
                    System.exit(0);
                }else if(i+1 > args.length){
                    System.err.println("no enough argument, source and output files expected");
                    System.exit(0);
                }


            }else
                fortress = args[i];
        }



        // Open the file given in argument
        source = new FileReader(fortress);
        final Parser parser = new Parser(source);

        //launch the parsing
        ParseTree parseTree = parser.Program();
//        parser.printUsedRules();
        /*
            if latex output file is given as argument then write in it
            if not, then don't
            !!!!!!! LATEX FROM ORIGINAL PARSE TREE
    //      */
    //    if(latex != null){
    //        output = new FileWriter(latex);
    //        output.write(parseTree.toLaTeX());
    //        output.close();
    //    }

        /**
         * AST PRE TREE GENERATION
         * LATEX FROM AST PRE TREE
         */
        AbstractSyntaxTree ast = ASTGeneration.ProgramNode(parseTree);
        if(latex!=null){
            output = new FileWriter(latex);
            output.write(ast.toLaTeX());
            output.close();
        }



    }


}
