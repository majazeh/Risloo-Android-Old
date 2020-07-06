package com.majazeh.risloo.Views.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.majazeh.risloo.R;

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