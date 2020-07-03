package com.majazeh.risloo.Views.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.WindowDecorator;

public class CallUsActivity extends AppCompatActivity {

    // Widgets
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_call_us);

        initializer();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        toolbar = findViewById(R.id.activity_call_us_toolbar);
    }

    private void listener() {
        toolbar.setNavigationOnClickListener(v -> finish());
    }

}