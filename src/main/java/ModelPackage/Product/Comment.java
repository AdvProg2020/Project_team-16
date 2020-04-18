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
}