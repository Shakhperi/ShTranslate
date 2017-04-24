package com.example.macbookairmd760.yandexTranslate.Component;

import com.example.macbookairmd760.yandexTranslate.Model.Language;

import java.util.Comparator;

/**
 * Created by macbookairmd760 on 14.04.17.
 */

public class LanguageComparator implements Comparator<Language> {
    @Override
    public int compare(Language o1, Language o2) {
        return o1.getTitle().compareTo(o2.getTitle());
    }
}
