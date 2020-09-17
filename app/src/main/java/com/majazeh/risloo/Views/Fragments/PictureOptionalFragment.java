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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.OptionalAdapter;

import org.json.JSONException;

public class PictureOptionalFragment extends Fragment {

    // ViewModels
    private SampleViewModel viewModel;

    // Adapters
    private OptionalAdapter adapter;

    // Objects
    private Activity activity;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    private TextView questionTextView;
    private ImageView questionImageView;
    private RecyclerView answerRecyclerView;

    public PictureOptionalFragment(Activity activity, SampleViewModel viewModel) {
        this.activity = activity;
        this.viewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_picture_optional, viewGroup, false);

        initializer(view);

        return view;
    }

    private void initializer(View view) {
        sharedPreferences = activity.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        adapter = new OptionalAdapter(activity);
        adapter.setAnswer(viewModel.getOptions(viewModel.getIndex()), viewModel.answeredPosition(sharedPreferences.getString("sampleId",""), viewModel.getIndex()), viewModel);

        questionTextView = view.findViewById(R.id.fragment_picture_optional_question_textView);
        try {
            questionTextView.setText(viewModel.getItem(viewModel.getIndex()).get("text").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        questionImageView = view.findViewById(R.id.fragment_picture_optional_question_imageView);
//        try {
//            questionImageView.setImageResource();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        answerRecyclerView = view.findViewById(R.id.fragment_picture_optional_answer_recyclerView);
        answerRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._24sdp)));
        answerRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        answerRecyclerView.setHasFixedSize(true);
        answerRecyclerView.setAdapter(adapter);
    }

}