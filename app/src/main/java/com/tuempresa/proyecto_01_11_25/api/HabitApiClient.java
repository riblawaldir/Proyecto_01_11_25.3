package com.tuempresa.proyecto_01_11_25.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tuempresa.proyecto_01_11_25.model.Habit;
import com.tuempresa.proyecto_01_11_25.utils.HabitTypeAdapter;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Cliente singleton para manejar las llamadas a la API usando Retrofit.
 * Configura Retrofit con Gson y un adaptador personalizado para HabitType.
 */
public class HabitApiClient {
    private static final String TAG = "HabitApiClient";
    
    // URL base de la API - Configurar según tu servidor
    // Para desarrollo local en emulador: "http://10.0.2.2:5098/api/v1/" (puerto por defecto de .NET)
    // Para desarrollo local en dispositivo físico: "http://192.168.x.x:5098/api/v1/" (IP de tu PC)
    // Para producción: "https://demopagina.somee.com/api/v1/"
    // IMPORTANTE: Verifica el puerto en launchSettings.json de la API (puede ser 5098, 5000, etc.)
    private static final String BASE_URL = "http://10.0.2.2:5098/api/v1/"; // Emulador (puerto 5098 por defecto)
    // private static final String BASE_URL = "http://192.168.1.100:5098/api/v1/"; // Dispositivo físico (cambiar IP y puerto)
    // private static final String BASE_URL = "https://demopagina.somee.com/api/v1/"; // Producción
    
    private static HabitApiClient instance;
    private HabitApiService apiService;
    private ScoreApiService scoreApiService;
    private Retrofit retrofit;

    /**
     * Constructor privado para implementar patrón Singleton.
     */
    private HabitApiClient() {
        // Configurar Gson con adaptador personalizado para HabitType
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Habit.HabitType.class, new HabitTypeAdapter())
                .setLenient()
                .create();

        // Configurar interceptor de logging (solo en modo debug)
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Configurar OkHttpClient con timeout
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        // Configurar Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(HabitApiService.class);
        scoreApiService = retrofit.create(ScoreApiService.class);
    }

    /**
     * Obtiene la instancia singleton del cliente API.
     * @return Instancia de HabitApiClient
     */
    public static synchronized HabitApiClient getInstance() {
        if (instance == null) {
            instance = new HabitApiClient();
        }
        return instance;
    }

    /**
     * Obtiene el servicio de API configurado.
     * @return HabitApiService para realizar llamadas a la API
     */
    public HabitApiService getApiService() {
        return apiService;
    }

    /**
     * Obtiene el servicio de Score API configurado.
     * @return ScoreApiService para realizar llamadas a la API de scores
     */
    public ScoreApiService getScoreApiService() {
        return scoreApiService;
    }

    /**
     * Permite cambiar la URL base de la API (útil para testing o diferentes entornos).
     * @param baseUrl Nueva URL base
     */
    public void setBaseUrl(String baseUrl) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Habit.HabitType.class, new HabitTypeAdapter())
                .setLenient()
                .create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(HabitApiService.class);
        scoreApiService = retrofit.create(ScoreApiService.class);
        Log.d(TAG, "Base URL actualizada a: " + baseUrl);
    }

    /**
     * Obtiene la URL base actual.
     * @return URL base configurada
     */
    public String getBaseUrl() {
        return BASE_URL;
    }
}

