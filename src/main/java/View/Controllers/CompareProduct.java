package View.Controllers;

import View.PrintModels.MiniProductPM;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.shape.Rectangle;

public class CompareProduct {
    public JFXButton back;
    public JFXButton cartButt;
    public JFXButton minimize;
    public JFXButton close;
    public JFXComboBox<MiniProductPM> choose;
    public Rectangle mainProductImage;
    public Rectangle secondProductImage;
    public TableView table;
    public TableColumn feature;
    public TableColumn product1Col;
    public TableColumn product2Col;
}
