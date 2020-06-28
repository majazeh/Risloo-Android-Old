package com.majazeh.risloo.Views.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.majazeh.risloo.R;

public class SplashActivity extends AppCompatActivity {

    // Vars
    private boolean backPressed = false;

    // Objects
    private Handler handler;

    // Widgets
    private ImageView logoImageView;
    private TextView versionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_splash);

        initializer();

        launchActivity();
    }

    @SuppressLint("InlinedApi")
    private void decorator() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.Primary));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.Primary));
        }
    }

    private void initializer() {
        handler = new Handler();

        logoImageView = findViewById(R.id.activity_splash_logo_imageView);

        versionTextView = findViewById(R.id.activity_splash_version_textView);
        versionTextView.setText(getVersion());
    }

    private void launchActivity() {
        handler.postDelayed(() -> {
            if (!backPressed) {
                Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                startActivity(intent);

                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, 1000);
    }

    private String getVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            return "نسخه " + packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressed = true;

        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}