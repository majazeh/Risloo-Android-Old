package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.majazeh.risloo.Models.Controller.SampleController;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.IndexAdapter;
import com.majazeh.risloo.Views.Fragments.PFPFragment;
import com.majazeh.risloo.Views.Fragments.PFTFragment;
import com.majazeh.risloo.Views.Fragments.PPFragment;
import com.majazeh.risloo.Views.Fragments.TFPFragment;
import com.majazeh.risloo.Views.Fragments.TFTFragment;
import com.majazeh.risloo.Views.Fragments.TPFragment;

import org.json.JSONException;

public class SampleActivity extends AppCompatActivity {

    // ViewModels
    public static SampleViewModel viewModel;

    // Adapters
    private IndexAdapter adapter;

    // Objects
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    private TextView indexTextView, cancelTextView, finishTextView, navigateDialogConfirm, finishDialogTitle, finishDialogDescription, finishDialogPositive, finishDialogNegative, cancelDialogTitle, cancelDialogDescription, cancelDialogPositive, cancelDialogNegative;
    private ImageView forwardImageView, backwardImageView, navigateImageView;
    private ProgressBar flowProgressBar;
    private RecyclerView dialogNavigateRecyclerView;
    public Dialog progressDialog, navigateDialog, finishDialog, cancelDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_sample);

        initializer();

        detector();

        listener();

        checkStorage();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(SampleViewModel.class);

        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();

        handler = new Handler();

        indexTextView = findViewById(R.id.activity_sample_flow_textView);
        cancelTextView = findViewById(R.id.activity_sample_cancel_textView);
        finishTextView = findViewById(R.id.activity_sample_finish_textView);

        forwardImageView = findViewById(R.id.activity_sample_forward_imageView);
        backwardImageView = findViewById(R.id.activity_sample_backward_imageView);
        navigateImageView = findViewById(R.id.activity_sample_navigate_imageView);

        flowProgressBar = findViewById(R.id.activity_sample_flow_progressBar);

        navigateDialog = new Dialog(this, R.style.DialogTheme);
        navigateDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        navigateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        navigateDialog.setContentView(R.layout.dialog_navigate);
        navigateDialog.setCancelable(true);
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

        adapter = new IndexAdapter(this, viewModel, navigateDialog);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(navigateDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        navigateDialog.getWindow().setAttributes(layoutParams);
        WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams();
        layoutParams2.copyFrom(finishDialog.getWindow().getAttributes());
        layoutParams2.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams2.height = WindowManager.LayoutParams.WRAP_CONTENT;
        finishDialog.getWindow().setAttributes(layoutParams2);
        WindowManager.LayoutParams layoutParams3 = new WindowManager.LayoutParams();
        layoutParams3.copyFrom(cancelDialog.getWindow().getAttributes());
        layoutParams3.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams3.height = WindowManager.LayoutParams.WRAP_CONTENT;
        cancelDialog.getWindow().setAttributes(layoutParams3);

        dialogNavigateRecyclerView = navigateDialog.findViewById(R.id.dialog_navigate_recyclerView);
        dialogNavigateRecyclerView.addItemDecoration(new ItemDecorator("normalLayout", (int) getResources().getDimension(R.dimen._16sdp)));
        dialogNavigateRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.HORIZONTAL, false));
        dialogNavigateRecyclerView.setHasFixedSize(true);
        dialogNavigateRecyclerView.setAdapter(adapter);

        navigateDialogConfirm = navigateDialog.findViewById(R.id.dialog_navigate_close_textView);

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
            forwardImageView.setBackgroundResource(R.drawable.draw_8sdp_snow_ripple);
            backwardImageView.setBackgroundResource(R.drawable.draw_8sdp_snow_ripple);

            navigateImageView.setBackgroundResource(R.drawable.draw_8sdp_snow_oneside_ripple);

            finishTextView.setBackgroundResource(R.drawable.draw_8sdp_primary_ripple);
            cancelTextView.setBackgroundResource(R.drawable.draw_8sdp_solitude_ripple);

            navigateDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_white_ripple);

            finishDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
            finishDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
            cancelDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
            cancelDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
        }
    }

    private void listener() {
        finishTextView.setOnClickListener(v -> {
            finishTextView.setClickable(false);
            handler.postDelayed(() -> finishTextView.setClickable(true), 1000);
            finishDialog.show();
        });

        cancelTextView.setOnClickListener(v -> {
            cancelTextView.setClickable(false);
            handler.postDelayed(() -> cancelTextView.setClickable(true), 1000);
            cancelDialog.show();
        });

        forwardImageView.setOnClickListener(v -> {
            forwardImageView.setClickable(false);
            handler.postDelayed(() -> forwardImageView.setClickable(true), 100);

            if (viewModel.getNext() == null) {
                if (viewModel.firstUnanswered(sharedPreferences.getString("sampleId", "")) == -1) {
                    startActivity(new Intent(this, OutroActivity.class));
                    finish();
                    return;
                }
                viewModel.setIndex(viewModel.firstUnanswered(sharedPreferences.getString("sampleId", "")));
            }
            try {
                showFragment((String) viewModel.getAnswer(viewModel.getIndex()).get("type"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        backwardImageView.setOnClickListener(v -> {
            backwardImageView.setClickable(false);
            handler.postDelayed(() -> backwardImageView.setClickable(true), 100);

            viewModel.getPrev();
            try {
                showFragment((String) viewModel.getAnswer(viewModel.getIndex()).get("type"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        navigateImageView.setOnClickListener(v -> {
            navigateImageView.setClickable(false);
            handler.postDelayed(() -> navigateImageView.setClickable(true), 1000);
            dialogNavigateRecyclerView.scrollToPosition(viewModel.getIndex());
            navigateDialog.show();
        });

        navigateDialogConfirm.setOnClickListener(v -> {
            navigateDialogConfirm.setClickable(false);
            handler.postDelayed(() -> navigateDialogConfirm.setClickable(true), 1000);
            navigateDialog.dismiss();
        });

        navigateDialog.setOnCancelListener(dialog -> navigateDialog.dismiss());

        finishDialogPositive.setOnClickListener(v -> {
            finishDialogPositive.setClickable(false);
            handler.postDelayed(() -> finishDialogPositive.setClickable(true), 1000);
            finishDialog.dismiss();

            // TODO : Put Here
        });

        finishDialogNegative.setOnClickListener(v -> {
            finishDialogNegative.setClickable(false);
            handler.postDelayed(() -> finishDialogNegative.setClickable(true), 1000);
            finishDialog.dismiss();
        });

        finishDialog.setOnCancelListener(dialog -> finishDialog.dismiss());

        cancelDialogPositive.setOnClickListener(v -> {
            cancelDialogPositive.setClickable(false);
            handler.postDelayed(() -> cancelDialogPositive.setClickable(true), 1000);
            cancelDialog.dismiss();

            finish();
        });

        cancelDialogNegative.setOnClickListener(v -> {
            cancelDialogNegative.setClickable(false);
            handler.postDelayed(() -> cancelDialogNegative.setClickable(true), 1000);
            cancelDialog.dismiss();
        });

        cancelDialog.setOnCancelListener(dialog -> cancelDialog.dismiss());
    }

    private void checkStorage() {
        if (viewModel.hasStorage(sharedPreferences.getString("sampleId", ""))) {
            if (viewModel.firstUnanswered(sharedPreferences.getString("sampleId", "")) == -1) {
                startActivity(new Intent(this, OutroActivity.class));
                finish();
            } else {
                observeWork();
            }
        } else {
            observeWork();
        }
    }

    private void loadFragment(Fragment fragment, int enterAnim, int exitAnim) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(enterAnim, exitAnim);
        transaction.replace(R.id.activity_sample_frameLayout, fragment);
        transaction.commit();
    }

    public void showFragment(String type) {
        indexTextView.setText(viewModel.getIndex() + 1 + " " + "از" + " " + viewModel.getSize());

        flowProgressBar.setMax(viewModel.getSize());
        flowProgressBar.setProgress(viewModel.answeredSize(sharedPreferences.getString("sampleId", "")));

        dialogNavigateRecyclerView.scrollToPosition(viewModel.getIndex());

        adapter.setIndex(viewModel.readAnswerFromCache(sharedPreferences.getString("sampleId", "")));
        adapter.notifyDataSetChanged();

        switch (type) {
            case "TP":
                loadFragment(new TPFragment(this), R.anim.fade_in, R.anim.fade_out);
                break;
            case "optional":
                loadFragment(new TFTFragment(this, viewModel), R.anim.fade_in, R.anim.fade_out);
                break;
            case "TFP":
                loadFragment(new TFPFragment(this), R.anim.fade_in, R.anim.fade_out);
                break;
            case "PP":
                loadFragment(new PPFragment(this), R.anim.fade_in, R.anim.fade_out);
                break;
            case "PFT":
                loadFragment(new PFTFragment(this), R.anim.fade_in, R.anim.fade_out);
                break;
            case "PFP":
                loadFragment(new PFPFragment(this), R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }

    public void observeWork() {
        if (isNetworkConnected()) {
            progressDialog.show();
            SampleController.workStateSample.observe((LifecycleOwner) this, integer -> {
                if (integer == 1) {
                    try {
                        viewModel.checkStorage(sharedPreferences.getString("sampleId", ""));
                        adapter.setIndex(viewModel.readAnswerFromCache(sharedPreferences.getString("sampleId", "")));
                        if (viewModel.firstUnanswered(sharedPreferences.getString("sampleId", "")) == -1) {
                            finish();
                            return;
                        }
                        viewModel.setIndex(viewModel.firstUnanswered(sharedPreferences.getString("sampleId", "")));
                        progressDialog.dismiss();
                        showFragment((String) viewModel.getAnswer(viewModel.getIndex()).get("type"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    SampleController.workStateSample.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    // TODO: get exception
                }
            });
        } else {
            if (viewModel.getItems() != null) {
                try {
                    viewModel.checkStorage(sharedPreferences.getString("sampleId", ""));
                    viewModel.firstUnanswered(sharedPreferences.getString("sampleId", ""));
                    if (viewModel.firstUnanswered(sharedPreferences.getString("sampleId", "")) == -1) {
                        startActivity(new Intent(this, OutroActivity.class));
                        finish();
                        return;
                    }
                    adapter.setIndex(viewModel.readAnswerFromCache(sharedPreferences.getString("sampleId", "")));
                    viewModel.setIndex(viewModel.firstUnanswered(sharedPreferences.getString("sampleId", "")));

                    showFragment((String) viewModel.getAnswer(viewModel.getIndex()).get("type"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // you are offline
            }
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onBackPressed() {
        cancelDialog.show();
    }

}