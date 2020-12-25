package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Models.Repositories.SessionRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Managers.PathManager;
import com.majazeh.risloo.Utils.Managers.PermissionManager;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.ViewModels.SessionViewModel;

import org.json.JSONException;

import java.util.Objects;

public class CreatePracticeActivity extends AppCompatActivity {

    // ViewModels
    private SessionViewModel sessionViewModel;

    // Vars
    private String sessionId = "", title = "", content = "", practice = "";
    private boolean createPracticeException = false;

    // Objects
    private Bundle extras;
    private Handler handler;
    private ControlEditText controlEditText;
    private PathManager pathManager;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private EditText titleEditText, contentEditText;
    private LinearLayout createPracticeLinearLayout;
    private ImageView createPracticeImageView;
    private TextView suffixTextView, createPracticeTextView;
    private Button sendButton;
    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_create_practice);

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

    private void initializer() {
        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);

        handler = new Handler();

        extras = getIntent().getExtras();

        controlEditText = new ControlEditText();

        pathManager = new PathManager();

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.createPracticeTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        titleEditText = findViewById(R.id.activity_create_practice_title_editText);
        contentEditText = findViewById(R.id.activity_create_practice_content_editText);

        createPracticeLinearLayout = findViewById(R.id.activity_create_practice_file_linearLayout);

        createPracticeImageView = findViewById(R.id.activity_create_practice_file_imageView);

        suffixTextView = findViewById(R.id.activity_create_practice_suffix_textView);
        createPracticeTextView = findViewById(R.id.activity_create_practice_file_textView);

        sendButton = findViewById(R.id.activity_create_practice_send_button);

        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            createPracticeLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude_ripple_quartz);

            sendButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbarImageView.setOnClickListener(v -> {
            toolbarImageView.setClickable(false);
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 250);

            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        titleEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                if (!titleEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(titleEditText);
                    controlEditText.select(titleEditText);
                }
            }
            return false;
        });

        contentEditText.setOnTouchListener((v, event) -> {
            if(MotionEvent.ACTION_UP == event.getAction()) {
                if (!contentEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(contentEditText);
                    controlEditText.select(contentEditText);
                }
            }
            return false;
        });

        createPracticeLinearLayout.setOnClickListener(v -> {
            createPracticeLinearLayout.setClickable(false);
            handler.postDelayed(() -> createPracticeLinearLayout.setClickable(true), 250);

            if (createPracticeException) {
                clearException("practice");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (PermissionManager.filePermission(this)) {
                IntentManager.file(this);
            }
        });

        sendButton.setOnClickListener(v -> {
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (titleEditText.length() == 0) {
                controlEditText.error(this, titleEditText);
            }
            if (contentEditText.length() == 0) {
                controlEditText.error(this, contentEditText);
            }
            if (practice.equals("")) {
                errorException("practice");
            }

            if (titleEditText.length() != 0 && contentEditText.length() != 0 && !practice.equals("")) {
                controlEditText.clear(this, titleEditText);
                controlEditText.clear(this, contentEditText);
                clearException("practice");

                doWork();
            }
        });
    }

    private void setData() {
        if (extras.getString("id") != null)
            sessionId = extras.getString("id");
    }

    private void errorException(String type) {
        switch (type) {
            case "title":
                titleEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "content":
                contentEditText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
            case "practice":
                createPracticeException = true;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    createPracticeLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_violetred20p_ripple_violetred);
                } else {
                    createPracticeLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_violetred20p);
                }
                break;
        }
    }

    private void clearException(String type) {
        switch (type) {
            case "practice":
                createPracticeException = false;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    createPracticeLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude_ripple_quartz);
                } else {
                    createPracticeLinearLayout.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude);
                }
                break;
        }
    }

    private void doWork() {
        title = titleEditText.getText().toString().trim();
        content = contentEditText.getText().toString().trim();

        try {
            progressDialog.show();

            sessionViewModel.createPractice(sessionId,title, content, practice);
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork() {
        SessionRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (SessionRepository.work.equals("createPractice")) {
                if (integer == 1) {
                    setResult(RESULT_OK, null);
                    finish();

                    progressDialog.dismiss();
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    SessionRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    progressDialog.dismiss();
                    observeException();
                    SessionRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    SessionRepository.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    private void observeException() {
        if (ExceptionGenerator.current_exception.equals("createPractice")) {
            String exceptionToast = "";

            if (!ExceptionGenerator.errors.isNull("title")) {
                errorException("title");
                exceptionToast = ExceptionGenerator.getErrorBody("title");
            }
            if (!ExceptionGenerator.errors.isNull("content")) {
                errorException("content");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("content");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("content"));
                }
            }
            if (!ExceptionGenerator.errors.isNull("practice")) {
                errorException("practice");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("practice");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("practice"));
                }
            }
            Toast.makeText(this, exceptionToast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                IntentManager.file(this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                Uri uri = Objects.requireNonNull(data).getData();

                practice = pathManager.getLocalPath(this, uri);

                createPracticeTextView.setText(StringManager.substring(practice, '/'));
                createPracticeTextView.setTextColor(getResources().getColor(R.color.Nero));

                createPracticeImageView.setVisibility(View.GONE);
                suffixTextView.setVisibility(View.VISIBLE);

                switch (StringManager.substring(practice, '.')) {
                    case "png":
                        suffixTextView.setText(getResources().getString(R.string.createPracticeSuffixPNG));
                        break;
                    case "jpg":
                        suffixTextView.setText(getResources().getString(R.string.createPracticeSuffixJPG));
                        break;
                    case "jpeg":
                        suffixTextView.setText(getResources().getString(R.string.createPracticeSuffixJPEG));
                        break;
                    case "gif":
                        suffixTextView.setText(getResources().getString(R.string.createPracticeSuffixGIF));
                        break;
                    case "doc":
                        suffixTextView.setText(getResources().getString(R.string.createPracticeSuffixDOC));
                        break;
                    case "pdf":
                        suffixTextView.setText(getResources().getString(R.string.createPracticeSuffixPDF));
                        break;
                    case "mp4":
                        suffixTextView.setText(getResources().getString(R.string.createPracticeSuffixMP4));
                        break;
                    default:
                        suffixTextView.setText(StringManager.substring(practice, '.'));
                        break;
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == 100) {
                ExceptionGenerator.getException(false, 0, null, "FileException");
                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);

        FileManager.deleteFolderFromCache(this, "documents");
    }

}