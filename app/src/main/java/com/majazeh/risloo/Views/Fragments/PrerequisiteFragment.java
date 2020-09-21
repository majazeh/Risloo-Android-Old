package com.majazeh.risloo.Views.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.PrerequisiteAdapter;

import java.util.ArrayList;

public class PrerequisiteFragment extends Fragment {

    // ViewModels
    private SampleViewModel viewModel;

    // Adapters
    public PrerequisiteAdapter adapter;

    // Objects
    private Activity activity;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    private RecyclerView prerequisiteRecyclerView;

    public PrerequisiteFragment(Activity activity, SampleViewModel viewModel) {
        this.activity = activity;
        this.viewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_prerequisite, viewGroup, false);

        initializer(view);

        return view;
    }

    private void initializer(View view) {
        sharedPreferences = activity.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        adapter = new PrerequisiteAdapter(activity);
        adapter.setPrerequisite(viewModel.getPrerequisite(), viewModel.readPrerequisiteAnswerFromCache(sharedPreferences.getString("sampleId", "")), viewModel);

        prerequisiteRecyclerView = view.findViewById(R.id.fragment_prerequisite_recyclerView);
        prerequisiteRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._24sdp)));
        prerequisiteRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        prerequisiteRecyclerView.setHasFixedSize(true);
        prerequisiteRecyclerView.setAdapter(adapter);
    }

    public ArrayList prerequisites() {
        ArrayList answers = new ArrayList();
        for (Object key: adapter.answer.keySet()) {
            ArrayList list = new ArrayList<String>();

            list.add(key);
            list.add(adapter.answer.get(key));

            answers.add(list);
        }
        return answers;
    }

}