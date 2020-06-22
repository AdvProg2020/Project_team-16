package ModelPackage.System;

import ModelPackage.Product.Product;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.System.exeption.request.SellerHasActiveAdException;
import ModelPackage.Users.Advertise;
import ModelPackage.Users.MainContent;
import ModelPackage.Users.Request;
import ModelPackage.Users.RequestType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ContentManager {
    private static ContentManager contentManager = new ContentManager();

    public static ContentManager getInstance() {
        return contentManager;
    }

    public void addContent(String title, String content) {
        MainContent mainContent = new MainContent(title, content);
        DBManager.save(mainContent);
    }

    public void deleteContent(int id) {
        MainContent mainContent = DBManager.load(MainContent.class, id);
        if (mainContent != null) {
            DBManager.delete(mainContent);
        }
    }

    public void Advertise(int id, String username) throws SellerHasActiveAdException, NoSuchAProductException {
        checkIfAdvertisedYet(username);
        Product product = ProductManager.getInstance().findProductById(id);
        Advertise advertise = new Advertise(product, username);
        DBManager.save(advertise);
        Request request = new Request(username, RequestType.ADVERTISE,
                username + "has requested to create an ad on product " + product.getId(),
                advertise);
        RequestManager.getInstance().addRequest(request);
    }

    private void checkIfAdvertisedYet(String username) throws SellerHasActiveAdException {
        List<Advertise> activeAdvertises = getActiveAdvertises();
        for (Advertise advertise : activeAdvertises) {
            if (advertise.getUsername().equals(username))
                throw new SellerHasActiveAdException("You Have Active Ad");
        }
    }

    public List<Advertise> getActiveAdvertises() {
        List<Advertise> advertises = new CopyOnWriteArrayList<>(DBManager.loadAllData(Advertise.class));
        for (Advertise advertise : advertises) {
            if (!advertise.isActive()) advertises.remove(advertise);
        }
        return advertises;
    }
}
