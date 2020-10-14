package com.majazeh.risloo.Views.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Views.Activities.SampleActivity;
import com.majazeh.risloo.Views.Adapters.OptionalAdapter;

import org.json.JSONException;

import java.util.Objects;

public class TextOptionalFragment extends Fragment {

    // Adapters
    private OptionalAdapter adapter;

    // Objects
    private Activity activity;

    // Widgets
    private TextView questionTextView;
    private RecyclerView answerRecyclerView;

    public TextOptionalFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_text_optional, viewGroup, false);

        initializer(view);

        setData();

        return view;
    }

    private void initializer(View view) {
        adapter = new OptionalAdapter(activity);

        questionTextView = view.findViewById(R.id.fragment_text_optional_question_textView);

        answerRecyclerView = view.findViewById(R.id.fragment_text_optional_answer_recyclerView);
        answerRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._24sdp)));
        answerRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        answerRecyclerView.setHasFixedSize(true);
    }

    private void setData() {
        adapter.setAnswer(((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.getOptions(((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.getIndex()), ((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.answeredPosition(((SampleActivity) Objects.requireNonNull(getActivity())).sampleId, ((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.getIndex()), ((SampleActivity) Objects.requireNonNull(getActivity())).viewModel);
        answerRecyclerView.setAdapter(adapter);

        try {
            if (!((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.getItem(((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.getIndex()).get("text").toString().equals("null")) {
                questionTextView.setVisibility(View.VISIBLE);
                questionTextView.setText(((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.getItem(((SampleActivity) Objects.requireNonNull(getActivity())).viewModel.getIndex()).get("text").toString());
            } else {
                questionTextView.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}