package com.majazeh.risloo.Views.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.ViewModels.RelationshipViewModel;
import com.majazeh.risloo.Views.Adapters.RelationshipAdapter;

public class MyRelationshipFragment extends Fragment {

    // ViewModels
    private RelationshipViewModel viewModel;

    // Adapters
    private RelationshipAdapter adapter;

    // Objects
    private Activity activity;

    // Widgets
    private RecyclerView recyclerView;

    public MyRelationshipFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_my_relationship, viewGroup, false);

        initializer(view);

        return view;
    }

    private void initializer(View view) {
        viewModel = ViewModelProviders.of(this).get(RelationshipViewModel.class);

        adapter = new RelationshipAdapter(activity);
//        adapter.setRelationship(viewModel.getMy());

        recyclerView = view.findViewById(R.id.fragment_my_relationship_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorator("verticalLinearLayout",(int) getResources().getDimension(R.dimen._18sdp)));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(adapter);
    }

}