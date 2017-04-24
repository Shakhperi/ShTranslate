package com.example.macbookairmd760.yandexTranslate.Controller;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.macbookairmd760.yandexTranslate.Component.Listener.CallbackYandexApi;
import com.example.macbookairmd760.yandexTranslate.Component.Listener.EditTextImeBackListener;
import com.example.macbookairmd760.yandexTranslate.Component.EditTextKeyBoardEvent;
import com.example.macbookairmd760.yandexTranslate.Component.InternetState;
import com.example.macbookairmd760.yandexTranslate.View.LanguagesAdapter;
import com.example.macbookairmd760.yandexTranslate.Model.Dictionary;
import com.example.macbookairmd760.yandexTranslate.Model.ItemDictionary;
import com.example.macbookairmd760.yandexTranslate.Model.Language;
import com.example.macbookairmd760.yandexTranslate.Model.Word;
import com.example.macbookairmd760.yandexTranslate.Component.NetworkChangeReciever;
import com.example.macbookairmd760.yandexTranslate.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by macbookairmd760 on 20.04.17.
 */
public class TranslateFragment extends Fragment implements View.OnClickListener, EditText.OnEditorActionListener, NetworkChangeReciever.NetworkChangeReceiverListener {
    private ScrollView svTranslate;
    private LinearLayout llError;
    private LinearLayout llClear;
    private TextView tvTranslate;
    private Spinner sprLanguageFrom;
    private Spinner sprLanguageTo;
    private EditTextKeyBoardEvent etField;
    private ImageButton imbFavorite;
    private ImageButton imbSwapLanguage;

    private String[] itemsFrom;
    private String[] itemsTo;
    private String text = "";

    private ArrayList<Language> languagesFrom;
    private ArrayList<Language> languagesTo;

    private Language languageFrom = new Language();
    private Language languageTo = new Language();

    private Word word;

    private NetworkChangeReciever networkChangeReceiver;


    CallbackYandexApi<Word> callbackTranslate = new CallbackYandexApi<Word>() {
        @Override
        public void onSuccess(final Word result) {
            result.saveToHistory();
            tvTranslate.setText(result.getTranslateValue());
            tvTranslate.setMovementMethod(LinkMovementMethod.getInstance());

            imbFavorite.setImageResource(R.drawable.ic_favorite);
            imbFavorite.setVisibility(View.VISIBLE);

            if (isAdded()) {
                Context context = getActivity().getApplicationContext();

                if (context != null && InternetState.isConnected(context)) {
                    word.getDictionary(callbackDictionary);
                }
            }
        }

        @Override
        public void onError(Throwable throwable) {

        }
    };

    CallbackYandexApi<Language> callbackDetectLanguage = new CallbackYandexApi<Language>() {
        @Override
        public void onSuccess(Language result) {
            word.setLanguageFrom(result);

            translate();
        }

        @Override
        public void onError(Throwable throwable) {

        }
    };

    CallbackYandexApi<ArrayList<Language>> callbackLoadLanguages = new CallbackYandexApi<ArrayList<Language>>() {
        @Override
        public void onSuccess(ArrayList<Language> result) {
            Language.setLanguages(result);

            createListLanguages();
        }

        @Override
        public void onError(Throwable throwable) {

        }
    };

    CallbackYandexApi<Dictionary> callbackDictionary = new CallbackYandexApi<Dictionary>() {
        @Override
        public void onSuccess(Dictionary result) {
            if (result != null) {
                String stringDictionary = generateStringDictionary(result.getDictionaryList());

                SpannableString spannableString = new SpannableString("\n" + stringDictionary);
                spannableString.setSpan(new RelativeSizeSpan(0.8f), 0, stringDictionary.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, stringDictionary.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                tvTranslate.append(spannableString);
            }
        }

        @Override
        public void onError(Throwable throwable) {

        }
    };

    public void TranslateFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_translate, container, false);

