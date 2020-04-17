package View.PrintModels;

import lombok.Data;

import java.util.HashMap;

@Data
public class FullProductPM {
    private MiniProductPM productPM;
    private HashMap<String,String> features;
}
