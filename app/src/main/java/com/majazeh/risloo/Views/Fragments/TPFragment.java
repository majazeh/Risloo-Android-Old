package com.majazeh.risloo.Views.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.majazeh.risloo.R;
import com.majazeh.risloo.ViewModels.SampleViewModel;

public class TPFragment extends Fragment {

    // ViewModels
    private SampleViewModel viewModel;

    // Vars
    private String answer = "";
    private boolean answerTouch, answerError;

    // Objects
    private Activity activity;

    // Widgets
    private TextView questionTextView;
    private EditText answerEditText;

    public TPFragment(Activity activity, SampleViewModel viewModel) {
        this.activity = activity;
        this.viewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_tp, viewGroup, false);

        initializer(view);

        listener();

        return view;
    }

    private void initializer(View view) {
        questionTextView = view.findViewById(R.id.fragment_tp_question_textView);

        answerEditText = view.findViewById(R.id.fragment_tp_answer_editText);
        answerEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        answerEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                answerEditText.setBackgroundResource(R.drawable.draw_18sdp_primary_border);
                answerEditText.setCursorVisible(true);

                answerTouch = true;
            }
            return false;
        });
    }

    private void clearData() {
        answerEditText.setCursorVisible(false);

        answerEditText.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);

        answerTouch = false;
        answerError = false;
    }

}