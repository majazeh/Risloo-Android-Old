package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LifecycleOwner;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.WindowDecorator;

import org.json.JSONException;

import java.util.Objects;

public class CallUsActivity extends AppCompatActivity {

    // Vars
    private String name = "", mobile = "", message = "";

    // Objects
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    private Toolbar toolbar;
    private EditText inputEditText, nameEditText, mobileEditText, messageEditText;
    private Button sendButton;
    private Dialog infoDialog, repeatDialog, progressDialog;
    private TextView infoDialogTitle, infoDialogDescription, infoDialogConfirm, repeatDialogTitle, repeatDialogDescription, repeatDialogPositive, repeatDialogNegative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_call_us);

        initializer();

        detector();

        listener();

        if (firstTimeLoad()) {
            showDialog();
        }
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        handler = new Handler();

        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        toolbar = findViewById(R.id.activity_call_us_toolbar);

        nameEditText = findViewById(R.id.activity_call_us_name_editText);
        mobileEditText = findViewById(R.id.activity_call_us_mobile_editText);
        messageEditText = findViewById(R.id.activity_call_us_message_editText);

        sendButton = findViewById(R.id.activity_call_us_send_button);

        infoDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(infoDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        infoDialog.setContentView(R.layout.dialog_note);
        infoDialog.setCancelable(true);
        repeatDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(repeatDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        repeatDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        repeatDialog.setContentView(R.layout.dialog_action);
        repeatDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(infoDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        infoDialog.getWindow().setAttributes(layoutParams);

        infoDialogTitle = infoDialog.findViewById(R.id.dialog_note_title_textView);
        infoDialogTitle.setText(getResources().getString(R.string.CallUsInfoDialogTitle));
        infoDialogDescription = infoDialog.findViewById(R.id.dialog_note_description_textView);
        infoDialogDescription.setText(getResources().getString(R.string.CallUsInfoDialogDescription));
        infoDialogConfirm = infoDialog.findViewById(R.id.dialog_note_confirm_textView);
        infoDialogConfirm.setText(getResources().getString(R.string.CallUsInfoDialogConfirm));
        repeatDialogTitle = repeatDialog.findViewById(R.id.dialog_action_title_textView);
        repeatDialogTitle.setText(getResources().getString(R.string.CallUsRepeatDialogTitle));
        repeatDialogDescription = repeatDialog.findViewById(R.id.dialog_action_description_textView);
        repeatDialogDescription.setText(getResources().getString(R.string.CallUsRepeatDialogDescription));
        repeatDialogPositive = repeatDialog.findViewById(R.id.dialog_action_positive_textView);
        repeatDialogPositive.setText(getResources().getString(R.string.CallUsRepeatDialogPositive));
        repeatDialogNegative = repeatDialog.findViewById(R.id.dialog_action_negative_textView);
        repeatDialogNegative.setText(getResources().getString(R.string.CallUsRepeatDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            sendButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);

            infoDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            repeatDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            repeatDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        nameEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                if (!nameEditText.hasFocus()) {
                    if (inputEditText != null && inputEditText.hasFocus()) {
                        clearInput(inputEditText);
                    }

                    selectInput(nameEditText);
                    focusInput(nameEditText);
                }
            }
            return false;
        });

        mobileEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                if (!mobileEditText.hasFocus()) {
                    if (inputEditText != null && inputEditText.hasFocus()) {
                        clearInput(inputEditText);
                    }

                    selectInput(mobileEditText);
                    focusInput(mobileEditText);
                }
            }
            return false;
        });

        messageEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                if (!messageEditText.hasFocus()) {
                    if (inputEditText != null && inputEditText.hasFocus()) {
                        clearInput(inputEditText);
                    }

                    selectInput(messageEditText);
                    focusInput(messageEditText);
                }
            }
            return false;
        });

        sendButton.setOnClickListener(v -> {
            if (inputEditText != null && inputEditText.hasFocus()) {
                clearInput(inputEditText);
            }

            if (nameEditText.length() == 0) {
                errorInput(nameEditText);
            }
            if (mobileEditText.length() == 0) {
                errorInput(mobileEditText);
            }
            if (messageEditText.length() == 0) {
                errorInput(messageEditText);
            }

            if (nameEditText.length() != 0 && mobileEditText.length() != 0 && messageEditText.length() != 0) {
                clearInput(nameEditText);
                clearInput(mobileEditText);
                clearInput(messageEditText);

                doWork();
            }
        });

        infoDialogConfirm.setOnClickListener(v -> {
            infoDialogConfirm.setClickable(false);
            handler.postDelayed(() -> infoDialogConfirm.setClickable(true), 300);
            infoDialog.dismiss();
        });

        infoDialog.setOnCancelListener(dialog -> infoDialog.dismiss());

        repeatDialogPositive.setOnClickListener(v -> {
            repeatDialogPositive.setClickable(false);
            handler.postDelayed(() -> repeatDialogPositive.setClickable(true), 300);
            repeatDialog.dismiss();

            nameEditText.getText().clear();
            mobileEditText.getText().clear();
            messageEditText.getText().clear();
        });

        repeatDialogNegative.setOnClickListener(v -> {
            repeatDialogNegative.setClickable(false);
            handler.postDelayed(() -> repeatDialogNegative.setClickable(true), 300);
            repeatDialog.dismiss();

            finish();
        });

        repeatDialog.setOnCancelListener(dialog -> {
            repeatDialog.dismiss();

            nameEditText.getText().clear();
            mobileEditText.getText().clear();
            messageEditText.getText().clear();
        });

    }

    private boolean firstTimeLoad() {
        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        return sharedPreferences.getBoolean("firstTimeLoad", true);
    }

    private void showDialog() {
        infoDialog.show();

        editor.putBoolean("firstTimeLoad", false);
        editor.apply();
    }

    private void focusInput(EditText input) {
        this.inputEditText = input;
    }

    private void selectInput(EditText input) {
        input.setCursorVisible(true);
        input.setBackgroundResource(R.drawable.draw_16sdp_border_primary);
    }

    private void errorInput(EditText input) {
        input.clearFocus();
        input.setCursorVisible(false);
        input.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);

        hideKeyboard();
    }

    private void clearInput(EditText input) {
        input.clearFocus();
        input.setCursorVisible(false);
        input.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);

        hideKeyboard();
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    private void doWork() {
//        name = nameEditText.getText().toString().trim();
//        mobile = mobileEditText.getText().toString().trim();
//        message = messageEditText.getText().toString().trim();
//
//        try {
//            progressDialog.show();
//            viewModel.send(name, mobile, message);
//            observeWork();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void observeWork() {
//        CallUsRepository.workState.observe((LifecycleOwner) this, integer -> {
//            if (CallUsRepository.work.equals("send")) {
//                if (integer == 1) {
//                    repeatDialog.show();
//
//                    progressDialog.dismiss();
//                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
//                    CallUsRepository.workState.removeObservers((LifecycleOwner) this);
//                } else if (integer == 0) {
//                    progressDialog.dismiss();
//                    observeException();
//                    CallUsRepository.workState.removeObservers((LifecycleOwner) this);
//                } else if (integer == -2) {
//                    progressDialog.dismiss();
//                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
//                    CallUsRepository.workState.removeObservers((LifecycleOwner) this);
//                }
//            }
//        });
    }

    private void observeException() {
        if (ExceptionManager.current_exception.equals("callUs")) {
            try {
                if (!ExceptionManager.errors.isNull("name")) {
                    nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    Toast.makeText(this, "" + ExceptionManager.errors.getString("name"), Toast.LENGTH_SHORT).show();
                }
                if (!ExceptionManager.errors.isNull("mobile")) {
                    mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    Toast.makeText(this, "" + ExceptionManager.errors.getString("mobile"), Toast.LENGTH_SHORT).show();
                }
                if (!ExceptionManager.errors.isNull("message")) {
                    messageEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                    Toast.makeText(this, "" + ExceptionManager.errors.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}