package View.Controllers;

import ModelPackage.System.exeption.account.UserNotAvailableException;
import View.Main;
import View.PrintModels.UserMiniPM;
import com.jfoenix.controls.JFXButton;
import controler.ManagerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Data;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import java.io.IOException;
import java.util.Iterator;

public class ManageUsers {

    public JFXButton back;
    public JFXButton minimize;
    public JFXButton close;
    public TableView<RowFactory> table;
    public TableColumn<RowFactory, String> usernameCol;
    public TableColumn<RowFactory, ComboBox<String>> roleCol;
    public TableColumn<RowFactory, Button> viewUserCol;
    public TableColumn<RowFactory, Button> deleteUserCol;

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
    }

    private void handleBack() {
        try {
            Main.setRoot("ManagerAccount");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initUsersTable() {
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
        viewUserCol.setCellValueFactory(new PropertyValueFactory<>("view"));
        deleteUserCol.setCellValueFactory(new PropertyValueFactory<>("delete"));

        table.setItems(getUsers());
        //table.setItems(getTestUsers());
    }

    private ObservableList<RowFactory> getUsers() {
        ObservableList<RowFactory> users = FXCollections.observableArrayList();
        for (UserMiniPM user : managerController.manageUsers()) {
            users.add(new RowFactory(user.getUsername(), getRolesBox(user.getRole()), getViewButton(), getDeleteButton(user.getUsername())));
        }

        return users;
    }

    private ObservableList<RowFactory> getTestUsers(){
        ObservableList<RowFactory> users = FXCollections.observableArrayList();
        users.add(new RowFactory("sapa", getRolesBox("Seller"), getViewButton(), getDeleteButton("sapa")));
        users.add(new RowFactory("marmof", getRolesBox("Manager"), getViewButton(), getDeleteButton("marmof")));
        users.add(new RowFactory("kimi", getRolesBox("Customer"), getViewButton(), getDeleteButton("kimi")));
        users.add(new RowFactory("hatam", getRolesBox("Customer"), getViewButton(), getDeleteButton("hatam")));

        return users;
    }

    private ComboBox<String> getRolesBox(String role) {
        ComboBox<String> roles = new ComboBox<>();
        roles.setPromptText(role);
        roles.getItems().addAll("Customer", "Seller", "Manager");

        return roles;
    }

    private Button getViewButton() {
        Button view = new Button("View User");
        view.setOnAction(event -> handleViewUser());

        return view;
    }

    private void handleViewUser() {
        //TODO : View User Should Be Implemented!!!
    }

    private Button getDeleteButton(String username) {
        Button delete = new Button("Delete User");
        delete.setOnAction(event -> handleDeleteUser(username, delete));
        //delete.setFont(Font.loadFont(String.valueOf(FontAwesome.TRASH), 10));
        //TODO: Add Trash Icon To Delete!!!

        return delete;
    }

    private void handleDeleteUser(String username, Button button) {
        ObservableList<RowFactory> users = table.getItems();
        try {
            managerController.deleteUser(username);
        } catch (UserNotAvailableException e) {
            e.printStackTrace();
        }

        Iterator iterator = users.iterator();
        while (iterator.hasNext()){
            RowFactory user = (RowFactory) iterator.next();
            if (user.delete.equals(button)) {
                table.getItems().remove(user);
                break;
            }
        }
    }

    @Data
    public class RowFactory{
        private String username;
        private ComboBox<String> role;
        private Button view;
        private Button delete;

        public RowFactory(String username, ComboBox<String> role, Button view, Button delete) {
            this.username = username;
            this.role = role;
            this.view = view;
            this.delete = delete;
        }
    }

}
