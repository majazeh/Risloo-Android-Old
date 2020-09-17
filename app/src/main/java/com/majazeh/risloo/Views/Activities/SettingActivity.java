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
import com.majazeh.risloo.ViewModels.SettingViewModel;
import com.majazeh.risloo.Views.Adapters.SettingAdapter;

import org.json.JSONException;

public class SettingActivity extends AppCompatActivity {

    // ViewModels
    private SettingViewModel viewModel;

    // Adapters
    private SettingAdapter adapter;

    // Widgets
    private Toolbar toolbar;
    private RecyclerView settingsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_setting);

        initializer();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = new ViewModelProvider(this).get(SettingViewModel.class);

        adapter = new SettingAdapter(this);
        try {
            adapter.setMore(viewModel.getAll());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        toolbar = findViewById(R.id.activity_setting_toolbar);

        settingsRecyclerView = findViewById(R.id.activity_setting_recyclerView);
        settingsRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", 0, 0, 0));
        settingsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        settingsRecyclerView.setHasFixedSize(true);
        settingsRecyclerView.setAdapter(adapter);
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