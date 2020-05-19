package View.PrintModels;

import lombok.Data;


@Data
public class CategoryPM {
    private String name;
    private int indent;
    private int id;

    public CategoryPM(String name, int id,int indent) {
        this.name = name;
        this.id = id;
        this.indent = indent;
    }
}
