package View.PrintModels;

import lombok.Data;

@Data
public class MicroProduct {
    private String name;
    private int id;

    public MicroProduct(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }
}
