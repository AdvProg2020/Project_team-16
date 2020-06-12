package View.Controllers;

import ModelPackage.System.editPackage.UserEditAttributes;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import View.CacheData;
import View.Main;
import View.PrintModels.CompanyPM;
import View.PrintModels.UserFullPM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controler.AccountController;
import controler.SellerController;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;

import java.io.IOException;

public class SellerAccount {
    public JFXButton back;
    public JFXButton minimize;
    public JFXButton close;
    public JFXButton offMenuButt;
    public JFXButton productsButt;
    public JFXButton messagesButt;
    public JFXButton sellHistoryButt;
    public JFXButton changePassButt;
    public JFXButton logoutButt;
    public JFXButton newProduct;
    public JFXButton editFNameButt;
    public JFXButton editLNameButt;
    public JFXButton editEMailButt;
    public JFXButton editPhoneButt;
    public Label username;
    public Label fName;
    public Label lName;
    public Label email;
    public Label phone;
    public Label companyName;
    public Label companyPhone;
    public JFXTextField fNameText;
    public JFXTextField lNameText;
    public JFXTextField emailText;
    public JFXTextField phoneText;
    public JFXButton cancelButt;
    public JFXButton confirmButt;
    public ImageView image;

    private static final Paint redColor = Paint.valueOf("#c0392b");
    private static final Paint blueColor = Paint.valueOf("#405aa8");

    private AccountController accountController = AccountController.getInstance();
    private SellerController sellerController = SellerController.getInstance();
    private CacheData cacheData = CacheData.getInstance();

    private UserFullPM userFullPM;
    private CompanyPM companyPM;

    public void initialize(){
        handleButtons();
        setLabels();
    }

    private void setLabels() {
        try {
            userFullPM = accountController.viewPersonalInfo(cacheData.getUsername());
            companyPM = sellerController.viewCompanyInfo(cacheData.getUsername());
        } catch (UserNotAvailableException e) {
            System.out.println("User Not Found!!!");
        }

        username.setText(userFullPM.getUsername());
        fName.setText(userFullPM.getFirstName());
        lName.setText(userFullPM.getLastName());
        email.setText(userFullPM.getEmail());
        phone.setText(userFullPM.getPhoneNumber());
        companyName.setText(companyPM.getName());
        companyPhone.setText(companyPM.getPhone());
    }

    private void handleButtons() {
        minimize.setOnAction(event -> Main.minimize());
        close.setOnAction(event -> Main.close());
        back.setOnAction(event -> handleBack());
        offMenuButt.setOnAction(event -> handleOffs());
        productsButt.setOnAction(event -> handleProducts());
        messagesButt.setOnAction(event -> handleMessage());
        sellHistoryButt.setOnAction(event -> handleSellHistory());
        changePassButt.setOnAction(event -> handleChangePass());
        logoutButt.setOnAction(event -> handleLogout());
        editFNameButt.setOnAction(event -> handleEditFName());
        editLNameButt.setOnAction(event -> handleEditLName());
        editEMailButt.setOnAction(event -> handleEditEmail());
        editPhoneButt.setOnAction(event -> handleEditPhone());
        confirmButt.setOnAction(event -> handleConfirm());
        cancelButt.setOnAction(event -> handleCancel());
        newProduct.setOnAction(event -> handleNewProduct());
    }



}
