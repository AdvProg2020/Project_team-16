package View.Controllers;

import View.CacheData;
import View.Main;
import View.PrintModels.RequestPM;
import com.jfoenix.controls.JFXButton;
import controler.AccountController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class RequestView extends BackAbleController {
    public JFXButton backButt;
    public JFXButton minimize;
    public JFXButton close;

    public TableView<RequestPM> table;
    public TableColumn<RequestPM, Integer> id;
    public TableColumn<RequestPM, String> request;
    public TableColumn<RequestPM, String> status;

    @FXML
    private void initialize() {
        buttons();
        loadRequests();
    }

    private void buttons() {
        close.setOnAction(event -> ((Stage) close.getScene().getWindow()).close());
        minimize.setOnAction(event -> ((Stage) close.getScene().getWindow()).setIconified(true));
        backButt.setOnAction(event -> {
            try {
                Scene scene = new Scene(Main.loadFXML(back(), backForBackward()));
                Main.setSceneToStage(backButt, scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadRequests() {
        CacheData cacheData = CacheData.getInstance();
        List<RequestPM> pms = AccountController.getInstance()
                .viewRequestSent(cacheData.getUsername(), cacheData.getRole());
        if (pms != null) {
            loadTable(pms);
        }
    }

    private void loadTable(List<RequestPM> pms) {
        ObservableList<RequestPM> data = FXCollections.observableArrayList(pms);
        id.setCellValueFactory(new PropertyValueFactory<>("requestId"));
        status.setCellValueFactory(new PropertyValueFactory<>("requestType"));
        request.setCellValueFactory(new PropertyValueFactory<>("request"));
        table.setItems(data);
    }
}
