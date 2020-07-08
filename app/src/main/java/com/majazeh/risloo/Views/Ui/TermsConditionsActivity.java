package com.majazeh.risloo.Views.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.WindowDecorator;

public class TermsConditionsActivity extends AppCompatActivity {

    // Widgets
    private Toolbar toolbar;
    private TextView contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_terms_conditions);

        initializer();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        toolbar = findViewById(R.id.activity_terms_conditions_toolbar);

        contentTextView = findViewById(R.id.activity_terms_conditions_content_textView);
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