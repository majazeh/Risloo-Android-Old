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
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.CutCopyPasteEditText;
import com.majazeh.risloo.Views.Activities.EditAccountActivity;

import org.json.JSONException;

public class EditPasswordFragment extends Fragment {

    // Vars
    private String password = "";
    private boolean passwordVisibility = false;

    // Objects
    private Activity activity;

    // Widgets
    public CutCopyPasteEditText passwordEditText;
    private ImageView passwordImageView;
    private Button editButton;

    public EditPasswordFragment(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_edit_password, viewGroup, false);

        initializer(view);

        detector();

        listener();

        setData();

        return view;
    }

    private void initializer(View view) {
        passwordEditText = view.findViewById(R.id.edit_password_editText);

        passwordImageView = view.findViewById(R.id.edit_password_imageView);

        editButton = view.findViewById(R.id.edit_password_button);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            editButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        passwordEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!passwordEditText.hasFocus()) {
                    ((EditAccountActivity) getActivity()).controlEditText.focus(passwordEditText);
                    ((EditAccountActivity) getActivity()).controlEditText.select(passwordEditText);
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

        passwordEditText.setOnCutCopyPasteListener(new CutCopyPasteEditText.OnCutCopyPasteListener() {
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
                passwordImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_eye_light, null));

                ImageViewCompat.setImageTintList(passwordImageView, AppCompatResources.getColorStateList(activity, R.color.Primary));
                passwordEditText.setTransformationMethod(null);
            } else {
                passwordVisibility = false;
                passwordImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_eye_slash_light, null));

                ImageViewCompat.setImageTintList(passwordImageView, AppCompatResources.getColorStateList(activity, R.color.Mischka));
                passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        editButton.setOnClickListener(v -> {
            if (passwordEditText.length() == 0) {
                ((EditAccountActivity) getActivity()).controlEditText.error(getActivity(), passwordEditText);
            } else {
                ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), passwordEditText);

                doWork();
            }
        });
    }

    private void setData() {
        if (((EditAccountActivity) getActivity()).authViewModel.getPassword().equals("")) {
            passwordEditText.setHint(getResources().getString(R.string.EditPasswordHint));
        } else {
            password = ((EditAccountActivity) getActivity()).authViewModel.getPassword();
            passwordEditText.setText(password);
        }
    }

    private void doWork() {
        password = passwordEditText.getText().toString().trim();

        try {
            ((EditAccountActivity) getActivity()).progressDialog.show();

            ((EditAccountActivity) getActivity()).authViewModel.editPassword(password);
            ((EditAccountActivity) getActivity()).observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}