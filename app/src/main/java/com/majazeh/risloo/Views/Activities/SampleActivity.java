package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.ViewModels.SampleViewModelFactory;
import com.majazeh.risloo.Views.Adapters.IndexAdapter;
import com.majazeh.risloo.Views.Fragments.DescriptionFragment;
import com.majazeh.risloo.Views.Fragments.PFPFragment;
import com.majazeh.risloo.Views.Fragments.PFTFragment;
import com.majazeh.risloo.Views.Fragments.PPFragment;
import com.majazeh.risloo.Views.Fragments.PrerequisiteFragment;
import com.majazeh.risloo.Views.Fragments.TFPFragment;
import com.majazeh.risloo.Views.Fragments.TFTFragment;
import com.majazeh.risloo.Views.Fragments.TPFragment;

import org.json.JSONException;

public class SampleActivity extends AppCompatActivity {

    // ViewModels
    public static SampleViewModel viewModel;

    // Adapters
    private IndexAdapter adapter;

    // Vars
    private String showProgress = "-1";

    // Objects
    private Handler handler;
    private Animation animSlideIn, animSlideOut;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Button finishButton, cancelButton;
    private Dialog finishDialog, cancelDialog;
    private TextView finishDialogTitle, finishDialogDescription, finishDialogPositive, finishDialogNegative, cancelDialogTitle, cancelDialogDescription, cancelDialogPositive, cancelDialogNegative;
    public Dialog progressDialog, loadingDialog;
    public LinearLayout progressLinearLayout, buttonLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_sample);

        initializer();

        detector();

        listener();

        launchProcess();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        viewModel = ViewModelProviders.of(this, new SampleViewModelFactory(getApplication(), sharedPreferences.getString("sampleId", ""))).get(SampleViewModel.class);


        adapter = new IndexAdapter(this, viewModel);

        handler = new Handler();

        animSlideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        animSlideOut = AnimationUtils.loadAnimation(this, R.anim.slide_out_top);

        progressLinearLayout = findViewById(R.id.activity_sample_progress_linearLayout);
        buttonLinearLayout = findViewById(R.id.activity_sample_button_linearLayout);

        recyclerView = findViewById(R.id.activity_sample_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorator("horizontalLayout", (int) getResources().getDimension(R.dimen._18sdp), (int) getResources().getDimension(R.dimen._9sdp), (int) getResources().getDimension(R.dimen._18sdp)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);

        progressBar = findViewById(R.id.activity_sample_progressBar);

        finishButton = findViewById(R.id.activity_sample_finish_button);
        cancelButton = findViewById(R.id.activity_sample_cancel_button);

        finishDialog = new Dialog(this, R.style.DialogTheme);
        finishDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        finishDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        finishDialog.setContentView(R.layout.dialog_action);
        finishDialog.setCancelable(true);
        cancelDialog = new Dialog(this, R.style.DialogTheme);
        cancelDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        cancelDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancelDialog.setContentView(R.layout.dialog_action);
        cancelDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
        loadingDialog = new Dialog(this, 0);
        loadingDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loadingDialog.getWindow().setDimAmount(0);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(finishDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        finishDialog.getWindow().setAttributes(layoutParams);
        WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams();
        layoutParams2.copyFrom(cancelDialog.getWindow().getAttributes());
        layoutParams2.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams2.height = WindowManager.LayoutParams.WRAP_CONTENT;
        cancelDialog.getWindow().setAttributes(layoutParams2);

        finishDialogTitle = finishDialog.findViewById(R.id.dialog_action_title_textView);
        finishDialogTitle.setText(getResources().getString(R.string.SampleFinishDialogTitle));
        finishDialogDescription = finishDialog.findViewById(R.id.dialog_action_description_textView);
        finishDialogDescription.setText(getResources().getString(R.string.SampleFinishDialogDescription));
        finishDialogPositive = finishDialog.findViewById(R.id.dialog_action_positive_textView);
        finishDialogPositive.setText(getResources().getString(R.string.SampleFinishDialogPositive));
        finishDialogPositive.setTextColor(getResources().getColor(R.color.PrimaryDark));
        finishDialogNegative = finishDialog.findViewById(R.id.dialog_action_negative_textView);
        finishDialogNegative.setText(getResources().getString(R.string.SampleFinishDialogNegative));
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
            finishButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
            cancelButton.setBackgroundResource(R.drawable.draw_16sdp_solid_solitude_ripple_quartz);

            finishDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            finishDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            cancelDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            cancelDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    private void listener() {
        finishButton.setOnClickListener(v -> {
            finishButton.setClickable(false);
            handler.postDelayed(() -> finishButton.setClickable(true), 500);

            setAction();
        });

        cancelButton.setOnClickListener(v -> {
            cancelButton.setClickable(false);
            handler.postDelayed(() -> cancelButton.setClickable(true), 1000);
            cancelDialog.show();
        });

        finishDialogPositive.setOnClickListener(v -> {
            finishDialogPositive.setClickable(false);
            handler.postDelayed(() -> finishDialogPositive.setClickable(true), 1000);
            finishDialog.dismiss();

            closeSample();
        });

        cancelDialogPositive.setOnClickListener(v -> {
            cancelDialogPositive.setClickable(false);
            handler.postDelayed(() -> cancelDialogPositive.setClickable(true), 1000);
            cancelDialog.dismiss();

            finish();
        });

        finishDialogNegative.setOnClickListener(v -> {
            finishDialogNegative.setClickable(false);
            handler.postDelayed(() -> finishDialogNegative.setClickable(true), 1000);
            finishDialog.dismiss();
        });

        cancelDialogNegative.setOnClickListener(v -> {
            cancelDialogNegative.setClickable(false);
            handler.postDelayed(() -> cancelDialogNegative.setClickable(true), 1000);
            cancelDialog.dismiss();
        });

        finishDialog.setOnCancelListener(dialog -> finishDialog.dismiss());

        cancelDialog.setOnCancelListener(dialog -> cancelDialog.dismiss());
    }

    public void loadFragment(Fragment fragment, int enterAnim, int exitAnim) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(enterAnim, exitAnim);
        transaction.replace(R.id.activity_sample_frameLayout, fragment);
        transaction.commit();
    }

    public void showFragment() throws JSONException {
        showButtons();
        setText();

        switch (SampleRepository.theory) {
            case "description":
                loadFragment(new DescriptionFragment(this, viewModel), R.anim.fade_in, R.anim.fade_out);
                break;
            case "prerequisite":
                hideProgress();
                loadFragment(new PrerequisiteFragment(this, viewModel), R.anim.fade_in, R.anim.fade_out);
                break;
            case "sample":
                showProgress();
                setProgress();

                switch (viewModel.getType(viewModel.getIndex())) {
                    case "TP":
                        loadFragment(new TPFragment(this, viewModel), R.anim.fade_in, R.anim.fade_out);
                        break;
                    case "optional":
                        loadFragment(new TFTFragment(this, viewModel), R.anim.fade_in, R.anim.fade_out);
                        break;
                    case "TFP":
                        loadFragment(new TFPFragment(this, viewModel), R.anim.fade_in, R.anim.fade_out);
                        break;
                    case "PP":
                        loadFragment(new PPFragment(this, viewModel), R.anim.fade_in, R.anim.fade_out);
                        break;
                    case "PFT":
                        loadFragment(new PFTFragment(this, viewModel), R.anim.fade_in, R.anim.fade_out);
                        break;
                    case "PFP":
                        loadFragment(new PFPFragment(this, viewModel), R.anim.fade_in, R.anim.fade_out);
                        break;
                }
                break;
        }
    }

    private void showButtons() {
        buttonLinearLayout.setVisibility(View.VISIBLE);
    }

    private void showProgress() {
        if (showProgress.equals("-1")) {
            showProgress = "1";

            progressLinearLayout.startAnimation(animSlideIn);
            progressLinearLayout.setVisibility(View.VISIBLE);
        } else if (showProgress.equals("0")) {
            showProgress = "1";

            progressLinearLayout.startAnimation(animSlideIn);
            progressLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void hideProgress() {
        showProgress = "0";

        progressLinearLayout.startAnimation(animSlideOut);
        handler.postDelayed(() -> progressLinearLayout.setVisibility(View.GONE), 200);
    }

    private void setText() {
        if (SampleRepository.theory.equals("description") || SampleRepository.theory.equals("prerequisite")) {
            finishButton.setText(getResources().getString(R.string.SampleNext));
        } else if (SampleRepository.theory.equals("sample")) {
            finishButton.setText(getResources().getString(R.string.SampleFinish));
        }
    }

    private void setAction() {
        switch (SampleRepository.theory) {
            case "description":
                try {
                    SampleRepository.theory = "prerequisite";
                    showFragment();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "prerequisite":
                PrerequisiteFragment fragment = ((PrerequisiteFragment) getSupportFragmentManager().findFragmentById(R.id.activity_sample_frameLayout));
                if (fragment != null) {
                    fragment.doWork();
                    observeWorkAnswer();
                }
                break;
            case "sample":
                finishDialog.show();
                break;
        }
    }

    private void setRecyclerView() {
        adapter.setIndex(viewModel.readSampleAnswerFromCache(sharedPreferences.getString("sampleId", "")));
        recyclerView.setAdapter(adapter);
    }

    private void setProgress() {
        progressBar.setMax(viewModel.getSize());
        progressBar.setProgress(viewModel.answeredSize(sharedPreferences.getString("sampleId", "")));

        recyclerView.scrollToPosition(viewModel.getIndex());

        adapter.setIndex(viewModel.readSampleAnswerFromCache(sharedPreferences.getString("sampleId", "")));
        adapter.notifyDataSetChanged();
    }

    private void closeSample() {
        try {
            progressDialog.show();
            viewModel.close(sharedPreferences.getString("sampleId", ""));
            observeWorkSample();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void launchProcess() {
        SampleRepository.work = "getSingle";

        if (viewModel.firstUnAnswered(sharedPreferences.getString("sampleId", "")) >= 0) {
            loadingDialog.show();
        }
        try {
            viewModel.sample(sharedPreferences.getString("sampleId", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        observeWorkSample();
    }

    private void observeWorkSample() {

        SampleRepository.workStateSample.observe(this, integer -> {

            if (SampleRepository.work == "getSingle") {
                if (isNetworkConnected()) {
                    if (integer == 1) {
                        if (viewModel.checkPrerequisiteAnswerStorage(sharedPreferences.getString("sampleId", ""))) {
                            SampleRepository.theory = "description";
                            try {
                                showFragment();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (viewModel.hasSampleAnswerStorage(sharedPreferences.getString("sampleId", ""))) {
                                if (viewModel.firstUnAnswered(sharedPreferences.getString("sampleId", "")) == -1) {
                                    startActivity(new Intent(this, OutroActivity.class));
                                    finish();
                                    SampleRepository.workStateSample.removeObservers(this);
                                }
                            }
                            viewModel.checkSampleAnswerStorage(sharedPreferences.getString("sampleId", ""));
                            setRecyclerView();
                            if (viewModel.firstUnAnswered(sharedPreferences.getString("sampleId", "")) == -1) {
                                if (viewModel.answeredSize(sharedPreferences.getString("sampleId", "")) == viewModel.getSize()) {
                                    loadingDialog.dismiss();
                                    startActivity(new Intent(this, OutroActivity.class));
                                    finish();
                                    return;
                                } else {
                                    SampleRepository.theory = "sample";
                                    try {
                                        showFragment();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            viewModel.setIndex(viewModel.firstUnAnswered(sharedPreferences.getString("sampleId", "")));
                            loadingDialog.dismiss();
                            try {
                                SampleRepository.theory = "sample";
                                showFragment();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        }
                        loadingDialog.dismiss();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);


                    } else if (integer == 0) {

                        if (viewModel.getItems() == null) {
                            finish();
                            loadingDialog.dismiss();
                            Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        } else {
                            viewModel.checkSampleAnswerStorage(sharedPreferences.getString("sampleId", ""));
                            viewModel.firstUnAnswered(sharedPreferences.getString("sampleId", ""));
                            if (viewModel.firstUnAnswered(sharedPreferences.getString("sampleId", "")) == -1) {
                                startActivity(new Intent(this, OutroActivity.class));
                                finish();
                                return;
                            }

                            setRecyclerView();
                            viewModel.setIndex(viewModel.firstUnAnswered(sharedPreferences.getString("sampleId", "")));
                            try {
                                SampleRepository.theory = "sample";
                                showFragment();
                                loadingDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (integer == -2) {
                        setRecyclerView();
                        if (viewModel.hasPrerequisiteAnswerStorage(sharedPreferences.getString("sampleId", ""))) {
                            if (viewModel.checkPrerequisiteAnswerStorage(sharedPreferences.getString("sampleId", ""))) {
                                loadFragment(new DescriptionFragment(this, viewModel), R.anim.fade_in, R.anim.fade_out);
                            } else {
                                SampleRepository.theory = "sample";
                                try {
                                    showFragment();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            finish();
                        }

                        loadingDialog.dismiss();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    }
                } else {

                    if (viewModel.getItems() != null) {
                        viewModel.checkSampleAnswerStorage(sharedPreferences.getString("sampleId", ""));
                        viewModel.firstUnAnswered(sharedPreferences.getString("sampleId", ""));
                        if (viewModel.firstUnAnswered(sharedPreferences.getString("sampleId", "")) == -1) {
                            startActivity(new Intent(this, OutroActivity.class));
                            finish();
                            return;
                        }

                        setRecyclerView();
                        viewModel.setIndex(viewModel.firstUnAnswered(sharedPreferences.getString("sampleId", "")));
                        try {
                            SampleRepository.theory = "sample";
                            showFragment();
                            loadingDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            } else if (SampleRepository.work == "close") {
                if (integer == 1) {
                    setResult(RESULT_OK, null);
                    finish();

                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                }
            } else {

            }
        });
    }

    private void observeWorkAnswer() {
        SampleRepository.workStateAnswer.observe((LifecycleOwner) this, integer -> {
            if (SampleRepository.work == "sendPrerequisite") {
                if (integer == 1) {

                    loadingDialog.dismiss();
                    SampleRepository.workStateAnswer.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    loadingDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateAnswer.removeObservers((LifecycleOwner) this);
                } else if (integer == -2) {
                    loadingDialog.dismiss();
                    Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                    SampleRepository.workStateAnswer.removeObservers((LifecycleOwner) this);
                }
                if (integer != -1) {
                    SampleRepository.work = "getSingle";
                    try {
                        viewModel.sample(sharedPreferences.getString("sampleId", ""));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    observeWorkSample();
                }
            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onBackPressed() {
        switch (SampleRepository.theory) {
            case "sample":
                SampleRepository.theory = "prerequisite";
                try {
                    showFragment();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "prerequisite":
                SampleRepository.theory = "description";
                try {
                    showFragment();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "description":
                cancelDialog.show();
                break;
        }
    }

}