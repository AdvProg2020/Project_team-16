package View.PrintModels;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Data @Builder
public class MiniProductPM {
    private String name;
    private String id;
    private HashMap<String, Integer> sellers;
    private String brand;
    private float score;
    private String description;
}
