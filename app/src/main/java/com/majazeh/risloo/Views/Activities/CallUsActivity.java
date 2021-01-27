package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;

import java.util.Objects;

public class CallUsActivity extends AppCompatActivity {

    // Vars
    private String name = "", mobile = "", message = "";

    // Objects
    private Handler handler;
    private ControlEditText controlEditText;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private EditText nameEditText, mobileEditText, messageEditText;
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

        if (firstMessage()) {
            showDialog();
        }
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        handler = new Handler();

        controlEditText = new ControlEditText();

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.CallUsTitle));

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

        WindowManager.LayoutParams layoutParamsInfoDialog = new WindowManager.LayoutParams();
        layoutParamsInfoDialog.copyFrom(infoDialog.getWindow().getAttributes());
        layoutParamsInfoDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsInfoDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        infoDialog.getWindow().setAttributes(layoutParamsInfoDialog);

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
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            sendButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);

            infoDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            repeatDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            repeatDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbarImageView.setOnClickListener(v -> {
            toolbarImageView.setClickable(false);
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 250);

            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        nameEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                if (!nameEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(nameEditText);
                    controlEditText.select(nameEditText);
                }
            }
            return false;
        });

        mobileEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                if (!mobileEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(mobileEditText);
                    controlEditText.select(mobileEditText);
                }
            }
            return false;
        });

        messageEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                if (!messageEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(messageEditText);
                    controlEditText.select(messageEditText);
                }
            }
            return false;
        });

        sendButton.setOnClickListener(v -> {
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (nameEditText.length() == 0) {
                controlEditText.error(this, nameEditText);
            }
            if (mobileEditText.length() == 0) {
                controlEditText.error(this, mobileEditText);
            }
            if (messageEditText.length() == 0) {
                controlEditText.error(this, messageEditText);
            }

            if (nameEditText.length() != 0 && mobileEditText.length() != 0 && messageEditText.length() != 0) {
                controlEditText.clear(this, nameEditText);
                controlEditText.clear(this, mobileEditText);
                controlEditText.clear(this, messageEditText);

                doWork();
            }
        });

        infoDialogConfirm.setOnClickListener(v -> {
            infoDialogConfirm.setClickable(false);
            handler.postDelayed(() -> infoDialogConfirm.setClickable(true), 250);
            infoDialog.dismiss();
        });

        infoDialog.setOnCancelListener(dialog -> infoDialog.dismiss());

        repeatDialogPositive.setOnClickListener(v -> {
            repeatDialogPositive.setClickable(false);
            handler.postDelayed(() -> repeatDialogPositive.setClickable(true), 250);
            repeatDialog.dismiss();

            nameEditText.getText().clear();
            mobileEditText.getText().clear();
            messageEditText.getText().clear();
        });

        repeatDialogNegative.setOnClickListener(v -> {
            repeatDialogNegative.setClickable(false);
            handler.postDelayed(() -> repeatDialogNegative.setClickable(true), 250);
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

    private void errorException(String type) {
        switch (type) {
            case "name":
                nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "mobile":
                mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "message":
                messageEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
        }
    }

    private boolean firstMessage() {
        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        return sharedPreferences.getBoolean("firstMessage", true);
    }

    private void showDialog() {
        infoDialog.show();

        editor.putBoolean("firstMessage", false);
        editor.apply();
    }

    private void doWork() {
//        name = nameEditText.getText().toString().trim();
//        mobile = mobileEditText.getText().toString().trim();
//        message = messageEditText.getText().toString().trim();
//
//        try {
//            progressDialog.show();
//
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
//                    Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
//                    CallUsRepository.workState.removeObservers((LifecycleOwner) this);
//                } else if (integer == 0) {
//                    progressDialog.dismiss();
//                    observeException();
//                    CallUsRepository.workState.removeObservers((LifecycleOwner) this);
//                } else if (integer == -2) {
//                    progressDialog.dismiss();
//                    Toast.makeText(this, ExceptionManager.fa_message_text, Toast.LENGTH_SHORT).show();
//                    CallUsRepository.workState.removeObservers((LifecycleOwner) this);
//                }
//            }
//        });
    }

    private void observeException() {
        if (ExceptionGenerator.current_exception.equals("callUs")) {
            String exceptionToast = "";

            if (!ExceptionGenerator.errors.isNull("name")) {
                errorException("name");
                exceptionToast = ExceptionGenerator.getErrorBody("name");
            }
            if (!ExceptionGenerator.errors.isNull("mobile")) {
                errorException("mobile");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("mobile");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("mobile"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("message")) {
                errorException("message");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("message");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("message"));
                }
            }
            Toast.makeText(this, exceptionToast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}