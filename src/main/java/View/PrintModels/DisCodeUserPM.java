package View.PrintModels;

import lombok.Builder;
import lombok.Data;

@Data @Builder

public class DisCodeUserPM extends DiscountPM {
    private int count;
}