        llClear = (LinearLayout) view.findViewById(R.id.llClear);
        llError = (LinearLayout) view.findViewById(R.id.llError);
        svTranslate = (ScrollView) view.findViewById(R.id.svTranslate);
        sprLanguageFrom = (Spinner) view.findViewById(R.id.sprLangFrom);
        sprLanguageTo = (Spinner) view.findViewById(R.id.sprLangTo);
        etField = (EditTextKeyBoardEvent) view.findViewById(R.id.etField);
        tvTranslate = (TextView) view.findViewById(R.id.tvTranslate);
        imbFavorite = (ImageButton) view.findViewById(R.id.imbFavorite);
        imbSwapLanguage = (ImageButton) view.findViewById(R.id.imbSwapLang);

        llClear.setOnClickListener(this);
        imbFavorite.setOnClickListener(this);
        imbSwapLanguage.setOnClickListener(this);

        setupEditText();

        createListLanguages();

        return view;
    }

    @Override
    public void onResume() {
        networkChangeReceiver = new NetworkChangeReciever();
        networkChangeReceiver.addListener(this);
        if (isAdded()) {
            getActivity().registerReceiver(networkChangeReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (isAdded()) {
            getActivity().unregisterReceiver(networkChangeReceiver);
        }
        super.onPause();
      }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_clear);
        if (item != null) {
            item.setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llClear:
                etField.setText("");
                tvTranslate.setText("");
                imbFavorite.setVisibility(View.INVISIBLE);
                break;
            case R.id.imbFavorite:
                if (word.isFavorite()) {
                    word.setFavorite(false);
                    word.updateFromRealm();
                    imbFavorite.setImageResource(R.drawable.ic_favorite);
                } else {
                    word.setFavorite(true);
                    word.updateFromRealm();
                    imbFavorite.setImageResource(R.drawable.ic_favorite_selected);
                }
                break;
            case R.id.imbSwapLang:
                String valueLanguageFrom = itemsFrom[sprLanguageFrom.getSelectedItemPosition()];
                String valueLanguageTo = itemsTo[sprLanguageTo.getSelectedItemPosition()];

                int selectedFromPos = getSelectedItem(itemsFrom, valueLanguageTo);
                int selectedToPos = getSelectedItem(itemsTo, valueLanguageFrom);

                String detectLanguageValue = getResources().getString(R.string.value_detect_language);
                if (!valueLanguageFrom.equals(detectLanguageValue)) {
                    sprLanguageTo.setSelection(selectedToPos);
                    languageTo = languagesTo.get(selectedToPos);
                }
                sprLanguageFrom.setSelection(selectedFromPos);
                languageFrom = languagesFrom.get(selectedFromPos);

                finishedTyping();

                break;
        }
    }

    private void finishedTyping() {
        word = new Word();
        text = etField.getText().toString();

        if (!text.equals("")) {
            word = new Word(text);
            word.setLanguageTo(languageTo);

            String detectLanguageAlpha2 = getResources().getString(R.string.alpha2_detect_language);
            if (languageFrom.getAlpha2().equals(detectLanguageAlpha2)) {
                if (InternetState.isConnected(getContext())) {
                    word.detectLanguage(callbackDetectLanguage);
                    showTextViewTranslate();
                } else {
                    showError();
                }
            } else {
                word.setLanguageFrom(languageFrom);

                translate();
            }
        }
    }

    private void translate() {
        if (isAdded()) {
            Context context = getActivity().getApplicationContext();
            if (context != null && InternetState.isConnected(context)) {
                word.translate(callbackTranslate);
                showTextViewTranslate();
            } else if (context != null) {
                showError();
            }
        }
    }

    private void showTextViewTranslate() {
        llError.setVisibility(View.INVISIBLE);
        svTranslate.setVisibility(View.VISIBLE);
    }

    private void showError() {
        imbFavorite.setVisibility(View.INVISIBLE);
        llError.setVisibility(View.VISIBLE);
        svTranslate.setVisibility(View.INVISIBLE);
    }


    private int getSelectedItem(String[] listValueLanguage, String valueLanguage) {
        for (int i = 0; i < listValueLanguage.length; i++) {
            if (listValueLanguage[i].equals(valueLanguage)) {
                return i;
            }
        }
        return 0;
    }

    private void setupEditText() {
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        etField.requestFocus();
        etField.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etField.setRawInputType(InputType.TYPE_CLASS_TEXT);
        etField.setOnEditorActionListener(this);
        etField.setOnEditTextImeBackListener(new EditTextImeBackListener() {
            @Override
            public void onImeBack(EditTextKeyBoardEvent ctrl, String text) {
                finishedTyping();
            }
        });
    }

    private void createListLanguages() {
        if (Language.getLanguages() != null) {
            createListLanguageFrom();
            createListLanguageTo();
        } else {
            if (InternetState.isConnected(getContext().getApplicationContext())) {
                String alpha2 = Locale.getDefault().getLanguage();
                Language.loadLanguages(callbackLoadLanguages, alpha2);

            }
        }
    }

    private void createListLanguageFrom() {
        languagesFrom = (ArrayList<Language>) Language.getLanguages().clone();
        String defaultAlpha2 = Locale.getDefault().getLanguage();
        Language.sort(languagesFrom, defaultAlpha2);

        String alpha2 = getResources().getString(R.string.alpha2_detect_language);
        String value = getResources().getString(R.string.value_detect_language);

        Language language = new Language();
        language.setAlpha2(alpha2);
        language.setTitle(value);

        languagesFrom.add(0, language);

        itemsFrom = Language.getValues(languagesFrom);

        LanguagesAdapter adapter = new LanguagesAdapter(getActivity().getApplicationContext(), R.layout.spinner_item, itemsFrom);
        sprLanguageFrom.setAdapter(adapter);

        sprLanguageFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedAlpha2 = languagesFrom.get(position).getAlpha2();
                languageFrom.setAlpha2(selectedAlpha2);
                languageFrom.setTitle(languagesFrom.get(position).getTitle());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void createListLanguageTo() {
        String defaultAlhpa2 = Locale.getDefault().getLanguage();
        languagesTo = (ArrayList<Language>) Language.getLanguages().clone();
        Language.sort(languagesTo, defaultAlhpa2);

        itemsTo = Language.getValues(languagesTo);

        LanguagesAdapter adapter = new LanguagesAdapter(getActivity().getApplicationContext(), R.layout.spinner_item, itemsTo);
        adapter.setTextAlignment(LanguagesAdapter.TEXT_ALIGNMENT_RIGHT);
        sprLanguageTo.setAdapter(adapter);

        sprLanguageTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                languageTo.setAlpha2(languagesTo.get(position).getAlpha2());
                languageTo.setTitle(languagesTo.get(position).getTitle());

                etField.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        final boolean isEnterEvent = event != null
                && event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
        final boolean isEnterUpEvent = isEnterEvent && event.getAction() == KeyEvent.ACTION_UP;

        if (actionId == EditorInfo.IME_ACTION_DONE || isEnterUpEvent) {

            finishedTyping();
        }
        return false;
    }

    private String generateStringDictionary(List<ItemDictionary> itemDictionaryList) {
        String allResponse = "\n";

        for (ItemDictionary itemDictionary : itemDictionaryList) {
            String response = "";

            // собираем список переводов: слово,слово
            if (itemDictionary.getPartOfSpeech() != null){
                response += itemDictionary.getPartOfSpeech() +"\n"; // значение noun или verb

                // получаем слово
                response += word.getOriginValue();

                //отображаем транскрипцию, если есть
                if (!itemDictionary.getTranscription().equals("")) {
                    response +=  " [" +itemDictionary.getTranscription() + "]\n";
                } else {
                    response += "\n";
                }

                // собираем список
                int index = 0;
                for (ItemDictionary item : itemDictionary.getTranslateList()){
                    index++;
                    response += item.getValue();

                    // если элемент не последний ставим запятую
                    if (index != itemDictionary.getTranslateList().size()){
                        response += ", ";
                    }
                }
            }

            allResponse += response + "\n\n";
        }
        return allResponse;
    }

    @Override
    public void networkAvailable() {
        if (Language.getLanguages() == null) {
            createListLanguages();
        }

        showTextViewTranslate();
        finishedTyping();
    }

    @Override
    public void networkUnavailable() {
        showError();
    }
}
