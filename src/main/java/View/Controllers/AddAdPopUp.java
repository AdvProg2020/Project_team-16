package View.Controllers;

import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.System.exeption.request.SellerHasActiveAdException;
import View.CacheData;
import View.PrintModels.MicroProduct;
import controler.ContentController;
import controler.SellerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.control.SearchableComboBox;

public class AddAdPopUp {
    public SearchableComboBox<MicroProduct> products;
    public Button createAd;

    @FXML
    public void initialize() {
        loadProducts();
        binds();
        createAd.setOnAction(event -> handleAdvertise());
    }

    private void binds() {
        createAd.disableProperty().bind(products.valueProperty().isNull());
    }

    private void loadProducts() {
        try {
            ObservableList<MicroProduct> data = FXCollections.observableArrayList(
                    SellerController.getInstance().getProductsForSeller(CacheData.getInstance().getUsername())
            );
            products.setItems(data);
        } catch (UserNotAvailableException e) {
            new OopsAlert().show(e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleAdvertise() {
        int id = products.getSelectionModel().getSelectedItem().getId();
        String username = CacheData.getInstance().getUsername();
        try {
            ContentController.getInstance().addAd(id, username);
            Stage stage = (Stage) Stage.getWindows().filtered(Window::isFocused).get(0);
            Notification.show("Successful", "Request Sent To Manager", stage, false);
        } catch (SellerHasActiveAdException | NoSuchAProductException e) {
            Stage stage = (Stage) Stage.getWindows().filtered(Window::isFocused).get(0);
            Notification.show("Error", e.getMessage(), stage, true);
            e.printStackTrace();
        }
    }
}
