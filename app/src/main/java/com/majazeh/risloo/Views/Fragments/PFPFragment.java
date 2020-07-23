package com.majazeh.risloo.Views.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.PFPAdapter;

public class PFPFragment extends Fragment {

    // ViewModels
    private SampleViewModel viewModel;

    // Adapters
    private PFPAdapter adapter;

    // Objects
    private Activity activity;

    // Widgets
    private TextView questionTextView;
    private ImageView questionImageView;
    private RecyclerView answerRecyclerView;

    public PFPFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_pfp, viewGroup, false);

        initializer(view);

        return view;
    }

    private void initializer(View view) {
        adapter = new PFPAdapter(activity);
//        try {
//            adapter.setAnswer(viewModel.getItem(viewModel.getCurrentIndex()).get("answers"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        questionTextView = view.findViewById(R.id.fragment_pfp_question_textView);

        questionImageView = view.findViewById(R.id.fragment_pfp_question_imageView);

        answerRecyclerView = view.findViewById(R.id.fragment_pfp_answer_recyclerView);
        answerRecyclerView.addItemDecoration(new ItemDecorator("gridLayout",(int) getResources().getDimension(R.dimen._16sdp)));
        answerRecyclerView.setLayoutManager(new GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false));
        answerRecyclerView.setHasFixedSize(true);
//        answerRecyclerView.setAdapter(adapter);
    }

}