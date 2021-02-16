package com.majazeh.risloo.Views.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
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

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.CutCopyPasteEditText;
import com.majazeh.risloo.Views.Activities.AuthActivity;

import org.json.JSONException;

import java.util.Objects;

public class ChangePasswordFragment extends Fragment {

    // Vars
    private String password = "";
    private boolean passwordVisibility = false;

    // Objects
    private Activity activity;

    // Widgets
    private TextView changePasswordDescriptionTextView;
    public CutCopyPasteEditText changePasswordEditText;
    private Button changePasswordButton;
    private ImageView changePasswordImageView;
    private TextView changePasswordLinkTextView;

    public ChangePasswordFragment(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_change_password, viewGroup, false);

        initializer(view);

        detector();

        listener();

        return view;
    }

    private void initializer(View view) {
        changePasswordDescriptionTextView = view.findViewById(R.id.fragment_change_password_description_textView);

        changePasswordEditText = view.findViewById(R.id.fragment_change_password_editText);

        changePasswordImageView = view.findViewById(R.id.fragment_change_password_imageView);

        changePasswordButton = view.findViewById(R.id.fragment_change_password_button);

        changePasswordLinkTextView = view.findViewById(R.id.fragment_change_password_link_textView);
        changePasswordLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            changePasswordButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        changePasswordEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!changePasswordEditText.hasFocus()) {
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.focus(changePasswordEditText);
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.select(changePasswordEditText);
                }
            }
            return false;
        });

        changePasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (changePasswordEditText.length() == 0) {
                    changePasswordImageView.setVisibility(View.INVISIBLE);
                } else if (changePasswordEditText.length() == 1) {
                    changePasswordImageView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        changePasswordEditText.setOnCutCopyPasteListener(new CutCopyPasteEditText.OnCutCopyPasteListener() {
            @Override
            public void onCut() {

            }

            @Override
            public void onCopy() {

            }

            @Override
            public void onPaste() {
                if (changePasswordEditText.length() != 0) {
                    changePasswordImageView.setVisibility(View.VISIBLE);
                }
            }
        });

        changePasswordImageView.setOnClickListener(v -> {
            if (!passwordVisibility) {
                passwordVisibility = true;
                changePasswordImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_light));

                ImageViewCompat.setImageTintList(changePasswordImageView, AppCompatResources.getColorStateList(activity, R.color.Risloo500));
                changePasswordEditText.setTransformationMethod(null);
            } else {
                passwordVisibility = false;
                changePasswordImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_slash_light));

                ImageViewCompat.setImageTintList(changePasswordImageView, AppCompatResources.getColorStateList(activity, R.color.Gray300));
                changePasswordEditText.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        changePasswordButton.setOnClickListener(v -> {
            if (changePasswordEditText.length() == 0) {
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.error(getActivity(), changePasswordEditText);
            } else {
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.clear(getActivity(), changePasswordEditText);
                doWork();
            }
        });
    }

    private void doWork() {
        password = changePasswordEditText.getText().toString().trim();

        try {
            ((AuthActivity) Objects.requireNonNull(getActivity())).progressDialog.show();

            ((AuthActivity) Objects.requireNonNull(getActivity())).authViewModel.authTheory(password, "");
            ((AuthActivity) Objects.requireNonNull(getActivity())).observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}