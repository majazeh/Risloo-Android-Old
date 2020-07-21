package com.majazeh.risloo.Views.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.ViewModels.SampleViewModelFactory;
import com.majazeh.risloo.Views.Adapters.PrerequisiteAdapter;
import com.majazeh.risloo.Views.Adapters.QuestionAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_prerequisite);

        initializer();

        detector();

        listener();

        if (firstTimeLoad()) {
            infoDialog.show();
        }
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.White, R.color.Snow);
    }

    private void initializer() {
        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        viewModel = ViewModelProviders.of(this, new SampleViewModelFactory(getApplication(), sharedPreferences.getString("sampleId", ""))).get(SampleViewModel.class);

        adapter = new PrerequisiteAdapter(this);
//        adapter.setPrerequisite(viewModel.getAll());

        handler = new Handler();

        toolbar = findViewById(R.id.activity_prerequisite_toolbar);

        recyclerView = findViewById(R.id.activity_prerequisite_recyclerView);
        recyclerView.addItemDecoration(new ItemDecorator("subListLayout",(int) getResources().getDimension(R.dimen._16sdp)));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
//        recyclerView.setAdapter(adapter);

        startButton = findViewById(R.id.activity_prerequisite_start_button);

        infoDialog = new Dialog(this, R.style.DialogTheme);
        infoDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        infoDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        infoDialog.setContentView(R.layout.dialog_note);
        infoDialog.setCancelable(true);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(infoDialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        infoDialog.getWindow().setAttributes(layoutParams);

        infoDialogTitle = infoDialog.findViewById(R.id.dialog_note_title_textView);
        infoDialogTitle.setText(getResources().getString(R.string.PrerequisiteInfoDialogTitle));
        infoDialogDescription = infoDialog.findViewById(R.id.dialog_note_description_textView);
        try {
            infoDialogDescription.setText(viewModel.getDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
//            name = nameEditText.getText().toString().trim();
//            mobile = mobileEditText.getText().toString().trim();
//            message = messageEditText.getText().toString().trim();
//
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

}