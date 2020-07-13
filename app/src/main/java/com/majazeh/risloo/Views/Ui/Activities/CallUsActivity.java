package com.majazeh.risloo.Views.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.WindowDecorator;

public class CallUsActivity extends AppCompatActivity {

    // Vars
    private String name, mobile, message;
    private boolean nameTouch, nameError, mobileTouch, mobileError, messageTouch, messageError;

    // Objects
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    private Toolbar toolbar;
    private TextView infoDialogTitle, infoDialogDescription, infoDialogConfirm;
    private EditText nameEditText, mobileEditText, messageEditText;
    private Button sendButton;
    private Dialog infoDialog;

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
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        handler = new Handler();

        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        toolbar = findViewById(R.id.activity_call_us_toolbar);

        nameEditText = findViewById(R.id.activity_call_us_name_editText);
        nameEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mobileEditText = findViewById(R.id.activity_call_us_mobile_editText);
        mobileEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        messageEditText = findViewById(R.id.activity_call_us_message_editText);
        messageEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        sendButton = findViewById(R.id.activity_call_us_send_button);

        infoDialog = new Dialog(this, R.style.DialogTheme);
        infoDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        infoDialog.setContentView(R.layout.dialog_note);
        infoDialog.setCancelable(true);

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
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            sendButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);

            infoDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        infoDialogConfirm.setOnClickListener(v -> {
            infoDialogConfirm.setClickable(false);
            handler.postDelayed(() -> infoDialogConfirm.setClickable(true), 1000);
            infoDialog.dismiss();
        });

        infoDialog.setOnCancelListener(dialog -> infoDialog.dismiss());

        nameEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                nameEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                nameEditText.setCursorVisible(true);

                nameTouch = true;
                nameError = false;

                if (mobileError) {
                    mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                } mobileTouch = false;

                if (messageError) {
                    messageEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    messageEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                } messageTouch = false;
            }
            return false;
        });

        mobileEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                mobileEditText.setCursorVisible(true);

                mobileTouch = true;
                mobileError = false;

                if (messageError) {
                    messageEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    messageEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                } messageTouch = false;

                if (nameError) {
                    nameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    nameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                } nameTouch = false;
            }
            return false;
        });

        messageEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                messageEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                messageEditText.setCursorVisible(true);

                messageTouch = true;
                messageError = false;

                if (nameError) {
                    nameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    nameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                } nameTouch = false;

                if (mobileError) {
                    mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                } mobileTouch = false;
            }
            return false;
        });

        sendButton.setOnClickListener(v -> {
            name = nameEditText.getText().toString().trim();
            mobile = mobileEditText.getText().toString().trim();
            message = messageEditText.getText().toString().trim();

            if (nameEditText.length() == 0 || mobileEditText.length() == 0 || messageEditText.length() == 0) {
                checkInput();
            } else {
                clearData();
                sendMessage();
            }
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

    private void checkInput() {
        nameEditText.setCursorVisible(false);
        mobileEditText.setCursorVisible(false);
        messageEditText.setCursorVisible(false);

        nameTouch = false;
        mobileTouch = false;
        messageTouch = false;

        if (nameEditText.length() == 0 && mobileEditText.length() == 0 && messageEditText.length() == 0) {
            nameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            messageEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            nameError = true;
            mobileError = true;
            messageError = true;
        } else if (mobileEditText.length() == 0 && messageEditText.length() == 0) {
            nameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            messageEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            nameError = false;
            mobileError = true;
            messageError = true;
        } else if (nameEditText.length() == 0 && messageEditText.length() == 0) {
            nameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            messageEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            nameError = true;
            mobileError = false;
            messageError = true;
        } else if (nameEditText.length() == 0 && mobileEditText.length() == 0) {
            nameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            messageEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            nameError = true;
            mobileError = true;
            messageError = false;
        } else if (messageEditText.length() == 0) {
            nameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            messageEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            nameError = false;
            mobileError = false;
            messageError = true;
        } else if (mobileEditText.length() == 0) {
            nameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            messageEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            nameError = false;
            mobileError = true;
            messageError = false;
        } else if (nameEditText.length() == 0) {
            nameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            messageEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            nameError = true;
            mobileError = false;
            messageError = false;
        } else {
            nameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            messageEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            nameError = false;
            mobileError = false;
            messageError = false;
        }
    }

    private void clearData() {
        nameEditText.setCursorVisible(false);
        mobileEditText.setCursorVisible(false);
        messageEditText.setCursorVisible(false);

        nameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
        mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
        messageEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);

        nameTouch = false;
        mobileTouch = false;
        messageTouch = false;

        nameError = false;
        mobileError = false;
        messageError = false;
    }

    private void sendMessage() {
        // TODO : Send The Message To The Server
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}