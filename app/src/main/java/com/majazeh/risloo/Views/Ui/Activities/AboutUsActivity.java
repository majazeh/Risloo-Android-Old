package com.majazeh.risloo.Views.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.AboutUsViewModel;
import com.majazeh.risloo.Views.Adapters.ListAdapter;

public class AboutUsActivity extends AppCompatActivity {

    // ViewModel
    private AboutUsViewModel viewModel;

    // Adapters
    private ListAdapter adapter;

    // Widgets
    private Toolbar toolbar;
    private RecyclerView recyclerView;

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
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(AboutUsViewModel.class);

        adapter = new ListAdapter(this);
        adapter.setList(viewModel.getAll(), "AboutUs");

        toolbar = findViewById(R.id.activity_about_us_toolbar);

        recyclerView = findViewById(R.id.activity_about_us_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorator("listLayout",(int) getResources().getDimension(R.dimen._24sdp)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
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