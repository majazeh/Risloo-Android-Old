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
import com.majazeh.risloo.Views.Activities.SampleActivity;
import com.majazeh.risloo.Views.Adapters.PictoralAdapter;

import org.json.JSONException;

import java.util.Objects;

public class PicturePictoralFragment extends Fragment {

    // Adapters
    private PictoralAdapter adapter;

    // Objects
    private Activity activity;

    // Widgets
    private TextView questionTextView;
    private ImageView questionImageView;
    private RecyclerView answerRecyclerView;

    public PicturePictoralFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_picture_pictoral, viewGroup, false);

        initializer(view);

        setData();

        return view;
    }

    private void initializer(View view) {
        adapter = new PictoralAdapter(activity);

        questionTextView = view.findViewById(R.id.fragment_picture_pictoral_question_textView);

        questionImageView = view.findViewById(R.id.fragment_picture_pictoral_question_imageView);

        answerRecyclerView = view.findViewById(R.id.fragment_picture_pictoral_answer_recyclerView);
        answerRecyclerView.addItemDecoration(new ItemDecorator("gridLayout", (int) getResources().getDimension(R.dimen._8sdp), (int) getResources().getDimension(R.dimen._2sdp), (int) getResources().getDimension(R.dimen._24sdp)));
        answerRecyclerView.setLayoutManager(new GridLayoutManager(activity, 2, LinearLayoutManager.VERTICAL, false));
        answerRecyclerView.setHasFixedSize(true);
        answerRecyclerView.setAdapter(adapter);
    }

    private void setData() {
        adapter.setAnswer(((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.getOptions(((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.getIndex()), ((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.answeredPosition(((SampleActivity) Objects.requireNonNull(getActivity())).sampleId, ((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.getIndex()), ((SampleActivity) Objects.requireNonNull(getActivity())).viewModel);

        try {
            questionTextView.setText(((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.getItem(((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.getIndex()).get("text").toString());

//            questionImageView.setImageResource();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}