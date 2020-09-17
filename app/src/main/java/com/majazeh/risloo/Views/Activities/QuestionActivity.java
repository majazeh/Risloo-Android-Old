package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.QuestionViewModel;
import com.majazeh.risloo.Views.Adapters.QuestionAdapter;

import org.json.JSONException;

import java.util.HashMap;

public class QuestionActivity extends AppCompatActivity {

    // ViewModels
    private QuestionViewModel viewModel;

    // Adapters
    private QuestionAdapter adapter;

    // Vars
    private HashMap<Integer, Boolean> expands;

    // Widgets
    private Toolbar toolbar;
    private RecyclerView questionsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_question);

        initializer();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = new ViewModelProvider(this).get(QuestionViewModel.class);

        expands = new HashMap<>();
        try {
            for (int i = 0; i < viewModel.getAll().size(); i++) {
                expands.put(i, false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter = new QuestionAdapter(this);
        try {
            adapter.setQuestion(viewModel.getAll(), expands);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        toolbar = findViewById(R.id.activity_question_toolbar);

        questionsRecyclerView = findViewById(R.id.activity_question_recyclerView);
        questionsRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._16sdp)));
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        questionsRecyclerView.setHasFixedSize(true);
        questionsRecyclerView.setAdapter(adapter);
    }

    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}