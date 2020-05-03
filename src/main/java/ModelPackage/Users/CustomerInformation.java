package ModelPackage.Users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerInformation {
    private String address;
    private String zipCode;
    private String cartNumber;
    private String cardPassword;
}
