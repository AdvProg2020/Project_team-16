package View.Controllers;

import ModelPackage.Users.User;
import View.Main;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageUsers {

    public JFXButton back;
    public JFXButton minimize;
    public JFXButton close;
    public TableView<User> table;
    public TableColumn<User, String> usernameCol;
    public TableColumn<User, ComboBox<String>> roleCol;
    public TableColumn<User, Button> viewUserCol;
    public TableColumn<User, Button> deleteUserCol;

    @FXML
    public void initialize(){
        initButtons();
        initUsersTable();
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
    }

    private void handleBack() {
        try {
            Main.setRoot("ManagerAccount");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
