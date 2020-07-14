package com.majazeh.risloo.Views.Ui.Fragments;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.ViewModels.Sample.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.PFTAdapter;

public class PFTFragment extends Fragment {

    // ViewModels
    private SampleViewModel viewModel;

    // Adapters
    private PFTAdapter adapter;

    // Objects
    private Activity activity;

    // Widgets
    private TextView questionTextView;
    private ImageView questionImageView;
    private RecyclerView answerRecyclerView;

    public PFTFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_pft, viewGroup, false);

        initializer(view);

        return view;
    }

    private void initializer(View view) {
        adapter = new PFTAdapter(activity);
//        try {
//            adapter.setAnswer(viewModel.getItem(viewModel.getCurrentIndex()).get("answers"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        questionTextView = view.findViewById(R.id.fragment_pft_question_textView);

        questionImageView = view.findViewById(R.id.fragment_pft_question_imageView);

        answerRecyclerView = view.findViewById(R.id.fragment_pft_answer_recyclerView);
        answerRecyclerView.addItemDecoration(new ItemDecorator("verticalLinearLayout",(int) getResources().getDimension(R.dimen._16sdp)));
        answerRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        answerRecyclerView.setHasFixedSize(true);
//        answerRecyclerView.setAdapter(adapter);
    }

}