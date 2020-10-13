package com.majazeh.risloo.Views.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Views.Activities.CenterActivity;
import com.majazeh.risloo.Views.Adapters.CenterAdapter;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Objects;

public class AllCenterFragment extends Fragment {

    // Adapters
    private CenterAdapter adapter;

    // Vars
    private HashMap<Integer, Boolean> expands;

    private LinearLayoutManager layoutManager;

    // Objects
    private Activity activity;

    // Widgets
    private RecyclerView recyclerView;

    public AllCenterFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_all_center, viewGroup, false);

        initializer(view);

        setData();

        return view;
    }

    private void initializer(View view) {
        expands = new HashMap<>();

        adapter = new CenterAdapter(activity);

        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        recyclerView = view.findViewById(R.id.fragment_all_center_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorator("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._16sdp)));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {

                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        try {
                            Log.e("loading", String.valueOf(CenterActivity.loadingAll));
                            if (!CenterActivity.loadingAll) {
                                //pagingProgressBar.setVisibility(View.VISIBLE);
                                ((CenterActivity) Objects.requireNonNull(getActivity())).centerViewModel.centers();
                                ((CenterActivity) Objects.requireNonNull(getActivity())).observeWork();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void setData() {
        if (((CenterActivity) Objects.requireNonNull(getActivity())).centerViewModel.getAll() != null) {
            for (int i = 0; i < ((CenterActivity) Objects.requireNonNull(getActivity())).centerViewModel.getAll().size(); i++) {
                expands.put(i, false);
            }

            adapter.setCenter(((CenterActivity) Objects.requireNonNull(getActivity())).centerViewModel.getAll(), expands, "all", ((CenterActivity) Objects.requireNonNull(getActivity())).centerViewModel);
            recyclerView.setAdapter(adapter);
        }
    }
    public void notifyRecycler(){
        adapter.setCenter(((CenterActivity) Objects.requireNonNull(getActivity())).centerViewModel.getAll(), expands, "all", ((CenterActivity) Objects.requireNonNull(getActivity())).centerViewModel);
        CenterRepository.allPage++;
    }
}