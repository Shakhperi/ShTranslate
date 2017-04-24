package com.example.macbookairmd760.yandexTranslate.Controller;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.macbookairmd760.yandexTranslate.Model.Word;
import com.example.macbookairmd760.yandexTranslate.R;
import com.example.macbookairmd760.yandexTranslate.View.RealmWordListAdapter;
import com.example.macbookairmd760.yandexTranslate.View.ListWordAdapter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import io.realm.RealmResults;

/**
 * Created by macbookairmd760 on 20.04.17.
 */
public class ListWordFragment extends Fragment implements PropertyChangeListener {
    private boolean isFavorite = false;

    private ListWordAdapter adapter;
    private RealmResults<Word> words;
    private RecyclerView rvListWord;
    private LinearLayout llEmpty;
    private TextView tvEmpty;
    private ImageView imvEmpty;

    public ListWordFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_word, container, false);

        Word.addChangeListener(this);

        setHasOptionsMenu(false);

        setHasOptionsMenu(true);

        if (this.getArguments() != null) {
            isFavorite = this.getArguments().getBoolean("isFavorite", false);
        }
        loadWords();

        rvListWord = (RecyclerView) view.findViewById(R.id.rvListWord);
        llEmpty = (LinearLayout) view.findViewById(R.id.llEmpty);
        tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);
        imvEmpty = (ImageView) view.findViewById(R.id.imvEmpty);

        setupRecycler();
        showOrHideBlankView();

        return view;
    }

    private void showOrHideBlankView(){
        if (words.size() == 0) {
            // отображаем заглушку если нет переводов
            generateEmptyView();
        } else {
            // отображаем если переводы есть
            rvListWord.setVisibility(View.VISIBLE);
            llEmpty.setVisibility(View.INVISIBLE);
        }
    }

    // загрузка переводов история/избранное
    private void loadWords() {
       if (isFavorite) {
           words = Word.getFavorites();
       } else {
           words = Word.getHistory();
       }
    }

    private void setupRecycler() {
        rvListWord.setHasFixedSize(true);
        rvListWord.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ListWordAdapter(getContext(), isFavorite);
        rvListWord.setAdapter(adapter);

        RealmWordListAdapter realmAdapter = new RealmWordListAdapter(words);
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    // удаление всех переводов
    public void clearAll() {
        adapter.clearAll();

        generateEmptyView();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tab_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                clearAll();
                break;
        }
        return true;
    }

    private void generateEmptyView() {
        rvListWord.setVisibility(View.INVISIBLE);
        llEmpty.setVisibility(View.VISIBLE);

        String text = "";
        int resImage;

        if (isAdded()) {
            if (isFavorite) {
                text = getResources().getString(R.string.favorites_empty);
                resImage = R.drawable.ic_favorite_empty;
            } else {
                text = getResources().getString(R.string.history_empty);
                resImage = R.drawable.ic_history_empty;
            }

            tvEmpty.setText(text);
            imvEmpty.setImageResource(resImage);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        adapter.notifyDataSetChanged();
        showOrHideBlankView();
    }
}
