package View.Controllers;

import ModelPackage.System.exeption.account.UserNotAvailableException;
import View.CacheData;
import View.Main;
import View.PrintModels.UserFullPM;
import com.jfoenix.controls.JFXButton;
import controler.AccountController;
import controler.CustomerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class CustomerAccount {
    public JFXButton cartButt;
    public JFXButton minimize;
    public JFXButton close;
    public JFXButton back;
    public JFXButton ordersButt;
    public JFXButton discountButt;
    public JFXButton messagesButt;
    public JFXButton changePassButt;
    public JFXButton logoutButt;
    public JFXButton editFNameButt;
    public JFXButton editLNameButt;
    public JFXButton editEMailButt;
    public JFXButton editPhoneButt;
    public ImageView image;
    public Label username;
    public Label fName;
    public Label lName;
    public Label email;
    public Label phone;

    private CustomerController customerController = CustomerController.getInstance();
    private AccountController accountController = AccountController.getInstance();
    private CacheData cacheData = CacheData.getInstance();

    private UserFullPM userFullPM;


    @FXML
    public void initialize(){
        handleButtons();
    }

    private void handleButtons() {
        cartButt.setOnAction(event -> handleCartButton());
        minimize.setOnAction(event -> Main.minimize());
        close.setOnAction(event -> Main.close());
        back.setOnAction(event -> handleBack());
        ordersButt.setOnAction(event -> handleOrders());
        discountButt.setOnAction(event -> handleDiscount());
        messagesButt.setOnAction(event -> handleMessages());
        changePassButt.setOnAction(event -> handleChangePass());
        logoutButt.setOnAction(event -> handleLogout());
        editFNameButt.setOnAction(event -> handleEditFName());
        editLNameButt.setOnAction(event -> handleEditLName());
        editEMailButt.setOnAction(event -> handleEditEmail());
        editPhoneButt.setOnAction(event -> handleEditPhone());
    }

    private void handleMessages() {
    }

    private void handleBack() {
        try {
            Main.setRoot("MainPage");
        } catch (IOException e) {
            System.out.println("Could Not Initialize Main Menu!!!");
        }
    }

    private void handleCartButton() {

    }

    private void handleOrders() {

    }

    private void handleDiscount() {

    }

    private void handleEditPhone() {
    }

    private void handleEditEmail() {
    }

    private void handleEditLName() {
    }

    private void handleEditFName() {
    }

    private void handleLogout() {
    }

    private void handleChangePass() {
    }


}
