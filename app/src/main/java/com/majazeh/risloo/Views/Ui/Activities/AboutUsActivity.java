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
import com.majazeh.risloo.ViewModels.AboutUsViewModel;
import com.majazeh.risloo.Views.Adapters.ListAdapter;

import org.json.JSONException;

public class AboutUsActivity extends AppCompatActivity {

    // ViewModel
    private AboutUsViewModel viewModel;

    // Adapters
    private ListAdapter adapter;

    // Widgets
    private Toolbar toolbar;
    private TextView subTitle1TextView, subTitle2TextView, subTitle3TextView, description1TextView, description2TextView, description3TextView;
    private RecyclerView facilitiesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_about_us);

        initializer();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(AboutUsViewModel.class);

        adapter = new ListAdapter(this);
        adapter.setList(viewModel.getFacilities());

        toolbar = findViewById(R.id.activity_about_us_toolbar);

        subTitle1TextView = findViewById(R.id.activity_about_us_subTitle1_textView);
        subTitle2TextView = findViewById(R.id.activity_about_us_subTitle2_textView);
        subTitle3TextView = findViewById(R.id.activity_about_us_subTitle3_textView);
        description1TextView = findViewById(R.id.activity_about_us_description1_textView);
        description2TextView = findViewById(R.id.activity_about_us_description2_textView);
        description3TextView = findViewById(R.id.activity_about_us_description3_textView);

        try {
            subTitle1TextView.setText(viewModel.getAll().get(0).get("title").toString());
            subTitle2TextView.setText(viewModel.getAll().get(1).get("title").toString());
            subTitle3TextView.setText(viewModel.getAll().get(2).get("title").toString());
            description1TextView.setText(viewModel.getAll().get(0).get("description").toString());
            description2TextView.setText(viewModel.getAll().get(1).get("description").toString());
            description3TextView.setText(viewModel.getAll().get(2).get("description").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        facilitiesRecyclerView = findViewById(R.id.activity_about_us_facilities_recyclerView);
        facilitiesRecyclerView.addItemDecoration(new ItemDecorator("verticalLinearLayout",(int) getResources().getDimension(R.dimen._12sdp)));
        facilitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        facilitiesRecyclerView.setHasFixedSize(true);
        facilitiesRecyclerView.setAdapter(adapter);
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