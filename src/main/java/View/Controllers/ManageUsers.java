package View.Controllers;

import ModelPackage.System.exeption.account.UserNotAvailableException;
import View.Main;
import View.PrintModels.UserFullPM;
import com.jfoenix.controls.JFXButton;
import controler.AccountController;
import controler.ManagerController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageUsers extends BackAbleController {

    public JFXButton back;
    public JFXButton minimize;
    public JFXButton close;
    public TableView<UserFullPM> table;
    public TableColumn<UserFullPM, String> usernameCol;
    public TableColumn<UserFullPM, String> roleCol;
    public ImageView userImage;
    public Label username;
    public Label role;
    public Label lastname;
    public Label firstname;
    public Label email;
    public Label phone;
    public JFXButton deleteBtn;

    private ManagerController managerController = ManagerController.getInstance();

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
        deleteBtn.setOnAction(event -> handleDeleteUser());
        deleteBtn.disableProperty().bind(Bindings.isEmpty(table.getSelectionModel().getSelectedItems()));
    }

    private void handleBack() {
        try {
            Scene scene = new Scene(Main.loadFXML(back(), backForBackward()));
            Main.setSceneToStage(back, scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initUsersTable() {
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

        table.setItems(getUsers());
        //table.setItems(getTestUsers());

        table.getSelectionModel().selectedItemProperty().addListener( (v, oldUser, newUser) -> changeData(newUser));
    }

    private ObservableList<UserFullPM> getUsers() {
        ObservableList<UserFullPM> users = FXCollections.observableArrayList();
        users.addAll(managerController.manageUsers());

        return users;
    }

    private ObservableList<UserFullPM> getTestUsers(){
        ObservableList<UserFullPM> users = FXCollections.observableArrayList();

        users.add(new UserFullPM("marmof", "Mohamad", "Mofi", "mofi@gmail.com", "98 913 255 2322", "Seller"));
        users.add(new UserFullPM("hatam", "Ali", "Hata", "hatam@gmail.com", "98 912 342 2156", "Manager"));
        users.add(new UserFullPM("sapa", "Sajad", "Paki", "sapa@gmail.com", "98 920 156 7894", "Seller"));
        users.add(new UserFullPM("kimmi", "Kimmi", "idontknow", "kimmi@gmail.com", "98 913 442 4653", "Customer"));
        users.add(new UserFullPM("memo", "Mehran", "Montaz", "memo@gmail.com", "98 919 111 1600", "Customer"));

        return users;
    }

    private void changeData(UserFullPM newUser) {
        username.setText(newUser.getUsername());
        role.setText(newUser.getRole());
        firstname.setText(newUser.getFirstName());
        lastname.setText(newUser.getLastName());
        email.setText(newUser.getEmail());
        phone.setText(newUser.getPhoneNumber());
        userImage.setImage(AccountController.getInstance().userImage(newUser.getUsername()));
    }

    private void handleDeleteUser() {
        UserFullPM user = table.getSelectionModel().getSelectedItem();
        try {
            managerController.deleteUser(user.getUsername());
        } catch (UserNotAvailableException e) {
            e.printStackTrace();
        }

        ObservableList<UserFullPM> users = table.getItems();
        users.remove(user);

        Notification.show("Successful", "User was Deleted Successfully!!!", back.getScene().getWindow(), false);
    }
}
