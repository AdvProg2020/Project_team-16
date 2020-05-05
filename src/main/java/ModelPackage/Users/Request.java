package ModelPackage.Users;

import ModelPackage.Off.Off;
import ModelPackage.Product.Comment;
import ModelPackage.Product.Product;
import lombok.Data;

import javax.persistence.*;

@Data @Entity
@Table(name = "t_request")
public class Request {
    @Id @GeneratedValue
    private int requestId;

    @OneToOne
        @JoinColumn(name = "USER_HAS_REQUESTED")
    User userHasRequested;

    @Enumerated(EnumType.STRING)
    RequestType requestType;

    @Column(name = "REQUEST")
    String request;

    @OneToOne
        @JoinColumn(name = "OFF")
    Off off;

    @OneToOne
        @JoinColumn(name = "PRODUCT")
    Product product;

    @OneToOne
        @JoinColumn(name = "COMMENT")
    Comment comment;

    @OneToOne
        @JoinColumn(name = "SELLER")
    Seller seller;

    public Request(User usernameHasRequested, RequestType requestType, String request,Object toChange) {
        this.userHasRequested = usernameHasRequested;
        this.requestType = requestType;
        this.request = request;

        String className = toChange.getClass().getName();

        switch (className) {
            case "ModelPackage.Off.Off":
                off = (Off) toChange;
                break;
            case "ModelPackage.Product.Comment":
                comment = (Comment) toChange;
                break;
            case "ModelPackage.Product.Product":
                product = (Product) toChange;
                break;
            case "ModelPackage.Users.Seller":
                seller = (Seller) toChange;
                break;
        }
    }
}
