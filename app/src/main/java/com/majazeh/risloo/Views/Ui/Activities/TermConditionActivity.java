package com.majazeh.risloo.Views.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.TermConditionViewModel;
import com.majazeh.risloo.Views.Adapters.ListAdapter;

import org.json.JSONException;

public class TermConditionActivity extends AppCompatActivity {

    // ViewModel
    private TermConditionViewModel viewModel;

    // Adapters
    private ListAdapter accountsAdapter, termsAdapter;

    // Widgets
    private Toolbar toolbar;
    private TextView subTitle1TextView, subTitle2TextView, subTitle3TextView, subTitle4TextView, subTitle5TextView, subTitle6TextView, subTitle7TextView, description1TextView, description2TextView, description3TextView, description4TextView, description5TextView, description6TextView, description7TextView;
    private RecyclerView accountsRecyclerView, termsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_term_condition);

        initializer();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(TermConditionViewModel.class);

        accountsAdapter = new ListAdapter(this);
        termsAdapter = new ListAdapter(this);
        accountsAdapter.setList(viewModel.getAccounts());
        termsAdapter.setList(viewModel.getTerms());

        toolbar = findViewById(R.id.activity_term_condition_toolbar);

        subTitle1TextView = findViewById(R.id.activity_term_condition_subTitle1_textView);
        subTitle2TextView = findViewById(R.id.activity_term_condition_subTitle2_textView);
        subTitle3TextView = findViewById(R.id.activity_term_condition_subTitle3_textView);
        subTitle4TextView = findViewById(R.id.activity_term_condition_subTitle4_textView);
        subTitle5TextView = findViewById(R.id.activity_term_condition_subTitle5_textView);
        subTitle6TextView = findViewById(R.id.activity_term_condition_subTitle6_textView);
        subTitle7TextView = findViewById(R.id.activity_term_condition_subTitle7_textView);
        description1TextView = findViewById(R.id.activity_term_condition_description1_textView);
        description2TextView = findViewById(R.id.activity_term_condition_description2_textView);
        description3TextView = findViewById(R.id.activity_term_condition_description3_textView);
        description4TextView = findViewById(R.id.activity_term_condition_description4_textView);
        description5TextView = findViewById(R.id.activity_term_condition_description5_textView);
        description6TextView = findViewById(R.id.activity_term_condition_description6_textView);
        description7TextView = findViewById(R.id.activity_term_condition_description7_textView);

        try {
            subTitle1TextView.setText(viewModel.getAll().get(0).get("title").toString());
            subTitle2TextView.setText(viewModel.getAll().get(1).get("title").toString());
            subTitle3TextView.setText(viewModel.getAll().get(2).get("title").toString());
            subTitle4TextView.setText(viewModel.getAll().get(3).get("title").toString());
            subTitle5TextView.setText(viewModel.getAll().get(4).get("title").toString());
            subTitle6TextView.setText(viewModel.getAll().get(5).get("title").toString());
            subTitle7TextView.setText(viewModel.getAll().get(6).get("title").toString());
            description1TextView.setText(viewModel.getAll().get(0).get("description").toString());
            description2TextView.setText(viewModel.getAll().get(1).get("description").toString());
            description3TextView.setText(viewModel.getAll().get(2).get("description").toString());
            description4TextView.setText(viewModel.getAll().get(3).get("description").toString());
            description5TextView.setText(viewModel.getAll().get(4).get("description").toString());
            description6TextView.setText(viewModel.getAll().get(5).get("description").toString());
            description7TextView.setText(viewModel.getAll().get(6).get("description").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        accountsRecyclerView = findViewById(R.id.activity_term_condition_accounts_recyclerView);
        termsRecyclerView = findViewById(R.id.activity_term_condition_terms_recyclerView);
        accountsRecyclerView.addItemDecoration(new ItemDecorator("verticalLinearLayout",(int) getResources().getDimension(R.dimen._12sdp)));
        termsRecyclerView.addItemDecoration(new ItemDecorator("verticalLinearLayout",(int) getResources().getDimension(R.dimen._12sdp)));
        accountsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        termsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        accountsRecyclerView.setHasFixedSize(true);
        termsRecyclerView.setHasFixedSize(true);
        accountsRecyclerView.setAdapter(accountsAdapter);
        termsRecyclerView.setAdapter(termsAdapter);
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