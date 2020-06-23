package View.Controllers;

import ModelPackage.Users.MainContent;
import View.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Random;

import static View.Main.getFXMLLoader;

public class Content {
    @FXML
    private AnchorPane root;
    @FXML
    private Label title;
    @FXML
    private Text content;

    static final String[] COLORS = {
            "#ff9ff3",
            "#feca57",
            "#ff6b6b",
            "#48dbfb",
            "#1dd1a1",
            "#706fd3",
            "#ffda79",
            "#63cdda"
    };

    /**
     * @param content the {@Code MainContent} from Content Controller
     * @return a {@Code Parent} or
     * {@Code null} if  there is a problem loading FXML "Content"
     */
    public static Parent createContent(MainContent content) {
        FXMLLoader loader = getFXMLLoader("Content");
        try {
            Parent parent = loader.load();
            Content controller = loader.getController();
            controller.initData(content);
            System.out.println("here");
            return parent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    private void initialize() {
        root.setStyle("-fx-background-color: " + getRandomColor() + ";");
    }

    private String getRandomColor() {
        return COLORS[new Random().nextInt(COLORS.length)];
    }

    private void initData(MainContent content) {
        this.title.setText(content.getTitle());
        this.content.setText(content.getContent());
    }
}
