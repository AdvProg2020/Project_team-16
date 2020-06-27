package View.Controllers;

import ModelPackage.System.exeption.account.NotVerifiedSeller;
import ModelPackage.System.exeption.account.UserNotAvailableException;
import ModelPackage.System.exeption.account.WrongPasswordException;
import View.CacheData;
import View.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controler.AccountController;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

import java.io.IOException;

public class SignInUp {
    public JFXButton back;
    public JFXButton signInButton;
    public JFXButton signUpButton;
    public Pane signInPage;
    public Pane signUpPage;
    public JFXTextField usernameUp;
    public JFXPasswordField passwordUp;
    public JFXPasswordField rePasswordUp;
    public JFXTextField firstName;
    public JFXTextField lastName;
    public JFXTextField email;
    public JFXTextField phone;
    public JFXTextField balance;
    public JFXButton sellerBut;
    public JFXButton signUpSubmit;
    public JFXButton signInSubmit;
    public JFXTextField usernameIn;
    public JFXPasswordField passwordIn;

    private static final Paint redColor = Paint.valueOf("#c0392b");
    private static final Paint blueColor = Paint.valueOf("#405aa8");
    private static final Paint greenColor = Paint.valueOf("#BBB529");

    private static final AccountController accountController = AccountController.getInstance();

    @FXML
    public void initialize(){
        buttonInitialize();
        fieldsInitialize();
    }

    private void fieldsInitialize() {
        signInFields();
        signUpFields();
    }

    private void signInFields() {
        resetSettingForFields(usernameIn,"Username");
        resetSettingForFields(passwordIn,"Password");
    }

    private void signUpFields() {
        resetSettingForFields(usernameUp,"Username");
        resetSettingForFields(firstName,"First Name");
        resetSettingForFields(lastName,"Last Name");
        resetSettingForFields(email,"Email");
        resetSettingForFields(phone,"Phone Number");
        resetSettingForFields(rePasswordUp,"Repeat Password");
        resetSettingForFields(balance,"Balance");
        PasswordStrength.bindPassField(passwordUp);
    }

    private void resetSettingForFields(JFXTextField field,String prompt){
        field.textProperty().addListener(e->{
            field.setFocusColor(blueColor);
            field.setPromptText(prompt);
        });
    }

    private void resetSettingForFields(JFXPasswordField field, String prompt){
        field.textProperty().addListener(e->{
            field.setFocusColor(blueColor);
            field.setPromptText(prompt);
        });
    }

    private void buttonInitialize(){
        backInitialize();
        inUpSwitch();
        submitButtons();
    }

    private void submitButtons() {
        signInSubmit.setOnAction(e -> signInSubmitRequest());
        signUpSubmit.setOnAction(e -> signUpSubmitRequest());
        sellerBut.setOnAction(e -> sellerSignUpSubmitRequest());
    }

    private void sellerSignUpSubmitRequest() {
        if (checkForEmptyValues()){
            try {
                accountController.usernameInitialCheck(usernameUp.getText());
                String[] infoForSeller = new String[8];
                generateInfoPack(infoForSeller);
                CacheData.getInstance().setSignUpData(infoForSeller);
                try {
                    Main.setRoot("sellerSignUp");
                } catch (IOException ignore) {
                }
            } catch (UserNotAvailableException e) {
                errorField(usernameUp, "Username : This username is unavailable");
            }
        }
    }

