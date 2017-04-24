package com.example.macbookairmd760.yandexTranslate.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by macbookairmd760 on 24.04.17.
 */
public class YandexTranslate {
    // конфигурирование запросов
    protected static YandexTranslateApi createRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YandexTranslateApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(YandexTranslateApi.class);
    }
}
