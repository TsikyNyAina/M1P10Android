package com.example.disign.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager {
    static Retrofit retrofit;
    public static ApiService apiService;



    static {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://m1p10androidnode.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService=ApiManager.instance(ApiService.class);
    }

    public static <T> T instance (Class <T> clazz){
        return retrofit.create(clazz);
    }

}
