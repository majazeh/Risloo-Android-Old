package com.majazeh.risloo.Views.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
    private SharedPreferences sharedPreferences;

    // Widgets
    private TextView questionTextView;
    private ImageView questionImageView;
    private RecyclerView answerRecyclerView;

    public PFPFragment(Activity activity, SampleViewModel viewModel) {
        this.activity = activity;
        this.viewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_pfp, viewGroup, false);

        initializer(view);

        return view;
    }

    private void initializer(View view) {
        sharedPreferences = activity.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        adapter = new PFPAdapter(activity);
//        adapter.setAnswer(viewModel.getOptions(viewModel.getIndex()), viewModel.answeredPosition(sharedPreferences.getString("sampleId",""), viewModel.getIndex()), viewModel);

        questionTextView = view.findViewById(R.id.fragment_pfp_question_textView);
//        try {
//            questionTextView.setText(viewModel.getItem(viewModel.getIndex()).get("text").toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        questionImageView = view.findViewById(R.id.fragment_pfp_question_imageView);
//        try {
//            questionImageView.setImageResource();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        answerRecyclerView = view.findViewById(R.id.fragment_pfp_answer_recyclerView);
        answerRecyclerView.addItemDecoration(new ItemDecorator("gridLayout",(int) getResources().getDimension(R.dimen._16sdp)));
        answerRecyclerView.setLayoutManager(new GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false));
        answerRecyclerView.setHasFixedSize(true);
//        answerRecyclerView.setAdapter(adapter);
    }

}