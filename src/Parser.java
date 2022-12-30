import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {

    private Symbol lookAhead; //look ahead token creating the predictive parsing
    //here we store the look ahead, we need it in case it goes to far, if to far => can't go backward
    private Symbol actualToken;
    private LexicalUnit lookAheadType; //lexical unit of the look ahead for the switch/cases
    final LexicalAnalyzer lexer; //lexical analyzer implemented in first part of project
    private ArrayList<Integer> usedRules = new ArrayList<>(); //list storing the rules leading to the final derivation tree

    public Parser(FileReader source){ lexer = new LexicalAnalyzer(source);}

    /**
     * print the sequence of rules used to parse and make the derivation tree
     * of the input files
     */
    public void printUsedRules(){
        for(Integer integer: usedRules){
            System.out.print(integer + " ");
        }
        System.out.print("\n");
    }

    /**
     * function corresponding to the non-terminal Program
     * [1] Program           → BEGIN [ProgName] Code END
     * @return a Parent node which is the non terminal Program and the corresponding children
     */
    public ParseTree Program(){
        ArrayList<ParseTree> chldn = new ArrayList<>();
        getNextToken();
        switch (lookAheadType){
            case BEGIN:
                usedRules.add(1);
                chldn.add(match(LexicalUnit.BEGIN));
                getNextToken();
                chldn.add(match(LexicalUnit.PROGNAME));
                chldn.add(Code());
                getNextToken();
                chldn.add(match(LexicalUnit.END));
                break;
            default:
                syntaxError(lookAhead);
                break;

        }
        return new ParseTree(new Symbol("Program"), chldn);
    }

    /**
     * function corresponding to the non terminal Code
     * [2] Code                → Instruction CodeF
     * [3]                     → ε
     * @return a Parent node which is the non terminal Code and the corresponding children
     */
    private ParseTree Code() {
        ArrayList<ParseTree> chldn = new ArrayList<>();
        getNextToken();
        switch (lookAheadType){
            case END:
            case ELSE:
                usedRules.add(3);
                // chldn.add(new ParseTree(new Symbol("$\\varepsilon$")));
                // return new ParseTree(new Symbol("Code"), chldn);
                return null;
            default:
        }
        usedRules.add(2);
        chldn.add(Instruction()); chldn.add(CodeF());

        return new ParseTree(new Symbol("Code"), chldn);
    }

    /**
     * function corresponding to the non terminal CodeF
     * [4] CodeF               → , Code
     * [5]                     → ε
     * @return a parent node which is the non terminal CodeF and the corresponding children
     */
    private ParseTree CodeF() {
        ArrayList<ParseTree> chldn = new ArrayList<>();
        getNextToken();
        switch (lookAheadType){
            case END:
            case ELSE:
                usedRules.add(5);
                // chldn.add(new ParseTree(new Symbol("$\\varepsilon$")));
                // return new ParseTree(new Symbol("CodeF"), chldn);
                return null;
            case COMMA:
                usedRules.add(4);
                chldn.add(match(LexicalUnit.COMMA));
                chldn.add(Code());
                break;
            default:
                syntaxError(lookAhead);
                break;
        }
        return new ParseTree(new Symbol("CodeF"), chldn);
    }


    /**
     * function corresponding to the non terminal Instruction
     * [6] Instruction         → Assign
     * [7]                     → If
     * [8]                     → While
     * [9]                     → Print
     * [10]                    → Read
     * @return a parent node which is the non terminal Instruction and the corresponding children
     */
    private ParseTree Instruction() {
        ArrayList<ParseTree> chldn = new ArrayList<>();
        getNextToken();
        switch(lookAheadType){
            case VARNAME:
                usedRules.add(6);
                chldn.add(Assign());
                break;
            case IF:
                usedRules.add(7);
                chldn.add(If());
                break;
            case WHILE:
                usedRules.add(8);
                chldn.add(While());
                break;
            case PRINT:
                usedRules.add(9);
                chldn.add(Print());
                break;
            case READ:
                usedRules.add(10);
                chldn.add(Read());
                break;
            default:
                syntaxError(lookAhead);
                break;
        }
        return new ParseTree(new Symbol("Instruction"), chldn);
    }

    /**
     * function corresponding to the non terminal Assign
     * [11] Assign           → [VarName] := ExprArith
     * @return a parent node which is the non terminal Assign and the corresponding children
     */
    private ParseTree Assign(){
        ArrayList<ParseTree> chldn = new ArrayList<>();
        getNextToken();
        switch (lookAheadType){
            case VARNAME:
                usedRules.add(11);
                chldn.add(match(LexicalUnit.VARNAME));
                getNextToken();
                chldn.add(match(LexicalUnit.ASSIGN));
                getNextToken();
                chldn.add(ExprArith());
                break;
            default:
                syntaxError(lookAhead);
                break;
        }
        return new ParseTree(new Symbol("Assign"), chldn);
    }

    /**
     * function corresponding to the non terminal If
     * [12] If               → IF (Cond) THEN Code IfSeq
     * @return a parent node which is the non terminal IF and the corresponding children
     */
    private ParseTree If(){
        ArrayList<ParseTree> chldn = new ArrayList<>();
        getNextToken();
        switch (lookAheadType){
            case IF:
                usedRules.add(12);
                chldn.add(match(LexicalUnit.IF));
                getNextToken();
                chldn.add(match(LexicalUnit.LPAREN));
                chldn.add(Cond());
                getNextToken();
                chldn.add(match(LexicalUnit.RPAREN));
                getNextToken();
                chldn.add(match(LexicalUnit.THEN));
                chldn.add(Code());
                chldn.add(IfSeq());
                break;
            default:
                syntaxError(lookAhead);
                break;
        }
        return new ParseTree(new Symbol("If"), chldn);
    }

    /**
     * function corresponding to the non terminal IfSeq
     * [13] IfSeq              → END
     * [14]                    → ELSE Code END
     * @return a parent node which is the non terminal IfSeq and the corresponding children
     */
    private ParseTree IfSeq(){
        ArrayList<ParseTree> chldn = new ArrayList<>();
        getNextToken();
        switch (lookAheadType){
            case END:
                usedRules.add(13);
                chldn.add(match(LexicalUnit.END));
                break;
            case ELSE:
                usedRules.add(14);
                chldn.add(match(LexicalUnit.ELSE));
                chldn.add(Code());
                getNextToken();
                chldn.add(match(LexicalUnit.END));
                break;
            default:
                syntaxError(lookAhead);
                break;
        }

        return new ParseTree(new Symbol("IfSeq"), chldn);

    }

    /**
     * function corresponding to the non terminal While
     * [15] While            → WHILE (Cond) DO Code END
     * @return a parent node which is the non terminal While and the corresponding children
     */
    private ParseTree While(){
        ArrayList<ParseTree> chldn = new ArrayList<>();
        getNextToken();
        switch (lookAheadType){
            case WHILE:
                usedRules.add(15);
                chldn.add(match(LexicalUnit.WHILE));
                getNextToken();
                chldn.add(match(LexicalUnit.LPAREN));
                getNextToken();
                chldn.add(Cond());
                getNextToken();
                chldn.add(match(LexicalUnit.RPAREN));
                getNextToken();
                chldn.add(match(LexicalUnit.DO));
                chldn.add(Code());
                getNextToken();
                chldn.add(match(LexicalUnit.END));
                break;
            default:
                syntaxError(lookAhead);
                break;
        }
        return new ParseTree(new Symbol("While"), chldn);
    }

    /**
     * function corresponding to the non terminal Cond
     * [16] Cond             → ExprArith Comp
     * @return a parent node which is the non terminal Cond and the corresponding children
     */
    private ParseTree Cond(){
        ArrayList<ParseTree> chldn = new ArrayList<>();
        usedRules.add(16);
        chldn.add(ExprArith());
        chldn.add(Comp());
        return new ParseTree(new Symbol("Cond"), chldn);

    }

    /**
     * function corresponding to the non terminal Comp
     * [17] Comp             → = ExprArith
     * [18]                    → > ExprArith
     * [19]                    → < ExprArith
     * @return a parent node which is the non terminal Comp and the corresponding children
     */
    private ParseTree Comp(){
        ArrayList<ParseTree> chldn = new ArrayList<>();
        getNextToken();
        switch (lookAheadType){
            case EQUAL:
                usedRules.add(17);
                chldn.add(match(LexicalUnit.EQUAL));
                getNextToken();
                chldn.add(ExprArith());
                break;
            case GREATER:
                usedRules.add(18);
                chldn.add(match(LexicalUnit.GREATER));
                getNextToken();
                chldn.add(ExprArith());
                break;
            case SMALLER:
                usedRules.add(19);
                chldn.add(match(LexicalUnit.SMALLER));
                getNextToken();
                chldn.add(ExprArith());
                break;
            default:
                syntaxError(lookAhead);
                break;
        }
        return new ParseTree(new Symbol("Comp"), chldn);

    }

    /**
     * function corresponding to the non terminal ExprArith
     * [20] ExprArith        → Prod ExprArithF
     * @return a parent node which is the non terminal ExprArith and the corresponding children
     */
    private ParseTree ExprArith(){
        ArrayList<ParseTree> chldn = new ArrayList<>();
        usedRules.add(20);
        chldn.add(Prod());
        chldn.add(ExprArithF());
        return new ParseTree(new Symbol("ExprArith"), chldn);
    }

    /**
     * function corresponding to the non terminal ExprArithF
     * [21] ExprArithF       → + Prod ExprArithF
     * [22]                    → - Prod ExprArithF
     * [23]                    → ε
     * @return a parent node which is the non terminal ExprArithF and the corresponding children
     */
    private ParseTree ExprArithF(){
        ArrayList<ParseTree> chldn = new ArrayList<>();
        getNextToken();
        switch (lookAheadType){
            case END:
            case COMMA:
            case ELSE:
            case EQUAL:
            case GREATER:
            case SMALLER:
            case RPAREN:
                usedRules.add(23);
                // chldn.add(new ParseTree(new Symbol("$\\varepsilon$")));
                // return new ParseTree(new Symbol("ExprArithF"), chldn);
                return null;
            case PLUS:
                usedRules.add(21);
                chldn.add(match(LexicalUnit.PLUS));
                getNextToken();
                chldn.add(Prod());
                chldn.add(ExprArithF());
                break;
            case MINUS:
                usedRules.add(22);
                chldn.add(match(LexicalUnit.MINUS));
                getNextToken();
                chldn.add(Prod());
                chldn.add(ExprArithF());
                break;
            default:
                syntaxError(lookAhead);
                break;
        }
        return new ParseTree(new Symbol("ExprArithF"), chldn);
    }

    /**
     * function corresponding to the non terminal Prod
     * [24] Prod             → Atom ProdF
     * @return a parent node which is the non terminal Prod and the corresponding children
     */
    private ParseTree Prod(){
        ArrayList<ParseTree> chldn = new ArrayList<>();
        usedRules.add(24);
        chldn.add(Atom());
        chldn.add(ProdF());
        return new ParseTree(new Symbol("Prod"), chldn);
    }

    /**
     * function corresponding to the non terminal ProdF
     * [25] ProdF              → * Atom ProdF
     * [26]                    → / Atom ProdF
     * [27]                    → ε
     * @return a parent node which is the non terminal ProdF and the corresponding children
     */
    private ParseTree ProdF(){
        ArrayList<ParseTree> chldn = new ArrayList<>();
        getNextToken();
        switch (lookAheadType){
            case END:
            case ELSE:
            case COMMA:
            case EQUAL:
            case GREATER:
            case SMALLER:
            case PLUS:
            case MINUS:
            case RPAREN:
                usedRules.add(27);
                // chldn.add(new ParseTree(new Symbol("$\\varepsilon$")));
                // return new ParseTree(new Symbol("ProdF"), chldn);
                return null;
            case TIMES:
                usedRules.add(25);
                chldn.add(match(LexicalUnit.TIMES));
                chldn.add(Atom());
                chldn.add(ProdF());
                break;
            case DIVIDE:
                usedRules.add(26);
                chldn.add(match(LexicalUnit.DIVIDE));
                chldn.add(Atom());
                chldn.add(ProdF());
                break;
            default:
                syntaxError(lookAhead);
                break;
        }
        return new ParseTree(new Symbol("ProdF"), chldn);
    }

    /**
     * function corresponding to the non terminal Atom
     * [28] Atom               → -Atom
     * [29]                    → ( ExprArith )
     * [30]                    → [Number]
     * [31]                    → [VarName]
     * @return a parent node which is the non terminal Atom and the corresponding children
     */
    private ParseTree Atom(){
        ArrayList<ParseTree> chldn = new ArrayList<>();
        getNextToken();
        switch (lookAheadType){
            case MINUS:
                usedRules.add(28);
                chldn.add(match(LexicalUnit.MINUS));
                chldn.add(Atom());
                break;
            case LPAREN:
                usedRules.add(29);
                chldn.add(match(LexicalUnit.LPAREN));
                chldn.add(ExprArith());
                getNextToken();
                chldn.add(match(LexicalUnit.RPAREN));
                break;
            case NUMBER:
                usedRules.add(30);
                chldn.add(match(LexicalUnit.NUMBER));
                break;
            case VARNAME:
                usedRules.add(31);
                chldn.add(match(LexicalUnit.VARNAME));
                break;
            default:
                syntaxError(lookAhead);
                break;
        }

        return new ParseTree(new Symbol("Atom"), chldn);
    }

    /**
     * function corresponding to the non terminal Print
     * [32] Print            → PRINT([VarName])
     * @return a Parent node which is the non terminal Print and the corresponding children
     */
    private ParseTree Print(){
        ArrayList<ParseTree> chldn = new ArrayList<>();
        getNextToken();
        switch (lookAheadType){
            case PRINT:
                usedRules.add(32);
                chldn.add(match(LexicalUnit.PRINT));
                getNextToken();
                chldn.add(match(LexicalUnit.LPAREN));
                getNextToken();
                chldn.add(match(LexicalUnit.VARNAME));
                getNextToken();
                chldn.add(match(LexicalUnit.RPAREN));
                break;
            default:
                syntaxError(lookAhead);
                break;
        }
        return new ParseTree(new Symbol("Print"), chldn);
    }

    /**
     * function corresponding to the non terminal Read
     * [33] Read             → READ([VarName])
     * @return a Parent node which is the non terminal Read and the corresponding children
     */
    private ParseTree Read(){
        ArrayList<ParseTree> chldn = new ArrayList<>();
        getNextToken();
        switch (lookAheadType){
            case READ:
                usedRules.add(33);
                chldn.add(match(LexicalUnit.READ));
                getNextToken();
                chldn.add(match(LexicalUnit.LPAREN));
                getNextToken();
                chldn.add(match(LexicalUnit.VARNAME));
                getNextToken();
                chldn.add(match(LexicalUnit.RPAREN));
                break;
            default:
                syntaxError(lookAhead);
                break;
        }
        return new ParseTree(new Symbol("Read"), chldn);
    }

    /**
     * matching function checking whether the look ahead is equal to the expected
     * Lexical Unit => essential to a predictive parser
     * if doesn't match, launch an error
     * @param expectedTokenUnit expected Lexical unit
     * @return a Parent node of the ParseTree
     */
    private ParseTree match(LexicalUnit expectedTokenUnit){
        if(expectedTokenUnit.equals(lookAheadType)){
            ParseTree parent = new ParseTree(lookAhead);
            actualToken = lookAhead;
            return parent;
        }else
            syntaxError(lookAhead);

        return null;
    }

    /**
     * check if the lookahead has already been matched, if yes, then goes to the
     * next one, if not, doens't move
     */
    private void getNextToken(){
        // we get the next token only if the lookahead has already been treated
        // as we can't go backward, we must have a limiting variable
        if(actualToken == null || actualToken.equals(lookAhead)){
            try{
                lookAhead = lexer.nextToken();
            }catch (IOException e){
                e.printStackTrace();
            }
            lookAheadType = lookAhead.getType(); // retrieve the lexical unit of the token
        }

    }

    /**
     * launches an error, giving the problematic token, his line and column in the
     * input text file
     * print the rules until the problem
     * @param token problematic token
     */
    private void syntaxError(Symbol token){
        printUsedRules();
        System.err.println("Error! Please check the token: " + token.getValue() + " line: " + token.getLine() + " column: "+token.getColumn());
        System.exit(1);
    }
}