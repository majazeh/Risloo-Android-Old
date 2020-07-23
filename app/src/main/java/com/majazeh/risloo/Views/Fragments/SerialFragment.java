package com.majazeh.risloo.Views.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.majazeh.risloo.Models.Controller.AuthController;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.StringCustomizer;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Activities.ArchiveActivity;
import com.majazeh.risloo.Views.Activities.AuthActivity;

import org.json.JSONException;

import java.util.Objects;

public class SerialFragment extends Fragment {

    // ViewModels
    private AuthViewModel viewModel;
    private SampleViewModel viewModel2;

    // Vars
    private String serial = "";
    private boolean serialTouch, serialError;

    // Objects
    private Activity activity;
    private ClickableSpan serialLinkSpan, serialArchiveSpan;

    // Widgets
    private EditText serialEditText;
    private Button serialButton;
    private TextView serialLinkTextView, serialArchiveTextView;

    public SerialFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_serial, viewGroup, false);

        initializer(view);

        detector();

        listener();

        setText();

        return view;
    }

    private void initializer(View view) {
        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        viewModel2 = ViewModelProviders.of(this).get(SampleViewModel.class);

        serialEditText = view.findViewById(R.id.fragment_serial_editText);
        serialEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        serialButton = view.findViewById(R.id.fragment_serial_button);

        serialLinkTextView = view.findViewById(R.id.fragment_serial_link_textView);
        serialArchiveTextView = view.findViewById(R.id.fragment_serial_archive_textView);
        serialLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
        serialArchiveTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            serialButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        serialEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                serialEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                serialEditText.setCursorVisible(true);

                serialTouch = true;
                serialError = false;
            }
            return false;
        });

        serialButton.setOnClickListener(v -> {
            serial = serialEditText.getText().toString().trim();

            if (serialEditText.length() == 0) {
                checkInput();
            } else {
                clearData();
                doWork();
            }
        });

        serialLinkSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                AuthController.theory = "register";
                ((AuthActivity) Objects.requireNonNull(getActivity())).showFragment();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.PrimaryDark));
                textPaint.setUnderlineText(false);
            }
        };

        serialArchiveSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                startActivity(new Intent(activity, ArchiveActivity.class));
                activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_top);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.PrimaryDark));
                textPaint.setUnderlineText(false);
            }
        };
    }

    private void setText() {
        serialLinkTextView.setText(StringCustomizer.clickable(activity.getResources().getString(R.string.SerialLink), 18, 25, serialLinkSpan));
        serialArchiveTextView.setText(StringCustomizer.clickable(activity.getResources().getString(R.string.SerialArchive), 22, 36, serialArchiveSpan));
        if (viewModel2.files() != null) {
            serialArchiveTextView.setVisibility(View.VISIBLE);
        } else {
            serialArchiveTextView.setVisibility(View.GONE);
        }
    }

    private void checkInput() {
        serialEditText.setCursorVisible(false);

        serialTouch = false;

        if (serialEditText.length() == 0) {
            serialEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            serialError = true;
        }
    }

    private void clearData() {
        serialEditText.setCursorVisible(false);

        serialEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);

        serialTouch = false;
        serialError = false;
    }

    private void doWork() {
        try {
            ((AuthActivity) Objects.requireNonNull(getActivity())).progressDialog.show();
            viewModel.auth(serial);
            ((AuthActivity) Objects.requireNonNull(getActivity())).observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}