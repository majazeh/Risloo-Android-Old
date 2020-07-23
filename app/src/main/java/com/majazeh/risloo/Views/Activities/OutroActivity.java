package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;

import org.json.JSONException;

public class OutroActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    // ViewModels
    private SampleViewModel viewModel;

    // Vars
    private String work = "";
    private boolean storagePermissionsGranted = false, exit = false;

    // Objects
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    private Button internetButton, smsButton, downloadButton, laterButton;
    private TextView outroDialogTitle, outroDialogDescription, outroDialogPositive, outroDialogNegative;
    private Dialog outroDialog, progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_outro);

        initializer();

        detector();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            internetButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
            smsButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
            downloadButton.setBackgroundResource(R.drawable.draw_18sdp_solitude_ripple);
            laterButton.setBackgroundResource(R.drawable.draw_18sdp_solitude_ripple);

            outroDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
            outroDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
        }
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(SampleViewModel.class);

        handler = new Handler();

        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        internetButton = findViewById(R.id.activity_outro_internet_button);
        smsButton = findViewById(R.id.activity_outro_sms_button);
        downloadButton = findViewById(R.id.activity_outro_download_button);
        laterButton = findViewById(R.id.activity_outro_later_button);

        outroDialog = new Dialog(this, R.style.DialogTheme);
        outroDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        outroDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        outroDialog.setContentView(R.layout.dialog_action);
        outroDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(outroDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        outroDialog.getWindow().setAttributes(layoutParams);

        outroDialogTitle = outroDialog.findViewById(R.id.dialog_action_title_textView);
        outroDialogDescription = outroDialog.findViewById(R.id.dialog_action_description_textView);
        outroDialogPositive = outroDialog.findViewById(R.id.dialog_action_positive_textView);
        outroDialogPositive.setTextColor(getResources().getColor(R.color.PrimaryDark));
        outroDialogNegative = outroDialog.findViewById(R.id.dialog_action_negative_textView);
    }

    private void listener() {
        internetButton.setOnClickListener(v -> {
            internetButton.setClickable(false);
            handler.postDelayed(() -> internetButton.setClickable(true), 1000);

            outroDialogTitle.setText(getResources().getString(R.string.OutroInternetDialogTitle));
            outroDialogDescription.setText(getResources().getString(R.string.OutroInternetDialogDescription));
            outroDialogPositive.setText(getResources().getString(R.string.OutroInternetDialogPositive));
            outroDialogNegative.setText(getResources().getString(R.string.OutroInternetDialogNegative));

            outroDialog.show();

            work = "internet";
        });

        smsButton.setOnClickListener(v -> {
            smsButton.setClickable(false);
            handler.postDelayed(() -> smsButton.setClickable(true), 1000);

            outroDialogTitle.setText(getResources().getString(R.string.OutroSMSDialogTitle));
            outroDialogDescription.setText(getResources().getString(R.string.OutroSMSDialogDescription));
            outroDialogPositive.setText(getResources().getString(R.string.OutroSMSDialogPositive));
            outroDialogNegative.setText(getResources().getString(R.string.OutroSMSDialogNegative));

            outroDialog.show();

            work = "sms";
        });

        downloadButton.setOnClickListener(v -> {
            downloadButton.setClickable(false);
            handler.postDelayed(() -> downloadButton.setClickable(true), 1000);

            outroDialogTitle.setText(getResources().getString(R.string.OutroDownloadDialogTitle));
            outroDialogDescription.setText(getResources().getString(R.string.OutroDownloadDialogDescription));
            outroDialogPositive.setText(getResources().getString(R.string.OutroDownloadDialogPositive));
            outroDialogNegative.setText(getResources().getString(R.string.OutroDownloadDialogNegative));

            outroDialog.show();

            work = "download";
        });

        laterButton.setOnClickListener(v -> {
            laterButton.setClickable(false);
            handler.postDelayed(() -> laterButton.setClickable(true), 1000);

            finish();
        });

        outroDialogPositive.setOnClickListener(v -> {
            outroDialogPositive.setClickable(false);
            handler.postDelayed(() -> outroDialogPositive.setClickable(true), 1000);
            outroDialog.dismiss();

            if (work == "internet") {
                sendViaInternet();
            } else if (work == "sms") {
                sendViaSMS();
            } else if (work == "download") {
                exit = true;
                downloadFile();
            }
        });

        outroDialogNegative.setOnClickListener(v -> {
            outroDialogNegative.setClickable(false);
            handler.postDelayed(() -> outroDialogNegative.setClickable(true), 1000);
            outroDialog.dismiss();

            if (work == "download") {
                exit = false;
                downloadFile();
            }
        });

        outroDialog.setOnCancelListener(dialog -> {
            outroDialog.dismiss();
        });
    }

    private void sendViaInternet() {
        progressDialog.show();

        SampleRepository.cache = true;
        try {
            viewModel.sendAnswers(sharedPreferences.getString("sampleId", ""));
            SampleRepository.workStateAnswer.observe(this, integer -> {
                if (integer == 1) {
                    viewModel.deleteStorage(sharedPreferences.getString("sampleId", ""));

                    editor.remove("sampleId");
                    editor.apply();

                    startActivity(new Intent(this, AuthActivity.class));
                    finish();

                    Toast.makeText(this, "جواب ها به درستی ارسال شد", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "ارسال اطلاعات با مشکل مواجه شد!", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendViaSMS() {
        // TODO : Send The Sample Via SMS Intent
    }

    private void downloadFile() {
        if (storagePermissionsGranted) {
            saveFile();
        } else {
            checkStoragePermission();
        }
    }

    private void saveFile() {
        viewModel.saveAnswerToExternal(viewModel.readAnswerFromCache(sharedPreferences.getString("sampleId", "")), sharedPreferences.getString("sampleId", ""));

        if (exit) {
            viewModel.deleteStorage(sharedPreferences.getString("sampleId", ""));
        }

        editor.remove("sampleId");
        editor.apply();

        startActivity(new Intent(this, AuthActivity.class));
        finish();

        Toast.makeText(this, "جواب ها در پوشه Download ذخیره شد.", Toast.LENGTH_SHORT).show();
    }

    private void checkStoragePermission() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                storagePermissionsGranted = true;
                saveFile();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 100);
            }
        } else {
            storagePermissionsGranted = true;
            saveFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        storagePermissionsGranted = false;

        if (requestCode == 100) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                storagePermissionsGranted = true;
                saveFile();
            }
        }
    }

}