package ModelPackage.Product;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Data @NoArgsConstructor
@Entity
@Table(name = "t_comment")
public class Comment {
    @Setter(AccessLevel.NONE)
    @Id @GeneratedValue
    private int id;

    @Column(name = "USER")
    private String userId;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "TEXT")
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private CommentStatus status;

    @ManyToOne(cascade = CascadeType.ALL)
    private Product product;

    @Column(name = "BOUGHT_THIS_PRODUCT")
    private boolean boughtThisProduct;

    public Comment(String userId, String title, String text, CommentStatus status, boolean boughtThisProduct) {
        this.userId = userId;
        this.title = title;
        this.text = text;
        this.status = status;
        this.boughtThisProduct = boughtThisProduct;
    }
}