package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Intent;
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

import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Managers.PermissionManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class OutroActivity extends AppCompatActivity {

    // ViewModels
    private SampleViewModel sampleViewModel;

    // Vars
    private String sampleId = "", work = "";

    // Objects
    private Bundle extras;
    private Handler handler;

    // Widgets
    private Button internetButton, smsButton, downloadButton, laterButton;
    private Dialog outroDialog, progressDialog;
    private TextView outroDialogTitle, outroDialogDescription, outroDialogPositive, outroDialogNegative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_outro);

        initializer();

        detector();

        listener();

        setData();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            internetButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
            smsButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
            downloadButton.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude_ripple_quartz);
            laterButton.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude_ripple_quartz);

            outroDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            outroDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    private void initializer() {
        sampleViewModel = new ViewModelProvider(this).get(SampleViewModel.class);

        extras = getIntent().getExtras();

        handler = new Handler();

        internetButton = findViewById(R.id.activity_outro_internet_button);
        smsButton = findViewById(R.id.activity_outro_sms_button);
        downloadButton = findViewById(R.id.activity_outro_download_button);
        laterButton = findViewById(R.id.activity_outro_later_button);

        outroDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(outroDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        outroDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        outroDialog.setContentView(R.layout.dialog_action);
        outroDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParamsOutroDialog = new WindowManager.LayoutParams();
        layoutParamsOutroDialog.copyFrom(outroDialog.getWindow().getAttributes());
        layoutParamsOutroDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsOutroDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        outroDialog.getWindow().setAttributes(layoutParamsOutroDialog);

        outroDialogTitle = outroDialog.findViewById(R.id.dialog_action_title_textView);
        outroDialogDescription = outroDialog.findViewById(R.id.dialog_action_description_textView);
        outroDialogPositive = outroDialog.findViewById(R.id.dialog_action_positive_textView);
        outroDialogPositive.setTextColor(getResources().getColor(R.color.PrimaryDark));
        outroDialogNegative = outroDialog.findViewById(R.id.dialog_action_negative_textView);
    }

    private void listener() {
        internetButton.setOnClickListener(v -> {
            internetButton.setClickable(false);
            handler.postDelayed(() -> internetButton.setClickable(true), 250);

            outroDialogTitle.setText(getResources().getString(R.string.OutroInternetDialogTitle));
            outroDialogDescription.setText(getResources().getString(R.string.OutroInternetDialogDescription));
            outroDialogPositive.setText(getResources().getString(R.string.OutroInternetDialogPositive));
            outroDialogNegative.setText(getResources().getString(R.string.OutroInternetDialogNegative));

            outroDialog.show();

            work = "internet";
        });

        smsButton.setOnClickListener(v -> {
            smsButton.setClickable(false);
            handler.postDelayed(() -> smsButton.setClickable(true), 250);

            outroDialogTitle.setText(getResources().getString(R.string.OutroSMSDialogTitle));
            outroDialogDescription.setText(getResources().getString(R.string.OutroSMSDialogDescription));
            outroDialogPositive.setText(getResources().getString(R.string.OutroSMSDialogPositive));
            outroDialogNegative.setText(getResources().getString(R.string.OutroSMSDialogNegative));

            outroDialog.show();

            work = "sms";
        });

        downloadButton.setOnClickListener(v -> {
            downloadButton.setClickable(false);
            handler.postDelayed(() -> downloadButton.setClickable(true), 250);

            outroDialogTitle.setText(getResources().getString(R.string.OutroDownloadDialogTitle));
            outroDialogDescription.setText(getResources().getString(R.string.OutroDownloadDialogDescription));
            outroDialogPositive.setText(getResources().getString(R.string.OutroDownloadDialogPositive));
            outroDialogNegative.setText(getResources().getString(R.string.OutroDownloadDialogNegative));

            outroDialog.show();

            work = "download";
        });

        laterButton.setOnClickListener(v -> {
            laterButton.setClickable(false);
            handler.postDelayed(() -> laterButton.setClickable(true), 250);

            finish();
        });

        outroDialogPositive.setOnClickListener(v -> {
            outroDialogPositive.setClickable(false);
            handler.postDelayed(() -> outroDialogPositive.setClickable(true), 250);
            outroDialog.dismiss();

            switch (work) {
                case "internet":
                    sendViaInternet();
                    break;
                case "sms":
                    sendViaSMS();
                    break;
                case "download":
                    downloadFile();
                    break;
            }
        });

        outroDialogNegative.setOnClickListener(v -> {
            outroDialogNegative.setClickable(false);
            handler.postDelayed(() -> outroDialogNegative.setClickable(true), 250);
            outroDialog.dismiss();
        });

        outroDialog.setOnCancelListener(dialog -> outroDialog.dismiss());
    }

    private void setData() {
        sampleId = extras.getString("sampleId");
    }

    private void sendViaInternet() {
        try {
            progressDialog.show();

            sampleViewModel.sendAllAnswers(sampleId);
            observeWork("answer");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendViaSMS() {
        String body = "sms_body";
        StringBuilder result = new StringBuilder();
        String number = "+989195934528";

        JSONArray jsonArray = FileManager.readArrayFromCache(this, "Answers" + "/" + sampleId);

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                result.append(jsonArray.getJSONObject(i).getString("answer"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        IntentManager.sendTo(this, number, body, result.toString());
    }

    private void downloadFile() {
        if (PermissionManager.storagePermission(this)) {
            saveFile();
        }
    }

    private void saveFile() {
        FileManager.writeAnswersToExternal(this, FileManager.readArrayFromCache(this, "Answers" + "/" + sampleId), sampleId);
        changeStatus();
        finish();

        ExceptionGenerator.getException(false, 0, null, "SavedToDownloadException");
        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
    }

    private void changeStatus() {
        try {
            JSONObject jsonObject = FileManager.readObjectFromCache(this, "Samples" + "/" + sampleId);
            JSONObject data = jsonObject.getJSONObject("data");

            data.put("status", "sent");
            jsonObject.put("data", data);

            FileManager.writeObjectToCache(this, jsonObject, "Samples" + "/" + sampleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork(String method) {
        switch (method) {
            case "sample":
                SampleRepository.workStateSample.observe(this, integer -> {
                    if (integer == 1) {
                        finish();

                        progressDialog.dismiss();
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        progressDialog.dismiss();
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        progressDialog.dismiss();
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    }
                });
                break;
            case "answer":
                SampleRepository.workStateAnswer.observe(this, integer -> {
                    if (integer == 1) {
                        try {
                            changeStatus();
                            sampleViewModel.close(sampleId);
                            observeWork("sample");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        SampleRepository.workStateAnswer.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        progressDialog.dismiss();
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateAnswer.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        progressDialog.dismiss();
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateAnswer.removeObservers((LifecycleOwner) this);
                    }
                });
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                saveFile();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                changeStatus();
                finish();
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == 200) {
                ExceptionGenerator.getException(false, 0, null, "SendToException");
                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
            }
        }
    }

}