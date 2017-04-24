package com.example.macbookairmd760.yandexTranslate.Model;

import com.example.macbookairmd760.yandexTranslate.Component.LanguageComparator;
import com.example.macbookairmd760.yandexTranslate.Component.Listener.CallbackYandexApi;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by macbookairmd760 on 05.04.17.
 */

public class Language extends RealmObject {
    private String title = "";
    private String alpha2 = "";

    @Ignore
    private static ArrayList<Language> languages = new ArrayList<>();

    public static void loadLanguages(final CallbackYandexApi<ArrayList<Language>> callbackYandexApi, String alpha2) {
        Callback<HashMap<String, Object>> callback = new Callback<HashMap<String, Object>>() {
            @Override
            public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
                LinkedTreeMap<String, String> listLanguages = (LinkedTreeMap<String, String>) response.body().get("langs");
                Object[] listAlpha2 = listLanguages.keySet().toArray();

                // создаем объекты языков
                for (int i=0; i<listLanguages.size(); i++) {
                    Language language = new Language();
                    language.setAlpha2(listAlpha2[i].toString());
                    language.setTitle(listLanguages.get(listAlpha2[i]).toString());

                    languages.add(language);
                }

                callbackYandexApi.onSuccess(languages);
            }

            @Override
            public void onFailure(Call<HashMap<String, Object>> call, Throwable throwable) {
                callbackYandexApi.onError(throwable);
            }
        };

        YandexTranslateApi yandexTranslateApi = YandexTranslate.createRetrofit();

        Call<HashMap<String, Object>> call = yandexTranslateApi.getLanguages(YandexTranslateApi.API_KEY, alpha2);
        call.enqueue(callback);
    }

    // определение языка по слову
    public static void detect(final CallbackYandexApi<Language> callbackYandexApi, String text) {
        Callback<HashMap<String, String>> callback = new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                Language language = new Language();

                String alpha2 = response.body().get("lang");
                String title = Language.getTitle(alpha2);

                language.setAlpha2(alpha2);
                language.setTitle(title);

                callbackYandexApi.onSuccess(language);
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable throwable) {
                callbackYandexApi.onError(throwable);
            }
        };

        YandexTranslateApi yandexTranslateApi = YandexTranslate.createRetrofit();

        Call<HashMap<String, String>> call = yandexTranslateApi.detectLanguage(YandexTranslateApi.API_KEY, text);
        call.enqueue(callback);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlpha2() {
        return alpha2;
    }

    public void setAlpha2(String alpha2) {
        this.alpha2 = alpha2;
    }

    public static String[] getValues(ArrayList<Language> languages) {
        String[] values = new String[languages.size()];
        for (int i = 0; i< languages.size(); i++) {
            values[i] = languages.get(i).getTitle();
        }
        return values;
    }

    // сортировка коллекции языков с выбором первого в списке
    public static void sort(ArrayList<Language> languages, String alpha2) {
        int pos = 0;
        for (int i = 0; i < languages.size(); i++) {
            if(languages.get(i).getAlpha2().equals(alpha2)) {
                pos = i;
                break;
            }
        }
        Language language = languages.get(0);
        languages.set(0, languages.get(pos));
        languages.set(pos, language);

        Collections.sort(languages.subList(1, languages.size()-1), new LanguageComparator());
    }

    public static ArrayList<Language> getLanguages() {
        return languages;
    }

    public static void setLanguages(ArrayList<Language> languages) {
        Language.languages = languages;
    }

    // получение названия языка по альфе
    public static String getTitle(String alpha2) {
        for (Language language : languages) {
            if (language.alpha2.equals(alpha2)) {
                return language.getTitle();
            }
        }

        return "";
    }
}


