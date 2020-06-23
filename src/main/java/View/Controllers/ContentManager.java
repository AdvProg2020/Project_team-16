package View.Controllers;

import ModelPackage.Users.MainContent;
import View.Main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import controler.ContentController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import static View.Controllers.Notification.show;

public class ContentManager {
    public JFXButton back;
    public JFXButton minimize;
    public JFXButton close;
    public JFXTextField crTitle;
    public JFXTextArea crContent;
    public Label title;
    public Text content;
    public ListView<MainContent> contentList;
    public JFXButton submit;
    public JFXButton remove;
    public JFXButton refresh;

    private static ContentController contentController = ContentController.getInstance();

    @FXML
    public void initialize() {
        load();
        buttons();
        binds();
        listeners();
    }

    private void listeners() {
        contentList.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue != null) {
                loadContent(newValue);
            }
        });
    }

    private void loadContent(MainContent newValue) {
        title.setText(newValue.getTitle());
        content.setText(newValue.getContent());
    }

    private void binds() {
        remove.disableProperty().bind(Bindings.isEmpty(contentList.getSelectionModel().getSelectedItems()));
        submit.disableProperty().bind(crTitle.textProperty().isEmpty().or(crContent.textProperty().isEmpty()));
    }

    private void load() {
        ObservableList<MainContent> data = FXCollections
                .observableArrayList(ContentController.getInstance().getMainContents());
        contentList.setItems(data);
    }

    private void buttons() {
        close.setOnAction(event -> ((Stage) close.getScene().getWindow()).close());
        minimize.setOnAction(event -> ((Stage) close.getScene().getWindow()).setIconified(true));
        back.setOnAction(event -> handleBack());
        submit.setOnAction(event -> handleSubmit());
        remove.setOnAction(event -> handleDelete());
        refresh.setOnAction(event -> {
            contentList.getItems().clear();
            load();
        });
    }

    private void handleDelete() {
        MainContent item = contentList.getSelectionModel().getSelectedItem();
        contentController.deleteContent(
                item.getId()
        );
        show("Successful", "Done!",
                close.getScene().getWindow(), false);
    }

    private void handleSubmit() {
        contentController.addContent(crTitle.getText(), crContent.getText());
        show("Successful", "Done! Reload To See It",
                close.getScene().getWindow(), false);
    }

    private void handleBack() {
        Stage stage = (Stage) close.getScene().getWindow();
        try {
            Scene scene = new Scene(Main.loadFXML("ManagerAccount"));
            Main.moveSceneOnMouse(scene, stage);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
