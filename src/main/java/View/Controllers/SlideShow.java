package View.Controllers;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;


import java.io.IOException;
import java.util.List;

import static View.Main.*;

public class SlideShow {
    public AnchorPane container;
    public JFXButton previous;
    public JFXButton next;

    private List<Node> nodeList;
    private int currentIndex = 0;
    private int total = 0;

    public static Parent makeSlideShow(List<Node> nodes) {
        FXMLLoader loader = getFXMLLoader("SlideShow");
        try {
            Parent parent = loader.load();
            SlideShow controller = loader.getController();
            controller.initData(nodes);
            return parent;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    private void initialize() {
        buttons();
        timeline();
    }

    private void timeline() {
        Timeline automatic = new Timeline(new KeyFrame(Duration.seconds(5), event -> showNext()));
        automatic.play();
    }

    private void buttons() {
        next.setOnAction(event -> showNext());
        previous.setOnAction(event -> showPrevious());
    }

    private void showNext() {
        if (currentIndex < total) {
            currentIndex++;
        } else {
            currentIndex = 0;
        }
        show(nodeList.get(currentIndex));
    }

    private void showPrevious() {
        if (currentIndex == 0) {
            currentIndex = total;
        } else {
            currentIndex--;
        }
        show(nodeList.get(currentIndex));
    }

    private void show(Node node) {
        Node current = container.getChildren().get(0);
        FadeTransition fadeOut = makeFade(current, 1, 0, 200);
        fadeOut.setOnFinished(event -> {
            container.getChildren().clear();
            FadeTransition fadeIn = makeFade(node, 0, 1, 200);
            container.getChildren().add(node);
            fadeIn.play();
        });
        fadeOut.play();
    }

    private void initData(List<Node> list) {
        nodeList = list;
        currentIndex = 0;
        total = list.size() - 1;
        container.getChildren().clear();
        container.getChildren().add(list.get(0));
    }
}