    private void signUpSubmitRequest() {
        if (checkForEmptyValues()){
            try {
                accountController.usernameInitialCheck(usernameUp.getText());
                sendSignUpRequest();
                Notification.show("Successful", "Your Account was Created Successfully!!!", back.getScene().getWindow(), false);
                try {
                    Scene scene = new Scene(Main.loadFXML("MainPage"));
                    Main.setSceneToStage(back, scene);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (UserNotAvailableException e) {
                errorField(usernameUp, "Username Is Unavailable");
                //Notification.show("Error","Username UnAvailable",back.getScene().getWindow(),true);
                e.printStackTrace();
            }

        }else {
            Notification.show("Error", "Please Check The Fields.", back.getScene().getWindow(), true);
        }
    }

    private void sendSignUpRequest() {
        String[] info = new String[7];
        generateInfoPack(info);
        try {
            accountController.createAccount(info, "customer");
        } catch (UserNotAvailableException e) {
            errorField(usernameUp, "Username Is Invalid");
            e.printStackTrace();
        }
    }

    private void generateInfoPack(String[] infoPack) {
       infoPack[0] = usernameUp.getText();
       infoPack[1] = passwordUp.getText();
       infoPack[2] = firstName.getText();
       infoPack[3] = lastName.getText();
       infoPack[4] = email.getText();
       infoPack[5] = phone.getText();
       infoPack[6] = balance.getText();
    }

    private boolean checkForEmptyValues(){
        if (usernameUp.getText().isEmpty()){
            errorField(usernameUp,"Username Is Required");
            return false;
        } else if (passwordUp.getText().isEmpty()){
            return false;
        } else if (PasswordStrength.calculatePasswordStrength(passwordUp.getText()) < 4){
            return false;
        }  else if (!rePasswordUp.getText().equals(passwordUp.getText())){
            errorField(rePasswordUp,"Doesn't Match Above");
            return false;
        } else if (firstName.getText().isEmpty()) {
            errorField(firstName,"First Name Is Required");
            return false;
        } else if (lastName.getText().isEmpty()) {
            errorField(lastName,"Last Name Is Required");
            return false;
        } else if (email.getText().isEmpty()) {
            errorField(email,"Email Is Required");
            return false;
        } else if (!email.getText().matches(("\\S+@\\S+\\.(org|net|ir|com|uk|site)"))) {
            errorField(email,"Wrong Email Format");
            return false;
        } else if (phone.getText().isEmpty()){
            errorField(phone,"Phone Number Is Required");
            return false;
        } else if (balance.getText().isEmpty()){
            errorField(balance,"Balance Is Required");
            return false;
        } else if (!balance.getText().matches("(^\\d{1,19}$)?")){
            errorField(balance,"Balance Must Be Numerical");
            return false;
        }
        else return true;
    }

    private void errorField(JFXTextField field,String prompt){
        field.setPromptText(prompt);
        field.setFocusColor(redColor);
        field.requestFocus();
    }

    private void errorField(JFXPasswordField field,String prompt){
        field.setPromptText(prompt);
        field.setFocusColor(redColor);
        field.requestFocus();
    }

    private void signInSubmitRequest() {
        if (usernameIn.getText().isEmpty()){
            errorField(usernameIn,"Username Is Required *");
        }else if (passwordIn.getText().isEmpty()) {
            errorField(passwordIn,"Password Is Required");
        }
        else {
            sendSignInRequest();
            try {
                Scene scene = new Scene(Main.loadFXML("MainPage"));
                Main.setSceneToStage(back, scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendSignInRequest() {
        try {
            CacheData.getInstance().setRole(accountController.login(usernameIn.getText(), passwordIn.getText()));
            CacheData.getInstance().setUsername(usernameIn.getText());
            Notification.show("Successful", "Logged In Successfully!!!", back.getScene().getWindow(), false);
        } catch (NotVerifiedSeller e) {
            new OopsAlert().show("Your Account Isn't Verified Yet");
        } catch (UserNotAvailableException e) {
            errorField(usernameIn,"Username Not Exist");
        } catch (WrongPasswordException e){
            errorField(passwordIn,"Incorrect Password");
        }
    }

    private void inUpSwitch() {
        signInButton.setOnAction(e->{
            if (!signInPage.isVisible()){
                signUpPage.setVisible(false);
                signInPage.setVisible(true);
            }
        });

        signUpButton.setOnAction(e->{
            if (!signUpPage.isVisible()){
                signInPage.setVisible(false);
                signUpPage.setVisible(true);
            }
        });
    }

    private void backInitialize() {
        back.setOnAction(e->{
            try {
                Scene scene = new Scene(Main.loadFXML("MainPage"));
                Main.setSceneToStage(back, scene);
            } catch (IOException ignore) {}
        });
    }
}
