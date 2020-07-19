package com.majazeh.risloo.Views.Ui.Activities;

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
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Models.Repositories.Sample.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.Sample.SampleViewModel;
import com.majazeh.risloo.ViewModels.Sample.SampleViewModelFactory;
import com.majazeh.risloo.Views.Adapters.IndexAdapter;
import com.majazeh.risloo.Views.Ui.Fragments.PFPFragment;
import com.majazeh.risloo.Views.Ui.Fragments.PFTFragment;
import com.majazeh.risloo.Views.Ui.Fragments.PPFragment;
import com.majazeh.risloo.Views.Ui.Fragments.TFPFragment;
import com.majazeh.risloo.Views.Ui.Fragments.TFTFragment;
import com.majazeh.risloo.Views.Ui.Fragments.TPFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private TextView indexTextView, navigateDialogConfirm, cancelDialogTitle, cancelDialogDescription, cancelDialogPositive, cancelDialogNegative;
    private ImageView cancelImageView, forwardImageView, backwardImageView, navigateImageView;
    private ProgressBar flowProgressBar;
    private RecyclerView dialogNavigateRecyclerView;
    public Dialog progressDialog, navigateDialog, cancelDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_sample);

        initializer();

        detector();

        listener();

        observeWork();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        viewModel = ViewModelProviders.of(this, new SampleViewModelFactory(getApplication(), sharedPreferences.getString("sampleId", ""))).get(SampleViewModel.class);

        handler = new Handler();

        cancelImageView = findViewById(R.id.activity_sample_cancel_imageView);
        forwardImageView = findViewById(R.id.activity_sample_forward_imageView);
        backwardImageView = findViewById(R.id.activity_sample_backward_imageView);
        navigateImageView = findViewById(R.id.activity_sample_navigate_imageView);

        flowProgressBar = findViewById(R.id.activity_sample_flow_progressBar);

        indexTextView = findViewById(R.id.activity_sample_index_imageView);

        progressDialog = new Dialog(this, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);
        navigateDialog = new Dialog(this, R.style.DialogTheme);
        navigateDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        navigateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        navigateDialog.setContentView(R.layout.dialog_navigate);
        navigateDialog.setCancelable(true);
        cancelDialog = new Dialog(this, R.style.DialogTheme);
        cancelDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        cancelDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancelDialog.setContentView(R.layout.dialog_action);
        cancelDialog.setCancelable(true);

        adapter = new IndexAdapter(this, viewModel, navigateDialog);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(navigateDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        navigateDialog.getWindow().setAttributes(layoutParams);
        WindowManager.LayoutParams layoutParams2 = new WindowManager.LayoutParams();
        layoutParams2.copyFrom(cancelDialog.getWindow().getAttributes());
        layoutParams2.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams2.height = WindowManager.LayoutParams.WRAP_CONTENT;
        cancelDialog.getWindow().setAttributes(layoutParams2);

        dialogNavigateRecyclerView = navigateDialog.findViewById(R.id.dialog_navigate_recyclerView);
        dialogNavigateRecyclerView.addItemDecoration(new ItemDecorator("listLayout", (int) getResources().getDimension(R.dimen._16sdp)));
        dialogNavigateRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.HORIZONTAL, false));
        dialogNavigateRecyclerView.setHasFixedSize(true);
        dialogNavigateRecyclerView.setAdapter(adapter);

        navigateDialogConfirm = navigateDialog.findViewById(R.id.dialog_navigate_close_textView);

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
            cancelImageView.setBackgroundResource(R.drawable.draw_oval_snow_ripple_violetred);

            forwardImageView.setBackgroundResource(R.drawable.draw_8sdp_snow_ripple);
            backwardImageView.setBackgroundResource(R.drawable.draw_8sdp_snow_ripple);

            navigateImageView.setBackgroundResource(R.drawable.draw_8sdp_snow_oneside_ripple);

            navigateDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_white_ripple);

            cancelDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
            cancelDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
        }
    }

    private void listener() {
        cancelImageView.setOnClickListener(v -> {
            cancelImageView.setClickable(false);
            handler.postDelayed(() -> cancelImageView.setClickable(true), 1000);

            cancelDialog.show();
        });

        forwardImageView.setOnClickListener(v -> {
            forwardImageView.setClickable(false);
            handler.postDelayed(() -> forwardImageView.setClickable(true), 100);

            if (viewModel.next() == null) {
                if (viewModel.getLastUnAnswer(sharedPreferences.getString("sampleId", "")) == -1) {
                    finish();
                    return;
                }
                viewModel.setIndex(viewModel.getLastUnAnswer(sharedPreferences.getString("sampleId", "")));
            }
            try {
                showFragment((String) viewModel.getAnswer(viewModel.getCurrentIndex()).get("type"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        backwardImageView.setOnClickListener(v -> {
            backwardImageView.setClickable(false);
            handler.postDelayed(() -> backwardImageView.setClickable(true), 100);

            viewModel.prev();
            try {
                showFragment((String) viewModel.getAnswer(viewModel.getCurrentIndex()).get("type"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        navigateImageView.setOnClickListener(v -> {
            navigateImageView.setClickable(false);
            handler.postDelayed(() -> navigateImageView.setClickable(true), 1000);
            dialogNavigateRecyclerView.scrollToPosition(viewModel.getCurrentIndex());
            navigateDialog.show();

        });

        navigateDialogConfirm.setOnClickListener(v -> {
            navigateDialogConfirm.setClickable(false);
            handler.postDelayed(() -> navigateDialogConfirm.setClickable(true), 1000);
            navigateDialog.dismiss();
        });

        navigateDialog.setOnCancelListener(dialog -> navigateDialog.dismiss());

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

    private void loadFragment(Fragment fragment, int enterAnim, int exitAnim) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(enterAnim, exitAnim);
        transaction.replace(R.id.activity_sample_frameLayout, fragment);
        transaction.commit();
    }

    public void showFragment(String type) {
        indexTextView.setText(viewModel.getCurrentIndex() + 1 + " " + "از" + " " + viewModel.getSize());
        flowProgressBar.setProgress(viewModel.answerSize(sharedPreferences.getString("sampleId", "")));
        adapter.setIndex(viewModel.readFromCache(sharedPreferences.getString("sampleId", "")));
        adapter.notifyDataSetChanged();
        switch (type) {
            case "TP":
                loadFragment(new TPFragment(this), 0, 0);
                break;
            case "optional":
                loadFragment(new TFTFragment(this, viewModel), R.anim.fade_in, R.anim.fade_out);
                break;
            case "TFP":
                loadFragment(new TFPFragment(this), 0, 0);
                break;
            case "PP":
                loadFragment(new PPFragment(this), 0, 0);
                break;
            case "PFT":
                loadFragment(new PFTFragment(this), 0, 0);
                break;
            case "PFP":
                loadFragment(new PFPFragment(this), 0, 0);
                break;
        }
    }

    // TODO : Check This Piece of Code
    public void observeWork() {
        checkStorage();
//        viewModel.getLastUnAnswer(sharedPreferences.getString("sampleId", ""));
        if (viewModel.getLastUnAnswer(sharedPreferences.getString("sampleId", "")) == -1) {
            finish();
            return;
        }
        if (isNetworkConnected()) {
            progressDialog.show();
            SampleRepository.workStateSample.observe((LifecycleOwner) this, integer -> {
                if (integer == 1) {
                    try {
                        progressDialog.dismiss();
                        adapter.setIndex(viewModel.readFromCache(sharedPreferences.getString("sampleId", "")));
                        viewModel.setIndex(viewModel.getLastUnAnswer(sharedPreferences.getString("sampleId", "")));
                        showFragment((String) viewModel.getAnswer(viewModel.getCurrentIndex()).get("type"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    // TODO: get exception
                } else {

                }

            });
        } else {
            if (viewModel.getItems() != null) {
                try {
                    adapter.setIndex(viewModel.readFromCache(sharedPreferences.getString("sampleId", "")));
                    viewModel.setIndex(viewModel.getLastUnAnswer(sharedPreferences.getString("sampleId", "")));
                    Log.e("get current index", String.valueOf(viewModel.getCurrentIndex()));

                    showFragment((String) viewModel.getAnswer(viewModel.getCurrentIndex()).get("type"));
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

    private void checkStorage() {
        if (!viewModel.hasStorage(sharedPreferences.getString("sampleId", ""))) {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < viewModel.getSize(); i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("index", i);
                    jsonObject.put("answer", "");
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            viewModel.writeToCache(jsonArray, sharedPreferences.getString("sampleId", ""));
        }
    }

    @Override
    public void onBackPressed() {
        cancelDialog.show();
    }

}