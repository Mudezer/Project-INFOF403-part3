import java.util.ArrayList;
import java.util.List;


/**
 * This class represents an abstract syntax tree. It is used to represent the
 * syntax tree of a program.
 * It has the same structure as the Parse tree, but it is not a subclass of it.
 */
public class AbstractSyntaxTree {

    /**
     * The label of the node.
     */
    private Symbol label;

    /**
     * It's children, which are trees themselves.
     */
    private List<AbstractSyntaxTree> children;

    /**
     * Returns the children of the tree.
     */
    public List<AbstractSyntaxTree> getChildren() {
        return children;
    }

    /**
     * Sets the children of the tree.
     */
    public Symbol getLabel() {
        return label;
    }

    /**
     * Sets the label of the tree.
     */
    public void setLabel(Symbol label) {
        this.label = label;
    }

    /**
     * Creates a singleton tree with only a root labeled by lbl.
     * @param lbl The label of the root
     */
    public AbstractSyntaxTree(Symbol lbl){
        this.label = lbl;
        this.children = new ArrayList<AbstractSyntaxTree>();
    }

    /**
     * Creates a tree with root labeled by lbl and children chldn.
     * @param lbl The label of the root
     * @param chldn Its children
     */
    public AbstractSyntaxTree(Symbol lbl, List<AbstractSyntaxTree> chldn){
        this.label = lbl;
        this.children = chldn;
    }

    /**
     * Adds a child to the tree.
     * @param child The child to be added
     */
    public void addChild(AbstractSyntaxTree child) {
        this.children.add(child);
    }

    /**
     * Removes a child from the tree.
     * @param child The child to be removed
     */
    public void removeChild(AbstractSyntaxTree child) {
        this.children.remove(child);
    }

    /**
     * Writes the tree as LaTeX code
     */
    public String toLaTexTree() {
        StringBuilder treeTeX = new StringBuilder();
        treeTeX.append("[");
        treeTeX.append("{" + label.toTexString() + "}");
        treeTeX.append(" ");

        for (AbstractSyntaxTree child : children) {
            if(child != null){
                treeTeX.append(child.toLaTexTree());
            }
        }
        treeTeX.append("]");
        return treeTeX.toString();
    }

    /**
     * Writes the tree as TikZ code. TikZ is a language to specify drawings in LaTeX
     * files.
     */
    public String toTikZ() {
        StringBuilder treeTikZ = new StringBuilder();
        treeTikZ.append("node {");
        treeTikZ.append(label.toTexString());  // Implement this yourself in Symbol.java
        treeTikZ.append("}\n");
        for (AbstractSyntaxTree child : children) {
            if(child != null){
                treeTikZ.append("child { ");
                treeTikZ.append(child.toTikZ());
                treeTikZ.append(" }\n");
            }
            
        }
        return treeTikZ.toString();
    }

    /**
     * Writes the tree as a TikZ picture. A TikZ picture embeds TikZ code so that
     * LaTeX undertands it.
     */
    public String toTikZPicture() {
        return "\\begin{tikzpicture}[tree layout]\n\\" + toTikZ() + ";\n\\end{tikzpicture}";
    }


    /**
     * Writes the tree as a forest picture. Returns the tree in forest enviroment
     * using the latex code of the tree
     */
    public String toForestPicture() {
        return "\\begin{forest}for tree={rectangle, draw, l sep=20pt}" + toLaTexTree() + ";\n\\end{forest}";
    }

    /**
     * Writes the tree as a LaTeX document which can be compiled using PDFLaTeX.
     * <br>
     * <br>
     * The result can be used with the command:
     *
     * <pre>
     * pdflatex some-file.tex
     * </pre>
     */
    public String toLaTeX() {
        return "\\documentclass[border=5pt]{standalone}\n\n\\usepackage{tikz}\n\\usepackage{forest}\n\n\\begin{document}\n\n"
                + toForestPicture() + "\n\n\\end{document}\n%% Local Variables:\n%% TeX-engine: pdflatex\n%% End:";
    }

}
