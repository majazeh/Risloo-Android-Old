package com.majazeh.risloo.Views.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Views.Activities.SampleActivity;

import java.util.Objects;

import io.noties.markwon.Markwon;

public class DescriptionFragment extends Fragment {

    // Objects
    private Activity activity;

    // Widgets
    private TextView markdownTextView;

    public DescriptionFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_description, viewGroup, false);

        initializer(view);

        setData();

        return view;
    }

    private void initializer(View view) {
        markdownTextView = view.findViewById(R.id.fragment_description_textView);
    }

    private void setData() {
        Markwon.create(activity).setMarkdown(markdownTextView,  ((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.getDescription());
    }

}