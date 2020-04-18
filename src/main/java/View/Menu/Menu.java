package View.Menu;

import lombok.Data;

import java.util.HashMap;

@Data
public abstract class Menu {
    private String name;
    private Menu parent;
    private HashMap<String,Menu> subMenus;

    public Menu(String name, Menu parent) {
        this.name = name;
        this.parent = parent;
    }
}
