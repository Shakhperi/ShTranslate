package com.example.macbookairmd760.yandexTranslate.View;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by macbookairmd760 on 18.04.17.
 */

public class RealmModelAdapter<T extends RealmObject> extends RealmBaseAdapter<T> {
    private RealmResults<T> realmResults;
    public RealmModelAdapter(RealmResults<T> realmResults) {
        super(realmResults);
        this.realmResults = realmResults;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    public RealmResults<T> getRealmResults() {
        return realmResults;
    }
}
