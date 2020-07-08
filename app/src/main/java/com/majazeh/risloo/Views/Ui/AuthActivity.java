package com.majazeh.risloo.Views.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Models.Repositories.Authentication.AuthController;
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
    private String input, name, mobile, gender = "male", password, previousStep = "none", currentStep = "serial";
    private boolean inputTouch, inputError, nameTouch, nameError, mobileTouch, mobileError, passwordTouch, passwordError, passwordVisibility;

    // Objects
    private ClickableSpan serialLinkSpan, passwordLinkSpan, mobileLinkSpan;

    // Widgets
    private Toolbar titleToolbar;
    private LinearLayout authLinearLayout, registerLinearLayout;
    private TextView authDescriptionTextView;
    private EditText authInputEditText, registerNameEditText, registerMobileEditText, registerPasswordEditText;
    private TabLayout registerGenderTabLayout;
    private ImageView registerPasswordImageView;
    private Button authButton, registerButton;
    private TextView authLinkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_auth);

        initializer();

        detector();

        listener();

        launchStep(viewModel.getStep());
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        titleToolbar = findViewById(R.id.activity_auth_toolbar);

        authLinearLayout = findViewById(R.id.activity_auth_linearLayout);
        registerLinearLayout = findViewById(R.id.activity_register_linearLayout);

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

        authButton = findViewById(R.id.activity_auth_button);
        registerButton = findViewById(R.id.activity_register_button);

        authLinkTextView = findViewById(R.id.activity_auth_link_textView);
        authLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            authButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
            registerButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        titleToolbar.setNavigationOnClickListener(v -> startActivity(new Intent(this, MoreActivity.class)));

        authInputEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                authInputEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                authInputEditText.setCursorVisible(true);

                inputTouch = true;
                inputError = false;
            }
            return false;
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

        authButton.setOnClickListener(v -> {
            input = authInputEditText.getText().toString().trim();

            if (authInputEditText.length() == 0) {
                checkInput("auth");
            } else {
                clearData("auth");
                checkState();

            }
        });

        registerButton.setOnClickListener(v -> {
            name = registerNameEditText.getText().toString().trim();
            mobile = registerMobileEditText.getText().toString().trim();
            password = registerPasswordEditText.getText().toString().trim();

            if (registerNameEditText.length() == 0 || registerMobileEditText.length() == 0 || registerPasswordEditText.length() == 0) {
                checkInput("register");
            } else {
                clearData("register");

                try {
                    viewModel.signIn(name, gender, mobile, password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AuthController.workState.observe((LifecycleOwner) this, integer -> {
                    if (AuthController.workState.getValue() == 1) {
                        previousStep = "register";
                        currentStep = "mobile";
                        showAuth();
                        launchStep(viewModel.getStep());
                    } else {
                        // TODO: handle error with AuthController.exception
                    }
                });
            }
        });

        serialLinkSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                previousStep = "serial";
                currentStep = "register";
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

        mobileLinkSpan = new ClickableSpan() {
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
        authLinearLayout.setVisibility(View.VISIBLE);
        registerLinearLayout.setVisibility(View.GONE);
    }

    private void showRegister() {
        titleToolbar.setTitle(getResources().getString(R.string.RegisterTitle));
        authLinearLayout.setVisibility(View.GONE);
        registerLinearLayout.setVisibility(View.VISIBLE);
    }

    private void launchStep(JSONObject step) {
        try {
            titleToolbar.setTitle(step.get("title").toString());
            authDescriptionTextView.setText(step.get("description").toString());
            authInputEditText.setText("");
            authInputEditText.setHint(step.get("hint").toString());
            authButton.setText(step.get("button").toString());

            if (step.get("link").toString() == "") {
                authLinkTextView.setVisibility(View.INVISIBLE);
            } else {
                authLinkTextView.setVisibility(View.VISIBLE);

                if (step.get("step") == "serial") {
                    authLinkTextView.setText(StringCustomizer.clickable(step.get("link").toString(), 18, 25, serialLinkSpan));
                } else if (step.get("step") == "password") {
                    authLinkTextView.setText(StringCustomizer.clickable(step.get("link").toString(), 26, 33, passwordLinkSpan));
                } else if (step.get("step") == "mobile") {
                    authLinkTextView.setText(StringCustomizer.clickable(step.get("link").toString(), 24, 34, mobileLinkSpan));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void launchSample() {
        startActivity(new Intent(this, SampleActivity.class));
    }

    private void checkState() {
        // TODO: start loading
        switch (AuthController.theory) {
            case "":
                try {
                    viewModel.auth(input);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AuthController.workState.observe((LifecycleOwner) this, integer -> {
                    if (AuthController.workState.getValue() == 1) {
                        previousStep = "serial";
                        currentStep = "password";
                        if (AuthController.theory.equals("")) {
                            AuthController.workState.removeObservers((LifecycleOwner) this);
                            launchSample();
                        } else if (AuthController.theory.equals("auth"))
                            authentication();
                        else
                            launchStep(viewModel.getStep());
                        // TODO: end loading
                    } else if (AuthController.workState.getValue() == 0) {
                        // TODO: handle error with AuthController.exception
                        //Log.e("listener: ", String.valueOf(AuthController.workState.getValue()));
                    } else {
                        //noting
                    }
                });
                break;
            case "password":
                try {
                    viewModel.auth_theory(input, "");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AuthController.workState.observe((LifecycleOwner) this, integer -> {
                    if (AuthController.workState.getValue() == 1) {
                        previousStep = "serial";
                        currentStep = "password";
                        if (AuthController.theory.equals("")) {
                            AuthController.workState.removeObservers((LifecycleOwner) this);
                            launchSample();
                        } else if (AuthController.theory.equals("auth"))
                            authentication();
                        else
                            launchStep(viewModel.getStep());
                        // TODO: end loading
                    } else if (AuthController.workState.getValue() == 0) {
                        // TODO: handle error with AuthController.exception
                        //Log.e("listener: ", String.valueOf(AuthController.workState.getValue()));
                    } else {
                        //noting
                    }
                });
                break;
            case "mobileCode":
                try {
                    viewModel.auth_theory("", input);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AuthController.workState.observe((LifecycleOwner) this, integer -> {
                    if (AuthController.workState.getValue() == 1) {
                        previousStep = "serial";
                        currentStep = "password";
                        if (AuthController.theory.equals("")) {
                            AuthController.workState.removeObservers((LifecycleOwner) this);
                            launchSample();
                        } else if (AuthController.theory.equals("auth"))
                            authentication();
                        else
                            launchStep(viewModel.getStep());
                        // TODO: end loading
                    } else if (AuthController.workState.getValue() == 0) {
                        // TODO: handle error with AuthController.exception
                        //Log.e("listener: ", String.valueOf(AuthController.workState.getValue()));
                    } else {
                        //noting
                    }
                });
                break;

        }
        Log.e("checkState: ", AuthController.token);

    }

    public void authentication() {
        try {
            viewModel.auth_theory("", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AuthController.workState.observe((LifecycleOwner) this, integer -> {
            if (AuthController.workState.getValue() == 1) {
                previousStep = "serial";
                currentStep = "password";
                if (AuthController.theory.equals("")) {
                    AuthController.workState.removeObservers((LifecycleOwner) this);
                    launchSample();
                } else
                    launchStep(viewModel.getStep());
                // TODO: end loading
            } else if (AuthController.workState.getValue() == 0) {
                // TODO: handle error with AuthController.exception
                //Log.e("listener: ", String.valueOf(AuthController.workState.getValue()));
            } else {
                //noting
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // TODO: back process
    }

    @Override
    public void onBackPressed() {
        switch (AuthController.theory) {
            case "pin":
                if (previousStep == "mobile") {
                    previousStep = "none";
                    currentStep = "serial";
                    launchStep(viewModel.getStep());
                } else if (previousStep == "password") {
                    previousStep = "serial";
                    currentStep = "password";
                    launchStep(viewModel.getStep());
                }
                break;
            case "mobile":
                previousStep = "serial";
                currentStep = "register";
                showRegister();
                break;
            case "password":
            case "register":
                previousStep = "none";
                currentStep = "serial";
                showAuth();
                launchStep(viewModel.getStep());
                break;
            case "serial":
                finish();
                break;
        }
    }

}