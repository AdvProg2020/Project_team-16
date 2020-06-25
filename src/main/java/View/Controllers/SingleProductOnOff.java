package View.Controllers;

import View.CacheData;
import View.Main;
import View.PrintModels.OffProductPM;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Date;

import static java.lang.String.format;

public class SingleProductOnOff {
    public ImageView image;
    public Label name;
    public Label percent;
    public Label hour;
    public Label min;
    public Label sec;
    public Label price;
    public Label days;
    public Rectangle root;

    private static final int SECONDS_PER_DAY = 86_400;
    private static final int SECONDS_PER_HOUR = 3600;
    private static final int SECONDS_PER_MINUTE = 60;

    private int duration;
    private Timeline timeline;
    private int id;

    public static Parent generate(OffProductPM pm) throws IOException {
        FXMLLoader loader = Main.getFXMLLoader("SingleProductOnOff");
        Parent parent = loader.load();
        SingleProductOnOff controller = loader.getController();
        controller.initData(pm);
        return parent;
    }

    @FXML
    private void initialize() {
        root.setOnMouseClicked(event -> {
            CacheData.getInstance().setProductId(id);
            try {
                Scene scene = new Scene(Main.loadFXML("ProductDigest",
                        "MainPage", "ProductsPage"));
                Main.setSceneToStage(root, scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void initData(OffProductPM pm) {
        image.setImage(pm.getImage());
        name.setText(pm.getName());
        percent.setText("" + pm.getPercent() + "%");
        price.setText("" + pm.getOffPrice() + "$");
        duration = (int) Duration.millis(pm.getEnd().getTime() - new Date().getTime()).toSeconds();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> updateTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        id = pm.getId();
    }

    private void updateTime() {
        int d = duration / SECONDS_PER_DAY;
        int h = (duration % SECONDS_PER_DAY) / SECONDS_PER_HOUR;
        int m = ((duration % SECONDS_PER_DAY) % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE;
        int s = (((duration % SECONDS_PER_DAY) % SECONDS_PER_HOUR) % SECONDS_PER_MINUTE);
        days.setText(format("%03d", d));
        hour.setText(format("%02d", h));
        min.setText(format("%02d", m));
        sec.setText(format("%02d", s));
        duration--;
        if (duration == 0) timeline.stop();
    }

}
