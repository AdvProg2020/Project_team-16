package ModelPackage.Log;

import ModelPackage.Product.Product;
import ModelPackage.Product.SoldProduct;
import ModelPackage.Users.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;

@Setter @Getter
@Entity
@Table(name = "t_sell_log")
public class SellLog extends Log {
    @OneToOne
        @JoinColumn(name = "PRODUCT")
    private SoldProduct product;

    @Column(name = "MONEY_GOTTEN")
    private int moneyGotten;

    @Column(name = "DISCOUNT")
    private int discount;

    @OneToOne
        @JoinColumn(name = "BUYER")
    private User buyer;

    public SellLog(SoldProduct product, int moneyGotten, int discount, User buyer, Date date, DeliveryStatus deliveryStatus) {
        super(date, deliveryStatus);
        this.product = product;
        this.moneyGotten = moneyGotten;
        this.discount = discount;
        this.buyer = buyer;
    }
}