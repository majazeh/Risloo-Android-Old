package com.majazeh.risloo.Views.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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

import java.util.HashMap;

public class MyCenterFragment extends Fragment {

    // ViewModels
    private CenterViewModel viewModel;

    // Adapters
    private CenterAdapter adapter;

    // Vars
    private HashMap<Integer, Boolean> expands;

    // Objects
    private Activity activity;

    // Widgets
    private RecyclerView recyclerView;
    private LinearLayout emptyLayout;

    public MyCenterFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_my_center, viewGroup, false);

        initializer(view);

        setRecyclerView();

        return view;
    }

    private void initializer(View view) {
        viewModel = ViewModelProviders.of(this).get(CenterViewModel.class);
        expands = new HashMap<>();
        if (viewModel.getMy() != null) {
            for (int i = 0; i < viewModel.getMy().size(); i++) {
                expands.put(i, false);
            }
        }
        adapter = new CenterAdapter(activity);
        adapter.setCenter(viewModel.getMy(), expands, "my");

        recyclerView = view.findViewById(R.id.fragment_my_center_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorator("verticalLinearLayout", (int) getResources().getDimension(R.dimen._18sdp)));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        emptyLayout = view.findViewById(R.id.fragment_my_center_emptyLayout);
    }

    private void setRecyclerView() {
        if (viewModel.getMy() != null) {
            recyclerView.setAdapter(adapter);

            recyclerView.setVisibility(View.VISIBLE);
            emptyLayout.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.VISIBLE);
        }
    }

}