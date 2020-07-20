package com.majazeh.risloo.Views.Ui.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.majazeh.risloo.Models.Controller.AuthController;
import com.majazeh.risloo.R;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Ui.Activities.AuthActivity;

import org.json.JSONException;

import java.util.Objects;

public class MobileFragment extends Fragment {

    // ViewModels
    private AuthViewModel viewModel;

    // Vars
    private String mobile = "";
    private boolean mobileTouch, mobileError;

    // Objects
    private Activity activity;

    // Widgets
    private EditText mobileEditText;
    private Button mobileButton;

    public MobileFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_mobile, viewGroup, false);

        initializer(view);

        detector();

        listener();

        return view;
    }

    private void initializer(View view) {
        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        mobileEditText = view.findViewById(R.id.fragment_mobile_editText);
        mobileEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        mobileButton = view.findViewById(R.id.fragment_mobile_button);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mobileButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        mobileEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                mobileEditText.setCursorVisible(true);

                mobileTouch = true;
                mobileError = false;
            }
            return false;
        });

        mobileEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mobileEditText.length() == 11) {
                    mobile = mobileEditText.getText().toString().trim();

                    clearData();
                    doWork();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobileButton.setOnClickListener(v -> {
            mobile = mobileEditText.getText().toString().trim();

            if (mobileEditText.length() == 0) {
                checkInput();
            } else {
                clearData();
                doWork();
            }
        });
    }

    private void checkInput() {
        mobileEditText.setCursorVisible(false);

        mobileTouch = false;

        if (mobileEditText.length() == 0) {
            mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            mobileError = true;
        }
    }

    private void clearData() {
        mobileEditText.setCursorVisible(false);

        mobileEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);

        mobileTouch = false;
        mobileError = false;
    }

    private void doWork() {
        try {
            ((AuthActivity) Objects.requireNonNull(getActivity())).progressDialog.show();
            if (AuthController.theory.equals("mobile"))
                viewModel.auth(mobile);
            else
                viewModel.recovery(mobile);
            ((AuthActivity) Objects.requireNonNull(getActivity())).observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}