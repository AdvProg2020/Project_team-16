package View;

import ModelPackage.System.database.DBManager;
import ModelPackage.System.database.HibernateUtil;
import View.Menu.MainMenu;
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

    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        DBManager.initialLoad();
        try {
            launch(args);
        }catch (Exception e){
            HibernateUtil.shutdown();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        /*window = stage;
        scene = new Scene(loadFXML("Starter"));
        stage.setScene(scene);
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();*/
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
        window.setScene(scene);
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getClassLoader().getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
