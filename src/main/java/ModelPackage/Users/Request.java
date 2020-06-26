package ModelPackage.Users;

import ModelPackage.Off.Off;
import ModelPackage.System.editPackage.OffChangeAttributes;
import ModelPackage.Product.Comment;
import ModelPackage.Product.Product;
import ModelPackage.System.editPackage.ProductEditAttribute;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;

@Data @Entity
@Table(name = "t_request")
public class Request {
    @Id @GeneratedValue
    private int requestId;

    private String userHasRequested;

    @Enumerated(EnumType.STRING)
    RequestType requestType;

    @Column(name = "REQUEST", length = 4096)
    String request;

    @OneToOne
        @JoinColumn(name = "OFF")
    Off off;

    @OneToOne
        @JoinColumn(name = "OFF_EDIT")
    OffChangeAttributes offEdit;

    @OneToOne
        @JoinColumn(name = "PRODUCT")
    Product product;

    @OneToOne
    @JoinColumn(name = "PRODUCT_EDIT")
    ProductEditAttribute productEditAttribute;

    @OneToOne
        @JoinColumn(name = "COMMENT")
    Comment comment;

    @OneToOne
    Advertise advertise;

    @ManyToOne
    Seller seller;

    private boolean done;

    private boolean accepted;

    public Request(String usernameHasRequested, RequestType requestType, String request,Object toChange) {
        this.userHasRequested = usernameHasRequested;
        this.requestType = requestType;
        this.request = request;
        done = false;
        accepted = false;
        String className = toChange.getClass().getName();

        switch (className) {
            case "ModelPackage.System.editPackage.OffChangeAttributes":
                offEdit = (OffChangeAttributes) toChange;
                break;
            case "ModelPackage.Off.Off" :
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
            case "ModelPackage.System.editPackage.ProductEditAttribute":
                productEditAttribute = (ProductEditAttribute) toChange;
                break;
            case "ModelPackage.Users.Advertise":
                advertise = (Advertise) toChange;
                break;
        }
    }

    public Request() {

    }
}
