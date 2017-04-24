package com.example.macbookairmd760.yandexTranslate.View;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.macbookairmd760.yandexTranslate.R;

/**
 * Created by macbookairmd760 on 23.04.17.
 *
 * Для вывода списка языков в spinner
 */
public class LanguagesAdapter extends ArrayAdapter<String>  {
    static public String TEXT_ALIGNMENT_LEFT = "LEFT";
    static public String TEXT_ALIGNMENT_RIGHT = "RIGHT";

    private String[] languages;
    private String textAlignment = TEXT_ALIGNMENT_LEFT;

    public LanguagesAdapter(Context context, int resource, String[] languages) {
        super(context, resource);

        this.languages = languages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.spinner_item, parent, false);
        TextView tvHeaderItem = (TextView) view.findViewById(R.id.tvLanguage);

        if (getTextAlignment().equals(TEXT_ALIGNMENT_LEFT)) {
            tvHeaderItem.setGravity(Gravity.LEFT);
        } else {
            tvHeaderItem.setGravity(Gravity.RIGHT);
        }

        tvHeaderItem.setText(languages[position]);

        tvHeaderItem.setTextColor(getContext().getColor(R.color.colorPrimaryDark));
        tvHeaderItem.setTextSize(18);

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.spinner_item, parent, false);
        TextView tvHeaderItem = (TextView) view.findViewById(R.id.tvLanguage);
        tvHeaderItem.setText(languages[position]);

        return view;
    }

    @Override
    public int getCount() {
        return languages.length;
    }

    private String getTextAlignment() {
        return textAlignment;
    }

    public void setTextAlignment(String textAlignment) {
        this.textAlignment = textAlignment;
    }

}
