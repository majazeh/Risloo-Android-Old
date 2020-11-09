//package com.majazeh.risloo.Views.Fragments;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.majazeh.risloo.R;
//import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
//import com.majazeh.risloo.Views.Activities.RoomActivity;
//import com.majazeh.risloo.Views.Adapters.RoomAdapter;
//
//import org.json.JSONException;
//
//import java.util.Objects;
//
//public class AllRoomFragment extends Fragment {
//
//    // Adapters
//    private RoomAdapter adapter;
//
//    // Objects
//    private Activity activity;
//    private LinearLayoutManager layoutManager;
//
//    // Widgets
//    private RecyclerView recyclerView;
//    public ProgressBar pagingProgressBar;
//    private LinearLayout infoLayout;
//    private ImageView infoImageView;
//    private TextView infoTextView;
//
//    public AllRoomFragment(Activity activity) {
//        this.activity = activity;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
//        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_all_room, viewGroup, false);
//
//        initializer(view);
//
//        listener();
//
//        setData();
//
//        return view;
//    }
//
//    private void initializer(View view) {
//        adapter = new RoomAdapter(activity);
//
//        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
//
//        recyclerView = view.findViewById(R.id.fragment_all_room_recyclerView);
//        recyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._16sdp)));
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setHasFixedSize(true);
//
//        pagingProgressBar = view.findViewById(R.id.fragment_all_room_progressBar);
//
//        infoLayout = view.findViewById(R.id.layout_info_linearLayout);
//
//        infoImageView = view.findViewById(R.id.layout_info_imageView);
//        infoTextView = view.findViewById(R.id.layout_info_textView);
//    }
//
//    private void listener() {
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) {
//
//                    int visibleItemCount = layoutManager.getChildCount();
//                    int totalItemCount = layoutManager.getItemCount();
//                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
//
//                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
//                        try {
//                            if (!((RoomActivity) Objects.requireNonNull(getActivity())).loadingAll) {
//                                pagingProgressBar.setVisibility(View.VISIBLE);
//                                ((RoomActivity) Objects.requireNonNull(getActivity())).roomViewModel.rooms(((RoomActivity) Objects.requireNonNull(getActivity())).search);
//                                ((RoomActivity) Objects.requireNonNull(getActivity())).observeWork();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        });
//    }
//
//    private void setData() {
//        if (((RoomActivity) Objects.requireNonNull(getActivity())).roomViewModel.getAll() != null) {
//            adapter.setRoom(((RoomActivity) Objects.requireNonNull(getActivity())).roomViewModel.getAll());
//            recyclerView.setAdapter(adapter);
//
//            infoLayout.setVisibility(View.GONE);
//        } else {
//            infoLayout.setVisibility(View.VISIBLE);
//
//            if (((RoomActivity) Objects.requireNonNull(getActivity())).search.equals("")) {
//                setInfoLayout("empty");
//            } else {
//                setInfoLayout("search");
//            }
//        }
//    }
//
//    private void setInfoLayout(String type) {
//        switch (type) {
//            case "empty":
//                infoImageView.setImageResource(R.drawable.illu_empty);
//                infoTextView.setText(getResources().getString(R.string.AppEmpty));
//                break;
//            case "search":
//                infoImageView.setImageResource(R.drawable.illu_empty);
//                infoTextView.setText(getResources().getString(R.string.AppSearchEmpty));
//                break;
//        }
//    }
//
//    public void notifyRecycler() {
//        adapter.setRoom(((RoomActivity) Objects.requireNonNull(getActivity())).roomViewModel.getAll());
//
//        ((RoomActivity) Objects.requireNonNull(getActivity())).loadingAll = false;
//        RoomRepository.allPage++;
//
//        pagingProgressBar.setVisibility(View.GONE);
//    }
//
//}