package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.AuthViewModel;

public class EditAccountActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel viewModel;

    // Objects
    private Handler handler;

    // Widgets
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_edit_account);

        initializer();

        detector();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightTransparentWindow(this, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        handler = new Handler();

        toolbar = findViewById(R.id.activity_edit_account_toolbar);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

        }
    }

    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });
    }

    private void observeWork() {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}