package com.majazeh.risloo.Views.Ui.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
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
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.StringCustomizer;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.Views.Ui.Activities.AuthActivity;

import org.json.JSONException;

import java.util.Locale;
import java.util.Objects;

public class PinFragment extends Fragment {

    // ViewModels
    private AuthViewModel viewModel;

    // Vars
    private String pin = "";
    private boolean pinTouch, pinError;

    // Objects
    private Activity activity;
    private ClickableSpan pinLinkSpan;
    private CountDownTimer pinCountDownTimer;

    // Widgets
    private EditText pinEditText;
    private Button pinButton;
    private ViewFlipper pinViewFlipper;
    private TextView pinLinkTextView, pinTimerTextView;

    public PinFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_pin, viewGroup, false);

        initializer(view);

        detector();

        listener();

        setText();

        observeTimer();

        return view;
    }

    private void initializer(View view) {
        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        pinViewFlipper = view.findViewById(R.id.fragment_pin_viewFlipper);

        pinEditText = view.findViewById(R.id.fragment_pin_editText);
        pinEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        pinButton = view.findViewById(R.id.fragment_pin_button);

        pinLinkTextView = view.findViewById(R.id.fragment_pin_link_textView);
        pinLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());

        pinTimerTextView = view.findViewById(R.id.fragment_pin_timer_textView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            pinButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        pinEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                pinEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                pinEditText.setCursorVisible(true);

                pinTouch = true;
                pinError = false;
            }
            return false;
        });

        pinEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pinEditText.length() == 6) {
                    pin = pinEditText.getText().toString().trim();

                    clearData();
                    doWork("pin");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pinButton.setOnClickListener(v -> {
            pin = pinEditText.getText().toString().trim();

            if (pinEditText.length() == 0) {
                checkInput();
            } else {
                clearData();
                doWork("pin");
            }
        });

        pinLinkSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                doWork("verification");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.PrimaryDark));
                textPaint.setUnderlineText(false);
            }
        };

        pinCountDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                pinTimerTextView.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                showTimer(false);
                ((AuthActivity) Objects.requireNonNull(getActivity())).callTimer.removeObservers((LifecycleOwner) activity);
            }
        };
    }

    private void setText() {
        pinLinkTextView.setText(StringCustomizer.clickable(activity.getResources().getString(R.string.PinLink), 24, 34, pinLinkSpan));
    }

    private void observeTimer() {
        ((AuthActivity) Objects.requireNonNull(getActivity())).callTimer.observe((LifecycleOwner) activity, aBoolean -> {
            if (aBoolean) {
                showTimer(true);
            }
        });
    }

    public void showTimer(boolean value) {
        if (value) {
            pinCountDownTimer.start();

            pinViewFlipper.setInAnimation(activity, R.anim.slide_in_left_with_fade);
            pinViewFlipper.setOutAnimation(activity, R.anim.slide_out_right_with_fade);
            pinViewFlipper.showNext();
        } else {
            pinCountDownTimer.cancel();

            pinViewFlipper.setInAnimation(activity, R.anim.slide_in_right_with_fade);
            pinViewFlipper.setOutAnimation(activity, R.anim.slide_out_left_with_fade);
            pinViewFlipper.showPrevious();
        }
    }

    private void checkInput() {
        pinEditText.setCursorVisible(false);

        pinTouch = false;

        if (pinEditText.length() == 0) {
            pinEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
            pinError = true;
        }
    }

    private void clearData() {
        pinEditText.setCursorVisible(false);

        pinEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);

        pinTouch = false;
        pinError = false;
    }

    private void doWork(String value) {
        try {
            ((AuthActivity) Objects.requireNonNull(getActivity())).progressDialog.show();
            if (value == "pin") {
                viewModel.authTheory("", pin);
            } else if (value == "verification") {
                viewModel.verification();
            }
            ((AuthActivity) Objects.requireNonNull(getActivity())).observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}