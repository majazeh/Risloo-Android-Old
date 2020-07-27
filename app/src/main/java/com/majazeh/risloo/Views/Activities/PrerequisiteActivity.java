package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Models.Controller.SampleController;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.PrerequisiteAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

public class PrerequisiteActivity extends AppCompatActivity {


    // ViewModels
    public static SampleViewModel viewModel;

    // Adapters
    private PrerequisiteAdapter adapter;

    // Objects
    private Handler handler;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Widgets
    private Toolbar toolbar;
    private TextView infoDialogTitle, infoDialogDescription, infoDialogConfirm;
    private RecyclerView recyclerView;
    private Button startButton;
    private Dialog infoDialog;
    public Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);
        viewModel = ViewModelProviders.of(this).get(SampleViewModel.class);
        if (viewModel.firstUnanswered(sharedPreferences.getString("sampleId", "")) <= 0) {

            decorator();

            setContentView(R.layout.activity_prerequisite);

            initializer();

            detector();

            listener();

            observeWork();
        } else {
            launchSample();
        }
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {

        adapter = new PrerequisiteAdapter(this);

        handler = new Handler();

        toolbar = findViewById(R.id.activity_prerequisite_toolbar);

        recyclerView = findViewById(R.id.activity_prerequisite_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorator("subListLayout", (int) getResources().getDimension(R.dimen._16sdp)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        startButton = findViewById(R.id.activity_prerequisite_start_button);

        infoDialog = new Dialog(this, R.style.DialogTheme);
        infoDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        infoDialog.setContentView(R.layout.dialog_note);
        infoDialog.setCancelable(true);

        progressDialog = new Dialog(this, R.style.DialogTheme);
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(infoDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        infoDialog.getWindow().setAttributes(layoutParams);

        infoDialogTitle = infoDialog.findViewById(R.id.dialog_note_title_textView);
        infoDialogTitle.setText(getResources().getString(R.string.PrerequisiteInfoDialogTitle));
        infoDialogDescription = infoDialog.findViewById(R.id.dialog_note_description_textView);

        infoDialogConfirm = infoDialog.findViewById(R.id.dialog_note_confirm_textView);
        infoDialogConfirm.setText(getResources().getString(R.string.PrerequisiteInfoDialogConfirm));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            startButton.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);

            infoDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_snow_ripple);
        }
    }

    private void listener() {
        toolbar.setNavigationOnClickListener(v -> finish());

        infoDialogConfirm.setOnClickListener(v -> {
            infoDialogConfirm.setClickable(false);
            handler.postDelayed(() -> infoDialogConfirm.setClickable(true), 1000);
            infoDialog.dismiss();
        });

        infoDialog.setOnCancelListener(dialog -> infoDialog.dismiss());

        startButton.setOnClickListener(v -> {
            JSONArray jsonArray = new JSONArray();
            // TODO : put items to json array like this jsonArray.put(the position of question, answer of that position);
            viewModel.savePrerequisiteToCache(jsonArray, sharedPreferences.getString("sampleId", ""));
            //adapter.answers();

            viewModel.sendPre(adapter.answers());
            SampleController.workStateAnswer.observe((LifecycleOwner) this, integer -> {
                if (integer == 1) {
                    launchSample();
                } else if (integer == 0) {
                    // error
                } else if (integer == -1) {
                    // do nothing
                } else if (integer == -2) {
                    // offline
                } else {
                    // do nothing
                }
            });

//            if (nameEditText.length() == 0 || mobileEditText.length() == 0 || messageEditText.length() == 0) {
//                checkInput();
//            } else {
//                clearData();
//                sendMessage();
//            }
        });
    }

    private boolean firstTimeLoad() {
        return true;
    }

    private void launchSample() {
        startActivity(new Intent(this, SampleActivity.class));
        finish();
    }

    public void observeWork() {
        try {
            viewModel.getSample(sharedPreferences.getString("sampleId", ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (isNetworkConnected()) {
            progressDialog.show();
            SampleController.workStateSample.observe((LifecycleOwner) this, integer -> {
                if (integer == 1) {
                    try {
                        progressDialog.dismiss();
                        infoDialog.show();
                        infoDialogDescription.setText(viewModel.getDescription());
                        adapter.setPrerequisite(viewModel.getPrerequisite());
                        recyclerView.setAdapter(adapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    SampleController.workStateSample.removeObservers((LifecycleOwner) this);
                } else if (integer == 0) {
                    progressDialog.dismiss();
                    Toast.makeText(this, SampleController.exception, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, AuthActivity.class));
                    finish();
                    // TODO: get exception
                }
            });
        } else {
            if (viewModel.getItems() != null) {
                try {
                    infoDialogDescription.setText(viewModel.getDescription());
                    adapter.setPrerequisite(viewModel.getPrerequisite());
                    recyclerView.setAdapter(adapter);
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

}