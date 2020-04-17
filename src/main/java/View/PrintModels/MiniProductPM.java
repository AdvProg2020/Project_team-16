package View.PrintModels;

import lombok.Data;

import java.util.*;

@Data
public class MiniProductPM {
    private String name;
    private String id;
    private HashMap<String,String> sellers;
    private String brand;
    private float score;
    private String description;
}
