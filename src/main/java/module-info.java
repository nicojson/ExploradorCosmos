module com.example.explorandoelcosmos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;
    requires jbcrypt;
    requires com.google.gson;
    requires java.net.http;
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires okhttp3;
    requires kotlin.stdlib;
    requires okhttp3.logging; // Logging module


    opens com.example.explorandoelcosmos to javafx.fxml;
    opens com.example.explorandoelcosmos.model to com.google.gson, retrofit2.converter.gson;
    exports com.example.explorandoelcosmos;
    exports com.example.explorandoelcosmos.connection;
    opens com.example.explorandoelcosmos.connection to javafx.fxml;
    exports com.example.explorandoelcosmos.dao;
    opens com.example.explorandoelcosmos.dao to javafx.fxml;
    exports com.example.explorandoelcosmos.controllers;
    opens com.example.explorandoelcosmos.controllers to javafx.fxml;
    exports com.example.explorandoelcosmos.service;
    opens com.example.explorandoelcosmos.service to javafx.fxml;
    exports com.example.explorandoelcosmos.model;
}
