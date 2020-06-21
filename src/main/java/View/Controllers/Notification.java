package View.Controllers;

import javafx.geometry.Pos;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class Notification {
    public static void show(String title, String message, Window window, boolean error){
        Notifications notification = Notifications.create()
                .title(title)
                .text(message)
                .hideAfter(Duration.seconds(2))
                .position(Pos.TOP_CENTER)
                .owner(window);
        if (error){
            notification.showError();
        } else {
            notification.showInformation();
        }
    }
}
