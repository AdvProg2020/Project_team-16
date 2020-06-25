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
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;

public class CustomerAccount extends BackAbleController {
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
    public JFXButton chooseProf;
    public Circle imageCircle;
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
    public JFXButton requestsButt;

    private static final Paint redColor = Paint.valueOf("#c0392b");
    private static final Paint blueColor = Paint.valueOf("#405aa8");
    private static final String userPhoto = "/Images/user-png-icon-male-user-icon-512.png";


    private AccountController accountController = AccountController.getInstance();
    private CacheData cacheData = CacheData.getInstance();

    private UserFullPM userFullPM;

    @FXML
    public void initialize(){
        handleButtons();
        setLabels();
        loadImage();
    }

    private void loadImage() {
        Image image = accountController.userImage(username.getText());
        if (image == null) {
            image = new Image(userPhoto);
        }
        imageCircle.setFill(new ImagePattern(image));
    }

    private void setLabels() {
        try {
            userFullPM = accountController.viewPersonalInfo(cacheData.getUsername());
        } catch (UserNotAvailableException e) {
            System.out.println("User Not Found!!!");
        }

        // userFullPM = getTestUser();

        username.setText(userFullPM.getUsername());
        fName.setText(userFullPM.getFirstName());
        lName.setText(userFullPM.getLastName());
        email.setText(userFullPM.getEmail());
        phone.setText(userFullPM.getPhoneNumber());
    }

    private UserFullPM getTestUser(){
        return new UserFullPM(
                "marmof",
                "Mohamad",
                "Mofayezi",
                "marmof@gmail.com",
                "989132255442",
                "Customer"
        );
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
        cartButt.setOnAction(event -> handleCartButton());
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
        chooseProf.setOnAction(event -> handleChooseProf());
        requestsButt.setOnAction(event -> handleRequestView());
    }

    private void handleRequestView() {
        try {
            Scene scene = new Scene(Main.loadFXML("RequestView", backForForward("CustomerAccount")));
            Main.setSceneToStage(requestsButt, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleChooseProf() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpeg", "*.png", "*.jpg"));
        File selected = fileChooser.showOpenDialog(back.getScene().getWindow());

        if (selected != null) {
            Image toImage = new Image(String.valueOf(selected.toURI()));
            imageCircle.setFill(new ImagePattern(toImage));
            try {
                InputStream inputStream = new FileInputStream(selected);
                accountController.saveNewImage(inputStream, username.getText());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Notification.show("Successful", "Your Profile Pic was Updated!!!", back.getScene().getWindow(), false);
        }
    }

    private void handleBack() {
        try {
            Scene scene = new Scene(Main.loadFXML(back(), backForBackward()));
            Main.setSceneToStage(back, scene);
        } catch (IOException e) {
            System.out.println("Could Not Initialize Main Menu!!!");
        }
    }

    private void handleMessages() {
        try {
            Scene scene = new Scene(Main.loadFXML("Messages", backForForward("CustomerAccount")));
            Main.setSceneToStage(back, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleCartButton() {
        try {
            Scene scene = new Scene(Main.loadFXML("Cart", backForForward("CustomerAccount")));
            Main.setSceneToStage(back, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleOrders() {
        try {
            Scene scene = new Scene(Main.loadFXML("OrderHistory", backForForward("CustomerAccount")));
            Main.setSceneToStage(back, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDiscount() {
        try {
            Scene scene = new Scene(Main.loadFXML("DiscountCodeCustomer", backForForward("CustomerAccount")));
            Main.setSceneToStage(back, scene);
        } catch (IOException e) {
            e.printStackTrace();
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
        // TODO: 6/22/2020
        CacheData.getInstance().logout();
        try {
            back.getScene().setRoot(Main.loadFXML("MainPage"));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        String newPass = editPassDialog.show();

        if (newPass != null) {
            UserEditAttributes attributes = new UserEditAttributes();
            attributes.setNewPassword(newPass);
            try {
                accountController.editPersonalInfo(cacheData.getUsername(), attributes);
            } catch (UserNotAvailableException e) {
                e.printStackTrace();
            }
            Notification.show("Successful", "Your Password was Changed!!!", back.getScene().getWindow(), false);
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
        if (updateEditAttributes(attributes)) {
            try {
                accountController.editPersonalInfo(cacheData.getUsername(), attributes);
            } catch (UserNotAvailableException e) {
                e.printStackTrace();
            }

            confirmButt.setVisible(false);
            cancelButt.setVisible(false);

            Notification.show("Successful", "Your Information was Updated!!!", back.getScene().getWindow(), false);
        }
    }

    private boolean updateEditAttributes(UserEditAttributes attributes) {
        if (fNameText.isVisible() && !checkInput(fNameText)) {
            attributes.setNewFirstName(fNameText.getText());
            fName.setText(fNameText.getText());
            return true;
        } else if (lNameText.isVisible() && !checkInput(lNameText)) {
            attributes.setNewLastName(lNameText.getText());
            lName.setText(lNameText.getText());
            return true;
        } else if (phoneText.isVisible() && !checkInput(phoneText)) {
            if (phoneText.getText().matches("\\d+")) {
                attributes.setNewPhone(phoneText.getText());
                phone.setText(phoneText.getText());
                return true;
            } else {
                errorField(phoneText,"Wrong Phone Number Format");
                return false;
            }
        } else if (emailText.isVisible() && !checkInput(emailText)) {
            if (emailText.getText().matches(("\\S+@\\S+\\.(org|net|ir|com|uk|site)"))){
                attributes.setNewEmail(emailText.getText());
                email.setText(emailText.getText());
                return true;
            } else {
                errorField(emailText,"Wrong Email Format");
                return false;
            }
        }
        return false;
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
