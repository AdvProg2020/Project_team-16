package View.Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

public class SaleHistory {
    public JFXButton back;
    public JFXButton minimize;
    public JFXButton close;
    public TableColumn sellNo;
    public TableColumn product;
    public TableColumn date;
    public TableColumn price;
    public TableColumn off;
    public TableColumn delStatus;
    public Label totalSell;
    public LineChart sell;
    public BarChart barProductChar;
}
