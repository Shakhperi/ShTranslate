package com.example.macbookairmd760.yandexTranslate.Model;

import com.example.macbookairmd760.yandexTranslate.Component.Listener.CallbackYandexApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by macbookairmd760 on 23.04.17.
 */
public class Dictionary {
    @SerializedName("def")
    private List<ItemDictionary> dictionaryList;
    private static YandexDictionaryApi yandexDictionaryApi;

    public static void get(final CallbackYandexApi<Dictionary> callbackYandexApi, String text, String languages, String defaultLanguage) {
        Callback<Dictionary> callback = new Callback<Dictionary>() {
            @Override
            public void onResponse(Call<Dictionary> call, Response<Dictionary> response) {
                callbackYandexApi.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Dictionary> call, Throwable throwable) {
                callbackYandexApi.onError(throwable);
            }
        };

        createRetrofit();

        Call<Dictionary> call = yandexDictionaryApi.lookUp(YandexDictionaryApi.API_KEY, text, languages, defaultLanguage);
        call.enqueue(callback);
    }

    private static void createRetrofit() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(YandexDictionaryApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        yandexDictionaryApi = retrofit.create(YandexDictionaryApi.class);
    }

    public List<ItemDictionary> getDictionaryList() {
        return dictionaryList;
    }
}
