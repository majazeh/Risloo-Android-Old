package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.ExplodeViewModel;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    // ViewModels
    private ExplodeViewModel viewModel;

    // Objects
    private Handler handler;

    // Widgets
    private TextView versionTextView;
    private ProgressBar updateProgressBar;
    private Dialog updateDialog;
    private TextView updateDialogTitle, updateDialogDescription, updateDialogPositive, updateDialogNegative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_splash);

        initializer();

        detector();

        listener();

        checkUpdate();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.darkShowSystemUI(this);
        windowDecorator.darkSetSystemUIColor(this, getResources().getColor(R.color.Primary), getResources().getColor(R.color.Primary));
    }

    private void initializer() {
        viewModel = new ViewModelProvider(this).get(ExplodeViewModel.class);

        handler = new Handler();

        versionTextView = findViewById(R.id.activity_splash_version_textView);
        versionTextView.setText(currentVersion());

        updateProgressBar = findViewById(R.id.activity_splash_update_progressBar);

        updateDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(updateDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateDialog.setContentView(R.layout.dialog_action);
        updateDialog.setCancelable(true);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(updateDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        updateDialog.getWindow().setAttributes(layoutParams);

        updateDialogTitle = updateDialog.findViewById(R.id.dialog_action_title_textView);
        updateDialogDescription = updateDialog.findViewById(R.id.dialog_action_description_textView);
        updateDialogPositive = updateDialog.findViewById(R.id.dialog_action_positive_textView);
        updateDialogPositive.setTextColor(getResources().getColor(R.color.PrimaryDark));
        updateDialogNegative = updateDialog.findViewById(R.id.dialog_action_negative_textView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            updateDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            updateDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    private void listener() {
        updateDialogPositive.setOnClickListener(v -> {
            updateDialogPositive.setClickable(false);
            handler.postDelayed(() -> updateDialogPositive.setClickable(true), 300);
            updateDialog.dismiss();

            IntentManager.googlePlay(this);
            finish();
        });

        updateDialogNegative.setOnClickListener(v -> {
            updateDialogNegative.setClickable(false);
            handler.postDelayed(() -> updateDialogNegative.setClickable(true), 300);
            updateDialog.dismiss();

            if (viewModel.forceUpdate()) {
                finish();
            } else {
                launchIntro();
            }
        });

        updateDialog.setOnCancelListener(dialog -> {
            updateDialog.dismiss();

            if (viewModel.forceUpdate()) {
                finish();
            } else {
                launchIntro();
            }
        });
    }

    private void checkUpdate() {
        if (viewModel.hasUpdate()) {
            if (viewModel.forceUpdate()) {
                updateDialogTitle.setText(newVersion());
                updateDialogDescription.setText(getResources().getString(R.string.SplashUpdateDialogForceDescription));
                updateDialogPositive.setText(getResources().getString(R.string.SplashUpdateDialogForcePositive));
                updateDialogNegative.setText(getResources().getString(R.string.SplashUpdateDialogForceNegative));

                updateDialog.show();
            } else {
                updateDialogTitle.setText(newVersion());
                updateDialogDescription.setText(getResources().getString(R.string.SplashUpdateDialogNotForceDescription));
                updateDialogPositive.setText(getResources().getString(R.string.SplashUpdateDialogNotForcePositive));
                updateDialogNegative.setText(getResources().getString(R.string.SplashUpdateDialogNotForceNegative));

                updateDialog.show();
            }
        } else {
            checkContent();
        }
    }

    private void checkContent() {
        if (viewModel.newContent()) {
            updateProgressBar.setVisibility(View.VISIBLE);
            versionTextView.setText(getResources().getString(R.string.SplashLoading));
            // TODO : Load Our Samples Content Or Add New Samples
            updateProgressBar.setVisibility(View.INVISIBLE);
            versionTextView.setText(currentVersion());
        }
        launchIntro();
    }

    private String currentVersion() {
        return getResources().getString(R.string.SplashVersion) + " " + viewModel.currentVersion();
    }

    private String newVersion() {
        return getResources().getString(R.string.SplashVersion) + " " + viewModel.newVersion() + " " + getResources().getString(R.string.SplashArrived);
    }

    private void launchIntro() {
        handler.postDelayed(() -> {
            startActivity(new Intent(this, IntroActivity.class));
            finish();
        }, 1000);
    }

    @Override
    public void finish() {
        super.finish();
        handler.removeCallbacksAndMessages(null);
    }

}