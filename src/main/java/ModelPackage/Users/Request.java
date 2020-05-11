package ModelPackage.Users;

import ModelPackage.Off.Off;
import ModelPackage.Product.Comment;
import ModelPackage.Product.Product;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;

@Data @Entity
@Table(name = "t_request")
public class Request {
    @Setter(AccessLevel.NONE)
    @Id @GeneratedValue
    private int requestId;

    @OneToOne
        @JoinColumn(name = "USER_HAS_REQUESTED")
    User userHasRequested;

    @Enumerated(EnumType.STRING)
    RequestType requestType;

    @Column(name = "REQUEST")
    String request;

    @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "OFF")
    Off off;

    @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "PRODUCT")
    Product product;

    @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "COMMENT")
    Comment comment;

    @OneToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "SELLER")
    Seller seller;

    @Column
    private int idOfRequestedItem;

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
