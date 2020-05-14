package View.PrintModels;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data @Builder
public class FullProductPM {
    private MiniProductPM product;
    private Map<String, String> features;

    public FullProductPM(MiniProductPM product, Map<String, String> features) {
        this.product = product;
        this.features = features;
    }
}
