package ModelPackage.Product;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data @NoArgsConstructor
@Entity
@Table(name = "t_comment")
public class Comment {
    @Id @GeneratedValue
    private int id;

    @Column(name = "COMMENT_ID")
    private String idComment;

    @Column(name = "PRODUCT_STRING_ID")
    private String productId;

    @ManyToOne()
    private Product product;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "TEXT")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private CommentStatus status;

    @Column(name = "BOUGHT_THIS_PRODUCT")
    private boolean boughtThisProduct;

    public Comment(String productId, String userId, String title, String text, CommentStatus status, boolean boughtThisProduct) {
        this.productId = productId;
        this.userId = userId;
        this.title = title;
        this.text = text;
        this.status = status;
        this.boughtThisProduct = boughtThisProduct;
        this.idComment = generateId();
    }

    private static String generateId(){
        Date date = new Date();
        return String.format("CM%s%04d",date.toString().replaceAll("\\s","").replaceAll(":",""),(int)(Math.random()*9999+1));
    }
}
