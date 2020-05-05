package ModelPackage.Product;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "t_score")
public class Score {
    @Id @GeneratedValue
    private int id;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "PRODUCT_STRING_ID")
    private String productId;

    @Column(name = "SCORE")
    private int score;

    public Score(String userId, String productId, int score) {
        this.userId = userId;
        this.productId = productId;
        this.score = score;
    }
}
