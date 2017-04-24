package com.example.macbookairmd760.yandexTranslate.Component.Listener;

import com.example.macbookairmd760.yandexTranslate.Model.Word;

/**
 * Created by macbookairmd760 on 12.04.17.
 */

public interface CallbackYandexApi<T> {
    public void onSuccess(T result);
    public void onError(Throwable throwable);
}
