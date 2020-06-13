package View.Controllers;

import View.PrintModels.MessagePM;
import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

import java.util.Date;

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
}
