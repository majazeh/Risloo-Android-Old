package com.majazeh.risloo.Views.Fragments;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Views.Activities.AuthActivity;

import org.json.JSONException;

import java.util.Locale;
import java.util.Objects;

public class PinFragment extends Fragment {

    // Vars
    private String pin = "";

    // Objects
    private Activity activity;
    private ClickableSpan pinLinkSpan;
    private CountDownTimer pinCountDownTimer;

    // Widgets
    private TextView pinDescriptionTextView;
    public EditText pinEditText;
    private Button pinButton;
    private ViewFlipper pinViewFlipper;
    private TextView pinTimerTextView, pinLinkTextView;

    public PinFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_pin, viewGroup, false);

        initializer(view);

        detector();

        listener();

        setText();

        observeTimer();

        return view;
    }

    private void initializer(View view) {
        pinDescriptionTextView = view.findViewById(R.id.fragment_pin_description_textView);

        pinEditText = view.findViewById(R.id.fragment_pin_editText);

        pinButton = view.findViewById(R.id.fragment_pin_button);

        pinViewFlipper = view.findViewById(R.id.fragment_pin_viewFlipper);

        pinTimerTextView = view.findViewById(R.id.fragment_pin_timer_textView);
        pinLinkTextView = view.findViewById(R.id.fragment_pin_link_textView);
        pinLinkTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            pinButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        pinEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!pinEditText.hasFocus()) {
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.focus(pinEditText);
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.select(pinEditText);
                }
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
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.clear(getActivity(), pinEditText);
                    doWork("pin");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pinButton.setOnClickListener(v -> {
            if (pinEditText.length() == 0) {
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.error(getActivity(), pinEditText);
            } else {
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.clear(getActivity(), pinEditText);
                doWork("pin");
            }
        });

        pinLinkSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                ((AuthActivity) Objects.requireNonNull(getActivity())).callTimer.setValue(-1);
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
            }
        };
    }

    private void setText() {
        pinLinkTextView.setText(StringManager.clickable(activity.getResources().getString(R.string.PinLink), 24, 34, pinLinkSpan));
        pinCountDownTimer.start();
    }

    private void observeTimer() {
        ((AuthActivity) Objects.requireNonNull(getActivity())).callTimer.observe((LifecycleOwner) activity, integer -> {
            if (integer == 1) {
                showTimer(true);
                ((AuthActivity) Objects.requireNonNull(getActivity())).callTimer.removeObservers((LifecycleOwner) activity);
            } else if (integer == 0) {
                ((AuthActivity) Objects.requireNonNull(getActivity())).callTimer.removeObservers((LifecycleOwner) activity);
            }
        });
    }

    private void showTimer(boolean value) {
        if (value) {
            pinCountDownTimer.start();

            pinViewFlipper.setInAnimation(activity, R.anim.slide_in_right_with_fade);
            pinViewFlipper.setOutAnimation(activity, R.anim.slide_out_left_with_fade);
            pinViewFlipper.showPrevious();
        } else {
            pinCountDownTimer.cancel();

            pinViewFlipper.setInAnimation(activity, R.anim.slide_in_left_with_fade);
            pinViewFlipper.setOutAnimation(activity, R.anim.slide_out_right_with_fade);
            pinViewFlipper.showNext();
        }
    }

    private void doWork(String value) {
        pin = pinEditText.getText().toString().trim();

        try {
            ((AuthActivity) Objects.requireNonNull(getActivity())).progressDialog.show();
            if (value.equals("pin")) {
                ((AuthActivity) Objects.requireNonNull(getActivity())).viewModel.authTheory("", pin);
            } else if (value.equals("verification")) {
                ((AuthActivity) Objects.requireNonNull(getActivity())).viewModel.verification();
                observeTimer();
            }
            ((AuthActivity) Objects.requireNonNull(getActivity())).observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}