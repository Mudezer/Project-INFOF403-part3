import java.util.ArrayList;

public class ASTGeneration {

    public static void setParentInTree(AbstractSyntaxTree actualNode, AbstractSyntaxTree parent){
        actualNode.setParent(parent);
        for(AbstractSyntaxTree child: actualNode.getChildren()){
            setParentInTree(child, actualNode);
        }
    }

    public static AbstractSyntaxTree generateTree(ParseTree parseTree){
        Symbol label = parseTree.getLabel();
        ArrayList<AbstractSyntaxTree> chldn = new ArrayList<>();
        for(ParseTree child: parseTree.getChildren()){
            AbstractSyntaxTree childNode = generateTree(child);
            chldn.add(childNode);
        }

        AbstractSyntaxTree root = new AbstractSyntaxTree(label, chldn, null);
        setParentInTree(root, null);
        return root;
    }
}
