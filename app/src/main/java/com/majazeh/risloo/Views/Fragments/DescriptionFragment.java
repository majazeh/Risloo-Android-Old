package com.majazeh.risloo.Views.Fragments;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.majazeh.risloo.R;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Activities.SampleActivity;
import com.mukesh.MarkdownView;

import org.json.JSONException;

import java.util.Objects;

public class DescriptionFragment extends Fragment {

    // ViewModels
    private SampleViewModel viewModel;

    // Objects
    private Activity activity;

    // Widgets
    private MarkdownView markdownView;
    private Button descriptionButton;

    public DescriptionFragment(Activity activity, SampleViewModel viewModel) {
        this.activity = activity;
        this.viewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_description, viewGroup, false);

        initializer(view);

        detector();

        listener();

        return view;
    }

    private void initializer(View view) {
        markdownView = view.findViewById(R.id.fragment_description_markDownView);
        markdownView.setMarkDownText(viewModel.getDescription());

        descriptionButton = view.findViewById(R.id.fragment_description_button);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            descriptionButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
        }
    }

    private void listener() {
        descriptionButton.setOnClickListener(v -> doWork());
    }

    private void doWork() {
        ((SampleActivity) Objects.requireNonNull(getActivity())).loadFragment(new PrerequisiteFragment(activity, viewModel), R.anim.fade_in, R.anim.fade_out);
    }

}