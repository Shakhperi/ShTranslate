package com.example.macbookairmd760.yandexTranslate.Model;

import com.example.macbookairmd760.yandexTranslate.Component.Listener.CallbackYandexApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by macbookairmd760 on 05.04.17.
 */

public class Word extends RealmObject {
    @PrimaryKey
    private int id;

    private String translateValue;
    private String originValue;

    private Language languageFrom;
    private Language languageTo;

    private boolean isFavorite = false;
    private boolean isHistory  = true;

    private static String ID_ROW = "id";
    private static String HISTORY_ROW = "isHistory";
    private static String FAVORITE_ROW = "isFavorite";

    private static List<PropertyChangeListener> listener = new ArrayList<PropertyChangeListener>();

    @Ignore
    YandexTranslateApi yandexTranslateApi;

    public Word(String originValue) {
        this.originValue = originValue;
    }

    public Word() {}

    public void saveToHistory() {
        int nextId = 1; // id первого элемента
        boolean doSave = false;

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        Number currentIdNum = realm.where(Word.class).max(ID_ROW);

        if(currentIdNum != null) {
            Word w = realm.where(Word.class)
                    .equalTo(ID_ROW, currentIdNum.intValue())
                    .findFirst();

            // проверка на дубликат с последним добавленным
            if (!(w.getOriginValue().equals(getOriginValue())
                    && (w.getTranslateValue().equals(getTranslateValue()))
                    && w.getLanguageFrom().getAlpha2().equals(getLanguageFrom().getAlpha2())
                    && w.getLanguageTo().getAlpha2().equals(getLanguageTo().getAlpha2())
                    && w.isHistory == true)) {
                nextId = currentIdNum.intValue() + 1;

                doSave = true;
            } else {
                nextId = currentIdNum.intValue();
            }
        } else {
            doSave = true;
        }

        this.setId(nextId);

        if (doSave){
            Word word = realm.createObject(Word.class, nextId);
            generateWordForRealm(word, realm);
        }

        realm.commitTransaction();
    }

    // заполнение realm объекта данными из текущего (self) объекта
    private void generateWordForRealm(Word word, Realm realm) {
        word.setOriginValue(getOriginValue());
        word.setTranslateValue(getTranslateValue());
        word.setFavorite(isFavorite());

        Language languageFrom = realm.createObject(Language.class);
        languageFrom.setTitle(getLanguageFrom().getTitle());
        languageFrom.setAlpha2(getLanguageFrom().getAlpha2());

        Language languageTo = realm.createObject(Language.class);
        languageTo.setAlpha2(getLanguageTo().getAlpha2());
        languageTo.setTitle(getLanguageTo().getTitle());

        word.setLanguageFrom(languageFrom);
        word.setLanguageTo(languageTo);
    }

    public static RealmResults<Word> getHistory() {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Word> result = realm.where(Word.class)
                .equalTo(HISTORY_ROW, true)
                .findAllSorted(ID_ROW, Sort.DESCENDING);

        return result;
    }

    public static void removeHistory() {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Word> results = getHistory();

        if (results != null) {
            realm.beginTransaction();

            for (Word word : results) {
                // проверка для избежания удаления избранного при удалении истории
                if (word.isFavorite()) {
                    word.setHistory(false);
                } else {
                    word.deleteFromRealm();
                }
            }
            realm.commitTransaction();

            notifyListeners();
        }
    }

    public static RealmResults<Word> getFavorites() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Word> result = realm.where(Word.class)
                .equalTo(FAVORITE_ROW, true)
                .findAllSorted(ID_ROW, Sort.DESCENDING);

        return result;
    }

    public static void removeFavorites() {
        Realm realm = Realm.getDefaultInstance();

        RealmResults<Word> results = getFavorites();

        if (results != null) {
            realm.beginTransaction();

            for (Word word : results) {
                // проверка для избежания удаления истории при удалении избранного
                if (!word.isHistory) {
                    word.deleteFromRealm();
                } else {
                    word.setFavorite(false);
                }
            }
            realm.commitTransaction();

            notifyListeners();
        }
    }

    public void translate(final CallbackYandexApi<Word> callback) {
        Callback<HashMap<String, Object >> callbackTranslate = new Callback<HashMap<String, Object >>() {
            @Override
            public void onResponse(Call<HashMap<String, Object >> call, Response<HashMap<String, Object >> response) {
                if (response.body() != null) {
                    // получаем перевод слова/текста (коллекция) и собираем в строку
                    ArrayList<String> array = (ArrayList<String>) (response.body().get("text"));

                    StringBuilder strBuilder = new StringBuilder();
                    for(String str : array) {
                        strBuilder.append(str);
                    }

                    setTranslateValue(strBuilder.toString());

                    callback.onSuccess(Word.this);
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, Object >> call, Throwable throwable) {
                callback.onError(throwable);
            }
        };

        yandexTranslateApi = YandexTranslate.createRetrofit();

        // собираем альфу перевода en-ru*
        String languages = getLanguageFrom().getAlpha2() + "-" + getLanguageTo().getAlpha2();

        Call<HashMap<String, Object >> call = yandexTranslateApi.translate(YandexTranslateApi.API_KEY, getOriginValue(), languages);
        call.enqueue(callbackTranslate);
    }

    // обновление объекта в БД
    public void updateFromRealm() {
        Realm realm = Realm.getDefaultInstance();

        Word word = realm.where(Word.class)
                .equalTo(ID_ROW, getId())
                .findFirst();

        if (word != null) {
            realm.beginTransaction();

            word.setFavorite(isFavorite);

            realm.commitTransaction();
            notifyListeners();
        }
    }

    // определение языка по тексту
    public void detectLanguage(CallbackYandexApi callbackYandexApi) {
        Language.detect(callbackYandexApi, getOriginValue());
    }

    // оповещение подписанных слушателей об изменениях
    private static void notifyListeners() {
        notifyListeners(null, null, null);
    }

    private static void notifyListeners(String property, String oldValue, String newValue) {
        for (PropertyChangeListener name : listener) {
            name.propertyChange(new PropertyChangeEvent(Word.class, property, oldValue, newValue));
        }
    }

    // getter/setter

    public Language getLanguageFrom() {
        if (languageFrom == null) {
            return new Language();
        } else return languageFrom;
    }

    public void setLanguageFrom(Language languageFrom) {
        this.languageFrom = languageFrom;
    }

    public Language getLanguageTo() {
        if (languageTo == null) {
            return new Language();
        } else return languageTo;
    }

    public void setLanguageTo(Language languageTo) {
        this.languageTo = languageTo;
    }

    public String getOriginValue() {
        return this.originValue;
    }

    public void setOriginValue(String originValue) {
        this.originValue = originValue;
    }

    public String getTranslateValue() {
        return translateValue;
    }

    public void setTranslateValue(String translateValue) {
        this.translateValue = translateValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isHistory() {
        return isHistory;
    }

    public void setHistory(boolean isHistory) {
        this.isHistory = isHistory;
    }

    public static void addChangeListener(PropertyChangeListener newListener) {
        listener.add(newListener);
    }

    public void getDictionary(final CallbackYandexApi<Dictionary> callbackYandexApi) {
        String languages = getLanguageFrom().getAlpha2() + "-" + getLanguageTo().getAlpha2();
        String alpha2 = Locale.getDefault().getLanguage();

        Dictionary.get(callbackYandexApi, getOriginValue(), languages, alpha2);
    }
}

