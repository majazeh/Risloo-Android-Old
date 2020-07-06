package com.majazeh.risloo.Views.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.WindowDecorator;

public class AboutUsActivity extends AppCompatActivity {

    // Widgets
    private Toolbar toolbar;
    private TextView nameTextView, descriptionTextView;

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
        toolbar = findViewById(R.id.activity_about_us_toolbar);

        nameTextView = findViewById(R.id.activity_about_us_name_textView);
        descriptionTextView = findViewById(R.id.activity_about_us_description_textView);
    }

    private void listener() {
        toolbar.setNavigationOnClickListener(v -> finish());
    }

}