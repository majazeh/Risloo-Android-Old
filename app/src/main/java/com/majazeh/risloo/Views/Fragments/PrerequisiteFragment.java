package com.majazeh.risloo.Views.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Activities.SampleActivity;
import com.majazeh.risloo.Views.Adapters.PrerequisiteAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Objects;

public class PrerequisiteFragment extends Fragment {

    // ViewModels
    private SampleViewModel viewModel;

    // Adapters
    private PrerequisiteAdapter adapter;

    // Objects
    private Activity activity;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    private TextView descriptionTextView;
    private RecyclerView recyclerView;
    private Button prerequisiteButton;

    public PrerequisiteFragment(Activity activity, SampleViewModel viewModel) {
        this.activity = activity;
        this.viewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_prerequisite, viewGroup, false);

        initializer(view);

        detector();

        listener();

        return view;
    }

    private void initializer(View view) {
        adapter = new PrerequisiteAdapter(activity);
        adapter.setPrerequisite(viewModel.getPrerequisite());

        sharedPreferences = activity.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        descriptionTextView = view.findViewById(R.id.fragment_prerequisite_description_textView);

        recyclerView = view.findViewById(R.id.fragment_prerequisite_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorator("verticalLinearLayout", (int) getResources().getDimension(R.dimen._24sdp)));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        prerequisiteButton = view.findViewById(R.id.fragment_prerequisite_button);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            prerequisiteButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
        }
    }

    private void listener() {
        prerequisiteButton.setOnClickListener(v -> doWork());
    }

    private void doWork() {
        try {
            viewModel.savePrerequisiteToCache( new JSONArray(), sharedPreferences.getString("sampleId", ""));
            viewModel.sendPre(adapter.answers());
            ((SampleActivity) Objects.requireNonNull(getActivity())).observeWorkAnswer();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}