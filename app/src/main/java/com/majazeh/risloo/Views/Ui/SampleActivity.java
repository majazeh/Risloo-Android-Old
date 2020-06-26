package com.majazeh.risloo.Views.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.Toast;

import com.majazeh.risloo.R;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.ViewModels.SampleViewModelFactory;

public class SampleActivity extends AppCompatActivity {

    private SampleViewModel sampleViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        sampleViewModel = ViewModelProviders.of(this, new SampleViewModelFactory(this.getApplication(), checkTestUniqueId(1))).get(SampleViewModel.class);

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