package com.majazeh.risloo.Utils.Widgets;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.majazeh.risloo.R;

import java.util.Objects;

public class ControlEditText {

    // Widgets
    private EditText editText;

    public EditText input() {
        return editText;
    }

    public void focus(EditText editText) {
        this.editText = editText;
    }

    public void select(EditText editText) {
        editText.requestFocus();
        editText.setBackgroundResource(R.drawable.draw_16sdp_border_primary);
    }

    public void error(Activity activity, EditText editText) {
        editText.clearFocus();
        editText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);

        hideKeyboard(activity, editText);
    }

    public void clear(Activity activity, EditText editText) {
        editText.clearFocus();
        editText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);

        hideKeyboard(activity, editText);
    }

    private void hideKeyboard(Activity activity, EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}