package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Models.Repositories.ExplodeRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Managers.ParamsManager;
import com.majazeh.risloo.ViewModels.ExplodeViewModel;

import org.json.JSONException;

public class SplashActivity extends AppCompatActivity {

    // ViewModels
    private ExplodeViewModel explodeViewModel;

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

        launcher("intro");
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.darkShowSystemUI(this);
        windowDecorator.darkSetSystemUIColor(this, getResources().getColor(R.color.CoralRed), getResources().getColor(R.color.CoralRed));
    }

    private void initializer() {
        explodeViewModel = new ViewModelProvider(this).get(ExplodeViewModel.class);

        handler = new Handler();

        versionTextView = findViewById(R.id.version_textView);
        versionTextView.setText(currentVersion());

        updateProgressBar = findViewById(R.id.update_progressBar);

        updateDialog = new Dialog(this, R.style.DialogTheme);
        updateDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        updateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        updateDialog.setContentView(R.layout.dialog_action);
        updateDialog.setCancelable(true);
        updateDialog.getWindow().setAttributes(ParamsManager.set(updateDialog));

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
            handler.postDelayed(() -> updateDialogPositive.setClickable(true), 250);
            updateDialog.dismiss();

            IntentManager.googlePlay(this);
            launcher("finish");
        });

        updateDialogNegative.setOnClickListener(v -> {
            updateDialogNegative.setClickable(false);
            handler.postDelayed(() -> updateDialogNegative.setClickable(true), 250);
            updateDialog.dismiss();

            if (explodeViewModel.forceUpdate()) {
                launcher("finish");
            } else {
                launcher("intro");
            }
        });

        updateDialog.setOnCancelListener(dialog -> {
            updateDialog.dismiss();

            if (explodeViewModel.forceUpdate()) {
                launcher("finish");
            } else {
                launcher("intro");
            }
        });
    }

    private void setData() {
        updateDialogTitle.setText(newVersion());

        if (explodeViewModel.forceUpdate()) {
            updateDialogDescription.setText(getResources().getString(R.string.SplashUpdateDialogForceDescription));
            updateDialogPositive.setText(getResources().getString(R.string.SplashUpdateDialogForcePositive));
            updateDialogNegative.setText(getResources().getString(R.string.SplashUpdateDialogForceNegative));
        } else {
            updateDialogDescription.setText(getResources().getString(R.string.SplashUpdateDialogNotForceDescription));
            updateDialogPositive.setText(getResources().getString(R.string.SplashUpdateDialogNotForcePositive));
            updateDialogNegative.setText(getResources().getString(R.string.SplashUpdateDialogNotForceNegative));
        }

        updateDialog.show();
    }

    private void getData() {
        handler.postDelayed(() -> {
            try {
                versionTextView.setText(getResources().getString(R.string.SplashLoading));
                updateProgressBar.setVisibility(View.VISIBLE);

                explodeViewModel.explode();
                observeWork();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, 500);
    }

    private void observeWork() {
        ExplodeRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (ExplodeRepository.work.equals("explode")) {
                if (integer == 1) {
                    if (explodeViewModel.hasUpdate()) {
                        setData();
                    } else {
                        launcher("intro");
                    }

                    versionTextView.setText(currentVersion());
                    updateProgressBar.setVisibility(View.GONE);
                    ExplodeRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    launcher("finish");

                    versionTextView.setText(currentVersion());
                    updateProgressBar.setVisibility(View.GONE);
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    ExplodeRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    launcher("finish");

                    versionTextView.setText(currentVersion());
                    updateProgressBar.setVisibility(View.GONE);
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    ExplodeRepository.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    private void launcher(String activity) {
        if (activity.equals("intro")) {
            handler.postDelayed(() -> {
                startActivity(new Intent(this, IntroActivity.class));
                finish();
            }, 1000);
        } else if (activity.equals("finish")) {
            finish();
        }
    }

    private String currentVersion() {
        return getResources().getString(R.string.SplashVersion) + " " + explodeViewModel.currentVersion();
    }

    private String newVersion() {
        return getResources().getString(R.string.SplashVersion) + " " + explodeViewModel.newVersion() + " " + getResources().getString(R.string.SplashArrived);
    }

    @Override
    public void finish() {
        super.finish();
        handler.removeCallbacksAndMessages(null);
    }

}