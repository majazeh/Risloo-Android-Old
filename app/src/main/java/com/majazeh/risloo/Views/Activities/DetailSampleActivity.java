package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.content.Intent;
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
import com.majazeh.risloo.Views.Adapters.DetailSampleAnswersAdapter;
import com.majazeh.risloo.Views.Adapters.SearchAdapter;
import com.majazeh.risloo.Views.Adapters.DetailSampleImagesAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class DetailSampleActivity extends AppCompatActivity {

    // ViewModels
    private SampleViewModel viewModel;

    // Adapters
    private DetailSampleAnswersAdapter detailSampleAnswersAdapter;
    private DetailSampleImagesAdapter detailSampleImagesAdapter;
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
    private FrameLayout mainLayout;
    private LinearLayout infoLayout, loadingLayout;
    private ImageView infoImageView;
    private TextView infoTextView;
    private TextView scaleTextView, serialTextView, statusTextView, referenceHintTextView, referenceTextView, caseTextView, roomTextView, actionTextView, generalTextView, prerequisiteTextView, answerTextView;
    private ImageView statusImageView, referenceHintImageView, downloadImageView;
    private LinearLayout referenceLinearLayout, caseLinearLayout, roomLinearLayout;
    private CheckBox editCheckbox;
    private RecyclerView resultRecyclerView, generalRecyclerView, prerequisiteRecyclerView, testRecyclerView;
    private CardView loadingCardView, resultCardView, componentCardView;
    private Dialog downloadDialog, progressDialog;
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

        getData("getGeneral");
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        viewModel = new ViewModelProvider(this).get(SampleViewModel.class);

        detailSampleAnswersAdapter = new DetailSampleAnswersAdapter(this);
        detailSampleImagesAdapter = new DetailSampleImagesAdapter(this);

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
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Gray900));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.DetailSampleTitle));

        mainLayout = findViewById(R.id.activity_detail_sample_mainLayout);
        infoLayout = findViewById(R.id.layout_info_linearLayout);
        loadingLayout = findViewById(R.id.layout_loading_linearLayout);

        infoImageView = findViewById(R.id.layout_info_imageView);
        infoTextView = findViewById(R.id.layout_info_textView);

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
        answerTextView = findViewById(R.id.activity_detail_sample_answer_textView);

        statusImageView = findViewById(R.id.activity_detail_sample_status_imageView);
        referenceHintImageView = findViewById(R.id.activity_detail_sample_reference_hint_imageView);
        downloadImageView = findViewById(R.id.activity_detail_sample_download_imageView);

        referenceLinearLayout = findViewById(R.id.activity_detail_sample_reference_linearLayout);
        caseLinearLayout = findViewById(R.id.activity_detail_sample_case_linearLayout);
        roomLinearLayout = findViewById(R.id.activity_detail_sample_room_linearLayout);

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
        testRecyclerView = findViewById(R.id.activity_detail_sample_answer_recyclerView);
        testRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, (int) getResources().getDimension(R.dimen._4sdp), 0));
        testRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        testRecyclerView.setHasFixedSize(false);

        loadingCardView = findViewById(R.id.activity_detail_sample_loading_cardView);
        resultCardView = findViewById(R.id.activity_detail_sample_result_cardView);
        componentCardView = findViewById(R.id.activity_detail_sample_component_cardView);

        downloadDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(downloadDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        downloadDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        downloadDialog.setContentView(R.layout.dialog_search);
        downloadDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParamsDownloadDialog = new WindowManager.LayoutParams();
        layoutParamsDownloadDialog.copyFrom(downloadDialog.getWindow().getAttributes());
        layoutParamsDownloadDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsDownloadDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        downloadDialog.getWindow().setAttributes(layoutParamsDownloadDialog);

        downloadDialogTitleTextView = downloadDialog.findViewById(R.id.dialog_search_title_textView);
        downloadDialogTitleTextView.setText(getResources().getString(R.string.DetailSampleDownloadDialogTitle));

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
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 250);

            finish();
            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
        });

        retrySpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                relaunchData("getGeneral");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.Risloo800));
                textPaint.setUnderlineText(false);
            }
        };

        actionTextView.setOnClickListener(v -> {
            if (actionTextView.getText().toString().equals(getResources().getString(R.string.DetailSampleClose))) {
                setButton(actionTextView, false);

                doWork("close");
            } else if (actionTextView.getText().toString().equals(getResources().getString(R.string.DetailSampleScore))) {
                statusTextView.setText(getResources().getString(R.string.DetailSampleStatusScoring));
                statusTextView.setTextColor(getResources().getColor(R.color.Yellow500));
                ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.Yellow500));

                setButton(actionTextView, false);

                doWork("score");
            }
        });

        editCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                editCheckbox.setTextColor(getResources().getColor(R.color.Gray900));
                detailSampleAnswersAdapter.setEditable(true);
            } else {
                editCheckbox.setTextColor(getResources().getColor(R.color.Gray300));
                detailSampleAnswersAdapter.setEditable(false);
            }
        });

        downloadImageView.setOnClickListener(v -> {
            downloadImageView.setClickable(false);
            handler.postDelayed(() -> downloadImageView.setClickable(true), 250);

            downloadDialog.show();
        });

        downloadDialog.setOnCancelListener(dialog -> downloadDialog.dismiss());
    }

    private void setInfoLayout(String type) {
        switch (type) {
            case "error":
                infoImageView.setImageResource(R.drawable.illu_error);
                infoTextView.setMovementMethod(LinkMovementMethod.getInstance());
                infoTextView.setText(StringManager.clickable(getResources().getString(R.string.AppInfoError), 21, 30, retrySpan));
                break;
            case "connection":
                infoImageView.setImageResource(R.drawable.illu_connection);
                infoTextView.setMovementMethod(LinkMovementMethod.getInstance());
                infoTextView.setText(StringManager.clickable(getResources().getString(R.string.AppInfoConnection), 17, 26, retrySpan));
                break;
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
            button.setTextColor(getResources().getColor(R.color.Gray300));
            button.setBackgroundResource(R.drawable.draw_8sdp_border_quartz);
        }
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method) {
        switch (method) {
            case "getURLs":
                downloadDialogAdapter.setValues(arrayList, method, "DetailSample");
                recyclerView.setAdapter(downloadDialogAdapter);
                break;
            case "getPNGs":
                detailSampleImagesAdapter.setImage(arrayList);
                recyclerView.setAdapter(detailSampleImagesAdapter);
                break;
        }
    }

    private void setData() {
        try {
            JSONObject data = FileManager.readObjectFromCache(this, "sampleDetail" + "/" + sampleId);

            // ID
            if (data.has("id") && !data.isNull("id") && !data.get("id").equals("")) {
                serialTextView.setText(data.get("id").toString());
            }

            // Scale
            if (data.has("scale") && !data.isNull("scale") && !data.get("scale").equals("")) {
                JSONObject scale = (JSONObject) data.get("scale");
                scaleTitle = scale.getString("title");

                scaleTextView.setText(scaleTitle);
            }

            // Version
            if (data.has("version") && !data.isNull("version") && !data.get("version").equals("")) {
                scaleTextView.append(" " + data.get("version").toString());
            }

            // Edition
            if (data.has("edition") && !data.isNull("edition") && !data.get("edition").equals("")) {
                scaleTextView.append(" " + data.get("edition").toString());
            }

            // Status
            if (data.has("status") && !data.isNull("status") && !data.get("status").equals("")) {
                switch (data.get("status").toString()) {
                    case "seald":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusSeald));
                        statusTextView.setTextColor(getResources().getColor(R.color.Risloo800));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.Risloo800));

                        actionTextView.setText(getResources().getString(R.string.DetailSampleClose));
                        setButton(actionTextView, true);

                        showLoading = false;
                        break;

                    case "open":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusOpen));
                        statusTextView.setTextColor(getResources().getColor(R.color.Risloo800));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.Risloo800));

                        actionTextView.setText(getResources().getString(R.string.DetailSampleClose));
                        setButton(actionTextView, true);

                        showLoading = false;
                        break;

                    case "closed":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusClosed));
                        statusTextView.setTextColor(getResources().getColor(R.color.Risloo800));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.Risloo800));

                        actionTextView.setText(getResources().getString(R.string.DetailSampleScore));
                        setButton(actionTextView, true);

                        showLoading = false;
                        break;

                    case "scoring":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusScoring));
                        statusTextView.setTextColor(getResources().getColor(R.color.Yellow500));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.Yellow500));

                        actionTextView.setVisibility(View.INVISIBLE);

                        showLoading = true;
                        break;

                    case "craeting_files":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusCreatingFiles));
                        statusTextView.setTextColor(getResources().getColor(R.color.Yellow500));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.Yellow500));

                        actionTextView.setVisibility(View.INVISIBLE);

                        showLoading = true;
                        break;

                    case "done":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusDone));
                        statusTextView.setTextColor(getResources().getColor(R.color.Gray300));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.Gray300));

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
                        statusTextView.setText(data.get("status").toString());
                        statusTextView.setTextColor(getResources().getColor(R.color.Gray300));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.Gray300));

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

            // Reference
            if (data.has("client") && !data.isNull("client") && !data.get("client").equals("")) {
                JSONObject client = (JSONObject) data.get("client");

                referenceHintTextView.setText(getResources().getString(R.string.DetailSampleReference));
                referenceHintImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_user_light));

                referenceTextView.setText(client.get("name").toString());
                referenceLinearLayout.setVisibility(View.VISIBLE);
            } else if (data.has("code") && !data.isNull("code")) {
                referenceHintTextView.setText(getResources().getString(R.string.DetailSampleCode));
                referenceHintImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_hashtag_light));

                referenceTextView.setText(data.get("code").toString());
                referenceLinearLayout.setVisibility(View.VISIBLE);
            } else {
                referenceLinearLayout.setVisibility(View.GONE);
            }

            // Case
            if (data.has("case") && !data.isNull("case") && !data.get("case").equals("")) {
                JSONObject casse = (JSONObject) data.get("case");

                caseTextView.setText(casse.get("id").toString());
                caseLinearLayout.setVisibility(View.VISIBLE);
            } else {
                caseLinearLayout.setVisibility(View.GONE);
            }

            // Room
            if (data.has("room") && !data.isNull("room") && !data.get("room").equals("")) {
                JSONObject room = (JSONObject) data.get("room");

                JSONObject manager = (JSONObject) room.get("manager");

                JSONObject center = (JSONObject) room.get("center");
                JSONObject detail = (JSONObject) center.get("detail");

                roomTextView.setText(detail.get("title").toString());
                roomLinearLayout.setVisibility(View.VISIBLE);
            } else {
                roomLinearLayout.setVisibility(View.GONE);
            }

            if (showLoading) {
                loadingCardView.setVisibility(View.VISIBLE);
                loadingCardView.setAnimation(animFadeIn);

                getData("getScore");
            } else {
                loadingCardView.setVisibility(View.GONE);
                loadingCardView.setAnimation(animFadeOut);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getData(String method) {
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

    private void relaunchData(String method) {
        loadingLayout.setVisibility(View.VISIBLE);
        infoLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);

        getData(method);
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
                        infoLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);

                        setData();
                    } else if (integer != -1) {
                        if (FileManager.readObjectFromCache(this, "sampleDetail" + "/" + sampleId) == null) {
                            // General Detail is Empty

                            loadingLayout.setVisibility(View.GONE);
                            infoLayout.setVisibility(View.VISIBLE);
                            mainLayout.setVisibility(View.GONE);

                            if (integer == 0) {
                                setInfoLayout("error"); // Show Error
                            } else if (integer == -2) {
                                setInfoLayout("connection"); // Show Connection
                            }

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        } else {
                            // Show General Detail

                            loadingLayout.setVisibility(View.GONE);
                            infoLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);

                            setData();
                        }
                    }
                    break;
                case "getPrerequisite":

                    break;
                case "getAnswer":

                    break;
                case "getScore":
                    if (integer == 1) {
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);

                        getData("getGeneral");
                    } else if (integer == 0) {
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusClosed));
                        statusTextView.setTextColor(getResources().getColor(R.color.Risloo800));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.Risloo800));

                        actionTextView.setText(getResources().getString(R.string.DetailSampleScore));
                        setButton(actionTextView, true);

                        loadingCardView.setVisibility(View.GONE);
                        loadingCardView.setAnimation(animFadeOut);
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusClosed));
                        statusTextView.setTextColor(getResources().getColor(R.color.Risloo800));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.Risloo800));

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

                        getData("getGeneral");
                        setResult(RESULT_OK, null);
                    } else if (integer == 0) {
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusClosed));
                        statusTextView.setTextColor(getResources().getColor(R.color.Risloo800));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.Risloo800));

                        actionTextView.setText(getResources().getString(R.string.DetailSampleScore));
                        setButton(actionTextView, true);

                        loadingCardView.setVisibility(View.GONE);
                        loadingCardView.setAnimation(animFadeOut);
                        Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusClosed));
                        statusTextView.setTextColor(getResources().getColor(R.color.Risloo800));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.Risloo800));

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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                relaunchData("getGeneral");
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}