package com.example.explorandoelcosmos.service;

import com.example.explorandoelcosmos.dao.AppConfigDAO;
import com.example.explorandoelcosmos.dao.AppConfigDAOImpl;
import com.example.explorandoelcosmos.model.ApiEndpointConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;
import java.util.Optional;

public class NasaApiClient {

    // URLs base de las APIs
    private static final String NASA_LIBRARY_BASE_URL = "https://images-api.nasa.gov/";
    private static final String ASTRONOMY_API_BASE_URL = "https://api.astronomyapi.com/";

    // Instancias de los servicios Retrofit
    private static NasaApiService nasaLibraryService;
    private static NasaApiService astronomyApiService;

    // Claves de API (ahora se obtienen de ApiEndpointConfig)
    private static String NASA_LIBRARY_API_KEY;
    private static String ASTRONOMY_APP_ID;
    private static String ASTRONOMY_APP_SECRET;

    private static final AppConfigDAO appConfigDAO = new AppConfigDAOImpl();

    // Método para obtener la API Key de la NASA Library
    public static String getNasaLibraryApiKey() {
        if (NASA_LIBRARY_API_KEY == null) {
            Optional<ApiEndpointConfig> config = appConfigDAO.findEndpointConfigByName("Biblioteca NASA");
            if (config.isPresent()) {
                NASA_LIBRARY_API_KEY = config.get().getApiKey();
            }
            if (NASA_LIBRARY_API_KEY == null || NASA_LIBRARY_API_KEY.isEmpty()) {
                System.err.println("ADVERTENCIA: API Key para 'Biblioteca NASA' no encontrada. Por favor, configúrala en el panel de administración.");
            }
        }
        return NASA_LIBRARY_API_KEY;
    }

    // Métodos para obtener las credenciales de Astronomy API
    public static String getAstronomyAppId() {
        if (ASTRONOMY_APP_ID == null) {
            Optional<ApiEndpointConfig> config = appConfigDAO.findEndpointConfigByName("Astronomy API");
            if (config.isPresent()) {
                ASTRONOMY_APP_ID = config.get().getAppId();
            }
            if (ASTRONOMY_APP_ID == null || ASTRONOMY_APP_ID.isEmpty()) {
                System.err.println("ADVERTENCIA: Application ID para 'Astronomy API' no encontrado. Por favor, configúralo.");
            }
        }
        return ASTRONOMY_APP_ID;
    }

    public static String getAstronomyAppSecret() {
        if (ASTRONOMY_APP_SECRET == null) {
            Optional<ApiEndpointConfig> config = appConfigDAO.findEndpointConfigByName("Astronomy API");
            if (config.isPresent()) {
                ASTRONOMY_APP_SECRET = config.get().getAppSecret();
            }
            if (ASTRONOMY_APP_SECRET == null || ASTRONOMY_APP_SECRET.isEmpty()) {
                System.err.println("ADVERTENCIA: Application Secret para 'Astronomy API' no encontrado. Por favor, configúralo.");
            }
        }
        return ASTRONOMY_APP_SECRET;
    }

    // Métodos para obtener las instancias de los servicios
    public static NasaApiService getNasaLibraryService() {
        if (nasaLibraryService == null) {
            nasaLibraryService = createService(NASA_LIBRARY_BASE_URL);
        }
        return nasaLibraryService;
    }

    public static NasaApiService getAstronomyApiService() {
        if (astronomyApiService == null) {
            astronomyApiService = createService(ASTRONOMY_API_BASE_URL);
        }
        return astronomyApiService;
    }

    private static NasaApiService createService(String baseUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);

        // Añadir interceptor de autenticación Basic para Astronomy API
        if (ASTRONOMY_API_BASE_URL.equals(baseUrl)) {
            httpClientBuilder.addInterceptor(chain -> {
                String credentials = Credentials.basic(getAstronomyAppId(), getAstronomyAppSecret());
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization", credentials)
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            });
        }

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClientBuilder.build())
                .build();

        return retrofit.create(NasaApiService.class);
    }

    // Este método ahora devuelve la clave de la Biblioteca NASA
    public static String getApiKey() {
        return getNasaLibraryApiKey();
    }
}
