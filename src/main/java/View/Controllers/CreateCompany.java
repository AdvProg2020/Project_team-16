package View.Controllers;

import View.CacheData;
import View.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controler.AccountController;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

import java.io.IOException;

public class CreateCompany {
    public JFXButton back;
    public JFXTextField name;
    public JFXTextField category;
    public JFXTextField phone;
    public JFXButton createButt;
    public AnchorPane rootPane;
    private static final Paint redColor = Paint.valueOf("#c0392b");
    private static final Paint blueColor = Paint.valueOf("#405aa8");

    @FXML
    public void initialize() {
        buttonInitialize();
        fieldsInitialize();
    }

    private void buttonInitialize() {
        back.setOnAction(e->{
            try {
                Main.setRoot("sellerSignUp");
            } catch (IOException ignore) {}
        });
        createButt.setOnAction(e -> {
            if (checkForErrors()){
                sendCompanyRequestSend();
            }
        });
    }

    private void sendCompanyRequestSend() {
        String[] info = new String[3];
        info[0] = name.getText();
        info[1] = phone.getText();
        info[2] = category.getText();
        int id = AccountController.getInstance().createCompany(info);
        CacheData.getInstance().setCompanyID(id);
    }

    private boolean checkForErrors() {
        if (name.getText().isEmpty()) {
            errorField(name,"Name Is Required");
            return false;
        } else if (category.getText().isEmpty()) {
            errorField(category,"Company Category Is Required");
            return false;
        } else if (phone.getText().isEmpty()) {
            errorField(phone,"Phone Is Required");
            return false;
        } else return true;
    }

    private void fieldsInitialize() {
        resetSettingForFields(name,"Company Name");
        resetSettingForFields(phone,"Phone");
        resetSettingForFields(category,"Company Category");
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
