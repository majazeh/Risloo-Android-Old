package com.majazeh.risloo.Views.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.R;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Activities.AuthActivity;

import org.json.JSONException;

import java.util.Objects;

public class RegisterFragment extends Fragment {

    // ViewModels
    private AuthViewModel viewModel;

    // Vars
    private String name = "", mobile = "", gender = "male", password = "";
    private boolean nameError, mobileError, passwordError;
    private boolean passwordVisibility;

    // Objects
    private Activity activity;

    // Widgets
    private TextView registerDescriptionTextView;
    private EditText nameEditText, mobileEditText, passwordEditText;
    private TabLayout genderTabLayout;
    private Button registerButton;
    private ImageView passwordImageView;

    public RegisterFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_register, viewGroup, false);

        initializer(view);

        detector();

        listener();

        return view;
    }

    private void initializer(View view) {
        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        registerDescriptionTextView = view.findViewById(R.id.fragment_register_description_textView);

        nameEditText = view.findViewById(R.id.fragment_register_name_editText);
        mobileEditText = view.findViewById(R.id.fragment_register_mobile_editText);
        passwordEditText = view.findViewById(R.id.fragment_register_password_editText);

        genderTabLayout = view.findViewById(R.id.fragment_register_gender_tabLayout);

        passwordImageView = view.findViewById(R.id.fragment_register_imageView);

        registerButton = view.findViewById(R.id.fragment_register_button);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            registerButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        nameEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_primary);
                nameEditText.setCursorVisible(true);

                nameError = false;

                passwordImageView.setVisibility(View.INVISIBLE);

                if (mobileError) {
                    mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                } else {
                    mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                }

                if (passwordError) {
                    passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                } else {
                    passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                }
            }
            return false;
        });

        mobileEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_primary);
                mobileEditText.setCursorVisible(true);

                mobileError = false;

                passwordImageView.setVisibility(View.INVISIBLE);

                if (passwordError) {
                    passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                } else {
                    passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                }

                if (nameError) {
                    nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                } else {
                    nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                }
            }
            return false;
        });

        passwordEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_primary);
                passwordEditText.setCursorVisible(true);

                passwordError = false;

                if (passwordEditText.length() != 0) {
                    passwordImageView.setVisibility(View.VISIBLE);
                }

                if (nameError) {
                    nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                } else {
                    nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                }

                if (mobileError) {
                    mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                } else {
                    mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                }
            }
            return false;
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (passwordEditText.length() == 0) {
                    passwordImageView.setVisibility(View.INVISIBLE);
                } else if (passwordEditText.length() == 1) {
                    passwordImageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        genderTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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

        passwordImageView.setOnClickListener(v -> {
            if (!passwordVisibility) {
                passwordVisibility = true;
                passwordImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_light));

                ImageViewCompat.setImageTintList(passwordImageView, AppCompatResources.getColorStateList(activity, R.color.Primary));
                passwordEditText.setTransformationMethod(null);
            } else {
                passwordVisibility = false;
                passwordImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_slash_light));

                ImageViewCompat.setImageTintList(passwordImageView, AppCompatResources.getColorStateList(activity, R.color.Mischka));
                passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        registerButton.setOnClickListener(v -> {
            name = nameEditText.getText().toString().trim();
            mobile = mobileEditText.getText().toString().trim();
            password = passwordEditText.getText().toString().trim();

            if (nameEditText.length() == 0 || mobileEditText.length() == 0 || passwordEditText.length() == 0) {
                checkInput();
            } else {
                clearData();
                doWork();
            }
        });
    }

    private void checkInput() {
        nameEditText.setCursorVisible(false);
        mobileEditText.setCursorVisible(false);
        passwordEditText.setCursorVisible(false);

        if (nameEditText.length() == 0 && mobileEditText.length() == 0 && passwordEditText.length() == 0) {
            nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
            mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
            passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
            nameError = true;
            mobileError = true;
            passwordError = true;
        } else if (mobileEditText.length() == 0 && passwordEditText.length() == 0) {
            nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
            mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
            passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
            nameError = false;
            mobileError = true;
            passwordError = true;
        } else if (nameEditText.length() == 0 && passwordEditText.length() == 0) {
            nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
            mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
            passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
            nameError = true;
            mobileError = false;
            passwordError = true;
        } else if (nameEditText.length() == 0 && mobileEditText.length() == 0) {
            nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
            mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
            passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
            nameError = true;
            mobileError = true;
            passwordError = false;
        } else if (passwordEditText.length() == 0) {
            nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
            mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
            passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
            nameError = false;
            mobileError = false;
            passwordError = true;
        } else if (mobileEditText.length() == 0) {
            nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
            mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
            passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
            nameError = false;
            mobileError = true;
            passwordError = false;
        } else if (nameEditText.length() == 0) {
            nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
            mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
            passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
            nameError = true;
            mobileError = false;
            passwordError = false;
        } else {
            nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
            mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
            passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
            nameError = false;
            mobileError = false;
            passwordError = false;
        }
    }

    private void clearData() {
        nameEditText.setCursorVisible(false);
        mobileEditText.setCursorVisible(false);
        passwordEditText.setCursorVisible(false);

        nameEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
        mobileEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
        passwordEditText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);

        nameError = false;
        mobileError = false;
        passwordError = false;
    }

    private void doWork() {
        try {
            ((AuthActivity) Objects.requireNonNull(getActivity())).progressDialog.show();
            viewModel.register(name, mobile, gender, password);
            ((AuthActivity) Objects.requireNonNull(getActivity())).observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}