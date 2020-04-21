package ModelPackage.Product;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class Score {
    private String userId;
    private String productId;
    private int score;

    public Score(String userId, String productId, int score) {
        this.userId = userId;
        this.productId = productId;
        this.score = score;
    }
}
