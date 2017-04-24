package com.example.macbookairmd760.yandexTranslate.Model;

import com.example.macbookairmd760.yandexTranslate.MyApplication;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by macbookairmd760 on 23.04.17.
 */
public interface YandexDictionaryApi {
    String BASE_URL = MyApplication.DICTIONARY_API_URL;
    String API_KEY = MyApplication.DICTIONARY_API_KEY;

    @FormUrlEncoded
    @POST("api/v1/dicservice.json/lookup")
    Call<Dictionary> lookUp(@Field("key") String key,
                                            @Field("text") String text,
                                            @Field("lang") String language,
                                            @Field("ui") String ui);

}
