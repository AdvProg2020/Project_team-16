package ModelPackage.Off;

import ModelPackage.Product.Product;
import ModelPackage.Users.Seller;
import lombok.*;

import java.sql.Time;
import java.util.List;

@Data
@Builder
public class Off {
    private String offId;
    private Seller seller;
    private List<Product> products;
    private OffStatus offStatus;
    private Time startTime;
    private Time endTime;
    private int offPercentage;
}
enum OffStatus{
    CREATION,
    EDIT,
    ACCEPTED
}
