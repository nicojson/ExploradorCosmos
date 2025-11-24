module com.example.explorandoelcosmos {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires javafx.media;

    // Database and utilities
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires jbcrypt;

    // UI additional controls
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    // Retrofit and networking
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires okhttp3;
    requires okhttp3.logging;
    requires com.google.gson;

    // iText PDF generation

    // HikariCP connection pool
    requires com.zaxxer.hikari;

    // Kotlin standard library
    requires kotlin.stdlib;

    // Open packages for reflection
    opens com.example.explorandoelcosmos.controllers to javafx.fxml;
    opens com.example.explorandoelcosmos.model to com.google.gson, javafx.base;

    // Export application packages
    exports com.example.explorandoelcosmos;
    exports com.example.explorandoelcosmos.dao;
    exports com.example.explorandoelcosmos.service;
    exports com.example.explorandoelcosmos.connection;
    exports com.example.explorandoelcosmos.controllers;
    exports com.example.explorandoelcosmos.model;
}
