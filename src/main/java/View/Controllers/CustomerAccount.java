package View.Controllers;

import ModelPackage.System.editPackage.UserEditAttributes;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import View.CacheData;
import View.Main;
import View.PrintModels.UserFullPM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controler.AccountController;
import controler.CustomerController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    public JFXTextField fNameText;
    public JFXTextField lNameText;
    public JFXTextField emailText;
    public JFXTextField phoneText;
    public JFXButton cancelButt;
    public JFXButton confirmButt;
    private static final Paint redColor = Paint.valueOf("#c0392b");
    private static final Paint blueColor = Paint.valueOf("#405aa8");

    private AccountController accountController = AccountController.getInstance();
    private CacheData cacheData = CacheData.getInstance();

    private UserFullPM userFullPM;

    @FXML
    public void initialize(){
        handleButtons();
        setLabels();
    }

    private void setLabels() {
        try {
            userFullPM = accountController.viewPersonalInfo(cacheData.getUsername());
        } catch (UserNotAvailableException e) {
            System.out.println("User Not Found!!!");
        }

        username.setText(userFullPM.getUsername());
        fName.setText(userFullPM.getFirstName());
        lName.setText(userFullPM.getLastName());
        email.setText(userFullPM.getEmail());
        phone.setText(userFullPM.getPhoneNumber());
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
        confirmButt.setOnAction(event -> handleConfirm());
        cancelButt.setOnAction(event -> handleCancel());
    }

    private void handleBack() {
        try {
            Main.setRoot("MainPage");
        } catch (IOException e) {
            System.out.println("Could Not Initialize Main Menu!!!");
        }
    }

    private void handleMessages() {
        try {
            Main.setRoot("MessageMenu");
        } catch (IOException e) {
            System.out.println("");
        }
    }

    private void handleCartButton() {
        try {
            Main.setRoot("Cart");
        } catch (IOException e) {
            System.out.println("");
        }
    }

    private void handleOrders() {
        try {
            Main.setRoot("OrderHistory");
        } catch (IOException e) {
            System.out.println("");
        }
    }

    private void handleDiscount() {
        try {
            Main.setRoot("DiscountCodeCustomer");
        } catch (IOException e) {
            System.out.println("");
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
