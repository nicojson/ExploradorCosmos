module com.example.explorandoelcosmos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires jbcrypt;

    opens com.example.explorandoelcosmos to javafx.fxml;
    exports com.example.explorandoelcosmos;
    exports com.example.explorandoelcosmos.connection;
    opens com.example.explorandoelcosmos.connection to javafx.fxml;
    exports com.example.explorandoelcosmos.dao;
    opens com.example.explorandoelcosmos.dao to javafx.fxml;
    exports com.example.explorandoelcosmos.model;
    opens com.example.explorandoelcosmos.model to javafx.fxml;
    exports com.example.explorandoelcosmos.controllers;
    opens com.example.explorandoelcosmos.controllers to javafx.fxml;
}