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

import com.majazeh.risloo.Models.Controller.AuthController;
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

import java.util.ArrayList;

public class SampleActivity extends AppCompatActivity {

    // ViewModels
    public static SampleViewModel viewModel;

    // Adapters
    private IndexAdapter adapter;

    // Objects
    private Handler handler;

    // Widgets
    private TextView indexTextView, navigateDialogConfirm, cancelDialogTitle, cancelDialogDescription, cancelDialogPositive, cancelDialogNegative;
    private ImageView cancelImageView, forwardImageView, backwardImageView, navigateImageView, navigateDialogForwardImageView, navigateDialogBackwardImageView;
    private ProgressBar flowProgressBar;
    private RecyclerView dialogNavigateRecyclerView;
    private Dialog navigateDialog, cancelDialog;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_sample);

        initializer();

        detector();

        listener();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        sharedPreferences = getSharedPreferences("STORE", Context.MODE_PRIVATE);
        adapter = new IndexAdapter(this);
        viewModel = ViewModelProviders.of(this, new SampleViewModelFactory(getApplication(), sharedPreferences.getString("sampleId", ""))).get(SampleViewModel.class);
        handler = new Handler();


        cancelImageView = findViewById(R.id.activity_sample_cancel_imageView);
        forwardImageView = findViewById(R.id.activity_sample_forward_imageView);
        backwardImageView = findViewById(R.id.activity_sample_backward_imageView);
        navigateImageView = findViewById(R.id.activity_sample_navigate_imageView);

        flowProgressBar = findViewById(R.id.activity_sample_flow_progressBar);

        indexTextView = findViewById(R.id.activity_sample_index_imageView);

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

        navigateDialogForwardImageView = navigateDialog.findViewById(R.id.dialog_navigate_forward_textView);
        navigateDialogBackwardImageView = navigateDialog.findViewById(R.id.dialog_navigate_backward_textView);

        dialogNavigateRecyclerView = navigateDialog.findViewById(R.id.dialog_navigate_recyclerView);
        dialogNavigateRecyclerView.addItemDecoration(new ItemDecorator("gridLayout", (int) getResources().getDimension(R.dimen._16sdp)));
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

        if (isNetworkConnected()) {
            SampleRepository.workStateSample.observe((LifecycleOwner) this, integer -> {
                if (integer == 1) {
                    adapter.setIndex(viewModel.getItems());
                    try {
                        createStorage();
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
                adapter.setIndex(viewModel.getItems());
                try {
                    createStorage();
                    showFragment((String) viewModel.getAnswer(viewModel.getCurrentIndex()).get("type"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // you are offline
            }
        }
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            cancelImageView.setBackgroundResource(R.drawable.draw_oval_snow_ripple);

            forwardImageView.setBackgroundResource(R.drawable.draw_8sdp_snow_ripple);
            backwardImageView.setBackgroundResource(R.drawable.draw_8sdp_snow_ripple);

            navigateImageView.setBackgroundResource(R.drawable.draw_8sdp_snow_oneside_ripple);

            navigateDialogForwardImageView.setBackgroundResource(R.drawable.draw_oval_white_ripple);
            navigateDialogBackwardImageView.setBackgroundResource(R.drawable.draw_oval_white_ripple);

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
            if (viewModel.next() == null){
                finish();
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
            navigateDialog.show();
        });

        navigateDialogForwardImageView.setOnClickListener(v -> {
            navigateDialogForwardImageView.setClickable(false);
            handler.postDelayed(() -> navigateDialogForwardImageView.setClickable(true), 100);

        });

        navigateDialogBackwardImageView.setOnClickListener(v -> {
            navigateDialogBackwardImageView.setClickable(false);
            handler.postDelayed(() -> navigateDialogBackwardImageView.setClickable(true), 100);

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

    public void showFragment(String type) throws JSONException {
//        Log.e("local", String.valueOf(SampleRepository.localData));
//        Log.e("local", String.valueOf(viewModel.readFromCache(sharedPreferences.getString("sampleId", ""))));
        indexTextView.setText(viewModel.getCurrentIndex() + 1 + " از " + viewModel.getSize());
        switch (type) {
            case "TP":
                loadFragment(new TPFragment(this), 0, 0);
                break;
            case "optional":
                loadFragment(new TFTFragment(this, viewModel), 0, 0);
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

    @Override
    public void onBackPressed() {
        cancelDialog.show();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    private void createStorage(){
        if (!viewModel.hasStorage(sharedPreferences.getString("sampleId", ""))) {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < viewModel.getSize(); i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("index", "");
                    jsonObject.put("answer", "");
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            viewModel.writeToCache(jsonArray, sharedPreferences.getString("sampleId", ""));
        }
    }
}