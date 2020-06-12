package View.Controllers;

import ModelPackage.System.AccountManager;
import View.CacheData;
import View.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;

import java.io.IOException;
import java.util.concurrent.Callable;

public class SellerSignUp {
    public AnchorPane rootPane;
    public JFXButton back;
    public JFXTextField Company;
    public JFXButton signUpSubmit;
    public JFXButton createCompanyButt;
    private String[] info;

    @FXML
    public void initialize(){
        info = CacheData.getInstance().getSignUpData();
        int companyId = CacheData.getInstance().getCompanyID();
        if (companyId != 0){
            Company.setText(Integer.toString(companyId));
        }
        buttonsInitialize();
        fieldInitialize();
        CacheData.getInstance().setCompanyID(0);
    }

    private void fieldInitialize() {
        Company.textProperty().addListener(e->{
            Company.setFocusColor(Paint.valueOf("#405aa8"));
            Company.setPromptText("Company ID");
        });
    }

    private void buttonsInitialize() {
        back.setOnAction(e->{
            try {
                Main.setRoot("SignInUp");
            } catch (IOException ignore) {}
        });
        createCompanyButt.setOnAction(e ->{
            try {
                Main.setRoot("createCompany");
            } catch (IOException ignore) {}
        });
        signUpSubmit.setOnAction(e-> signUpRequest());
    }

    private void signUpRequest() {
        if (Company.getText().isEmpty()){
            Company.setPromptText("Company ID Is Required");
            Company.setFocusColor(Paint.valueOf("#c0392b"));
            Company.requestFocus();
        } else {
            String balance = info[6];
            info[6] = Company.getText();
            info[7] = balance;
            AccountManager.getInstance().createAccount(info,"seller");
            System.out.println("Done");
        }
    }

}
