package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.TermConditionViewModel;
import com.majazeh.risloo.Views.Adapters.ListAdapter;

import org.json.JSONException;

public class TermConditionActivity extends AppCompatActivity {

    // ViewModel
    private TermConditionViewModel viewModel;

    // Adapters
    private ListAdapter adapter;

    // Widgets
    private Toolbar toolbar;
    private RecyclerView termsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_term_condition);

        initializer();

        listener();

        setData();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = new ViewModelProvider(this).get(TermConditionViewModel.class);

        adapter = new ListAdapter(this);

        toolbar = findViewById(R.id.activity_term_condition_toolbar);

        termsRecyclerView = findViewById(R.id.activity_term_condition_recyclerView);
        termsRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", (int) getResources().getDimension(R.dimen._24sdp), (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._24sdp)));
        termsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        termsRecyclerView.setHasFixedSize(true);
    }

    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
    }

    private void setData() {
        try {
            adapter.setList(viewModel.getAll(), "TermCondition");
            termsRecyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}