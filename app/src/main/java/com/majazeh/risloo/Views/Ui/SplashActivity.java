package com.majazeh.risloo.Views.Ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.WindowDecorator;

public class SplashActivity extends AppCompatActivity {

    // Vars
    private boolean backPressed = false;

    // Objects
    private Handler handler;
    private WindowManager.LayoutParams layoutParams;

    // Widgets
    private ImageView logoImageView;
    private TextView loadingTextView, versionTextView, updateDialogTitle, updateDialogDescription, updateDialogPositive, updateDialogNegative;
    private ProgressBar updatingProgressBar;
    private Dialog updateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_splash);

        initializer();

        detector();

        listener();

        launchActivity();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.defaultWindow(this, R.color.Primary, R.color.Primary);
    }

    private void initializer() {
        handler = new Handler();

        logoImageView = findViewById(R.id.activity_splash_logo_imageView);

        loadingTextView = findViewById(R.id.activity_splash_loading_textView);
        versionTextView = findViewById(R.id.activity_splash_version_textView);
        versionTextView.setText(appVersion());

        updatingProgressBar = findViewById(R.id.activity_splash_updating_progressBar);

        updateDialog = new Dialog(this, R.style.DialogTheme);
        updateDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateDialog.setContentView(R.layout.dialog_action);
        updateDialog.setCancelable(true);

        layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(updateDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        updateDialog.getWindow().setAttributes(layoutParams);

        updateDialogTitle = updateDialog.findViewById(R.id.dialog_action_title_textView);
        updateDialogTitle.setText(getResources().getString(R.string.SplashUpdateDialogTitle));
        updateDialogDescription = updateDialog.findViewById(R.id.dialog_action_description_textView);
        updateDialogDescription.setText(getResources().getString(R.string.SplashUpdateDialogDescription));
        updateDialogPositive = updateDialog.findViewById(R.id.dialog_action_positive_textView);
        updateDialogPositive.setText(getResources().getString(R.string.SplashUpdateDialogPositive));
        updateDialogNegative = updateDialog.findViewById(R.id.dialog_action_negative_textView);
        updateDialogNegative.setText(getResources().getString(R.string.SplashUpdateDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            updateDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_quartz_ripple);
            updateDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_quartz_ripple);
        }
    }

    private void listener() {
        updateDialogPositive.setOnClickListener(v -> {
            // TODO : Update The App In Background
        });

        updateDialogNegative.setOnClickListener(v -> {
            updateDialogNegative.setClickable(false);
            handler.postDelayed(() -> updateDialogNegative.setClickable(true), 1000);
            updateDialog.dismiss();

            launchActivity();
        });

        updateDialog.setOnCancelListener(dialog -> {
            updateDialog.dismiss();

            launchActivity();
        });
    }

    private String appVersion() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            return getResources().getString(R.string.SplashVersion) + " " + packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } return null;
    }

    private void launchActivity() {
        handler.postDelayed(() -> {
            if (!backPressed) {
                Intent intent = new Intent(this, IntroActivity.class);
                startActivity(intent);

                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backPressed = true;

        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}