package com.majazeh.risloo.Views.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
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

    // Objects
    private Activity activity;
    private LinearLayoutManager layoutManager;

    // Widgets
    private RecyclerView recyclerView;
    public ProgressBar pagingProgressBar;
    private LinearLayout infoLayout;
    private ImageView infoImageView;
    private TextView infoTextView;

    public AllCenterFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_all_center, viewGroup, false);

        initializer(view);

        listener();

        setData();

        return view;
    }

    private void initializer(View view) {
        expands = new HashMap<>();

        adapter = new CenterAdapter(activity);

        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);

        recyclerView = view.findViewById(R.id.fragment_all_center_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._16sdp)));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        pagingProgressBar = view.findViewById(R.id.fragment_all_center_progressBar);

        infoLayout = view.findViewById(R.id.layout_info);

        infoImageView = view.findViewById(R.id.layout_info_imageView);
        infoTextView = view.findViewById(R.id.layout_info_textView);
    }

    private void listener() {
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
                            if (!((CenterActivity) Objects.requireNonNull(getActivity())).loadingAll) {
                                pagingProgressBar.setVisibility(View.VISIBLE);
                                ((CenterActivity) Objects.requireNonNull(getActivity())).centerViewModel.centers(((CenterActivity) Objects.requireNonNull(getActivity())).search);
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

            infoLayout.setVisibility(View.GONE);
        } else {
            infoLayout.setVisibility(View.VISIBLE);

            if (((CenterActivity) Objects.requireNonNull(getActivity())).search.equals("")) {
                setInfoLayout("empty");
            } else {
                setInfoLayout("search");
            }
        }
    }

    private void setInfoLayout(String type) {
        switch (type) {
            case "empty":
                infoImageView.setImageResource(R.drawable.illu_empty);
                infoTextView.setText(getResources().getString(R.string.AppEmpty));
                break;
            case "search":
                infoImageView.setImageResource(R.drawable.illu_empty);
                infoTextView.setText(getResources().getString(R.string.AppSearchEmpty));
                break;
        }
    }

    public void notifyRecycler() {
        for (int i = (CenterRepository.allPage-1)*15; i <((CenterRepository.allPage-1)*15)+15; i++) {
            expands.put(i, false);
        }

        adapter.setCenter(((CenterActivity) Objects.requireNonNull(getActivity())).centerViewModel.getAll(), expands, "all", ((CenterActivity) Objects.requireNonNull(getActivity())).centerViewModel);

        ((CenterActivity) Objects.requireNonNull(getActivity())).loadingAll = false;
        CenterRepository.allPage++;

        pagingProgressBar.setVisibility(View.GONE);
    }

}