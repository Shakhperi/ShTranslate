package com.example.macbookairmd760.yandexTranslate.Component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import com.example.macbookairmd760.yandexTranslate.Component.Listener.EditTextImeBackListener;

/**
 * Created by macbookairmd760 on 19.04.17.
 */
public class EditTextKeyBoardEvent extends EditText {
    private EditTextImeBackListener onImeBack;

    public EditTextKeyBoardEvent(Context context) {

        super(context);
    }

    public EditTextKeyBoardEvent(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public EditTextKeyBoardEvent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_UP) {
            if (onImeBack != null)
                onImeBack.onImeBack(this, this.getText().toString());
        }
        return super.dispatchKeyEvent(event);
    }

    public void setOnEditTextImeBackListener(EditTextImeBackListener listener) {
        onImeBack = listener;
    }
}


