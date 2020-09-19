package com.majazeh.risloo.Views.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.majazeh.risloo.R;
import com.majazeh.risloo.ViewModels.SampleViewModel;

import org.json.JSONException;

public class TextTypingFragment extends Fragment {

    // ViewModels
    private SampleViewModel viewModel;

    // Vars
    private String answer = "";

    // Objects
    private Activity activity;

    // Widgets
    private TextView questionTextView;
    private EditText answerEditText;

    public TextTypingFragment(Activity activity, SampleViewModel viewModel) {
        this.activity = activity;
        this.viewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_text_typing, viewGroup, false);

        initializer(view);

        listener();

        return view;
    }

    private void initializer(View view) {
        questionTextView = view.findViewById(R.id.fragment_text_typing_question_textView);
        try {
            questionTextView.setText(viewModel.getItem(viewModel.getIndex()).get("text").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        answerEditText = view.findViewById(R.id.fragment_text_typing_answer_editText);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        answerEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!answerEditText.hasFocus()) {

                }
            }
            return false;
        });
    }

}