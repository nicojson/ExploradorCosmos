package com.example.explorandoelcosmos.service;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class NotificationManager {

    private static void show(String title, String text, Pos position) {
        Notifications notificationBuilder = Notifications.create()
                .title(title)
                .text(text)
                .position(position)
                .hideAfter(Duration.seconds(5))
                .owner(null); // Se puede asignar a una ventana específica si es necesario

        // Aplicar el tema oscuro si está disponible
        notificationBuilder.darkStyle();
        
        notificationBuilder.show();
    }

    public static void showInfo(String title, String text) {
        show(title, text, Pos.TOP_RIGHT);
    }

    public static void showSuccess(String title, String text) {
        // ControlsFX no tiene un método .showSuccess() directo, pero podemos simularlo
        // o simplemente usar showInfo para consistencia.
        show(title, text, Pos.TOP_RIGHT);
    }

    public static void showError(String title, String text) {
        Notifications notificationBuilder = Notifications.create()
                .title(title)
                .text(text)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(8)) // Los errores duran un poco más
                .owner(null);
        
        notificationBuilder.darkStyle();
        notificationBuilder.showError();
    }
    
    public static void showWarning(String title, String text) {
        Notifications notificationBuilder = Notifications.create()
                .title(title)
                .text(text)
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(6))
                .owner(null);
        
        notificationBuilder.darkStyle();
        notificationBuilder.showWarning();
    }
}
