package com.majazeh.risloo.Views.Ui.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Sample;
import com.majazeh.risloo.Models.Controller.AuthController;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.ViewModels.Sample.SampleViewModel;
import com.majazeh.risloo.ViewModels.Sample.SampleViewModelFactory;
import com.majazeh.risloo.Views.Adapters.TFTAdapter;
import com.majazeh.risloo.Views.Ui.Activities.SampleActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TFTFragment extends Fragment {

    // ViewModels
    private SampleViewModel viewModel;

    // Adapters
    private TFTAdapter adapter;

    // Objects
    private Activity activity;
    private SharedPreferences sharedPreferences;

    // Widgets
    private TextView questionTextView;
    private RecyclerView answerRecyclerView;

    public TFTFragment(Activity activity,SampleViewModel viewModel) {
        this.activity = activity;
        this.viewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_tft, viewGroup, false);

        initializer(view);

        return view;
    }

    private void initializer(View view) {

        sharedPreferences = activity.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        adapter = new TFTAdapter(activity,viewModel);
        try {
            adapter.setAnswer(viewModel.getOptions(viewModel.getCurrentIndex()),viewModel.answerPosition(sharedPreferences.getString("sampleId",""),viewModel.getCurrentIndex()));;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        questionTextView = view.findViewById(R.id.fragment_tft_question_textView);
        try {
            questionTextView.setText(viewModel.getItem(viewModel.getCurrentIndex()).get("text").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        answerRecyclerView = view.findViewById(R.id.fragment_tft_answer_recyclerView);
        answerRecyclerView.addItemDecoration(new ItemDecorator("verticalLinearLayout",(int) getResources().getDimension(R.dimen._16sdp)));
        answerRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        answerRecyclerView.setHasFixedSize(true);
        answerRecyclerView.setAdapter(adapter);
    }

}