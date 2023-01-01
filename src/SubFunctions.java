import java.util.List;
import java.util.ArrayList;


/**
 * This class represents a subfunction of a program in the intermediate code.
 * A program can have multiple subfunctions.
 */
public class SubFunctions {

    private String label;
    private List<String> content;

    /**
     * Creates a new subfunction with the given label.
     * @param label The label of the subfunction.
     */
    public SubFunctions(String label) {
        this.label = label;
        this.content = new ArrayList<>();
    }


    /**
     * Adds a line of code to the subfunction.
     * @param content The line of code to add.
     */
    public void addContent(String content) {
        this.content.add(content);
    }

    /**
     * Returns the label of the subfunction.
     * @return The label of the subfunction.
     */
    public String toString(){
        StringBuilder code = new StringBuilder();
        code.append("\t").append(this.label).append(":\n");
        for(String line : this.content){
            code.append("\t\t").append(line).append("\n");
        }

        return code.toString();
    }
    
}
