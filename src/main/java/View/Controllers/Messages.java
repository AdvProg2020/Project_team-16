package View.Controllers;

import ModelPackage.System.exeption.account.UserNotAvailableException;
import View.CacheData;
import View.Main;
import View.PrintModels.MessagePM;
import com.jfoenix.controls.JFXButton;
import controler.MessageController;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Messages extends BackAbleController {
    public JFXButton back;
    public JFXButton cartButt;
    public JFXButton minimize;
    public JFXButton close;
    public Label subject;
    public Label date;
    public Text message;
    public TableView<MessagePM> listTable;
    public TableColumn<MessagePM, String> subjectColumn;
    public TableColumn<MessagePM, Date> dateColumn;
    public TableColumn<MessagePM, Boolean> readColumn;

    private final MessageController messageController = MessageController.getInstance();

    @FXML
    public void initialize() {
        initButtons();
        loadTable();
        listeners();
        binds();
    }

    private void binds() {
        cartButt.disableProperty().bind(cacheData.roleProperty.isEqualTo("Customer").not().and(
                Bindings.isEmpty(cacheData.roleProperty).not()
        ));
    }

    private void listeners() {
        listTable.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue != null) {
                loadMessage(newValue);
            }
        });
    }

    private void loadMessage(MessagePM pm) {
        messageController.openMessage(pm.getId());
        subject.setText(pm.getSubject());
        date.setText(pm.getDate().toString());
        message.setText(pm.getMessage());
    }

    public ArrayList<MessagePM> messageTest() {
        ArrayList<MessagePM> messagePMS = new ArrayList<>();
        MessagePM messagePM = new MessagePM(1, "Kala",
                "Very Good", false, new Date(654561321));
        MessagePM messagePM1 = new MessagePM(2, "Purchase",
                "Very Bad", false, new Date(321564651));
        messagePMS.add(messagePM); messagePMS.add(messagePM1);
        return messagePMS;
    }

    private void loadTable() {
        try {
            ArrayList<MessagePM> list = /*messageTest();*/ messageController.getMessagesForUser(CacheData.getInstance().getUsername());
            ObservableList<MessagePM> data = FXCollections.observableArrayList(list);
            subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
            readColumn.setCellValueFactory(new PropertyValueFactory<>("isRead"));
            listTable.setItems(data);
            if (false) throw new UserNotAvailableException();
        } catch (UserNotAvailableException e) {
            e.printStackTrace();
        }
    }

    private void initButtons() {
        back.setOnAction(e -> handleBackButton());
        minimize.setOnAction(e -> minimize());
        close.setOnAction(e -> close());
        cartButt.setOnAction(e -> handleCartButt());
    }

    private void handleCartButt() {
        try {
            Scene scene = new Scene(Main.loadFXML("Cart", backForForward("Messages")));
            Main.setSceneToStage(back, scene);
        } catch (IOException ignore) {}
    }

    private void close() {
        Stage window = (Stage) back.getScene().getWindow();
        window.close();
    }

    private void minimize() {
        Stage window = (Stage) back.getScene().getWindow();
        window.setIconified(true);
    }

    private void handleBackButton() {
        try {
            Scene scene = new Scene(Main.loadFXML(back(), backForBackward()));
            Main.setSceneToStage(back, scene);
        } catch (IOException ignore) {}
    }
}
