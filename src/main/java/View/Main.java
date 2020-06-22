package View;

import ModelPackage.System.database.DBManager;
import ModelPackage.System.database.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    private static Stage window;
    private static Scene scene;
    private static double xOffset;
    private static double yOffset;

    public static void main(String[] args) {
        HibernateUtil.startUtil();
        DBManager.initialLoad();
        try {
            launch(args);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;
        try {
            scene = new Scene(loadFXML("ProductDigest"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        moveSceneOnMouse(scene, stage);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        window.setOnCloseRequest(e-> close());
        stage.show();
    }

    public static void moveSceneOnMouse(Scene scene, Stage stage) {
        scene.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });
        scene.setOnMouseDragged(e -> {
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
    }

    public static void minimize(){
        window.setIconified(true);
    }

    public static void close(){
        window.close();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        window.setScene(scene);
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getClassLoader().getResource("./fxmls/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void stop() throws Exception {
        HibernateUtil.shutdown();
        super.stop();
    }
}
