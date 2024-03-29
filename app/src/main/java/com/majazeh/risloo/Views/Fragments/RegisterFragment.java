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

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.CutCopyPasteEditText;
import com.majazeh.risloo.Views.Activities.AuthActivity;

import org.json.JSONException;

import java.util.Objects;

public class RegisterFragment extends Fragment {

    // Vars
    private String name = "", mobile = "", gender = "male", password = "";
    private boolean passwordVisibility = false;
    public boolean genderException = false;

    // Objects
    private Activity activity;

    // Widgets
    private TextView registerDescriptionTextView;
    public EditText nameEditText, mobileEditText;
    public CutCopyPasteEditText passwordEditText;
    public TabLayout genderTabLayout;
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
                if (!nameEditText.hasFocus()) {
                    if (((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.input() != null && ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.input().hasFocus()) {
                        ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.clear(getActivity(), ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.input());
                    }

                    if (passwordImageView.getVisibility() == View.VISIBLE) {
                        passwordImageView.setVisibility(View.INVISIBLE);
                    }

                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.focus(nameEditText);
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.select(nameEditText);
                }
            }
            return false;
        });

        mobileEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!mobileEditText.hasFocus()) {
                    if (((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.input() != null && ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.input().hasFocus()) {
                        ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.clear(getActivity(), ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.input());
                    }

                    if (passwordImageView.getVisibility() == View.VISIBLE) {
                        passwordImageView.setVisibility(View.INVISIBLE);
                    }

                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.focus(mobileEditText);
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.select(mobileEditText);
                }
            }
            return false;
        });

        passwordEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!passwordEditText.hasFocus()) {
                    if (((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.input() != null && ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.input().hasFocus()) {
                        ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.clear(getActivity(), ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.input());
                    }

                    if (passwordEditText.length() != 0) {
                        passwordImageView.setVisibility(View.VISIBLE);
                    }

                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.focus(passwordEditText);
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.select(passwordEditText);
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

        genderTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (genderException) {
                    ((AuthActivity) Objects.requireNonNull(getActivity())).clearException(AuthRepository.theory, "gender");
                }

                if (((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.input() != null && ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.input().hasFocus()) {
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.clear(getActivity(), ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.input());
                }

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

                ImageViewCompat.setImageTintList(passwordImageView, AppCompatResources.getColorStateList(activity, R.color.Risloo500));
                passwordEditText.setTransformationMethod(null);
            } else {
                passwordVisibility = false;
                passwordImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_eye_slash_light));

                ImageViewCompat.setImageTintList(passwordImageView, AppCompatResources.getColorStateList(activity, R.color.Gray300));
                passwordEditText.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        registerButton.setOnClickListener(v -> {
            if (((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.input() != null && ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.input().hasFocus()) {
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.clear(getActivity(), ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.input());
            }

            if (nameEditText.length() == 0) {
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.error(getActivity(), nameEditText);
            }
            if (mobileEditText.length() == 0) {
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.error(getActivity(), mobileEditText);
            }
            if (passwordEditText.length() == 0) {
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.error(getActivity(), passwordEditText);
            }

            if (nameEditText.length() != 0 && mobileEditText.length() != 0 && passwordEditText.length() != 0) {
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.clear(getActivity(), nameEditText);
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.clear(getActivity(), mobileEditText);
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.clear(getActivity(), passwordEditText);

                doWork();
            }
        });
    }

    private void doWork() {
        name = nameEditText.getText().toString().trim();
        mobile = mobileEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();

        try {
            ((AuthActivity) Objects.requireNonNull(getActivity())).progressDialog.show();

            ((AuthActivity) Objects.requireNonNull(getActivity())).authViewModel.register(name, mobile, gender, password);
            ((AuthActivity) Objects.requireNonNull(getActivity())).observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}