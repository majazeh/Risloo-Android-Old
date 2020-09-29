package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.IntentCaller;
import com.majazeh.risloo.Utils.WindowDecorator;

import org.json.JSONException;

import java.util.Objects;

public class SendDocumentsActivity extends AppCompatActivity {

    // Vars
    private String file = "";
    private boolean filePermissionsGranted = false;

    // Objects
    private IntentCaller intentCaller;
    private Handler handler;

    // Widgets
    private Toolbar toolbar;
    private ImageView fileImageView;
    private TextView fileTextView;
    private LinearLayout fileLinearLayout;
    private Button sendButton;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_send_documents);

        initializer();

        detector();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        intentCaller = new IntentCaller();

        handler = new Handler();

        toolbar = findViewById(R.id.activity_send_documents_toolbar);

        fileImageView = findViewById(R.id.activity_send_documents_file_imageView);

        fileTextView = findViewById(R.id.activity_send_documents_file_textView);

        fileLinearLayout = findViewById(R.id.activity_send_documents_file_linearLayout);

        sendButton = findViewById(R.id.activity_send_documents_send_button);

        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            fileLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude_ripple_quartz);

            sendButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        fileLinearLayout.setOnClickListener(v -> {
            fileLinearLayout.setClickable(false);
            handler.postDelayed(() -> fileLinearLayout.setClickable(true), 300);

            if (filePermissionsGranted) {
                intentCaller.file(this, getResources().getString(R.string.SendDocumentsFileChooser));
            } else {
                checkFilePermission();
            }
        });

        sendButton.setOnClickListener(v -> {
            if (file.equals("")) {
                Toast.makeText(this, "فایلی انتخاب نشده است.", Toast.LENGTH_SHORT).show();
            } else {
                doWork();
            }
        });

    }
    
    private void doWork() {
//        try {
//            progressDialog.show();
//            viewModel.sendDocuments(file);
//            observeWork();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void observeWork() {
        AuthRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (AuthRepository.work.equals("sendDocuments")) {
                if (integer == 1) {
                    finish();

                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    progressDialog.dismiss();
                    observeException();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    AuthRepository.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    private void observeException() {
        if (ExceptionManager.current_exception.equals("sendDocuments")) {
            try {
                if (!ExceptionManager.errors.isNull("file")) {
                    Toast.makeText(this, "" + ExceptionManager.errors.getString("file"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkFilePermission() {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                filePermissionsGranted = true;
                intentCaller.file(this, getResources().getString(R.string.SendDocumentsFileChooser));
            } else {
                ActivityCompat.requestPermissions(this, permissions, 300);
            }
        } else {
            filePermissionsGranted = true;
            intentCaller.file(this, getResources().getString(R.string.SendDocumentsFileChooser));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 300) {
            filePermissionsGranted = false;

            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                filePermissionsGranted = true;
                intentCaller.file(this, getResources().getString(R.string.SendDocumentsFileChooser));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)     {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 300) {
                Uri uri = Objects.requireNonNull(data).getData();

                file = Objects.requireNonNull(uri).getPath();

                ImageViewCompat.setImageTintList(fileImageView, null);
                fileImageView.setImageDrawable(getResources().getDrawable(R.drawable.soc_adobe));

                fileTextView.setText(file);
                fileTextView.setTextColor(getResources().getColor(R.color.Nero));
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == 300)
                Toast.makeText(this, "فایلی انتخاب نشده است.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}