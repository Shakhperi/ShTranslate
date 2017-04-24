package com.example.macbookairmd760.yandexTranslate.View;

import com.example.macbookairmd760.yandexTranslate.Model.Word;

import io.realm.RealmResults;

/**
 * Created by macbookairmd760 on 18.04.17.
 */

public class RealmWordListAdapter extends RealmModelAdapter<Word> {
    private RealmResults<Word> words;

    public RealmWordListAdapter(RealmResults<Word> realmResults) {
        super(realmResults);
        words = realmResults;
    }

    public RealmResults<Word> getWords() {
        return words;
    }

    @Override
    public int getCount() {
        return words.size();
    }
}
