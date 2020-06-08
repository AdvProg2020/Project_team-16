package View.Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class MainPage {
    @FXML
    private Pane categories;
    @FXML
    private JFXButton catButt;

    @FXML
    private void showHideCats(){
        if (categories.isVisible()){
            categories.setVisible(false);
        }else {
            categories.setVisible(true);
        }
    }

    @FXML
    public void initialize(){
        catButt.setOnMouseClicked(e -> showHideCats());
    }
}
