package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.IndexAdapter;
import com.majazeh.risloo.Views.Fragments.DescriptionFragment;
import com.majazeh.risloo.Views.Fragments.PicturePictoralFragment;
import com.majazeh.risloo.Views.Fragments.PictureOptionalFragment;
import com.majazeh.risloo.Views.Fragments.PictureTypingFragment;
import com.majazeh.risloo.Views.Fragments.PrerequisiteFragment;
import com.majazeh.risloo.Views.Fragments.TextPictoralFragment;
import com.majazeh.risloo.Views.Fragments.TextOptionalFragment;
import com.majazeh.risloo.Views.Fragments.TextTypingFragment;

import org.json.JSONException;

import java.util.Objects;

public class SampleActivity extends AppCompatActivity {

    // ViewModels
    public SampleViewModel viewModel;

    // Adapters
    private IndexAdapter adapter;

    // Vars
    public String sampleId = "";
    private boolean showPreByAnswer = false;

    // Objects
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Animation animSlideIn, animSlideOut;
    private ClickableSpan retrySpan;

    // Widgets
    private LinearLayout progressLinearLayout;
    private RecyclerView indexRecyclerView;
    private ProgressBar progressBar;
    private Button closeButton, cancelButton;
    private TextView retryTextView;
    private ImageView retryImageView;
    private RelativeLayout mainLayout;
    private LinearLayout retryLayout, loadingLayout;
    public Dialog closeDialog, cancelDialog, progressDialog;
    private TextView closeDialogAnswered, closeDialogUnAnswered, closeDialogPositive, closeDialogNegative, cancelDialogTitle, cancelDialogDescription, cancelDialogPositive, cancelDialogNegative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_sample);

        initializer();

        detector();

        listener();

        launchProcess("getSingle");
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        viewModel = new ViewModelProvider(this).get(SampleViewModel.class);

        adapter = new IndexAdapter(this);

        handler = new Handler();

        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        sampleId = sharedPreferences.getString("sampleId", "");

        showPreByAnswer = viewModel.answeredSize(sampleId) == 0;

        animSlideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        animSlideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_top);

        progressLinearLayout = findViewById(R.id.activity_sample_progress_linearLayout);

        indexRecyclerView = findViewById(R.id.activity_sample_recyclerView);
        indexRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("horizontalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._8sdp), (int) getResources().getDimension(R.dimen._16sdp)));
        indexRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        indexRecyclerView.setHasFixedSize(true);

        progressBar = findViewById(R.id.activity_sample_progressBar);

        closeButton = findViewById(R.id.activity_sample_close_button);
        cancelButton = findViewById(R.id.activity_sample_cancel_button);

        retryImageView = findViewById(R.id.activity_sample_retry_imageView);

        retryTextView = findViewById(R.id.activity_sample_retry_textView);
        retryTextView.setMovementMethod(LinkMovementMethod.getInstance());

        mainLayout = findViewById(R.id.activity_sample_mainLayout);
        retryLayout = findViewById(R.id.activity_sample_retryLayout);
        loadingLayout = findViewById(R.id.activity_sample_loadingLayout);

        closeDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(closeDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        closeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        closeDialog.setContentView(R.layout.dialog_close);
        closeDialog.setCancelable(true);
        cancelDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(cancelDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        cancelDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancelDialog.setContentView(R.layout.dialog_action);
        cancelDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(closeDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        closeDialog.getWindow().setAttributes(layoutParams);
        WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams();
        layoutParams2.copyFrom(cancelDialog.getWindow().getAttributes());
        layoutParams2.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams2.height = WindowManager.LayoutParams.WRAP_CONTENT;
        cancelDialog.getWindow().setAttributes(layoutParams2);

        closeDialogAnswered = closeDialog.findViewById(R.id.dialog_close_answered_textView);
        closeDialogUnAnswered = closeDialog.findViewById(R.id.dialog_close_unanswered_textView);
        closeDialogPositive = closeDialog.findViewById(R.id.dialog_close_positive_textView);
        closeDialogNegative = closeDialog.findViewById(R.id.dialog_close_negative_textView);
        cancelDialogTitle = cancelDialog.findViewById(R.id.dialog_action_title_textView);
        cancelDialogTitle.setText(getResources().getString(R.string.SampleCancelDialogTitle));
        cancelDialogDescription = cancelDialog.findViewById(R.id.dialog_action_description_textView);
        cancelDialogDescription.setText(getResources().getString(R.string.SampleCancelDialogDescription));
        cancelDialogPositive = cancelDialog.findViewById(R.id.dialog_action_positive_textView);
        cancelDialogPositive.setText(getResources().getString(R.string.SampleCancelDialogPositive));
        cancelDialogPositive.setTextColor(getResources().getColor(R.color.VioletRed));
        cancelDialogNegative = cancelDialog.findViewById(R.id.dialog_action_negative_textView);
        cancelDialogNegative.setText(getResources().getString(R.string.SampleCancelDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            closeButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
            cancelButton.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude_ripple_quartz);

            closeDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            closeDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            cancelDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            cancelDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    private void listener() {
        retrySpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                launchProcess("getSingle");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.PrimaryDark));
                textPaint.setUnderlineText(false);
            }
        };

        closeButton.setOnClickListener(v -> {
            closeButton.setClickable(false);
            handler.postDelayed(() -> closeButton.setClickable(true), 300);

            onNextPressed();
        });

        cancelButton.setOnClickListener(v -> {
            cancelButton.setClickable(false);
            handler.postDelayed(() -> cancelButton.setClickable(true), 300);
            cancelDialog.show();
        });

        closeDialogPositive.setOnClickListener(v -> {
            closeDialogPositive.setClickable(false);
            handler.postDelayed(() -> closeDialogPositive.setClickable(true), 300);
            closeDialog.dismiss();

            launchProcess("sendAnswers");
        });

        cancelDialogPositive.setOnClickListener(v -> {
            cancelDialogPositive.setClickable(false);
            handler.postDelayed(() -> cancelDialogPositive.setClickable(true), 300);
            cancelDialog.dismiss();

            finish();
        });

        closeDialogNegative.setOnClickListener(v -> {
            closeDialogNegative.setClickable(false);
            handler.postDelayed(() -> closeDialogNegative.setClickable(true), 300);
            closeDialog.dismiss();
        });

        cancelDialogNegative.setOnClickListener(v -> {
            cancelDialogNegative.setClickable(false);
            handler.postDelayed(() -> cancelDialogNegative.setClickable(true), 300);
            cancelDialog.dismiss();
        });

        closeDialog.setOnCancelListener(dialog -> closeDialog.dismiss());

        cancelDialog.setOnCancelListener(dialog -> cancelDialog.dismiss());
    }

    private void loadFragment(Fragment fragment, int enterAnim, int exitAnim) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(enterAnim, exitAnim);
        transaction.replace(R.id.activity_sample_frameLayout, fragment);
        transaction.commit();
    }

    public void showFragment() {
        switch (SampleRepository.theory) {
            case "description":
                setButtonText();
                setProgress();

                loadFragment(new DescriptionFragment(this), R.anim.fade_in, R.anim.fade_out);
                break;
            case "prerequisite":
                setButtonText();
                setProgress();

                loadFragment(new PrerequisiteFragment(this), R.anim.fade_in, R.anim.fade_out);
                break;
            case "sample":
                setButtonText();
                setProgress();

                switch (viewModel.getType(viewModel.getIndex())) {
                    case "textTyping":
                        loadFragment(new TextTypingFragment(this), R.anim.fade_in, R.anim.fade_out);
                        break;
                    case "optional":
                        loadFragment(new TextOptionalFragment(this), R.anim.fade_in, R.anim.fade_out);
                        break;
                    case "textPictoral":
                        loadFragment(new TextPictoralFragment(this), R.anim.fade_in, R.anim.fade_out);
                        break;
                    case "pictureTyping":
                        loadFragment(new PictureTypingFragment(this), R.anim.fade_in, R.anim.fade_out);
                        break;
                    case "pictureOptional":
                        loadFragment(new PictureOptionalFragment(this), R.anim.fade_in, R.anim.fade_out);
                        break;
                    case "picturePictoral":
                        loadFragment(new PicturePictoralFragment(this), R.anim.fade_in, R.anim.fade_out);
                        break;
                }
                break;
        }
    }

    private void setRetryLayout(String type) {
        if (type.equals("error")) {
            retryImageView.setImageResource(R.drawable.illu_error);
            retryTextView.setText(StringManager.clickable(getResources().getString(R.string.AppError), 21, 30, retrySpan));
        } else if (type.equals("connection")) {
            retryImageView.setImageResource(R.drawable.illu_connection);
            retryTextView.setText(StringManager.clickable(getResources().getString(R.string.AppConnection), 17, 26, retrySpan));
        }
    }

    private void setButtonText() {
        switch (SampleRepository.theory) {
            case "description":
            case "prerequisite":
                if (!closeButton.getText().toString().equals(R.string.SampleNext)) {
                    closeButton.setText(getResources().getString(R.string.SampleNext));
                }
                break;
            case "sample":
                if (!closeButton.getText().toString().equals(R.string.SampleClose)) {
                    closeButton.setText(getResources().getString(R.string.SampleClose));
                }
                break;
        }
    }

    public void setProgress() {
        switch (SampleRepository.theory) {
            case "description":
            case "prerequisite":
                if (progressLinearLayout.getVisibility() == View.VISIBLE) {
                    progressLinearLayout.startAnimation(animSlideOut);
                    handler.postDelayed(() -> progressLinearLayout.setVisibility(View.GONE), 200);
                }
                break;
            case "sample":
                if (progressLinearLayout.getVisibility() != View.VISIBLE) {
                    progressLinearLayout.startAnimation(animSlideIn);
                    progressLinearLayout.setVisibility(View.VISIBLE);
                }
                progressBar.setMax(viewModel.getSize());
                progressBar.setProgress(viewModel.answeredSize(sampleId));

                closeDialogAnswered.setText(String.valueOf(viewModel.answeredSize(sampleId)));
                closeDialogUnAnswered.setText(String.valueOf(viewModel.getSize() - viewModel.answeredSize(sampleId)));

                indexRecyclerView.scrollToPosition(viewModel.getIndex());

                adapter.setIndex(viewModel.readSampleAnswerFromCache(sampleId), viewModel);
                adapter.notifyDataSetChanged();

                break;
        }
    }

    private void setRecyclerView() {
        adapter.setIndex(viewModel.readSampleAnswerFromCache(sampleId), viewModel);
        indexRecyclerView.setAdapter(adapter);

        viewModel.setIndex(viewModel.firstUnAnswered(sampleId));
    }

    private void launchProcess(String method) {
        switch (method) {
            case "getSingle":
                try {
                    viewModel.sample(sampleId);
                    observeWorkSample();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "sendAnswers":
                try {
                    progressDialog.show();
                    viewModel.sendAnswers(sampleId);
                    observeWorkAnswer();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "sendPrerequisite":
                try {
                    PrerequisiteFragment fragment = ((PrerequisiteFragment) getSupportFragmentManager().findFragmentById(R.id.activity_sample_frameLayout));
                    if (fragment != null) {
                        if (fragment.prerequisites().isEmpty()) {
                            ExceptionGenerator.getException(false, 0, null, "FillOneException", "sample");
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        } else {
                            if (fragment.adapter.inputHandleEditText.getInput() != null && fragment.adapter.inputHandleEditText.getInput().hasFocus()) {
                                fragment.adapter.inputHandleEditText.clear(this, fragment.adapter.inputHandleEditText.getInput());
                            }

                            progressDialog.show();
                            viewModel.sendPrerequisite(sampleId, fragment.prerequisites());
                            observeWorkAnswer();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void observeWorkSample()    {
        SampleRepository.workStateSample.observe(this, integer -> {
            if (SampleRepository.work.equals("getSingle")) {
                if (integer == 1) {
                    if (viewModel.hasPrerequisiteAnswerStorage(sampleId)) {
                        if (viewModel.checkPrerequisiteAnswerStorage(sampleId) || showPreByAnswer) {
                            // Show Description
                            showPreByAnswer = false;

                            SampleRepository.theory = "description";
                            showFragment();

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        } else {
                            if (viewModel.firstUnAnswered(sampleId) == -1) {
                                if (viewModel.answeredSize(sampleId) == viewModel.getSize()) {
                                    // Answered All Questions

                                    Intent intent = (new Intent(this, OutroActivity.class));
                                    intent.putExtra("sampleId", sampleId);

                                    startActivity(intent);
                                    finish();

                                    SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                                } else {
                                    // Show Sample

                                    setRecyclerView();

                                    SampleRepository.theory = "sample";
                                    showFragment();

                                    loadingLayout.setVisibility(View.GONE);
                                    retryLayout.setVisibility(View.GONE);
                                    mainLayout.setVisibility(View.VISIBLE);

                                    SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                                }
                            } else {
                                // Show Sample

                                setRecyclerView();

                                SampleRepository.theory = "sample";
                                showFragment();

                                loadingLayout.setVisibility(View.GONE);
                                retryLayout.setVisibility(View.GONE);
                                mainLayout.setVisibility(View.VISIBLE);

                                SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                            }
                        }
                    } else {
                        // Show Description

                        SampleRepository.theory = "description";
                        showFragment();

                        loadingLayout.setVisibility(View.GONE);
                        retryLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    }
                } else if (integer == 0) {
                    if (viewModel.getItems() == null) {
                        if (viewModel.hasPrerequisiteAnswerStorage(sampleId)) {
                            if (viewModel.checkPrerequisiteAnswerStorage(sampleId)) {
                                // Show Description

                                SampleRepository.theory = "description";
                                showFragment();

                                loadingLayout.setVisibility(View.GONE);
                                retryLayout.setVisibility(View.GONE);
                                mainLayout.setVisibility(View.VISIBLE);

                                SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                            } else {
                                // Show Sample

                                setRecyclerView();

                                SampleRepository.theory = "sample";
                                showFragment();

                                loadingLayout.setVisibility(View.GONE);
                                retryLayout.setVisibility(View.GONE);
                                mainLayout.setVisibility(View.VISIBLE);

                                SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                            }
                        } else {
                            // Sample is Empty And Error

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.VISIBLE);
                            mainLayout.setVisibility(View.GONE);

                            setRetryLayout("error");

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        }
                    } else {
                        if (viewModel.firstUnAnswered(sampleId) == -1) {
                            // Answered All Questions

                            Intent intent = (new Intent(this, OutroActivity.class));
                            intent.putExtra("sampleId", sampleId);

                            startActivity(intent);
                            finish();

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        } else {
                            // Show Sample

                            setRecyclerView();

                            SampleRepository.theory = "sample";
                            showFragment();

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        }
                    }
                } else if (integer == -2) {
                    if (viewModel.getItems() == null) {
                        if (viewModel.hasPrerequisiteAnswerStorage(sampleId)) {
                            if (viewModel.checkPrerequisiteAnswerStorage(sampleId)) {
                                // Show Description

                                SampleRepository.theory = "description";
                                showFragment();

                                loadingLayout.setVisibility(View.GONE);
                                retryLayout.setVisibility(View.GONE);
                                mainLayout.setVisibility(View.VISIBLE);

                                SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                            } else {
                                // Show Sample

                                setRecyclerView();

                                SampleRepository.theory = "sample";
                                showFragment();

                                loadingLayout.setVisibility(View.GONE);
                                retryLayout.setVisibility(View.GONE);
                                mainLayout.setVisibility(View.VISIBLE);

                                SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                            }
                        } else {
                            // Sample is Empty And Connection

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.VISIBLE);
                            mainLayout.setVisibility(View.GONE);

                            setRetryLayout("connection");

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        }
                    } else {
                        if (viewModel.firstUnAnswered(sampleId) == -1) {
                            // Answered All Questions

                            Intent intent = (new Intent(this, OutroActivity.class));
                            intent.putExtra("sampleId", sampleId);

                            startActivity(intent);
                            finish();

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        } else {
                            // Show Sample

                            setRecyclerView();

                            SampleRepository.theory = "sample";
                            showFragment();

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        }
                    }
                }else if (integer == -3){
                    setResult(RESULT_OK, null);
                    finish();
                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                }

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            } else if (SampleRepository.work.equals("close")) {
                if (integer == 1) {
                    setResult(RESULT_OK, null);
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
            }
        });
    }

    private void observeWorkAnswer() {
        SampleRepository.workStateAnswer.observe((LifecycleOwner) this, integer -> {
            if (SampleRepository.work.equals("sendPrerequisite")) {
                if (integer == 1) {
                    launchProcess("getSingle");

                    SampleRepository.workStateAnswer.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    launchProcess("getSingle");

                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateAnswer.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    launchProcess("getSingle");

                    Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateAnswer.removeObservers((LifecycleOwner) this);
                }
            } else if (SampleRepository.work.equals("sendAnswers")) {
                if (integer == 1){
                    try {
                        viewModel.close(sampleId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    observeWorkSample();
                }else if (integer == -2){
                    Intent intent = (new Intent(this, OutroActivity.class));
                    intent.putExtra("sampleId", sampleId);

                    startActivity(intent);
                    finish();

                    progressDialog.dismiss();
                    SampleRepository.workStateAnswer.removeObservers((LifecycleOwner) this);
                }
            }
            if (integer != -1){
                SampleRepository.workStateAnswer.removeObservers((LifecycleOwner) this);
            }
        });
    }

    private void onNextPressed() {
        switch (SampleRepository.theory) {
            case "description":
                SampleRepository.theory = "prerequisite";
                showFragment();
                break;
            case "prerequisite":
                launchProcess("sendPrerequisite");
                break;
            case "sample":
                closeDialog.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        switch (SampleRepository.theory) {
            case "sample":
                SampleRepository.theory = "prerequisite";
                showFragment();
                break;
            case "prerequisite":
                SampleRepository.theory = "description";
                showFragment();
                break;
            case "description":
                cancelDialog.show();
                break;
        }
    }

}