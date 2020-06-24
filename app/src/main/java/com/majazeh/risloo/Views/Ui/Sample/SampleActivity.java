package com.majazeh.risloo.Views.Ui.Sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import com.majazeh.risloo.R;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.ViewModels.SampleViewModelFactory;

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        SampleViewModel viewModel = ViewModelProviders.of(this, new SampleViewModelFactory(this.getApplication(), checkTestUniqueId(1))).get(SampleViewModel.class);

    }

    public String checkTestUniqueId(int testUniqueId) {
        switch (testUniqueId) {
            case 1:
                return "Barzeghar.json";
            default:
                return "null";
        }
    }
}