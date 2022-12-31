import java.util.List;
import java.util.ArrayList;

public class SubFunctions {

    private String label;
    private List<String> content;

    public SubFunctions(String label) {
        this.label = label;
        this.content = new ArrayList<>();
    }

    public void addContent(String content) {
        this.content.add(content);
    }

    public String toString(){
        StringBuilder code = new StringBuilder();
        code.append("\t").append(this.label).append(":\n");
        for(String line : this.content){
            code.append("\t\t").append(line).append("\n");
        }

        return code.toString();
    }
    
}
