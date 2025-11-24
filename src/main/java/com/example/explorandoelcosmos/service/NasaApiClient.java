package com.example.explorandoelcosmos.service;

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
    private static final String JWST_API_BASE_URL = "https://api.jwstapi.com/";

    // Instancias de los servicios Retrofit
    private static NasaApiService nasaLibraryService;
    private static NasaApiService astronomyApiService;
    private static JWSTApi jwstApiService;

    // Claves de API (ahora se obtienen de ConfigLoader)
    private static String NASA_LIBRARY_API_KEY;
    private static String ASTRONOMY_API_KEY;
    private static String JWST_API_KEY;

    // Método para obtener la API Key de la NASA Library
    public static String getNasaLibraryApiKey() {
        if (NASA_LIBRARY_API_KEY == null) {
            Optional<ApiEndpointConfig> config = ConfigLoader.getEndpointConfig("NASA");
            if (config.isPresent()) {
                NASA_LIBRARY_API_KEY = config.get().getApiKey();
            }
            if (NASA_LIBRARY_API_KEY == null || NASA_LIBRARY_API_KEY.isEmpty()) {
                System.err.println(
                        "ADVERTENCIA: API Key para 'NASA' no encontrada. Por favor, configúrala en el panel de administración.");
            }
        }
        return NASA_LIBRARY_API_KEY;
    }

    // Método para obtener la API Key de Astronomy API (formato ID:Secret)
    public static String getAstronomyApiKey() {
        if (ASTRONOMY_API_KEY == null) {
            Optional<ApiEndpointConfig> config = ConfigLoader.getEndpointConfig("Astronomy API");
            if (config.isPresent()) {
                ASTRONOMY_API_KEY = config.get().getApiKey();
            }
            if (ASTRONOMY_API_KEY == null || ASTRONOMY_API_KEY.isEmpty()) {
                System.err.println(
                        "ADVERTENCIA: API Key para 'Astronomy API' no encontrada. Por favor, configúrala (Formato ID:Secret) en el panel de administración.");
            }
        }
        return ASTRONOMY_API_KEY;
    }

    // Método para obtener la API Key de JWST
    public static String getJwstApiKey() {
        if (JWST_API_KEY == null) {
            Optional<ApiEndpointConfig> config = ConfigLoader.getEndpointConfig("JWST");
            if (config.isPresent()) {
                JWST_API_KEY = config.get().getApiKey();
            }
            if (JWST_API_KEY == null || JWST_API_KEY.isEmpty()) {
                System.err.println("ADVERTENCIA: API Key para 'JWST' no encontrada. Por favor, configúrala.");
            }
        }
        return JWST_API_KEY;
    }

    // Métodos para obtener las instancias de los servicios
    public static NasaApiService getNasaLibraryService() {
        if (nasaLibraryService == null) {
            nasaLibraryService = createService(NASA_LIBRARY_BASE_URL).create(NasaApiService.class);
        }
        return nasaLibraryService;
    }

    public static NasaApiService getAstronomyApiService() {
        if (astronomyApiService == null) {
            astronomyApiService = createService(ASTRONOMY_API_BASE_URL).create(NasaApiService.class);
        }
        return astronomyApiService;
    }

    public static JWSTApi getJwstApiService() {
        if (jwstApiService == null) {
            jwstApiService = createService(JWST_API_BASE_URL).create(JWSTApi.class);
        }
        return jwstApiService;
    }

    private static Retrofit createService(String baseUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);

        // Añadir interceptor de autenticación Basic para Astronomy API
        if (ASTRONOMY_API_BASE_URL.equals(baseUrl)) {
            httpClientBuilder.addInterceptor(chain -> {
                String apiKey = getAstronomyApiKey();
                String credentials;
                if (apiKey != null && apiKey.contains(":")) {
                    String[] parts = apiKey.split(":");
                    credentials = Credentials.basic(parts[0], parts[1]);
                } else {
                    // Fallback or assume it's already a token
                    credentials = apiKey != null ? apiKey : "";
                }

                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("Authorization", credentials)
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            });
        }

        // Añadir interceptor para JWST API Key
        if (JWST_API_BASE_URL.equals(baseUrl)) {
            httpClientBuilder.addInterceptor(chain -> {
                String apiKey = getJwstApiKey();
                Request original = chain.request();
                Request request = original.newBuilder()
                        .header("X-API-KEY", apiKey != null ? apiKey : "")
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            });
        }

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClientBuilder.build())
                .build();
    }

    // Este método ahora devuelve la clave de la Biblioteca NASA
    public static String getApiKey() {
        return getNasaLibraryApiKey();
    }
}
