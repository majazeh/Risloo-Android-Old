package com.majazeh.risloo.Views.Ui.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.majazeh.risloo.R;
import com.majazeh.risloo.ViewModels.Sample.SampleViewModel;

public class PPFragment extends Fragment {

    // ViewModels
    private SampleViewModel viewModel;

    // Vars
    private String answer = "";
    private boolean answerTouch, answerError;

    // Objects
    private Activity activity;

    // Widgets
    private TextView questionTextView;
    private ImageView questionImageView;
    private EditText answerEditText;

    public PPFragment(Activity activity) {
        this.activity = activity;
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
        questionTextView = view.findViewById(R.id.fragment_pp_question_textView);

        questionImageView = view.findViewById(R.id.fragment_pp_question_imageView);

        answerEditText = view.findViewById(R.id.fragment_pp_answer_editText);
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