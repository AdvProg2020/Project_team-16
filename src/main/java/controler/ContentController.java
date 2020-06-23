package controler;

import ModelPackage.System.ContentManager;
import ModelPackage.System.database.DBManager;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.System.exeption.request.SellerHasActiveAdException;
import ModelPackage.Users.Advertise;
import ModelPackage.Users.MainContent;
import View.PrintModels.AdPM;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class ContentController extends Controller {
    private static ContentController contentController = new ContentController();

    public static ContentController getInstance() {
        return contentController;
    }

    private static ContentManager contentManager = ContentManager.getInstance();

    public List<AdPM> getAds() {
        List<Advertise> activeAdvertises = contentManager.getActiveAdvertises();
        List<AdPM> list = new ArrayList<>();
        activeAdvertises.forEach(advertise -> list.add(createAdPm(advertise)));
        return list;
    }

    private AdPM createAdPm(Advertise advertise) {
        int productId = advertise.getProduct().getId();
        Image image = ProductController.getInstance().loadMainImage(productId);
        return new AdPM(productId, advertise.getProduct().getName(), advertise.getUsername(), image);
    }

    public List<MainContent> getMainContents() {
        return DBManager.loadAllData(MainContent.class);
    }

    public void addAd(int id, String username)
            throws SellerHasActiveAdException, NoSuchAProductException {
        contentManager.Advertise(id, username);
    }

    public void addContent(String title, String content){
        contentManager.addContent(title, content);
    }

    public void deleteContent(int id) {
        contentManager.deleteContent(id);
    }
}