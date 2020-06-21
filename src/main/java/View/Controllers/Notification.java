package View.Controllers;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.awt.*;

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
