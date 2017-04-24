package com.example.macbookairmd760.yandexTranslate.Model;

import com.example.macbookairmd760.yandexTranslate.Model.Language;
import com.example.macbookairmd760.yandexTranslate.Model.Word;
import com.example.macbookairmd760.yandexTranslate.MyApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by macbookairmd760 on 04.04.17.
 */

public interface YandexTranslateApi {
    String BASE_URL = MyApplication.TRANSLATE_API_URL;
    String API_KEY = MyApplication.TRANSLATE_API_KEY;

    @FormUrlEncoded
    @POST("api/v1.5/tr.json/translate")
    Call<HashMap<String, Object>> translate(@Field("key") String key, @Field("text") String text, @Field("lang") String language);

    @FormUrlEncoded
    @POST("api/v1.5/tr.json/getLangs")
    Call<HashMap<String, Object>> getLanguages(@Field("key") String key, @Field("ui") String language);

    @FormUrlEncoded
    @POST("api/v1.5/tr.json/detect")
    Call<HashMap<String, String>> detectLanguage(@Field("key") String key, @Field("text") String text);


    @FormUrlEncoded
    @POST("api/v1.5/tr.json/translate")
    Call<Map<String, Map<String, String>>> translateTest(@Field("key") String key, @Field("text") String text, @Field("lang") String language);
}
