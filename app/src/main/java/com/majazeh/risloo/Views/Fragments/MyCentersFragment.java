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
import com.majazeh.risloo.Views.Activities.CentersActivity;
import com.majazeh.risloo.Views.Adapters.CentersAdapter;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Objects;

public class MyCentersFragment extends Fragment {

    // Adapters
    private CentersAdapter centersAdapter;

    // Vars
    private HashMap<Integer, Boolean> expands;

    // Objects
    private Activity activity;
    private LinearLayoutManager layoutManager;

    // Widgets
    private RecyclerView centersRecyclerView;
    public ProgressBar pagingProgressBar;
    private LinearLayout infoLayout;
    private ImageView infoImageView;
    private TextView infoTextView;

    public MyCentersFragment(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_my_centers, viewGroup, false);

        initializer(view);

        listener();

        setData();

        return view;
    }

    private void initializer(View view) {
        expands = new HashMap<>();

        centersAdapter = new CentersAdapter(activity);

        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);

        centersRecyclerView = view.findViewById(R.id.fragment_my_centers_recyclerView);
        centersRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._16sdp)));
        centersRecyclerView.setLayoutManager(layoutManager);
        centersRecyclerView.setHasFixedSize(true);

        pagingProgressBar = view.findViewById(R.id.fragment_my_centers_progressBar);

        infoLayout = view.findViewById(R.id.layout_info_linearLayout);

        infoImageView = view.findViewById(R.id.layout_info_imageView);
        infoTextView = view.findViewById(R.id.layout_info_textView);
    }

    private void listener() {
        centersRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {

                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        try {
                            if (!((CentersActivity) Objects.requireNonNull(getActivity())).loadingMy) {
                                pagingProgressBar.setVisibility(View.VISIBLE);
                                ((CentersActivity) Objects.requireNonNull(getActivity())).centerViewModel.myCenters(((CentersActivity) Objects.requireNonNull(getActivity())).search);
                                ((CentersActivity) Objects.requireNonNull(getActivity())).observeWork();
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
        if (((CentersActivity) Objects.requireNonNull(getActivity())).centerViewModel.getMy() != null) {
            for (int i = 0; i < ((CentersActivity) Objects.requireNonNull(getActivity())).centerViewModel.getMy().size(); i++) {
                expands.put(i, false);
            }

            centersAdapter.setCenter(((CentersActivity) Objects.requireNonNull(getActivity())).centerViewModel.getMy(), expands, "my");
            centersRecyclerView.setAdapter(centersAdapter);

            infoLayout.setVisibility(View.GONE);
        } else {
            infoLayout.setVisibility(View.VISIBLE);

            if (((CentersActivity) Objects.requireNonNull(getActivity())).search.equals("")) {
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
                infoTextView.setText(getResources().getString(R.string.AppInfoEmpty));
                break;
            case "search":
                infoImageView.setImageResource(R.drawable.illu_empty);
                infoTextView.setText(getResources().getString(R.string.AppSearchEmpty));
                break;
        }
    }

    public void notifyRecycler() {
        for (int i = (CenterRepository.myPage-1)*15; i <((CenterRepository.myPage-1)*15)+15; i++) {
            expands.put(i, false);
        }

        centersAdapter.setCenter(((CentersActivity) Objects.requireNonNull(getActivity())).centerViewModel.getMy(), expands, "my");

        ((CentersActivity) Objects.requireNonNull(getActivity())).loadingMy = false;
        CenterRepository.myPage++;

        pagingProgressBar.setVisibility(View.GONE);
    }

}