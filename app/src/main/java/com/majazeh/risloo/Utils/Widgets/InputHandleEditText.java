package com.majazeh.risloo.Utils.Widgets;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.majazeh.risloo.R;

import java.util.Objects;

public class InputHandleEditText {

    // Widgets
    private EditText inputEditText;

    public EditText getInput() {
        return inputEditText;
    }

    public void focus(EditText editText) {
        this.inputEditText = editText;
    }

    public void select(EditText editText) {
        editText.requestFocus();
        editText.setCursorVisible(true);
        editText.setBackgroundResource(R.drawable.draw_16sdp_border_primary);
    }

    public void error(Activity activity, EditText editText) {
        editText.clearFocus();
        editText.setCursorVisible(false);
        editText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);

        hideKeyboard(activity);
    }

    public void clear(Activity activity, EditText editText) {
        editText.clearFocus();
        editText.setCursorVisible(false);
        editText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);

        hideKeyboard(activity);
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

}