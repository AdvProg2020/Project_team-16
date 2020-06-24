package ModelPackage.System;

import ModelPackage.Off.DiscountCode;
import ModelPackage.Product.NoSuchSellerException;
import ModelPackage.Product.Product;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.discount.NoSuchADiscountCodeException;
import ModelPackage.System.exeption.discount.UserNotExistedInDiscountCodeException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.Users.*;
import mockit.MockUp;
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
    @Mocked
    private DiscountCode superDiscountCode;
    @Mocked
    private DiscountCode classicDiscountCode;
    @Mocked
    private Customer sapa;

    @Mock
    public void save(Object object) {
        if (object instanceof Seller)
            firstSeller = (Seller) object;
        else if (object instanceof Customer)
            sapa = ((Customer) object);
        else if (object instanceof Product) {
            handleSaveProduct(object);
        } else if (object instanceof SubCart)
            handleSaveSubCart(object);
        else if (object instanceof Cart)
            handleSaveCart(object);
        else if (object instanceof DiscountCode)
            handleSaveDiscount(object);
    }

    private void handleSaveDiscount(Object object) {
        if (((DiscountCode) object).getCode().equals("Dis#12"))
            superDiscountCode = ((DiscountCode) object);
        else if (((DiscountCode) object).getCode().equals("Dis#13"))
            classicDiscountCode = ((DiscountCode) object);
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
    public <T> T load(Class<T> type, Serializable serializable)
            throws NoSuchSellerException, NoSuchAProductException, NoSuchADiscountCodeException, UserNotExistedInDiscountCodeException {
        if (type.isInstance(firstSeller)) {
            if (serializable.equals("ali110"))
                return type.cast(firstSeller);
            else throw new NoSuchSellerException();
        } else if (type.isInstance(sapa)) {
            if (serializable.equals("sapa"))
                return type.cast(sapa);
            else throw new UserNotExistedInDiscountCodeException("marmof");
        }
        else if (type.isInstance(product)) {
            if (serializable.equals(1))
                return type.cast(product);
            else if (serializable.equals(2))
                return type.cast(product1);
            else throw new NoSuchAProductException(serializable.toString());
        } else if (type.isInstance(subCart))
            return type.cast(subCart);
        else if (type.isInstance(cart))
            return type.cast(cart);
        else if (type.isInstance(superDiscountCode)) {
            if (serializable.equals("Dis#12"))
                return type.cast(superDiscountCode);
            else if (serializable.equals("Dis#13"))
                return type.cast(classicDiscountCode);
            else if (serializable.equals("Dis#15"))
                return null;
            throw new NoSuchADiscountCodeException(serializable.toString());
        }
        return null;
    }

    @Mock
    public void delete(Object object) throws NoSuchADiscountCodeException {
        if (object instanceof DiscountCode) {
            handleDeleteDiscount(((DiscountCode) object));
        }
    }

    private void handleDeleteDiscount(DiscountCode discountCode) throws NoSuchADiscountCodeException {
        if (discountCode.getCode().equals("Dis#12"))
            superDiscountCode = null;
        else if (discountCode.getCode().equals("Dis#13"))
            classicDiscountCode = null;
        else throw new NoSuchADiscountCodeException("Dis#14");
    }
}
