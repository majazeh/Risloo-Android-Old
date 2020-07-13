package com.majazeh.risloo.Views.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.WindowDecorator;

public class OutroActivity extends AppCompatActivity {

    // Vars
    private String work = "";

    // Objects
    private Handler handler;

    // Widgets
    private Button internetButton, smsButton, downloadButton, laterButton;
    private TextView outroDialogTitle, outroDialogDescription, outroDialogPositive, outroDialogNegative;
    private Dialog outroDialog;

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
        handler = new Handler();

        internetButton = findViewById(R.id.activity_outro_internet_button);
        smsButton = findViewById(R.id.activity_outro_sms_button);
        downloadButton = findViewById(R.id.activity_outro_download_button);
        laterButton = findViewById(R.id.activity_outro_later_button);

        outroDialog = new Dialog(this, R.style.DialogTheme);
        outroDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        outroDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        outroDialog.setContentView(R.layout.dialog_action);
        outroDialog.setCancelable(true);

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
                downloadFile();
            }
        });

        outroDialogNegative.setOnClickListener(v -> {
            outroDialogNegative.setClickable(false);
            handler.postDelayed(() -> outroDialogNegative.setClickable(true), 1000);
            outroDialog.dismiss();
        });

        outroDialog.setOnCancelListener(dialog -> {
            outroDialog.dismiss();
        });
    }

    private void sendViaInternet() {
        // TODO : Send The Sample Via Internet Call
    }

    private void sendViaSMS() {
        // TODO : Send The Sample Via SMS Intent
    }

    private void downloadFile() {
        // TODO : Download Sample Answer File
    }

}