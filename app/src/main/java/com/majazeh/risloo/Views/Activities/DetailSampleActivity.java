package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.pm.PackageManager;
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
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Managers.PermissionManager;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.DetailSampleAdapter;
import com.majazeh.risloo.Views.Adapters.SearchAdapter;
import com.majazeh.risloo.Views.Adapters.ZoomageAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class DetailSampleActivity extends AppCompatActivity {

    // ViewModels
    private SampleViewModel viewModel;

    // Adapters
    private DetailSampleAdapter detailSampleAdapter;
    private ZoomageAdapter zoomageAdapter;
    private SearchAdapter downloadDialogAdapter;

    // Vars
    public String sampleId = "", scaleTitle = "", downloadUrl = "";
    private boolean showLoading = false, showCardView = false;

    // Objects
    private Bundle extras;
    private Handler handler;
    private ClickableSpan retrySpan;
    private Animation animFadeIn, animFadeOut;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private TextView retryTextView, scaleTextView, serialTextView, statusTextView, referenceHintTextView, referenceTextView, caseTextView, roomTextView, actionTextView, generalTextView, prerequisiteTextView, testTextView;
    private ImageView retryImageView, statusImageView, referenceHintImageView, downloadImageView;
    private CheckBox editCheckbox;
    private RecyclerView resultRecyclerView, generalRecyclerView, prerequisiteRecyclerView, testRecyclerView;
    private Dialog progressDialog;
    private FrameLayout mainLayout;
    private LinearLayout retryLayout, loadingLayout, referenceLinearLayout, caseLinearLayout, roomLinearLayout;
    private CardView loadingCardView, resultCardView, componentCardView;
    private Dialog downloadDialog;
    private TextView downloadDialogTitleTextView;
    private RecyclerView downloadDialogRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_detail_sample);

        initializer();

        detector();

        listener();

        launchProcess("getGeneral");
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = new ViewModelProvider(this).get(SampleViewModel.class);

        detailSampleAdapter = new DetailSampleAdapter(this);
        zoomageAdapter = new ZoomageAdapter(this);
        downloadDialogAdapter = new SearchAdapter(this);

        handler = new Handler();

        extras = getIntent().getExtras();
        sampleId = Objects.requireNonNull(extras).getString("id");

        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.DetailSampleTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        retryTextView = findViewById(R.id.activity_detail_sample_retry_textView);
        retryTextView.setMovementMethod(LinkMovementMethod.getInstance());

        scaleTextView = findViewById(R.id.activity_detail_sample_scale_textView);
        serialTextView = findViewById(R.id.activity_detail_sample_serial_textView);
        statusTextView = findViewById(R.id.activity_detail_sample_status_textView);
        referenceHintTextView = findViewById(R.id.activity_detail_sample_reference_hint_textView);
        referenceTextView = findViewById(R.id.activity_detail_sample_reference_textView);
        caseTextView = findViewById(R.id.activity_detail_sample_case_textView);
        roomTextView = findViewById(R.id.activity_detail_sample_room_textView);
        actionTextView = findViewById(R.id.activity_detail_sample_action_textView);
        generalTextView = findViewById(R.id.activity_detail_sample_general_textView);
        prerequisiteTextView = findViewById(R.id.activity_detail_sample_prerequisite_textView);
        testTextView = findViewById(R.id.activity_detail_sample_test_textView);

        retryImageView = findViewById(R.id.activity_detail_sample_retry_imageView);
        statusImageView = findViewById(R.id.activity_detail_sample_status_imageView);
        referenceHintImageView = findViewById(R.id.activity_detail_sample_reference_hint_imageView);
        downloadImageView = findViewById(R.id.activity_detail_sample_download_imageView);

        editCheckbox = findViewById(R.id.activity_detail_sample_edit_checkbox);

        resultRecyclerView = findViewById(R.id.activity_detail_sample_result_recyclerView);
        resultRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, (int) getResources().getDimension(R.dimen._4sdp), 0));
        resultRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        resultRecyclerView.setHasFixedSize(false);
        generalRecyclerView = findViewById(R.id.activity_detail_sample_general_recyclerView);
        generalRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, (int) getResources().getDimension(R.dimen._4sdp), 0));
        generalRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        generalRecyclerView.setHasFixedSize(false);
        prerequisiteRecyclerView = findViewById(R.id.activity_detail_sample_prerequisite_recyclerView);
        prerequisiteRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, (int) getResources().getDimension(R.dimen._4sdp), 0));
        prerequisiteRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        prerequisiteRecyclerView.setHasFixedSize(false);
        testRecyclerView = findViewById(R.id.activity_detail_sample_test_recyclerView);
        testRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, (int) getResources().getDimension(R.dimen._4sdp), 0));
        testRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        testRecyclerView.setHasFixedSize(false);

        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        mainLayout = findViewById(R.id.activity_detail_sample_mainLayout);
        retryLayout = findViewById(R.id.activity_detail_sample_retryLayout);
        loadingLayout = findViewById(R.id.activity_detail_sample_loadingLayout);
        referenceLinearLayout = findViewById(R.id.activity_detail_sample_reference_linearLayout);
        caseLinearLayout = findViewById(R.id.activity_detail_sample_case_linearLayout);
        roomLinearLayout = findViewById(R.id.activity_detail_sample_room_linearLayout);

        loadingCardView = findViewById(R.id.activity_detail_sample_loading_cardView);
        resultCardView = findViewById(R.id.activity_detail_sample_result_cardView);
        componentCardView = findViewById(R.id.activity_detail_sample_component_cardView);

        downloadDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(downloadDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        downloadDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        downloadDialog.setContentView(R.layout.dialog_search);
        downloadDialog.setCancelable(true);

        WindowManager.LayoutParams layoutParamsDownload = new WindowManager.LayoutParams();
        layoutParamsDownload.copyFrom(downloadDialog.getWindow().getAttributes());
        layoutParamsDownload.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsDownload.height = WindowManager.LayoutParams.WRAP_CONTENT;
        downloadDialog.getWindow().setAttributes(layoutParamsDownload);

        downloadDialogTitleTextView = downloadDialog.findViewById(R.id.dialog_search_title_textView);
        downloadDialogTitleTextView.setText(getResources().getString(R.string.DetailDownloadDialogTitle));

        downloadDialogRecyclerView = downloadDialog.findViewById(R.id.dialog_search_recyclerView);
        downloadDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        downloadDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        downloadDialogRecyclerView.setHasFixedSize(true);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            downloadImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbarImageView.setOnClickListener(v -> {
            toolbarImageView.setClickable(false);
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 300);

            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        retrySpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                relaunchProcess("getGeneral");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.PrimaryDark));
                textPaint.setUnderlineText(false);
            }
        };

        actionTextView.setOnClickListener(v -> {
            if (actionTextView.getText().toString().equals(getResources().getString(R.string.DetailSampleClose))) {
                setButton(actionTextView, false);

                doWork("close");
            } else if (actionTextView.getText().toString().equals(getResources().getString(R.string.DetailSampleScore))) {
                statusTextView.setText(getResources().getString(R.string.DetailSampleStatusScoring));
                statusTextView.setTextColor(getResources().getColor(R.color.MoonYellow));
                ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.MoonYellow));

                setButton(actionTextView, false);

                doWork("score");
            }
        });

        editCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                editCheckbox.setTextColor(getResources().getColor(R.color.Nero));
                detailSampleAdapter.setEditable(true);
            } else {
                editCheckbox.setTextColor(getResources().getColor(R.color.Mischka));
                detailSampleAdapter.setEditable(false);
            }
        });

        downloadImageView.setOnClickListener(v -> {
            downloadImageView.setClickable(false);
            handler.postDelayed(() -> downloadImageView.setClickable(true), 300);

            downloadDialog.show();
        });

        downloadDialog.setOnCancelListener(dialog -> downloadDialog.dismiss());
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

    private void setButton(TextView button, boolean clickable) {
        if (clickable) {
            button.setClickable(true);
            button.setTextColor(getResources().getColor(R.color.White));

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                button.setBackgroundResource(R.drawable.draw_8sdp_solid_primary_ripple_primarydark);
            } else {
                button.setBackgroundResource(R.drawable.draw_8sdp_solid_primary);
            }
        } else {
            button.setClickable(false);
            button.setTextColor(getResources().getColor(R.color.Mischka));
            button.setBackgroundResource(R.drawable.draw_8sdp_border_quartz);
        }
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method) {
        switch (method) {
            case "getURLs":
                downloadDialogAdapter.setValue(arrayList, method, "DetailSample");
                recyclerView.setAdapter(downloadDialogAdapter);
                break;
            case "getPNGs":
                zoomageAdapter.setZoomages(arrayList);
                recyclerView.setAdapter(zoomageAdapter);
                break;
        }
    }

    private void setData() {
        try {
            JSONObject data = FileManager.readObjectFromCache(this, "sampleDetail" + "/" + sampleId);
            serialTextView.setText(data.getString("id"));

            if (data.has("scale") && !data.isNull("scale")) {
                JSONObject scale = data.getJSONObject("scale");

                scaleTitle = scale.getString("title");

                scaleTextView.setText(scaleTitle);

                switch (data.getString("status")) {
                    case "seald":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusSeald));
                        statusTextView.setTextColor(getResources().getColor(R.color.PrimaryDark));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.PrimaryDark));

                        actionTextView.setText(getResources().getString(R.string.DetailSampleClose));
                        setButton(actionTextView, true);

                        showLoading = false;
                        break;
                    case "open":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusOpen));
                        statusTextView.setTextColor(getResources().getColor(R.color.PrimaryDark));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.PrimaryDark));

                        actionTextView.setText(getResources().getString(R.string.DetailSampleClose));
                        setButton(actionTextView, true);

                        showLoading = false;
                        break;
                    case "closed":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusClosed));
                        statusTextView.setTextColor(getResources().getColor(R.color.PrimaryDark));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.PrimaryDark));

                        actionTextView.setText(getResources().getString(R.string.DetailSampleScore));
                        setButton(actionTextView, true);

                        showLoading = false;
                        break;
                    case "scoring":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusScoring));
                        statusTextView.setTextColor(getResources().getColor(R.color.MoonYellow));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.MoonYellow));

                        actionTextView.setVisibility(View.INVISIBLE);

                        showLoading = true;
                        break;
                    case "craeting_files":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusCreatingFiles));
                        statusTextView.setTextColor(getResources().getColor(R.color.MoonYellow));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.MoonYellow));

                        actionTextView.setVisibility(View.INVISIBLE);

                        showLoading = true;
                        break;
                    case "done":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusDone));
                        statusTextView.setTextColor(getResources().getColor(R.color.Mischka));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.Mischka));

                        actionTextView.setVisibility(View.INVISIBLE);

                        if (viewModel.getAllURLs() != null) {
                            setRecyclerView(viewModel.getAllURLs(), downloadDialogRecyclerView, "getURLs");
                            showCardView = true;
                        }
                        if (viewModel.getAllPNGs() != null) {
                            setRecyclerView(viewModel.getAllPNGs(), resultRecyclerView, "getPNGs");
                            showCardView = true;
                        }

                        if (showCardView) {
                            resultCardView.setVisibility(View.VISIBLE);
                            showLoading = false;
                        } else {
                            showLoading = true;
                        }

                        break;
                    default:
                        statusTextView.setText(data.getString("status"));
                        statusTextView.setTextColor(getResources().getColor(R.color.Mischka));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.Mischka));

                        actionTextView.setVisibility(View.INVISIBLE);

                        if (viewModel.getAllURLs() != null) {
                            setRecyclerView(viewModel.getAllURLs(), downloadDialogRecyclerView, "getURLs");
                            showCardView = true;
                        }
                        if (viewModel.getAllPNGs() != null) {
                            setRecyclerView(viewModel.getAllPNGs(), resultRecyclerView, "getPNGs");
                            showCardView = true;
                        }

                        if (showCardView) {
                            resultCardView.setVisibility(View.VISIBLE);
                            showLoading = false;
                        } else {
                            showLoading = true;
                        }

                        break;
                }
            }

            if (data.has("client") && !data.isNull("client")) {
                JSONObject client = data.getJSONObject("client");

                referenceHintTextView.setText(getResources().getString(R.string.DetailSampleReference));
                referenceHintImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_light));
                referenceTextView.setText(client.getString("name"));
            } else if (data.has("code") && !data.isNull("code")) {

                referenceHintTextView.setText(getResources().getString(R.string.DetailSampleCode));
                referenceHintImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_hashtag_light));
                referenceTextView.setText(data.getString("code"));
            } else {
                referenceLinearLayout.setVisibility(View.GONE);
            }

            if (data.has("case") && !data.isNull("case")) {
                JSONObject Case = data.getJSONObject("case");

                caseTextView.setText(Case.getString("name"));
            } else {
                caseLinearLayout.setVisibility(View.GONE);
            }

            if (data.has("room") && !data.isNull("room")) {
                JSONObject room = data.getJSONObject("room");
                JSONObject center = room.getJSONObject("center");
                JSONObject detail = center.getJSONObject("detail");

                roomTextView.setText(detail.getString("title"));
            } else {
                roomLinearLayout.setVisibility(View.GONE);
            }

            if (showLoading) {
                loadingCardView.setVisibility(View.VISIBLE);
                loadingCardView.setAnimation(animFadeIn);

                launchProcess("getScore");
            } else {
                loadingCardView.setVisibility(View.GONE);
                loadingCardView.setAnimation(animFadeOut);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void launchProcess(String method) {
        try {
            if (method.equals("getGeneral")) {
                viewModel.general(sampleId);
            } else if (method.equals("getScore")) {
                viewModel.scores(sampleId);
            }
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void relaunchProcess(String method) {
        loadingLayout.setVisibility(View.VISIBLE);
        retryLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);

        launchProcess(method);
    }

    private void doWork(String method) {
        try {
            switch (method) {
                case "score":
                    loadingCardView.setVisibility(View.VISIBLE);
                    loadingCardView.setAnimation(animFadeIn);
                    viewModel.score(sampleId);
                    break;
                case "close":
                    progressDialog.show();
                    viewModel.close(sampleId);
                    break;
            }
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void observeWork() {
        SampleRepository.workStateSample.observe((LifecycleOwner) this, integer -> {
            switch (SampleRepository.work) {
                case "getGeneral":
                    if (integer == 1) {
                        // Show General Detail

                        loadingLayout.setVisibility(View.GONE);
                        retryLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);

                        setData();
                    } else if (integer != -1) {
                        if (FileManager.readObjectFromCache(this, "sampleDetail" + "/" + sampleId) == null) {
                            if (integer == 0) {
                                // General Detail is Empty And Error

                                loadingLayout.setVisibility(View.GONE);
                                retryLayout.setVisibility(View.VISIBLE);
                                mainLayout.setVisibility(View.GONE);

                                setRetryLayout("error");

                                SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                            } else if (integer == -2) {
                                // General Detail is Empty And Connection

                                loadingLayout.setVisibility(View.GONE);
                                retryLayout.setVisibility(View.VISIBLE);
                                mainLayout.setVisibility(View.GONE);

                                setRetryLayout("connection");

                                SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                            }
                        } else {
                            // Show General Detail

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);

                            setData();
                        }
                    }
                    break;
                case "getPrerequisite":

                    break;
                case "getTest":

                    break;
                case "getScore":
                    if (integer == 1) {
                        // Reset Data

                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);

                        launchProcess("getGeneral");
                    } else if (integer != -1) {
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusClosed));
                        statusTextView.setTextColor(getResources().getColor(R.color.PrimaryDark));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.PrimaryDark));

                        actionTextView.setText(getResources().getString(R.string.DetailSampleScore));
                        setButton(actionTextView, true);

                        loadingCardView.setVisibility(View.GONE);
                        loadingCardView.setAnimation(animFadeOut);
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    }

                    break;
                case "score":
                    if (integer == 1) {
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);

                        launchProcess("getGeneral");
                        setResult(RESULT_OK, null);
                    } else if (integer == 0) {
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusClosed));
                        statusTextView.setTextColor(getResources().getColor(R.color.PrimaryDark));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.PrimaryDark));

                        actionTextView.setText(getResources().getString(R.string.DetailSampleScore));
                        setButton(actionTextView, true);

                        loadingCardView.setVisibility(View.GONE);
                        loadingCardView.setAnimation(animFadeOut);
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusClosed));
                        statusTextView.setTextColor(getResources().getColor(R.color.PrimaryDark));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.PrimaryDark));

                        actionTextView.setText(getResources().getString(R.string.DetailSampleScore));
                        setButton(actionTextView, true);

                        loadingCardView.setVisibility(View.GONE);
                        loadingCardView.setAnimation(animFadeOut);
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "close":
                    if (integer == 1) {
                        setResult(RESULT_OK, null);

                        try {
                            JSONObject jsonObject = FileManager.readObjectFromCache(this, "sampleDetail" + "/" + sampleId);
                            Objects.requireNonNull(jsonObject).put("status", "closed");
                            FileManager.writeObjectToCache(this, jsonObject, "sampleDetail" + "/" + sampleId);

                            setData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        actionTextView.setText(getResources().getString(R.string.DetailSampleClose));
                        setButton(actionTextView, true);

                        progressDialog.dismiss();
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        actionTextView.setText(getResources().getString(R.string.DetailSampleClose));
                        setButton(actionTextView, true);

                        progressDialog.dismiss();
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    }
                    break;
            }
        });
    }

    public void observeSearchAdapter(Model model, String method) {
        try {
            switch (method) {
                case "getURLs":
                    downloadUrl = model.get("url").toString();

                    if (PermissionManager.storagePermission(this)) {
                        IntentManager.download(this, downloadUrl);
                        downloadDialog.dismiss();
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                IntentManager.download(this, downloadUrl);
                downloadDialog.dismiss();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}