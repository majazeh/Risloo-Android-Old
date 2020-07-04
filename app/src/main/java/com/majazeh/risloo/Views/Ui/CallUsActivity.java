package com.majazeh.risloo.Views.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.WindowDecorator;

public class CallUsActivity extends AppCompatActivity {

    // Vars
    private String name, mobile, message;
    private boolean nameTouch, nameError, mobileTouch, mobileError, messageTouch, messageError;

    // Widgets
    private Toolbar toolbar;
    private EditText nameEditText, mobileEditText, messageEditText;
    private Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_call_us);

        initializer();

        detector();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        toolbar = findViewById(R.id.activity_call_us_toolbar);

        nameEditText = findViewById(R.id.activity_call_us_name_editText);
        nameEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mobileEditText = findViewById(R.id.activity_call_us_mobile_editText);
        mobileEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        messageEditText = findViewById(R.id.activity_call_us_message_editText);
        messageEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        sendButton = findViewById(R.id.activity_call_us_send_button);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            sendButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbar.setNavigationOnClickListener(v -> finish());

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
                // TODO : Send The Message To The Server
            }
        });
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

}