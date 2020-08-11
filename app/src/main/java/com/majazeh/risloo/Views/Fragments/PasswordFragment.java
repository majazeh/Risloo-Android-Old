package com.majazeh.risloo.Views.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
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

import com.majazeh.risloo.Models.Controllers.AuthController;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.StringCustomizer;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Activities.AuthActivity;

import org.json.JSONException;

import java.util.Objects;

public class PasswordFragment extends Fragment {

    // ViewModels
    private AuthViewModel viewModel;

    // Vars
    private String password = "";
    private boolean passwordVisibility;

    // Objects
    private Activity activity;
    private ClickableSpan passwordLinkSpan;

    // Widgets
    private TextView passwordDescriptionTextView;
    private EditText passwordEditText;
    private Button passwordButton;
    private ImageView passwordImageView;
    private TextView passwordLinkTextView;

    public PasswordFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_password, viewGroup, false);

        initializer(view);

        detector();

        listener();

        setText();

        return view;
    }

    private void initializer(View view) {
        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        passwordDescriptionTextView = view.findViewById(R.id.fragment_password_description_textView);

        passwordEditText = view.findViewById(R.id.fragment_password_editText);

        passwordImageView = view.findViewById(R.id.fragment_password_imageView);

        passwordButton = view.findViewById(R.id.fragment_password_button);

        passwordLinkTextView = view.findViewById(R.id.fragment_password_link_textView);
        passwordLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            passwordButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        passwordEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                passwordEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                passwordEditText.setCursorVisible(true);
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

        passwordImageView.setOnClickListener(v -> {
            if (!passwordVisibility) {
                passwordVisibility = true;

                passwordImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye));

                ImageViewCompat.setImageTintList(passwordImageView, AppCompatResources.getColorStateList(activity, R.color.Primary));
                passwordEditText.setTransformationMethod(null);
            } else {
                passwordVisibility = false;

                passwordImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_slash));

                ImageViewCompat.setImageTintList(passwordImageView, AppCompatResources.getColorStateList(activity, R.color.Mischka));
                passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        passwordButton.setOnClickListener(v -> {
            password = passwordEditText.getText().toString().trim();

            if (passwordEditText.length() == 0) {
                checkInput();
            } else {
                clearData();
                doWork();
            }
        });

        passwordLinkSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                AuthController.theory = "recover";
                ((AuthActivity) Objects.requireNonNull(getActivity())).showFragment();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.PrimaryDark));
                textPaint.setUnderlineText(false);
            }
        };
    }

    private void setText() {
        passwordLinkTextView.setText(StringCustomizer.clickable(activity.getResources().getString(R.string.PasswordLink), 26, 33, passwordLinkSpan));
    }

    private void checkInput() {
        passwordEditText.setCursorVisible(false);

        if (passwordEditText.length() == 0) {
            passwordEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
        }
    }

    private void clearData() {
        passwordEditText.setCursorVisible(false);

        passwordEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
    }

    private void doWork() {
        try {
            ((AuthActivity) Objects.requireNonNull(getActivity())).progressDialog.show();
            viewModel.authTheory(password, "");
            ((AuthActivity) Objects.requireNonNull(getActivity())).observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}