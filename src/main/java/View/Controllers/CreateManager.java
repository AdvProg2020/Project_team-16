package View.Controllers;

import View.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controler.AccountController;
import javafx.fxml.FXML;
import javafx.scene.paint.Paint;

import java.io.IOException;

public class CreateManager {

    public JFXButton back;
    public JFXTextField username;
    public JFXPasswordField password;
    public JFXPasswordField rePassword;
    public JFXTextField firstName;
    public JFXTextField lastName;
    public JFXTextField email;
    public JFXTextField phone;
    public JFXButton submit;

    private AccountController accountController = AccountController.getInstance();

    private static final Paint redColor = Paint.valueOf("#c0392b");
    private static final Paint blueColor = Paint.valueOf("#405aa8");
    private static final Paint greenColor = Paint.valueOf("#BBB529");

    @FXML
    public void initialize(){
        initButtons();
        initFields();
    }

    private void initFields() {
        resetSettingForFields(username,"Username");
        resetSettingForFields(firstName,"First Name");
        resetSettingForFields(lastName,"Last Name");
        resetSettingForFields(email,"Email");
        resetSettingForFields(phone,"Phone Number");
        resetSettingForFields(password,"Password");
        resetSettingForFields(rePassword,"Repeat Password");

        bindPassField();
    }

    private void bindPassField() {
        password.textProperty().addListener(e ->{
            if (password.getText().isEmpty()){
                password.setPromptText("Password is Required");
                password.setFocusColor(redColor);
            }
            int strength = calculatePasswordStrength(password.getText());
            if (strength == 0) {
                password.setPromptText("Password must be more than 8 character");
                password.setFocusColor(redColor);
            } else if (strength < 4) {
                password.setPromptText("Password is weak");
                password.setFocusColor(redColor);
            } else if (strength < 6) {
                password.setPromptText("Password is good");
                password.setFocusColor(greenColor);
            } else if (strength <= 8) {
                password.setPromptText("Password is strong");
                password.setFocusColor(greenColor);
            } else {
                password.setPromptText("Password is INSANE! Hey buddy it's not a FBI account :)");
                password.setFocusColor(greenColor);
            }
        });
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


    private void initButtons() {
        back.setOnAction(event -> handleBack());
        submit.setOnAction(event -> handleSubmit());
    }

    private void handleSubmit() {
        if (checkForEmptyValues()){
            sendSignUpRequest();
        }
    }

    private void sendSignUpRequest() {
        String[] info = new String[7];
        generateInfoPack(info);
        accountController.createAccount(info,"manager");
    }

    private void generateInfoPack(String[] infoPack) {
        infoPack[0] = username.getText();
        infoPack[1] = password.getText();
        infoPack[2] = firstName.getText();
        infoPack[3] = lastName.getText();
        infoPack[4] = email.getText();
        infoPack[5] = phone.getText();
    }

    private boolean checkForEmptyValues(){
        if (username.getText().isEmpty()){
            errorField(username,"Username Is Required");
            return false;
        } else if (!rePassword.getText().equals(password.getText())){
            errorField(rePassword,"Doesn't Match Above");
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


    private void handleBack() {
        try {
            Main.setRoot("MainPage"); //TODO: Specify the best back Destination!!!
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int calculatePasswordStrength(String password){
        int iPasswordScore = 0;

        if( password.length() < 8 )
            return 0;
        else if( password.length() >= 10 )
            iPasswordScore += 2;
        else
            iPasswordScore += 1;

        if( password.matches("(?=.*[0-9]).*") )
            iPasswordScore += 2;
        if( password.matches("(?=.*[a-z]).*") )
            iPasswordScore += 2;
        if( password.matches("(?=.*[A-Z]).*") )
            iPasswordScore += 2;
        if( password.matches("(?=.*[~!@#$%^&*()_-]).*") )
            iPasswordScore += 2;

        return iPasswordScore;
    }
}
