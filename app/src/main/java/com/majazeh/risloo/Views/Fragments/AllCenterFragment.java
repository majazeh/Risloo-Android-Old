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
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.Views.Adapters.CenterAdapter;

public class AllCenterFragment extends Fragment {

    // ViewModels
    private CenterViewModel viewModel;

    // Adapters
    private CenterAdapter adapter;

    // Objects
    private Activity activity;

    // Widgets
    private RecyclerView recyclerView;

    public AllCenterFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_all_center, viewGroup, false);

        initializer(view);

        return view;
    }

    private void initializer(View view) {
        viewModel = ViewModelProviders.of(this).get(CenterViewModel.class);

        adapter = new CenterAdapter(activity);
        adapter.setCenter(viewModel.getAll());

        recyclerView = view.findViewById(R.id.fragment_all_center_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorator("verticalLinearLayout",(int) getResources().getDimension(R.dimen._18sdp)));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

}