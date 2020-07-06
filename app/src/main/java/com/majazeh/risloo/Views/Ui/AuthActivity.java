package com.majazeh.risloo.Views.Ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.Authentication.AuthViewModel;

import org.json.JSONException;

public class AuthActivity extends AppCompatActivity {

    // Widgets
    private Toolbar toolbar;

    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_auth);

        initializer();

        listener();


        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        try {
            //authViewModel.start("9356032043");
            authViewModel.signIn("test", "male","09124443322","111111");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        toolbar = findViewById(R.id.activity_auth_toolbar);
    }

    private void listener() {
        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(this, MoreActivity.class)));
    }

}