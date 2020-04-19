package ModelPackage.Product;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class Comment {
    private String productId;
    private String userId;
    private String title;
    private String text;
    private CommentStatus status;
    private boolean boughtThisProduct;

    public Comment(String productId, String userId, String title, String text, CommentStatus status, boolean boughtThisProduct) {
        this.productId = productId;
        this.userId = userId;
        this.title = title;
        this.text = text;
        this.status = status;
        this.boughtThisProduct = boughtThisProduct;
    }
}
