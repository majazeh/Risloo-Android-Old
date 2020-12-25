package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.ViewModels.SessionViewModel;
import com.majazeh.risloo.Views.Adapters.SearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Logger;

public class ReportActivity extends AppCompatActivity {


    // ViewModels
    private AuthViewModel authViewModel;
    public SessionViewModel sessionViewModel;

    // Vars
    public String typeId = "", report = "",typeTitle = "";
    public boolean loading = false, finished = true, typeException = false;

    // Objects
    private Bundle extras;
    private Handler handler;

    private SharedPreferences sharedPreferences;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView, typeTextView, typeDialogTitleTextView;
    private EditText reportEditText;
    private ControlEditText controlEditText;
    private Dialog typeDialog;
    private FrameLayout typeFrameLayout;
    private SearchAdapter typeDialogAdapter;
    private RecyclerView typeDialogRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_report);

        initializer();

        detector();

        listener();

//        setData();
//
//        getData();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);

        sharedPreferences = getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        handler = new Handler();

        extras = getIntent().getExtras();

        reportEditText = findViewById(R.id.report_editText);
        controlEditText = new ControlEditText();

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        typeDialogAdapter = new SearchAdapter(this);


        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        typeTextView = findViewById(R.id.report_type_textView);

        typeFrameLayout = findViewById(R.id.report_type_frameLayout);

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.ReportToolbarTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        typeDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(typeDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        typeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        typeDialog.setContentView(R.layout.dialog_search);
        typeDialog.setCancelable(true);
        typeDialogTitleTextView = typeDialog.findViewById(R.id.dialog_search_title_textView);
        typeDialogTitleTextView.setText(getResources().getString(R.string.ReportDialogTitle));

        typeDialogRecyclerView = typeDialog.findViewById(R.id.dialog_search_recyclerView);
        typeDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        typeDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        typeDialogRecyclerView.setHasFixedSize(true);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbarImageView.setOnClickListener(v -> {
            toolbarImageView.setClickable(false);
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 250);

            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        reportEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!reportEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(reportEditText);
                    controlEditText.select(reportEditText);
                }
            }
            return false;
        });

        typeTextView.setOnClickListener(v -> {
            typeTextView.setClickable(false);
            handler.postDelayed(() -> typeTextView.setClickable(true), 250);

            if (typeException) {
                clearException("report");
            }

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }
            try {
                Log.e("aaa", String.valueOf(sessionViewModel.getReportType(sharedPreferences.getBoolean("key", false))));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                setRecyclerView(sessionViewModel.getReportType(sharedPreferences.getBoolean("key", false)), typeDialogRecyclerView, "getType");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            typeDialog.show();
        });
    }

    private void clearException(String type) {
        switch (type) {
            case "report":
                typeException = false;
                typeFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
        }
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method) {
        switch (method) {
            case "getType":
                typeDialogAdapter.setValues(arrayList, method, "Report");
                recyclerView.setAdapter(typeDialogAdapter);
                break;
        }
    }

    public void observeSearchAdapter(Model model, String method) {
        try {
            switch (method) {
                case "getType":
                    if (!typeId.equals(model.get("en_title").toString())) {
                        typeId = model.get("en_title").toString();
                        typeTitle = model.get("fa_title").toString();

                        typeTextView.setText(typeTitle);
                        typeTextView.setTextColor(getResources().getColor(R.color.Grey));
                    } else if (typeId.equals(model.get("en_title").toString())) {
                        typeId = "";
                        typeTitle = "";

                        typeTextView.setText(getResources().getString(R.string.CreateSessionStatus));
                        typeTextView.setTextColor(getResources().getColor(R.color.Mischka));
                    }

                    typeDialog.dismiss();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}