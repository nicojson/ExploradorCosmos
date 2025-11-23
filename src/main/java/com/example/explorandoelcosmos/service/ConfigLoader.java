package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.dao.AppConfigDAO;
import com.example.explorandoelcosmos.dao.AppConfigDAOImpl;

public class ConfigLoader {

    private static final AppConfigDAO configDAO = new AppConfigDAOImpl();

    public static String getApiKey(String key) {
        // Busca la clave en la base de datos y devuelve el valor, o una cadena vacía si no se encuentra.
        return configDAO.findByKey(key).orElse("");
    }
}
