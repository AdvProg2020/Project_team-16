package ModelPackage.System;

import ModelPackage.Product.Company;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.Seller;
import ModelPackage.Users.User;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class SellerManagerTest {
    SellerManager sellerManager;
    Seller marmof;
    Company adidas;

    {
        adidas = new Company("Adidas","115", "Clothing",new ArrayList<>());
        sellerManager = SellerManager.getInstance();
        marmof = new Seller(
                "marmofayezi",
                "marmof.ir",
                "Cyrus",
                "Statham",
                "marmof@gmail.com",
                "+1 992 1122",
                new Cart(),
                adidas,
                10000000
        );

    }


    @Test
    public void viewCompanyInformation() throws UserNotAvailableException {
        new MockUp<AccountManager>(){
            @Mock
            User getUserByUsername(String username){
                return marmof;
            }
        };

        Company actual = sellerManager.viewCompanyInformation("marmofayezi");
        Assert.assertEquals(adidas,actual);
    }
}
