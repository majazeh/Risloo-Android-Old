package com.majazeh.risloo.Views.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.majazeh.risloo.R;
import com.majazeh.risloo.ViewModels.SampleViewModel;

public class PPFragment extends Fragment {

    // ViewModels
    private SampleViewModel viewModel;

    // Vars
    private String answer = "";

    // Objects
    private Activity activity;
    private SharedPreferences sharedPreferences;

    // Widgets
    private TextView questionTextView;
    private ImageView questionImageView;
    private EditText answerEditText;

    public PPFragment(Activity activity, SampleViewModel viewModel) {
        this.activity = activity;
        this.viewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_pp, viewGroup, false);

        initializer(view);

        listener();

        return view;
    }

    private void initializer(View view) {
        sharedPreferences = activity.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        questionTextView = view.findViewById(R.id.fragment_pp_question_textView);
//        try {
//            questionTextView.setText(viewModel.getItem(viewModel.getIndex()).get("text").toString());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        questionImageView = view.findViewById(R.id.fragment_pp_question_imageView);
//        try {
//            questionImageView.setImageResource();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        answerEditText = view.findViewById(R.id.fragment_pp_answer_editText);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        answerEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                answerEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                answerEditText.setCursorVisible(true);
            }
            return false;
        });
    }

    private void clearData() {
        answerEditText.setCursorVisible(false);

        answerEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
    }

}