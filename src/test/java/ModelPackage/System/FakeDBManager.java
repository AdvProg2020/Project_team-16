package ModelPackage.System;

import ModelPackage.Product.NoSuchSellerException;
import ModelPackage.Product.Product;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.Cart;
import ModelPackage.Users.SubCart;
import ModelPackage.Users.User;
import mockit.MockUp;
import ModelPackage.Users.Seller;
import mockit.Mock;
import mockit.Mocked;

import java.io.Serializable;
import java.util.ArrayList;

public class FakeDBManager extends MockUp<DBManager> {

    @Mocked
    private Seller firstSeller;
    @Mocked
    private Product product;
    @Mocked
    private Product product1;
    @Mocked
    private SubCart subCart;
    @Mocked
    private Cart cart;

    @Mock
    public void save(Object object) {
        if (object instanceof Seller)
            firstSeller = (Seller) object;
        else if (object instanceof Product) {
            handleSaveProduct(object);
        } else if (object instanceof SubCart)
            handleSaveSubCart(object);
        else if (object instanceof Cart)
            handleSaveCart(object);

    }

    private void handleSaveProduct(Object object) {
        if (((Product) object).getId() == 1)
            product = ((Product) object);
        else if (((Product) object).getId() == 2)
            product1 = ((Product) object);
    }

    private void handleSaveSubCart(Object object) {
        subCart = ((SubCart) object);
    }

    private void handleSaveCart(Object object) {
        cart = ((Cart) object);
    }

    @Mock
    public <T> T load(Class<T> type, Serializable serializable) throws NoSuchSellerException, NoSuchAProductException {
        if (type.isInstance(firstSeller)) {
            if (serializable.equals("ali110"))
                return type.cast(firstSeller);
            else throw new NoSuchSellerException();
        } else if (type.isInstance(product)) {
            if (serializable.equals(1))
                return type.cast(product);
            else if (serializable.equals(2))
                return type.cast(product1);
            else throw new NoSuchAProductException(serializable.toString());
        } else if (type.isInstance(subCart))
            return type.cast(subCart);
        else if (type.isInstance(cart))
            return type.cast(cart);
        return null;
    }
}
