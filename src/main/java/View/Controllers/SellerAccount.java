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
import javafx.stage.Stage;

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
        minimize.setOnAction(e -> {
            Stage stage = (Stage) minimize.getScene().getWindow();
            stage.setIconified(true);
        });
        close.setOnAction(e -> {
            Stage stage = (Stage) close.getScene().getWindow();
            stage.close();
        });
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

    private void handleNewProduct() {
        try {
            Main.setRoot("newProduct");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleSellHistory() {
        try {
            Main.setRoot("SaleHistory");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMessage() {
        try {
            Main.setRoot("Message");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleProducts() {
        try {
            Main.setRoot("productManagePage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleOffs() {
        try {
            Main.setRoot("OffManager");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleBack() {
        try {
            Main.setRoot("MainPage");
        } catch (IOException e) {
            System.out.println("Could Not Initialize Main Menu!!!");
        }
    }

    private void handleEditPhone() {
        enableEditFields(phoneText, phone);
        enableEditButts();
    }

    private void handleEditEmail() {
        enableEditFields(emailText, email);
        enableEditButts();
    }

    private void handleEditLName() {
        enableEditFields(lNameText, lName);
        enableEditButts();
    }

    private void handleEditFName() {
        enableEditFields(fNameText, fName);
        enableEditButts();
    }

    private void handleLogout() {
        // TODO : logout should be implemented!!!
    }

    private void enableEditFields(JFXTextField field, Label label) {
        label.setVisible(false);
        field.setVisible(true);
        field.setPromptText(label.getText());
    }

    private void disableEditFields(JFXTextField field, Label label) {
        label.setVisible(true);
        field.setVisible(false);
    }

    private void enableEditButts() {
        confirmButt.setVisible(true);
        cancelButt.setVisible(true);
    }

    private void handleChangePass() {
        EditPassDialog editPassDialog = new EditPassDialog();
        editPassDialog.show();
        String newPass = editPassDialog.show();
        UserEditAttributes attributes = new UserEditAttributes();
        attributes.setNewPassword(newPass);

        try {
            accountController.editPersonalInfo(cacheData.getUsername(), attributes);
        } catch (UserNotAvailableException e) {
            System.out.println("User Not Found!!!");
        }
    }

    private void handleCancel() {
        disableEditFields(phoneText, phone);
        disableEditFields(emailText, email);
        disableEditFields(fNameText, fName);
        disableEditFields(lNameText, lName);

        resetSettingForFields(phoneText, phone.getText());
        resetSettingForFields(emailText, email.getText());
        resetSettingForFields(lNameText, lName.getText());
        resetSettingForFields(fNameText, fName.getText());

        confirmButt.setVisible(false);
        cancelButt.setVisible(false);
    }

    private void handleConfirm() {
        UserEditAttributes attributes = new UserEditAttributes();
        updateEditAttributes(attributes);
        try {
            accountController.editPersonalInfo(cacheData.getUsername(), attributes);
        } catch (UserNotAvailableException e) {
            System.out.println("User Not Found!!!");
        }

        confirmButt.setVisible(false);
        cancelButt.setVisible(false);
    }

    private void updateEditAttributes(UserEditAttributes attributes) {
        if (phoneText.isVisible() && !checkInput(phoneText)) {
            if (phoneText.getText().matches("\\d+")) {
                attributes.setNewPhone(phoneText.getText());
            } else {
                errorField(phoneText,"Wrong Phone Number Format");
            }
        } else if (emailText.isVisible() && !checkInput(emailText)) {
            if (emailText.getText().matches(("\\S+@\\S+\\.(org|net|ir|com|uk|site)"))){
                attributes.setNewEmail(emailText.getText());
            } else {
                errorField(emailText,"Wrong Email Format");
            }
        } else if (fNameText.isVisible() && !checkInput(fNameText)) {
            attributes.setNewFirstName(fNameText.getText());
        } else if (lNameText.isVisible() && !checkInput(lNameText)) {
            attributes.setNewLastName(lNameText.getText());
        }
    }

    private boolean checkInput(JFXTextField field) {
        return field.getText().isEmpty();
    }

    private void errorField(JFXTextField field,String prompt){
        field.setPromptText(prompt);
        field.setFocusColor(redColor);
        field.requestFocus();
    }

    private void resetSettingForFields(JFXTextField field,String prompt){
        field.textProperty().addListener(e->{
            field.setFocusColor(blueColor);
            field.setPromptText(prompt);
        });
    }


}
