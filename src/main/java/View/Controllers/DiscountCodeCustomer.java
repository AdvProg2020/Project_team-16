package View.Controllers;

import ModelPackage.System.exeption.account.UserNotAvailableException;
import View.CacheData;
import View.Main;
import View.PrintModels.DisCodeUserPM;
import com.jfoenix.controls.JFXButton;
import controler.CustomerController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiscountCodeCustomer extends BackAbleController {
    public JFXButton back;
    public JFXButton cartButt;
    public JFXButton minimize;
    public JFXButton close;
    public TableView<DisCodeUserPM> codesTable;
    public TableColumn<DisCodeUserPM, String> codeCol;
    public TableColumn<DisCodeUserPM, Integer> countCol;

    public Label percent;
    public Label code;
    public Label max;
    public Pane disCode;
    public Label date;

    private CustomerController customerController = CustomerController.getInstance();
    private CacheData cacheData = CacheData.getInstance();

    @FXML
    public void initialize(){
        initButtons();
        initCodeTable();
        disCode.visibleProperty().bind(Bindings.isEmpty(codesTable.getSelectionModel().getSelectedItems()).not());
    }

    private void initCodeTable() {
        codeCol.setCellValueFactory(new PropertyValueFactory<>("discountCode"));
        countCol.setCellValueFactory(new PropertyValueFactory<>("count"));

        //codesTable.setItems(getDisCodes());

        codesTable.setItems(getTestDisCodes());

        codesTable.getSelectionModel().selectedItemProperty().addListener((v, oldCode, newCode) -> updateCoupon(newCode));
    }

    private ObservableList<DisCodeUserPM> getDisCodes(){
        ObservableList<DisCodeUserPM> discodes = FXCollections.observableArrayList();

        try {
            discodes.addAll(customerController.viewDiscountCodes(cacheData.getUsername(), cacheData.getSorts()));
        } catch (UserNotAvailableException e) {
            Notification.show("Error", e.getMessage(), back.getScene().getWindow(), true);
            e.printStackTrace();
        }

        return discodes;
    }

    private ObservableList<DisCodeUserPM> getTestDisCodes(){
        ObservableList<DisCodeUserPM> discodes = FXCollections.observableArrayList();

        discodes.add(new DisCodeUserPM("new_year", new Date(), new Date(1321354), 50, 100, 20));
        discodes.add(new DisCodeUserPM("welcome_to_school99", new Date(3232), new Date(211), 98, 250, 3));
        discodes.add(new DisCodeUserPM("covid19", new Date(4984), new Date(564), 10, 50, 10));
        discodes.add(new DisCodeUserPM("hay_day2020", new Date(), new Date(321354), 80, 10, 2));

        return discodes;
    }

    private void updateCoupon(DisCodeUserPM newCode) {
        code.setText("\"" + newCode.getDiscountCode() + "\"");
        percent.setText(String.valueOf(newCode.getOffPercentage()));
        max.setText(newCode.getMaxOfPriceDiscounted() + "$");
        date.setText(new SimpleDateFormat("EEE, d MMM yyyy").format(newCode.getEndTime()));
    }

    private void initButtons() {
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
    }

    private void handleCart() {
        try {
            Scene scene = new Scene(Main.loadFXML("Cart", backForForward("DiscountCodeCustomer")));
            Main.setSceneToStage(back, scene);
        } catch (IOException e) {
            Notification.show("Error", e.getMessage(), back.getScene().getWindow(), true);
            e.printStackTrace();
        }
    }

    private void handleBack() {
        try {
            Scene scene = new Scene(Main.loadFXML(back(), backForBackward()));
            Main.setSceneToStage(back, scene);
        } catch (IOException e) {
            Notification.show("Error", e.getMessage(), back.getScene().getWindow(), true);
            e.printStackTrace();
        }
    }
}
