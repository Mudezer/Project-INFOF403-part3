import java.util.ArrayList;

public class ASTGeneration {

//    private static AbstractSyntaxTree generateAST(ParseTree parseTree, AbstractSyntaxTree parent){
//        if(parseTree.getLabel().getType().equals(LexicalUnit.LPAREN)){
//            for(ParseTree child: parseTree.getChildren()){
//                generateAST(child, parent);
//            }
//            return parent;
//        }
//        else {
//            AbstractSyntaxTree ast = new AbstractSyntaxTree(parseTree.getLabel());
//            if(parent != null){
//                parent.getChildren().add(ast);
//            }
//            for(ParseTree child: parseTree.getChildren()){
//                generateAST(child, ast);
//            }
//            return ast;
//        }
//    }

//    public static AbstractSyntaxTree getAST(ParseTree parseTree){
//        return generateAST(parseTree);
//    }


//    private static AbstractSyntaxTree generateAST(ParseTree parseTree){
//        Symbol label = parseTree.getLabel();
//        ArrayList<AbstractSyntaxTree> chldn = new ArrayList<>();
//        if(label.getValue().equals("ProdF")){
//            for(ParseTree child: parseTree.getChildren()){
//                generateAST(child);
//            }
//        }else{
//            for(ParseTree child: parseTree.getChildren()){
//                chldn.add(generateAST(child));
//            }
//
//        }
//        return new AbstractSyntaxTree(label, chldn);
//    }
//    private static AbstractSyntaxTree generateAST(ParseTree parseTree){
//        Symbol label = parseTree.getLabel();
//        ArrayList<AbstractSyntaxTree> chldn = new ArrayList<>();
//        for(ParseTree child: parseTree.getChildren()){
//            if(child.getLabel().getValue().equals("ProdF")) {
//                chldn.add(generateAST());
//            }
//
//        }
//        return new AbstractSyntaxTree(label, chldn);
//    }

//    public AbstractSyntaxTree generateTree(ParseTree parseTree){
//        AbstractSyntaxTree root = new AbstractSyntaxTree(parseTree.getLabel());
//    }

}
