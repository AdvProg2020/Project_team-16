package View.Controllers;

import ModelPackage.System.exeption.account.UserNotAvailableException;
import View.CacheData;
import View.Main;
import View.PrintModels.MessagePM;
import com.jfoenix.controls.JFXButton;
import controler.MessageController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Messages {
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

    private final CacheData cacheData = CacheData.getInstance();
    private final MessageController messageController = MessageController.getInstance();
    private List<MessagePM> messages;

    @FXML
    public void initialize() {
        initButtons();
        loadMessagesForUser();
        loadTable();
        handleSelectedItem();
    }

    public List<MessagePM> messageTest() {
        ArrayList<MessagePM> messagePMS = new ArrayList<>();
        MessagePM messagePM = new MessagePM(1, "Kala",
                "Very Good", false, new Date(2018, Calendar.MARCH, 12));
        MessagePM messagePM1 = new MessagePM(1, "Purchase",
                "Very Bad", false, new Date(2020, Calendar.MARCH, 12));
        messagePMS.add(messagePM); messagePMS.add(messagePM1);
        return messagePMS;
    }

    private void handleSelectedItem() {
        MessagePM messagePM = listTable.getSelectionModel().getSelectedItem();
        if (messagePM != null) {
            subject.setText(messagePM.getSubject());
            date.setText(messagePM.getDate().toString());
            message.setText(messagePM.getMessage());
            messageController.openMessage(messagePM.getId());
        }

    }

    private void loadTable() {
        subjectColumn.setCellValueFactory(new PropertyValueFactory<>("subject"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        readColumn.setCellValueFactory(new PropertyValueFactory<>("isRead"));
        listTable.setItems(getObservableList());
    }

    private ObservableList<MessagePM> getObservableList() {
        return FXCollections.observableArrayList(messages);
    }

    private void loadMessagesForUser() {
        try {
            messages = messageController.getMessagesForUser(cacheData.getUsername());
        } catch (UserNotAvailableException ignore) {}
    }

    private void initButtons() {
        back.setOnAction(e -> handleBackButton());
        minimize.setOnAction(e -> minimize());
        close.setOnAction(e -> close());
        cartButt.setOnAction(e -> handleCartButt());
    }

    private void handleCartButt() {
        try {
            Main.setRoot("Cart");
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
            Main.setRoot("CustomerAccount");
        } catch (IOException ignore) {}
    }
}
