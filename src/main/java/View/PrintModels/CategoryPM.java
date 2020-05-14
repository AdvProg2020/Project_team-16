package View.PrintModels;

import lombok.Data;


@Data
public class CategoryPM {
    private String name;
    private int id;

    public CategoryPM(String name, int id) {
        this.name = name;
        this.id = id;
    }
}
