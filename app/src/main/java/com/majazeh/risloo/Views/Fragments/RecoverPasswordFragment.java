package com.majazeh.risloo.Views.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Views.Activities.AuthActivity;

import org.json.JSONException;

import java.util.Objects;

public class RecoverPasswordFragment extends Fragment {

    // Vars
    private String mobile = "";

    // Objects
    private Activity activity;

    // Widgets
    private TextView recoverPasswordDescriptionTextView;
    public EditText recoverPasswordEditText;
    private Button recoverPasswordButton;
    private TextView recoverPasswordLinkTextView;

    public RecoverPasswordFragment(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_recover_password, viewGroup, false);

        initializer(view);

        detector();

        listener();

        return view;
    }

    private void initializer(View view) {
        recoverPasswordDescriptionTextView = view.findViewById(R.id.fragment_recover_password_description_textView);

        recoverPasswordEditText = view.findViewById(R.id.fragment_recover_password_editText);

        recoverPasswordButton = view.findViewById(R.id.fragment_recover_password_button);

        recoverPasswordLinkTextView = view.findViewById(R.id.fragment_recover_password_link_textView);
        recoverPasswordLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            recoverPasswordButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        recoverPasswordEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!recoverPasswordEditText.hasFocus()) {
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.focus(recoverPasswordEditText);
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.select(recoverPasswordEditText);
                }
            }
            return false;
        });

        recoverPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (recoverPasswordEditText.length() == 11) {
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.clear(getActivity(), recoverPasswordEditText);
                    doWork();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        recoverPasswordButton.setOnClickListener(v -> {
            if (recoverPasswordEditText.length() == 0) {
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.error(getActivity(), recoverPasswordEditText);
            } else {
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.clear(getActivity(), recoverPasswordEditText);
                doWork();
            }
        });
    }

    private void doWork() {
        mobile = recoverPasswordEditText.getText().toString().trim();

        try {
            ((AuthActivity) Objects.requireNonNull(getActivity())).progressDialog.show();

            ((AuthActivity) Objects.requireNonNull(getActivity())).authViewModel.recovery(mobile);
            ((AuthActivity) Objects.requireNonNull(getActivity())).observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}