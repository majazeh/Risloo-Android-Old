package com.majazeh.risloo.Views.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.majazeh.risloo.R;
import com.majazeh.risloo.ViewModels.Authentication.AuthViewModel;
import com.majazeh.risloo.ViewModels.Sample.SampleViewModel;
import com.majazeh.risloo.ViewModels.Sample.SampleViewModelFactory;

import org.json.JSONException;

public class SampleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);


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