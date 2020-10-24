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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;

import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.CustomEditText;
import com.majazeh.risloo.Utils.StringManager;
import com.majazeh.risloo.Views.Activities.AuthActivity;

import org.json.JSONException;

import java.util.Objects;

public class PasswordFragment extends Fragment {

    // Vars
    private String password = "";
    private boolean passwordVisibility = false;

    // Objects
    private Activity activity;
    private ClickableSpan passwordLinkSpan;

    // Widgets
    private TextView passwordDescriptionTextView;
    public CustomEditText passwordEditText;
    private Button passwordButton;
    private ImageView passwordImageView;
    private TextView passwordLinkTextView;

    public PasswordFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_password, viewGroup, false);

        initializer(view);

        detector();

        listener();

        setText();

        return view;
    }

    private void initializer(View view) {
        passwordDescriptionTextView = view.findViewById(R.id.fragment_password_description_textView);

        passwordEditText = view.findViewById(R.id.fragment_password_editText);

        passwordImageView = view.findViewById(R.id.fragment_password_imageView);

        passwordButton = view.findViewById(R.id.fragment_password_button);

        passwordLinkTextView = view.findViewById(R.id.fragment_password_link_textView);
        passwordLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            passwordButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        passwordEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!passwordEditText.hasFocus()) {
                    ((AuthActivity) Objects.requireNonNull(getActivity())).inputHandler.focus(passwordEditText);
                    ((AuthActivity) Objects.requireNonNull(getActivity())).inputHandler.select(passwordEditText);
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

        passwordEditText.setOnCutCopyPasteListener(new CustomEditText.OnCutCopyPasteListener() {
            @Override
            public void onCut() {
                
            }

            @Override
            public void onCopy() {

            }

            @Override
            public void onPaste() {
                if (passwordEditText.length() != 0) {
                    passwordImageView.setVisibility(View.VISIBLE);
                }
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

        passwordButton.setOnClickListener(v -> {
            if (passwordEditText.length() == 0) {
                ((AuthActivity) Objects.requireNonNull(getActivity())).inputHandler.error(getActivity(), passwordEditText);
            } else {
                ((AuthActivity) Objects.requireNonNull(getActivity())).inputHandler.clear(getActivity(), passwordEditText);
                doWork();
            }
        });

        passwordLinkSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                AuthRepository.theory = "recover";
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
        passwordLinkTextView.setText(StringManager.clickable(activity.getResources().getString(R.string.PasswordLink), 26, 33, passwordLinkSpan));
    }

    private void doWork() {
        password = passwordEditText.getText().toString().trim();

        try {
            ((AuthActivity) Objects.requireNonNull(getActivity())).progressDialog.show();
            ((AuthActivity) Objects.requireNonNull(getActivity())).viewModel.authTheory(password, "");
            ((AuthActivity) Objects.requireNonNull(getActivity())).observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}