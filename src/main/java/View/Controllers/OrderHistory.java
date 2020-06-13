package View.Controllers;

import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.clcsmanager.NoSuchALogException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import View.CacheData;
import View.Data;
import View.Main;
import View.PrintModels.MiniProductPM;
import View.PrintModels.OrderLogPM;
import View.PrintModels.OrderMiniLogPM;
import View.PrintModels.OrderProductPM;
import com.jfoenix.controls.JFXButton;
import controler.CustomerController;
import controler.ProductController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.naming.Binding;
import java.io.IOException;
import java.util.ArrayList;

public class OrderHistory {
    public JFXButton back;
    public JFXButton cartButt;
    public JFXButton minimize;
    public JFXButton close;
    public Button viewProduct;
    public TableView<OrderMiniLogPM> orderTable;
    public TableColumn<OrderMiniLogPM, Integer> orderNoColumn;
    public TableColumn<OrderMiniLogPM, Data> dateColumn;
    public Label no;
    public Label date;
    public Label price;
    public Label discount;
    public Label delStatus;
    public TableView<OrderProductPM> productsTable;
    public TableColumn<OrderProductPM, String> pNameCol;
    public TableColumn<OrderProductPM, String> pSellerCol;
    public TableColumn<OrderProductPM, Long> pPriceCol;

    private CustomerController customerController = CustomerController.getInstance();
    private CacheData cacheData = CacheData.getInstance();
    private final String username = cacheData.getUsername();

    @FXML
    public void initialize(){
        initButts();
        initOrdersTable();
    }

    private void initOrdersTable() {
        orderNoColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        orderTable.setItems(getOrders());

        orderTable.getSelectionModel().selectedItemProperty().addListener( (v, oldOrder, newOrder) -> {
            try {
                changeData(customerController.showOrder(newOrder.getOrderId()), newOrder.getOrderId());
            } catch (NoSuchAProductException | NoSuchALogException e) {
                System.out.println("Oops!!!");
            }
        });
    }

    private void changeData(OrderLogPM order, int orderNo){
        no.setText(String.valueOf(orderNo));
        date.setText(order.getDate().toString());
        price.setText(String.valueOf(order.getPrice()));
        discount.setText(String.valueOf(order.getDiscount()));
        delStatus.setText(order.getDeliveryStatus());
        changeProductTable(order.getProductPMs());
    }

    private void changeProductTable(ArrayList<OrderProductPM> products) {
        pNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        pSellerCol.setCellValueFactory(new PropertyValueFactory<>("seller"));
        pPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productsTable.setItems(getProducts(products));
    }

    private ObservableList<OrderProductPM> getProducts(ArrayList<OrderProductPM> productPMS){
        ObservableList<OrderProductPM> products = FXCollections.observableArrayList();
        products.addAll(productPMS);
        return products;
    }

    private ObservableList<OrderMiniLogPM> getOrders(){
        ObservableList<OrderMiniLogPM> orders = FXCollections.observableArrayList();

        try {
            orders.addAll(customerController.viewOrders(username));
        } catch (UserNotAvailableException e) {
            System.out.println("User Not Found!!!");
        }

        return orders;
    }

    private void initButts() {
        minimize.setOnAction(e -> {
            Stage stage = (Stage) minimize.getScene().getWindow();
            stage.setIconified(true);
        });
        close.setOnAction(e -> {
            Stage stage = (Stage) close.getScene().getWindow();
            stage.close();
        });
        back.setOnAction(event -> handleBack());
        cartButt.setOnAction(event -> handleCart());
        viewProduct.setOnAction(event -> handleViewProduct());
        viewProduct.disableProperty().bind(Bindings.isEmpty(productsTable.getSelectionModel().getSelectedCells()));
    }

    private void handleViewProduct() {
        try {
            Main.setRoot("ProductDigest");
            int productId = ProductController.getInstance().getProductIdByName(productsTable.getSelectionModel().getSelectedItem().getName());
            cacheData.setProductId(productId);
            //TODO: Add Product ID to CacheData!!!
        } catch (IOException e) {
            System.out.println("Could Not Initialize Main Menu!!!");
        }
    }

    private void handleCart() {
        try {
            Main.setRoot("Cart");
        } catch (IOException e) {
            System.out.println("Could Not Initialize Main Menu!!!");
        }
    }

    private void handleBack() {
        try {
            Main.setRoot("CustomerAccount");
        } catch (IOException e) {
            System.out.println("Could Not Initialize Main Menu!!!");
        }
    }

}
