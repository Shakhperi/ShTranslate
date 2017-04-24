package com.example.macbookairmd760.yandexTranslate;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by macbookairmd760 on 16.04.17.
 */

public class MyApplication extends Application {
    public static String DICTIONARY_API_URL = "https://dictionary.yandex.net/";
    public static String DICTIONARY_API_KEY = "dict.1.1.20170423T015845Z.dc2ddd0df749366e.81b6abf8e1f15942090792c6a66c559903227088";

    public static String TRANSLATE_API_URL = "https://translate.yandex.net/";
    public static String TRANSLATE_API_KEY = "trnsl.1.1.20170402T084935Z.5f41cd7914b560fe.d0861a196bc646916f5600ca58c88e757db34760";

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        Realm.setDefaultConfiguration(new RealmConfiguration.Builder().build());

    }
}
