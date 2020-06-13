package View.Controllers;

import View.Main;
import View.PrintModels.CommentPM;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.IOException;

public class Comment {
    public Label title;
    public HBox bouthThisBox;
    public Text comment;
    public Label username;

    private static String user;
    private static String titleStr;
    private static String commentStr;
    private static boolean bought;

    public static Parent generateComment(CommentPM pm) throws IOException {
        user = pm.getUserName();
        titleStr = pm.getTitle();
        commentStr = pm.getComment();
        bought = pm.isBoughThis();

        return Main.loadFXML("Comment");
    }

    @FXML
    public void initialize() {
        title.setText(titleStr);
        comment.setText(commentStr);
        username.setText(user);
        bouthThisBox.setVisible(bought);
    }
}
