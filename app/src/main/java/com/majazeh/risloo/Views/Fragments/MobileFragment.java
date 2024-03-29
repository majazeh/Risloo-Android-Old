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

public class MobileFragment extends Fragment {

    // Vars
    private String mobile = "";

    // Objects
    private Activity activity;

    // Widgets
    private TextView mobileDescriptionTextView;
    public EditText mobileEditText;
    private Button mobileButton;
    private TextView mobileLinkTextView;

    public MobileFragment(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_mobile, viewGroup, false);

        initializer(view);

        detector();

        listener();

        return view;
    }

    private void initializer(View view) {
        mobileDescriptionTextView = view.findViewById(R.id.fragment_mobile_description_textView);

        mobileEditText = view.findViewById(R.id.fragment_mobile_editText);

        mobileButton = view.findViewById(R.id.fragment_mobile_button);

        mobileLinkTextView = view.findViewById(R.id.fragment_mobile_link_textView);
        mobileLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mobileButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        mobileEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!mobileEditText.hasFocus()) {
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.focus(mobileEditText);
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.select(mobileEditText);
                }
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
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.clear(getActivity(), mobileEditText);
                    doWork();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mobileButton.setOnClickListener(v -> {
            if (mobileEditText.length() == 0) {
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.error(getActivity(), mobileEditText);
            } else {
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.clear(getActivity(), mobileEditText);
                doWork();
            }
        });
    }

    private void doWork() {
        mobile = mobileEditText.getText().toString().trim();

        try {
            ((AuthActivity) Objects.requireNonNull(getActivity())).progressDialog.show();

            ((AuthActivity) Objects.requireNonNull(getActivity())).authViewModel.auth(mobile);
            ((AuthActivity) Objects.requireNonNull(getActivity())).observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}