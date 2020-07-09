package com.majazeh.risloo.Views.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Models.Controller.AuthController;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.StringCustomizer;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

public class AuthActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel viewModel;

    // Vars
    private String input, name, mobile, gender = "male", password;
    private boolean inputTouch, inputError, nameTouch, nameError, mobileTouch, mobileError, passwordTouch, passwordError;
    private boolean passwordVisibility;

    // Objects
    private ClickableSpan serialLinkSpan, passwordLinkSpan, pinLinkSpan;

    // Widgets
    private Toolbar titleToolbar;
    private ViewFlipper viewFlipper;
    private TextView authDescriptionTextView;
    private EditText authInputEditText, registerNameEditText, registerMobileEditText, registerPasswordEditText;
    private TabLayout registerGenderTabLayout;
    private ImageView registerPasswordImageView;
    private TextView authLinkTextView;
    private Button authButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_auth);

        initializer();

        detector();

        listener();

        launchStep(viewModel.getStep("auth"));
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        titleToolbar = findViewById(R.id.activity_auth_toolbar);

        viewFlipper = findViewById(R.id.activity_auth_viewFlipper);

        authDescriptionTextView = findViewById(R.id.activity_auth_description_textView);

        authInputEditText = findViewById(R.id.activity_auth_input_editText);
        authInputEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        registerNameEditText = findViewById(R.id.activity_register_name_editText);
        registerNameEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        registerMobileEditText = findViewById(R.id.activity_register_mobile_editText);
        registerMobileEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        registerPasswordEditText = findViewById(R.id.activity_register_password_editText);
        registerPasswordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        registerGenderTabLayout = findViewById(R.id.activity_register_gender_tabLayout);

        registerPasswordImageView = findViewById(R.id.activity_register_password_imageView);

        authLinkTextView = findViewById(R.id.activity_auth_link_textView);
        authLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        authButton = findViewById(R.id.activity_auth_button);
        registerButton = findViewById(R.id.activity_register_button);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            authButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
            registerButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        titleToolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(this, MoreActivity.class));
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });

        authInputEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                authInputEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                authInputEditText.setCursorVisible(true);

                inputTouch = true;
                inputError = false;
            }
            return false;
        });

        authInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (AuthController.theory.equals("mobileCode")) {
                    authInputEditText.setMaxEms(6);
                    if (authInputEditText.length() == 6) {
                        checkProcess();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        registerNameEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                registerNameEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                registerNameEditText.setCursorVisible(true);

                nameTouch = true;
                nameError = false;

                registerPasswordImageView.setVisibility(View.INVISIBLE);

                if (mobileError) {
                    registerMobileEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    registerMobileEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                }
                mobileTouch = false;

                if (passwordError) {
                    registerPasswordEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    registerPasswordEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                }
                passwordTouch = false;
            }
            return false;
        });

        registerMobileEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                registerMobileEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                registerMobileEditText.setCursorVisible(true);

                mobileTouch = true;
                mobileError = false;

                registerPasswordImageView.setVisibility(View.INVISIBLE);

                if (passwordError) {
                    registerPasswordEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    registerPasswordEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                }
                passwordTouch = false;

                if (nameError) {
                    registerNameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    registerNameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                }
                nameTouch = false;
            }
            return false;
        });

        registerPasswordEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                registerPasswordEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                registerPasswordEditText.setCursorVisible(true);

                passwordTouch = true;
                passwordError = false;

                if (registerPasswordEditText.length() != 0) {
                    registerPasswordImageView.setVisibility(View.VISIBLE);
                }

                if (nameError) {
                    registerNameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    registerNameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                }
                nameTouch = false;

                if (mobileError) {
                    registerMobileEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                } else {
                    registerMobileEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                }
                mobileTouch = false;
            }
            return false;
        });

        registerPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (registerPasswordEditText.length() == 0) {
                    registerPasswordImageView.setVisibility(View.INVISIBLE);
                } else if (registerPasswordEditText.length() == 1) {
                    registerPasswordImageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        registerGenderTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        gender = "male";
                        break;
                    case 1:
                        gender = "female";
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        registerPasswordImageView.setOnClickListener(v -> {
            if (!passwordVisibility) {
                passwordVisibility = true;
                registerPasswordImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye));
                ImageViewCompat.setImageTintList(registerPasswordImageView, AppCompatResources.getColorStateList(this, R.color.Primary));
                registerPasswordEditText.setTransformationMethod(null);
            } else {
                passwordVisibility = false;
                registerPasswordImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_slash));
                ImageViewCompat.setImageTintList(registerPasswordImageView, AppCompatResources.getColorStateList(this, R.color.Mischka));
                registerPasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        serialLinkSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                AuthController.preTheory = "";
                AuthController.theory = "register";
                showRegister();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.PrimaryDark));
                textPaint.setUnderlineText(false);
            }
        };

        passwordLinkSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                recoverPassword();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.PrimaryDark));
                textPaint.setUnderlineText(false);
            }
        };

        pinLinkSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                resendCode();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.PrimaryDark));
                textPaint.setUnderlineText(false);
            }
        };

        authButton.setOnClickListener(v -> {
            input = authInputEditText.getText().toString().trim();
            name = "";
            mobile = "";
            password = "";

            if (authInputEditText.length() == 0) {
                checkInput("auth");
            } else {
                clearData("auth");
                checkProcess();
            }
        });

        registerButton.setOnClickListener(v -> {
            input = "";
            name = registerNameEditText.getText().toString().trim();
            mobile = registerMobileEditText.getText().toString().trim();
            password = registerPasswordEditText.getText().toString().trim();

            if (registerNameEditText.length() == 0 || registerMobileEditText.length() == 0 || registerPasswordEditText.length() == 0) {
                checkInput("register");
            } else {
                clearData("register");
                AuthController.theory = "register";
                checkProcess();
            }
        });
    }

    private void checkInput(String value) {
        if (value == "auth") {
            authInputEditText.setCursorVisible(false);

            inputTouch = false;

            if (authInputEditText.length() == 0) {
                authInputEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                inputError = true;
            }
        } else if (value == "register") {
            registerNameEditText.setCursorVisible(false);
            registerMobileEditText.setCursorVisible(false);
            registerPasswordEditText.setCursorVisible(false);

            registerPasswordImageView.setVisibility(View.INVISIBLE);

            nameTouch = false;
            mobileTouch = false;
            passwordTouch = false;

            if (registerNameEditText.length() == 0 && registerMobileEditText.length() == 0 && registerPasswordEditText.length() == 0) {
                registerNameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                registerMobileEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                registerPasswordEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                nameError = true;
                mobileError = true;
                passwordError = true;
            } else if (registerMobileEditText.length() == 0 && registerPasswordEditText.length() == 0) {
                registerNameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                registerMobileEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                registerPasswordEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                nameError = false;
                mobileError = true;
                passwordError = true;
            } else if (registerNameEditText.length() == 0 && registerPasswordEditText.length() == 0) {
                registerNameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                registerMobileEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                registerPasswordEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                nameError = true;
                mobileError = false;
                passwordError = true;
            } else if (registerNameEditText.length() == 0 && registerMobileEditText.length() == 0) {
                registerNameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                registerMobileEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                registerPasswordEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                nameError = true;
                mobileError = true;
                passwordError = false;
            } else if (registerPasswordEditText.length() == 0) {
                registerNameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                registerMobileEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                registerPasswordEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                nameError = false;
                mobileError = false;
                passwordError = true;
            } else if (registerMobileEditText.length() == 0) {
                registerNameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                registerMobileEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                registerPasswordEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                nameError = false;
                mobileError = true;
                passwordError = false;
            } else if (registerNameEditText.length() == 0) {
                registerNameEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
                registerMobileEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                registerPasswordEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                nameError = true;
                mobileError = false;
                passwordError = false;
            } else {
                registerNameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                registerMobileEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                registerPasswordEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
                nameError = false;
                mobileError = false;
                passwordError = false;
            }
        }
    }

    private void clearData(String value) {
        if (value == "auth") {
            authInputEditText.setCursorVisible(false);
            authInputEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);

            inputTouch = false;
            inputError = false;
        } else if (value == "register") {
            registerNameEditText.setCursorVisible(false);
            registerMobileEditText.setCursorVisible(false);
            registerPasswordEditText.setCursorVisible(false);

            registerPasswordImageView.setVisibility(View.INVISIBLE);

            registerNameEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            registerMobileEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
            registerPasswordEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);

            nameTouch = false;
            mobileTouch = false;
            passwordTouch = false;

            nameError = false;
            mobileError = false;
            passwordError = false;
        }
    }

    private void recoverPassword() {
        // TODO : Recover User's Password If He Has Forgot It
    }

    private void resendCode() {
        // TODO : Resend Mobile Register Code If He Hasn't Got The Previous Code
    }

    private void showAuth() {
        viewFlipper.setInAnimation(this, R.anim.slide_in_right_with_fade);
        viewFlipper.setOutAnimation(this, R.anim.slide_out_left_with_fade);
        viewFlipper.showPrevious();
    }

    private void showRegister() {
        titleToolbar.setTitle(getResources().getString(R.string.RegisterTitle));
        viewFlipper.setInAnimation(this, R.anim.slide_in_left_with_fade);
        viewFlipper.setOutAnimation(this, R.anim.slide_out_right_with_fade);
        viewFlipper.showNext();
    }

    ////////////////////////////////////////////////////////////////////////

    private void authProcess(String theory) {

    }

    private void checkProcess() {
        // TODO : start loading
        try {
            switch (AuthController.theory) {
                case "auth":
                    viewModel.auth(input);
                    authenticating();
                    break;
                case "password":
                    viewModel.authTheory(input, "");
                    authenticating();
                    break;
                case "mobileCode":
                    viewModel.authTheory("", input);
                    authenticating();
                    break;
                case "register":
                    viewModel.register(name, mobile, gender, password);
                    authenticating();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void authenticating() {
        AuthController.workState.observe(this, integer -> {
            if (AuthController.workState.getValue() == 1) {
                if (AuthController.key == "") {
                    if (AuthController.callback == "") {
                        AuthController.workState.removeObservers(this);
                        startActivity(new Intent(this, SampleActivity.class));
                    } else {
                        launchStep(viewModel.getStep("auth"));
                    }
                } else {
                    if (AuthController.theory == "auth") {
                        try {
                            viewModel.authTheory("", "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else
                        launchStep(viewModel.getStep(AuthController.theory));
                }
                if (!name.equals("")){
                    showAuth();
                }
            } else if (AuthController.workState.getValue() == 0) {
                // TODO: handle error with AuthController.exception

            } else {
                // DO Nothing
            }

        });

    }

//    private void registering() {
//        AuthController.workState.observe(this, integer -> {
//            if (AuthController.workState.getValue() == 1) {
//                showAuth();
////                launchStep(viewModel.getStep("register"));
//            } else {
//                // TODO: handle error with AuthController.exception
//            }
//        });
//    }

    private void launchStep(JSONObject step) {
        try {
            titleToolbar.setTitle(step.get("title").toString());
            authDescriptionTextView.setText(step.get("description").toString());
            authInputEditText.setText("");
            authInputEditText.setHint(step.get("hint").toString());
            authButton.setText(step.get("button").toString());

            switch (AuthController.theory) {
                case "auth":
                    authLinkTextView.setText(StringCustomizer.clickable(step.get("link").toString(), 18, 25, serialLinkSpan));
                    break;
                case "password":
                    authLinkTextView.setText(StringCustomizer.clickable(step.get("link").toString(), 26, 33, passwordLinkSpan));
                    break;
                case "mobileCode":
                    authLinkTextView.setText(StringCustomizer.clickable(step.get("link").toString(), 24, 34, pinLinkSpan));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
//        switch (AuthController.theory) {
//            case "mobileCode":
//                if (AuthController.preTheory.equals("password")) {
//                    AuthController.theory.equals("password");
//                    launchStep(viewModel.getStep());
//                } else if (AuthController.preTheory.equals("register")) {
//
//                } else if (AuthController.preTheory.equals("")) {
//
//                }
//                break;
//            case "serial":
//                if (AuthController.theory.equals("password")) {
//                    launchStep(viewModel.getStep());
//                } else if (AuthController.theory.equals("register")) {
//                    showAuth();
//                    launchStep(viewModel.getStep());
//                }
//                break;
//            case "":
//                finish();
//                break;
//        }
    }

}