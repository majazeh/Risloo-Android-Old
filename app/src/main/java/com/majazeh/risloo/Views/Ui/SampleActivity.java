package com.majazeh.risloo.Views.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.majazeh.risloo.Models.Repositories.Sample.SampleItems;
import com.majazeh.risloo.Models.Repositories.Sample.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.ViewModels.Sample.SampleViewModel;

import org.json.JSONException;

public class SampleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        SampleViewModel viewModel = ViewModelProviders.of(this).get(SampleViewModel.class);
        viewModel.getLocalData().observe((LifecycleOwner) this, arrayLists -> {
            if (viewModel.inProgress()) {

            } else {
                try {
                    viewModel.process();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


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