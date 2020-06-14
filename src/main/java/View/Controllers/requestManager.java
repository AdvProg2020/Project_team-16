package View.Controllers;

import ModelPackage.System.exeption.product.NoSuchAProductException;
import ModelPackage.System.exeption.request.NoSuchARequestException;
import View.Main;
import View.PrintModels.RequestPM;
import com.jfoenix.controls.JFXButton;
import controler.ManagerController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class requestManager {

    public JFXButton back;
    public JFXButton minimize;
    public JFXButton close;
    public TableColumn<RequestPM, Integer> requestId;
    public TableColumn<RequestPM, String> requestType;
    public TableColumn<RequestPM, String> user;
    public TableColumn<RequestPM, String> request;
    public Button decline;
    public Button accept;
    public TableView<RequestPM> table;
    private static ManagerController managerController = ManagerController.getInstance();

    @FXML
    public void initialize() {
        buttonInitialize();
        tableInitialize();
    }

    private void buttonInitialize() {
        back.setOnAction(e -> doBack());
        minimize.setOnAction(e -> {
            Stage stage = (Stage) minimize.getScene().getWindow();
            stage.setIconified(true);
        });
        close.setOnAction(e -> {
            Stage stage = (Stage) close.getScene().getWindow();
            stage.close();
        });
        accept.setOnAction(e -> acceptRequest());
        decline.setOnAction(e -> declineRequest());
        accept.disableProperty().bind(Bindings.isEmpty(table.getSelectionModel().getSelectedItems()));
        decline.disableProperty().bind(Bindings.isEmpty(table.getSelectionModel().getSelectedItems()));
    }

    private void declineRequest() {
        RequestPM selected = table.getSelectionModel().getSelectedItem();
        int id = selected.getRequestId();
        try {
            managerController.declineRequest(id);
            table.getItems().remove(selected);
        } catch (NoSuchARequestException e) {
            e.printStackTrace();
        }
    }

    private void acceptRequest() {
        RequestPM selected = table.getSelectionModel().getSelectedItem();
        int id = selected.getRequestId();
        try {
            managerController.acceptRequest(id);
            table.getItems().remove(selected);
        } catch (NoSuchARequestException | NoSuchAProductException e) {
            e.printStackTrace();
        }
    }

    private void doBack() {
        try {
            back.getScene().setRoot(Main.loadFXML("ManagerAccount"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<RequestPM> load() {
        ArrayList<RequestPM> list = new ArrayList<RequestPM>();
        list.add(new RequestPM("Asghar", 321, "Create Product", "sadfasdf qwerdsaf sadfasdfwqerfsda sadf"));
        list.add(new RequestPM("Reza", 32, "Create Seller Account", "sadfasdf qwerdsaf sadfasdfwqerfsda sadf"));
        list.add(new RequestPM("TRRE", 452, "Edit Product", "sadfasdf qwerdsaf sadfasdfwqerfsda sadf"));
        list.add(new RequestPM("Lorem", 3897, "Create Off", "sadfasdf qwerdsaf sadfasdfwqerfsda sadf"));
        list.add(new RequestPM("Ipsum", 341, "Edit Off", "sadfasdf qwerdsaf sadfasdfwqerfsda sadf"));
        return list;
    }

    private void tableInitialize() {
        ArrayList<RequestPM> list = /*load();*/managerController.manageRequests();
        ObservableList<RequestPM> data = FXCollections.observableArrayList(list);
        requestId.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        requestType.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        user.setCellValueFactory(new PropertyValueFactory<>("requesterUserName"));
        request.setCellValueFactory(new PropertyValueFactory<>("request"));
        table.setItems(data);
    }
}
