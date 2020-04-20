package ModelPackage.Product;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class Comment {
    private String id;
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
        this.id = generateId();
    }

    private static String generateId(){
        Date date = new Date();
        return String.format("CM%s%04d",date.toString().replaceAll("\\s","").replaceAll(":",""),(int)(Math.random()*9999+1));
    }
}
