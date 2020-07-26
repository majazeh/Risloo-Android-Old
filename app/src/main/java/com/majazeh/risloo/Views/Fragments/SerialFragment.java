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

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private AuthViewModel authViewModel;
    private SampleViewModel sampleViewModel;

    // Vars
    private String serial = "";

    // Objects
    private Activity activity;
    private Animation animSlideIn;

    // Widgets
    private TextView serialDescriptionTextView;
    private EditText serialEditText;
    private Button serialButton;
    private LinearLayout serialLinkLinearLayout, serialArchiveLinearLayout;
    private TextView serialRegisterTextView, serialRecoveryTextView, serialIncompleteTextView, serialArchiveTextView;

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
        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);
        sampleViewModel = ViewModelProviders.of(this).get(SampleViewModel.class);

        animSlideIn = AnimationUtils.loadAnimation(activity, R.anim.slide_in_bottom);

        serialDescriptionTextView = view.findViewById(R.id.fragment_serial_description_textView);

        serialEditText = view.findViewById(R.id.fragment_serial_editText);

        serialButton = view.findViewById(R.id.fragment_serial_button);

        serialLinkLinearLayout = view.findViewById(R.id.fragment_serial_link_linearLayout);
        serialArchiveLinearLayout = view.findViewById(R.id.fragment_serial_archive_linearLayout);

        serialRegisterTextView = view.findViewById(R.id.fragment_serial_register_textView);
        serialRecoveryTextView = view.findViewById(R.id.fragment_serial_recovery_textView);
        serialIncompleteTextView = view.findViewById(R.id.fragment_serial_incomplete_textView);
        serialArchiveTextView = view.findViewById(R.id.fragment_serial_archive_textView);
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

        serialRegisterTextView.setOnClickListener(v -> {
            AuthController.theory = "register";
            ((AuthActivity) Objects.requireNonNull(getActivity())).showFragment();
        });

        serialRecoveryTextView.setOnClickListener(v -> {
            AuthController.theory = "recover";
            ((AuthActivity) Objects.requireNonNull(getActivity())).showFragment();
        });

        serialArchiveTextView.setOnClickListener(v -> {
            startActivity(new Intent(activity, ArchiveActivity.class));
            activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });
    }

    private void setText() {
        if (((AuthActivity) Objects.requireNonNull(getActivity())).token()) {
            serialLinkLinearLayout.setVisibility(View.VISIBLE);
        } else {
            serialDescriptionTextView.setText(activity.getResources().getString(R.string.SerialDescriptionToken));
            serialEditText.setHint(activity.getResources().getString(R.string.SerialHintToken));
            serialButton.setText(activity.getResources().getString(R.string.SerialButtonToken));

            serialLinkLinearLayout.setVisibility(View.INVISIBLE);
        }

        if (sampleViewModel.getStorageFiles() != null) {
            serialIncompleteTextView.setText(StringCustomizer.foregroundSize("شما" + " " + sampleViewModel.getStorageFiles().size() + " " + "نمونه ناقص دارید!", 4, 6, getResources().getColor(R.color.MoonYellow), (int) getResources().getDimension(R.dimen._15ssp)));
            serialArchiveLinearLayout.setVisibility(View.VISIBLE);
            serialArchiveLinearLayout.setAnimation(animSlideIn);
        } else {
            serialArchiveLinearLayout.setVisibility(View.GONE);
        }
    }

    private void checkInput() {
        serialEditText.setCursorVisible(false);

        if (serialEditText.length() == 0) {
            serialEditText.setBackgroundResource(R.drawable.draw_18sdp_violetred_border);
        }
    }

    private void clearData() {
        serialEditText.setCursorVisible(false);

        serialEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
    }

    private void doWork() {
        try {
            ((AuthActivity) Objects.requireNonNull(getActivity())).progressDialog.show();
            authViewModel.auth(serial);
            ((AuthActivity) Objects.requireNonNull(getActivity())).observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}