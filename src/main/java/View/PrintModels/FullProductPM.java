package View.PrintModels;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;

@Data @Builder
public class FullProductPM {
    private MiniProductPM product;
    private HashMap<String, String> features;
}
