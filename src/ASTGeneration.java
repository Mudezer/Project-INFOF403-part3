import java.util.ArrayList;

public class ASTGeneration {

    /**
     * Generate the AST from the given parse tree and set the parent of each node in the AST
     * @param parseTree 
     * @return root node of the generated AST
     */
    public static AbstractSyntaxTree getAST(ParseTree parseTree){
        AbstractSyntaxTree root = generateAST(parseTree);
        setParentInTree(root, null);
        optimizeAST(root);
        return root;
    }

    /**
     * Generate an AST from the given parse tree
     * this AST is only a copy of the parse tree that has not been optimized yet
     * @param parseTree : the parse tree retrieved after parsing
     * @return the pre-optimized AST
     */
    public static AbstractSyntaxTree generateAST(ParseTree parseTree){
        Symbol label = parseTree.getLabel();
        ArrayList<AbstractSyntaxTree> chldn = new ArrayList<>();
        for(ParseTree child: parseTree.getChildren()){
            AbstractSyntaxTree childNode = generateAST(child);
            chldn.add(childNode);
        }

        AbstractSyntaxTree root = new AbstractSyntaxTree(label, chldn, null);

        return root;
    }

    /**
     * Set the parent of each node in the tree
     * @param actualNode : the node whose parent is to be set
     * @param parent : the parent of the actualNode, for the root node, this parameter is null
     */
    public static void setParentInTree(AbstractSyntaxTree actualNode, AbstractSyntaxTree parent){
        for(AbstractSyntaxTree child: actualNode.getChildren()){
            if(child.getParent() != parent){
                child.setParent(parent);
            }
            setParentInTree(child, actualNode);
        }
    }

    public static void optimizeAST(AbstractSyntaxTree root){
        

        for(int i = 0; i< root.getChildren().size();i++){
            AbstractSyntaxTree child = root.getChildren().get(i);
            if(checkUnwantedChild(child)){
                for(AbstractSyntaxTree grandChild: child.getChildren()){
                    grandChild.setParent(root);
                }
                root.getChildren().remove(i);
                root.getChildren().addAll(i, child.getChildren());
                i--;
            }
            
        }
        for(AbstractSyntaxTree child: root.getChildren()){
            optimizeAST(child);
        }
    }

    private static boolean checkUnwantedChild(AbstractSyntaxTree child) {
        Symbol label = child.getLabel();
        switch(label.getValue().toString()){
            case "ProdF":
            case "Atom":
            case "$\\varepsilon$":
            case "BEGIN":
            case "END":
                return true;
            default:
                return false;
            
        }
    }



}
