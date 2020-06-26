package View.Controllers;

import ModelPackage.Product.NoSuchSellerException;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.cart.NoSuchAProductInCart;
import ModelPackage.System.exeption.cart.NotEnoughAmountOfProductException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import View.CacheData;
import View.Main;
import View.PrintModels.CartPM;
import View.PrintModels.InCartPM;
import View.PrintModels.MiniProductPM;
import com.jfoenix.controls.JFXButton;
import controler.CustomerController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Cart extends BackAbleController {
    public JFXButton back;
    public JFXButton minimize;
    public JFXButton close;

    public Label totalPrice;

    public JFXButton purchase;

    public TableView<InCartPM> tableView;
    public TableColumn<InCartPM, MiniProductPM> product;
    public TableColumn<InCartPM, Integer> price;
    public TableColumn<InCartPM, Integer> afterOff;
    public TableColumn<InCartPM, Integer> totalCol;
    public TableColumn<InCartPM, Integer> amount;

    public Button goToProduct;
    public Button delete;
    public Button increase;
    public Button decrease;


    private static CustomerController customerController = CustomerController.getInstance();
    private CacheData cacheData = CacheData.getInstance();

    @FXML
    private void initialize() {
        buttons();
        binds();
        load();
    }

    private void buttons() {
        close.setOnAction(e -> ((Stage) close.getScene().getWindow()).close());
        minimize.setOnAction(e -> ((Stage) close.getScene().getWindow()).setIconified(true));
        back.setOnAction(e -> handleBackButton());
        purchase.setOnAction(e -> purchase());
        delete.setOnAction(e -> delete());
        increase.setOnAction(e -> increaseDecrease(1));
        decrease.setOnAction(e -> increaseDecrease(-1));
        goToProduct.setOnAction(e -> viewProduct());
    }

    private void handleBackButton() {
        try {
            Scene scene = new Scene(Main.loadFXML(back(), backForBackward()));
            Main.setSceneToStage(back, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void binds() {
        goToProduct.disableProperty().bind(Bindings.isEmpty(tableView.getSelectionModel().getSelectedItems()));
        increase.disableProperty().bind(Bindings.isEmpty(tableView.getSelectionModel().getSelectedItems()));
        decrease.disableProperty().bind(Bindings.isEmpty(tableView.getSelectionModel().getSelectedItems()));
        delete.disableProperty().bind(Bindings.isEmpty(tableView.getSelectionModel().getSelectedItems()));
    }

    private void load() {
        loadTable();
        loadTotalPrice();
    }

    private void loadTotalPrice() {
        totalPrice.setText("" + tableView.getItems().stream().mapToInt(InCartPM::getTotal).sum() + "$");
    }

    private void purchase() {
        try {
            Scene scene = new Scene(Main.loadFXML("Purchase", backForForward("Cart")));
            Main.setSceneToStage(back, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void delete() {
        String username = cacheData.getUsername();
        InCartPM inCartPM = tableView.getSelectionModel().getSelectedItem();
        int productId = inCartPM.getProduct().getId();
        try {
            customerController.deleteProductFromCart(username, productId);
            tableView.getItems().remove(inCartPM);
            tableView.refresh();
            loadTotalPrice();
        } catch (UserNotAvailableException | NoSuchAProductInCart e) {
            Notification.show("Error", e.getMessage(), back.getScene().getWindow(), true);
        }
    }

    private void viewProduct() {
        InCartPM inCartPM = tableView.getSelectionModel().getSelectedItem();
        int productId = inCartPM.getProduct().getId();
        cacheData.setProductId(productId);
        try {
            Scene scene = new Scene(Main.loadFXML("ProductDigest", backForForward("Cart")));
            Main.setSceneToStage(new Stage(StageStyle.UNDECORATED), scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void increaseDecrease(int change) {
        String username = cacheData.getUsername();
        InCartPM inCartPM = tableView.getSelectionModel().getSelectedItem();
        int productId = inCartPM.getProduct().getId();
        try {
            customerController.changeAmount(username, productId, change);
            int current = inCartPM.getAmount();
            int tobe = current + change;
            if (tobe == 0) {
                tableView.getItems().remove(inCartPM);
                tableView.refresh();
            } else {
                inCartPM.setAmount(tobe);
                tableView.refresh();
            }
            loadTotalPrice();
        } catch (UserNotAvailableException | NoSuchAProductInCart | NoSuchAProductException | NoSuchSellerException | NotEnoughAmountOfProductException e) {
            Notification.show("Error", e.getMessage(), back.getScene().getWindow(), true);
        }
    }

    private void loadTable() {
        product.setCellValueFactory(new PropertyValueFactory<>("product"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        afterOff.setCellValueFactory(new PropertyValueFactory<>("offPrice"));
        amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        tableView.setItems(loadTableData());
        tableView.setPlaceholder(new Label("No Product In Cart :("));
    }

    private ObservableList<InCartPM> loadTableData() {
        try {
            CartPM pm = customerController.viewCart(cacheData.getUsername());
            return FXCollections.observableArrayList(pm.getPurchases());
        } catch (UserNotAvailableException e) {
            e.printStackTrace();
        }
        return null;
    }
}
