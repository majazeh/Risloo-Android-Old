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

import com.majazeh.risloo.Models.Repositories.RoomRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Views.Activities.RoomsActivity;
import com.majazeh.risloo.Views.Adapters.RoomsAdapter;

import org.json.JSONException;

import java.util.Objects;

public class MyRoomsFragment extends Fragment {

    // Adapters
    private RoomsAdapter adapter;

    // Objects
    private Activity activity;
    private LinearLayoutManager layoutManager;

    // Widgets
    private RecyclerView recyclerView;
    public ProgressBar pagingProgressBar;
    private LinearLayout infoLayout;
    private ImageView infoImageView;
    private TextView infoTextView;

    public MyRoomsFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_my_rooms, viewGroup, false);

        initializer(view);

        listener();

        setData();

        return view;
    }

    private void initializer(View view) {
        adapter = new RoomsAdapter(activity);

        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);

        recyclerView = view.findViewById(R.id.fragment_my_room_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._16sdp)));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        pagingProgressBar = view.findViewById(R.id.fragment_my_room_progressBar);

        infoLayout = view.findViewById(R.id.layout_info_linearLayout);

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
                            if (!((RoomsActivity) Objects.requireNonNull(getActivity())).loadingMy) {
                                pagingProgressBar.setVisibility(View.VISIBLE);
                                ((RoomsActivity) Objects.requireNonNull(getActivity())).roomViewModel.myRooms(((RoomsActivity) Objects.requireNonNull(getActivity())).search);
                                ((RoomsActivity) Objects.requireNonNull(getActivity())).observeWork();
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
        if (((RoomsActivity) Objects.requireNonNull(getActivity())).roomViewModel.getMy() != null) {
            adapter.setRoom(((RoomsActivity) Objects.requireNonNull(getActivity())).roomViewModel.getMy());
            recyclerView.setAdapter(adapter);

            infoLayout.setVisibility(View.GONE);
        } else {
            infoLayout.setVisibility(View.VISIBLE);

            if (((RoomsActivity) Objects.requireNonNull(getActivity())).search.equals("")) {
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
        adapter.setRoom(((RoomsActivity) Objects.requireNonNull(getActivity())).roomViewModel.getMy());

        ((RoomsActivity) Objects.requireNonNull(getActivity())).loadingMy = false;
        RoomRepository.myPage++;

        pagingProgressBar.setVisibility(View.GONE);
    }

}