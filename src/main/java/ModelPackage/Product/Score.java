package ModelPackage.Product;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Data
@Entity
@Table(name = "t_score")
public class Score {
    @Setter(AccessLevel.NONE)
    @Id @GeneratedValue
    private int id;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "PRODUCT_STRING_ID")
    private int productId;

    @Column(name = "SCORE")
    private int score;

    public Score(String userId, int productId, int score) {
        this.userId = userId;
        this.productId = productId;
        this.score = score;
    }

    public Score(){

    }
}
