package com.example.macbookairmd760.yandexTranslate.View;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.macbookairmd760.yandexTranslate.Model.Word;
import com.example.macbookairmd760.yandexTranslate.R;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import io.realm.Realm;

/**
 * Created by macbookairmd760 on 18.04.17.
 *
 * Для отображения истории и избранного
 */

public class ListWordAdapter extends RealmRecyclerViewAdapter<Word> {
    private Context context;
    private boolean isFavorite = false;
    private ImageButton imbFavorite;

    public ListWordAdapter(Context context, boolean isFavorite) {
        this.context = context;
        this.isFavorite = isFavorite;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);

        return new CardViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Word word = getItem(position);
        CardViewHolder cardViewHolder = (CardViewHolder) holder;

        cardViewHolder.tvOrigin.setText(word.getOriginValue());
        cardViewHolder.tvTranslate.setText(word.getTranslateValue());
        cardViewHolder.tvLanguageFrom.setText(word.getLanguageFrom().getTitle().toUpperCase());
        cardViewHolder.tvLanguageTo.setText(word.getLanguageTo().getTitle().toUpperCase());
        imbFavorite = cardViewHolder.imbFavorite;

        if (word.isFavorite()) {
            cardViewHolder.imbFavorite.setImageResource(R.drawable.ic_favorite_selected);
        } else {
            cardViewHolder.imbFavorite.setImageResource(R.drawable.ic_favorite);
        }
    }

    @Override
    public int getItemCount() {
        if (getRealmAdapter() != null) {
            int count = getRealmAdapter().getCount();
            return count;
        }

        return 0;
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private CardView card;
        private TextView tvOrigin;
        private TextView tvTranslate;
        private TextView tvLanguageFrom;
        private TextView tvLanguageTo;
        private ImageButton imbFavorite;

        Context context;

        public CardViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;

            card = (CardView) itemView.findViewById(R.id.cvWord);
            tvOrigin = (TextView) itemView.findViewById(R.id.tvOrigin);
            tvTranslate = (TextView) itemView.findViewById(R.id.tvTranslate);
            tvLanguageFrom = (TextView) itemView.findViewById(R.id.tvLanguageFrom);
            tvLanguageTo = (TextView) itemView.findViewById(R.id.tvLanguageTo);
            imbFavorite = (ImageButton) itemView.findViewById(R.id.imbFavorite);
            imbFavorite.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Realm realm = Realm.getDefaultInstance();

            Word word = realm.copyFromRealm(getItem(getAdapterPosition()));

            // при добавлении в избранное пользователем
            if (v.getId() == R.id.imbFavorite) {
                if (word.isFavorite()) {
                    imbFavorite.setImageResource(R.drawable.ic_favorite);
                } else {
                    imbFavorite.setImageResource(R.drawable.ic_favorite_selected);
                }

                word.setFavorite(!word.isFavorite());
                word.updateFromRealm();
            }
        }
    }

    public void clearAll() {
        if (isFavorite) {
            Word.removeFavorites();
        } else {
            Word.removeHistory();
        }
    }
}
