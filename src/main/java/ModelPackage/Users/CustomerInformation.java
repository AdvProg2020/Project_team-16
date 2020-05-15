package ModelPackage.Users;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_customerInfo")
public class CustomerInformation {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ZIP_CODE")
    private String zipCode;

    @Column(name = "CARD_NUMBER")
    private String cardNumber;

    @Column(name = "CARD_PASSWORD")
    private String cardPassword;

    public CustomerInformation(String address, String zipCode, String cardNumber, String cardPassword) {
        this.address = address;
        this.zipCode = zipCode;
        this.cardNumber = cardNumber;
        this.cardPassword = cardPassword;
    }
}
