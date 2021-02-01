package com.majazeh.risloo.Views.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Activities.ArchivesActivity;
import com.majazeh.risloo.Views.Activities.AuthActivity;

import org.json.JSONException;

import java.util.Objects;

public class SerialFragment extends Fragment {

    // ViewModels
    private SampleViewModel sampleViewModel;

    // Vars
    private String serial = "";

    // Objects
    private Activity activity;
    private Animation animSlideIn;

    // Widgets
    private TextView serialDescriptionTextView;
    public EditText serialEditText;
    private Button serialButton;
    private LinearLayout serialLinkLinearLayout, serialArchiveLinearLayout;
    private TextView serialRegisterTextView, serialRecoverPasswordTextView, serialIncompleteTextView, serialArchiveTextView;

    public SerialFragment(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_serial, viewGroup, false);

        initializer(view);

        detector();

        listener();

        setText();

        return view;
    }

    private void initializer(View view) {
        sampleViewModel = new ViewModelProvider(this).get(SampleViewModel.class);

        animSlideIn = AnimationUtils.loadAnimation(activity, R.anim.slide_in_bottom);

        serialDescriptionTextView = view.findViewById(R.id.fragment_serial_description_textView);

        serialEditText = view.findViewById(R.id.fragment_serial_editText);

        serialButton = view.findViewById(R.id.fragment_serial_button);

        serialLinkLinearLayout = view.findViewById(R.id.fragment_serial_link_linearLayout);
        serialArchiveLinearLayout = view.findViewById(R.id.fragment_serial_archive_linearLayout);

        serialRegisterTextView = view.findViewById(R.id.fragment_serial_register_textView);
        serialRecoverPasswordTextView = view.findViewById(R.id.fragment_serial_recover_password_textView);
        serialIncompleteTextView = view.findViewById(R.id.fragment_serial_incomplete_textView);
        serialArchiveTextView = view.findViewById(R.id.fragment_serial_archive_textView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            serialButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        serialEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!serialEditText.hasFocus()) {
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.focus(serialEditText);
                    ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.select(serialEditText);
                }
            }
            return false;
        });

        serialButton.setOnClickListener(v -> {
            if (serialEditText.length() == 0) {
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.error(getActivity(), serialEditText);
            } else {
                ((AuthActivity) Objects.requireNonNull(getActivity())).controlEditText.clear(getActivity(), serialEditText);
                doWork();
            }
        });

        serialRegisterTextView.setOnClickListener(v -> {
            AuthRepository.theory = "register";
            ((AuthActivity) Objects.requireNonNull(getActivity())).showFragment();
        });

        serialRecoverPasswordTextView.setOnClickListener(v -> {
            AuthRepository.theory = "recoverPassword";
            ((AuthActivity) Objects.requireNonNull(getActivity())).showFragment();
        });

        serialArchiveTextView.setOnClickListener(v -> {
            startActivity(new Intent(activity, ArchivesActivity.class));
            activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });
    }

    public void setText() {
        if (!((AuthActivity) Objects.requireNonNull(getActivity())).authViewModel.getToken().equals("")) {
            serialDescriptionTextView.setText(activity.getResources().getString(R.string.SerialDescriptionToken));
            serialEditText.setHint(activity.getResources().getString(R.string.SerialHintToken));
            serialButton.setText(activity.getResources().getString(R.string.SerialButtonToken));

            serialLinkLinearLayout.setVisibility(View.INVISIBLE);
        } else {
            serialDescriptionTextView.setText(activity.getResources().getString(R.string.SerialDescription));
            serialEditText.setHint(activity.getResources().getString(R.string.SerialHint));
            serialButton.setText(activity.getResources().getString(R.string.SerialButton));

            serialLinkLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void doWork() {
        serial = serialEditText.getText().toString().trim();

        try {
            ((AuthActivity) Objects.requireNonNull(getActivity())).progressDialog.show();

            ((AuthActivity) Objects.requireNonNull(getActivity())).authViewModel.auth(serial);
            ((AuthActivity) Objects.requireNonNull(getActivity())).observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (sampleViewModel.getArchive() != null) {
            serialIncompleteTextView.setText(StringManager.foregroundSize("شما" + " " + sampleViewModel.getArchive().size() + " " + "آزمون ناتمام دارید!", 4, 6, getResources().getColor(R.color.OrangePeel), (int) getResources().getDimension(R.dimen._14ssp)));

            serialArchiveLinearLayout.setVisibility(View.VISIBLE);
            serialArchiveLinearLayout.setAnimation(animSlideIn);
        } else {
            serialArchiveLinearLayout.setVisibility(View.GONE);
        }
    }

}