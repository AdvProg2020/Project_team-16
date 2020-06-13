package View.Controllers;

import ModelPackage.Log.DeliveryStatus;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.clcsmanager.NoSuchALogException;
import ModelPackage.System.exeption.product.NoSuchAProductException;
import View.CacheData;
import View.Main;
import View.PrintModels.OrderLogPM;
import View.PrintModels.OrderProductPM;
import com.jfoenix.controls.JFXButton;
import controler.CustomerController;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OrderHistory {
    public JFXButton back;
    public JFXButton cartButt;
    public JFXButton minimize;
    public JFXButton close;
    public Button viewProduct;
    public TableView<OrderLogPM> orderTable;
    public TableColumn<OrderLogPM, Integer> orderNoColumn;
    public TableColumn<OrderLogPM, String> dateColumn;
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
            changeData(newOrder);
        });
    }

    private void changeData(OrderLogPM order){
        no.setText(String.valueOf(order.getOrderId()));
        date.setText(order.getDate());
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

    private ObservableList<OrderLogPM> getOrders(){
        ObservableList<OrderLogPM> orders = FXCollections.observableArrayList();

        try {
            orders.addAll(customerController.viewOrders(username));
        } catch (UserNotAvailableException e) {
            System.out.println("User Not Found!!!");
        } catch (NoSuchALogException | NoSuchAProductException e) {
            e.printStackTrace();
        }

        //orders.addAll(loadOrders());

        return orders;
    }

    private ArrayList<OrderProductPM> loadProducts(){
        ArrayList<OrderProductPM> products = new ArrayList<>();
        products.add(new OrderProductPM(2, "pashmak", "hatam", 1000));
        products.add(new OrderProductPM(23, "dullForKimmi", "marmof", 25000));
        products.add(new OrderProductPM(2, "skirtForKimmi", "sapa", 35000));
        products.add(new OrderProductPM(2, "sweetForKimmi", "marmof", 500));

        return products;
    }

    private ArrayList<OrderLogPM> loadOrders(){
        ArrayList<OrderLogPM> orders = new ArrayList<>();
        orders.add(new OrderLogPM(1552, "2007-12-14 13:25:44", loadProducts(), DeliveryStatus.DELIVERED.toString(), 150000, 200000, 10));
        orders.add(new OrderLogPM(1342, "2003-05-16 19:14:45", loadProducts(), DeliveryStatus.DEPENDING.toString(), 20000, 1244, 50));
        orders.add(new OrderLogPM(1226, "2003-03-23 14:33:03", loadProducts(), DeliveryStatus.DELIVERED.toString(), 32500, 32555, 0));
        orders.add(new OrderLogPM(2588, "2012-09-12 21:23:22", loadProducts(), DeliveryStatus.DELIVERED.toString(), 13700, 45200, 2));

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
            cacheData.setProductId(productsTable.getSelectionModel().getSelectedItem().getId());
        } catch (IOException e) {
            System.out.println("Could Not Initialize Product Menu!!!");
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
