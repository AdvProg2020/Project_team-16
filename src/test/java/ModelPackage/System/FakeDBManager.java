package ModelPackage.System;

import ModelPackage.Log.SellLog;
import ModelPackage.Off.DiscountCode;
import ModelPackage.Product.*;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.clcsmanager.NoSuchACompanyException;
import ModelPackage.System.exeption.clcsmanager.NoSuchALogException;
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
    @Mocked
    private Company adidas;
    @Mocked
    private SellLog sellLog;
    @Mocked
    private Comment comment;

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
        else if (object instanceof Company)
            handleSaveCompany(object);
        else if (object instanceof SellLog)
            handleSaveSellLogs(object);
        else if (object instanceof Comment)
            handleSaveComments(object);
    }

    private void handleSaveComments(Object object) {
        comment = ((Comment) object);
    }

    private void handleSaveSellLogs(Object object) {
        if (((SellLog) object).getLogId() == 1)
            sellLog = ((SellLog) object);
    }

    private void handleSaveCompany(Object object) {
        if (((Company) object).getName().equals("Adidas"))
            adidas = ((Company) object);
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
            throws NoSuchSellerException, NoSuchAProductException, NoSuchADiscountCodeException,
            UserNotExistedInDiscountCodeException {
        if (type.isInstance(firstSeller)) {
            return handleLoadSeller(type, serializable);
        } else if (type.isInstance(sapa)) {
            return handleLoadUser(type, serializable);
        } else if (type.isInstance(product)) {
            return handleLoadProduct(type, serializable);
        } else if (type.isInstance(subCart)) {
            return handleLoadSubCart(type);
        } else if (type.isInstance(cart)) {
            return handleLoadCart(type);
        } else if (type.isInstance(superDiscountCode)) {
            return handleLoadDisCode(type, serializable);
        } else if (type.isInstance(adidas))
            return handleLoadCompany(type, serializable);
        else if (type.isInstance(sellLog))
            return handleLoadSellLog(type, serializable);
        else if (type.isInstance(comment))
            return type.cast(comment);
        return null;
    }

    private <T> T handleLoadSellLog(Class<T> type, Serializable serializable) {
        if (serializable.equals(1))
            return type.cast(sellLog);
       return null;
    }

    private <T> T handleLoadCompany(Class<T> type, Serializable serializable) {
        if (serializable.equals(adidas.getId()))
            return type.cast(adidas);
        else throw new NoSuchACompanyException(Integer.parseInt(serializable.toString()));
    }

    private <T> T handleLoadDisCode(Class<T> type, Serializable serializable) throws NoSuchADiscountCodeException {
        if (serializable.equals("Dis#12"))
            return type.cast(superDiscountCode);
        else if (serializable.equals("Dis#13"))
            return type.cast(classicDiscountCode);
        else if (serializable.equals("Dis#15"))
            return null;
        throw new NoSuchADiscountCodeException(serializable.toString());
    }

    private <T> T handleLoadCart(Class<T> type) {
        return type.cast(cart);
    }

    private <T> T handleLoadSubCart(Class<T> type) {
        return type.cast(subCart);
    }

    private <T> T handleLoadProduct(Class<T> type, Serializable serializable) throws NoSuchAProductException {
        if (serializable.equals(1))
            return type.cast(product);
        else if (serializable.equals(2))
            return type.cast(product1);
        else throw new NoSuchAProductException(serializable.toString());
    }

    private <T> T handleLoadUser(Class<T> type, Serializable serializable) throws UserNotExistedInDiscountCodeException {
        if (serializable.equals("sapa"))
            return type.cast(sapa);
        else throw new UserNotExistedInDiscountCodeException("marmof");
    }

    private <T> T handleLoadSeller(Class<T> type, Serializable serializable) throws NoSuchSellerException {
        if (serializable.equals("ali110") || serializable.equals("marmofayezi"))
            return type.cast(firstSeller);
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
