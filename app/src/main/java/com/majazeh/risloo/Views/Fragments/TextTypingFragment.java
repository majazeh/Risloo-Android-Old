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
import com.majazeh.risloo.Views.Activities.SampleActivity;

import org.json.JSONException;

import java.util.Objects;

public class TextTypingFragment extends Fragment {

    // Vars
    private String answer = "";

    // Objects
    private Activity activity;

    // Widgets
    private TextView questionTextView;
    private EditText answerEditText;

    public TextTypingFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_text_typing, viewGroup, false);

        initializer(view);

        listener();

        setData();

        return view;
    }

    private void initializer(View view) {
        questionTextView = view.findViewById(R.id.fragment_text_typing_question_textView);

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

    private void setData() {
        try {
            questionTextView.setText(((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.getItem(((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.getIndex()).get("text").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}