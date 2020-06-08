package View.Controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class ProductsPage {
    public JFXButton back;
    public JFXButton cartButt;
    public JFXButton minimize;
    public JFXButton close;
    public VBox filterVBox;
    public JFXSlider minPrice;
    public JFXSlider maxPrice;
    public ToggleGroup ADOrder;
    public ToggleGroup type;
    public ScrollPane productVBox;
    public TextField serch;
    public Button searchBut;
    public Button ignoreSearch;
    public JFXToggleButton offOnly;
}
