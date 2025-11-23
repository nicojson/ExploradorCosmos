module com.example.explorandoelcosmos {
    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;

    // Base de datos y Utilidades
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires jbcrypt;

    // UI y Controles Adicionales
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;

    // Retrofit y Networking
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires okhttp3;
    requires okhttp3.logging;
    requires com.google.gson;

    // iText PDF Generation
    requires kernel;
    requires layout;
    requires java.desktop;


    // Logging
    requires org.slf4j;
    requires org.slf4j.simple;

    // Kotlin
    requires kotlin.stdlib;

    // HikariCP
    requires com.zaxxer.hikari;
    requires io;


    // Abre paquetes a bibliotecas específicas
    opens com.example.explorandoelcosmos.controllers to javafx.fxml;
    opens com.example.explorandoelcosmos.model to com.google.gson, javafx.base;

    // Exporta los paquetes de la aplicación
    exports com.example.explorandoelcosmos;
    exports com.example.explorandoelcosmos.dao;
    exports com.example.explorandoelcosmos.service;
    exports com.example.explorandoelcosmos.connection;
    exports com.example.explorandoelcosmos.controllers;
    exports com.example.explorandoelcosmos.model;
}