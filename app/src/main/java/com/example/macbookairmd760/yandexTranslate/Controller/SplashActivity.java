package com.example.macbookairmd760.yandexTranslate.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.macbookairmd760.yandexTranslate.Component.Listener.CallbackYandexApi;
import com.example.macbookairmd760.yandexTranslate.Model.Language;
import com.example.macbookairmd760.yandexTranslate.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by macbookairmd760 on 08.04.17.
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            getListLanguages();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getListLanguages() {
        String alpha2 = Locale.getDefault().getLanguage();
        Language.loadLanguages(new CallbackYandexApi<ArrayList<Language>>() {
            @Override
            public void onSuccess(ArrayList<Language> result) {
                Language.setLanguages(result);
                startActivity();
            }

            @Override
            public void onError(Throwable throwable) {
                startActivity();
            }
        }, alpha2);
    }

    private void startActivity() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}
