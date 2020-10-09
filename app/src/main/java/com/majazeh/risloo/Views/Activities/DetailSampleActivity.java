package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Managers.FileManager;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.SquareImageView;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.StringCustomizer;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.DetailSampleAdapter;
import com.majazeh.risloo.Views.Dialogs.DownloadDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class DetailSampleActivity extends AppCompatActivity {

    // ViewModels
    private SampleViewModel viewModel;

    // Adapters
    private DetailSampleAdapter adapter;

    // Vars
    private String sampleId = "", scaleTitle = "", svgUrl = "", pngUrl = "", htmlUrl = "", pdfUrl = "";
    private boolean showLoading = false, showCardView = false;

    // Objects
    private Bundle extras;
    private ClickableSpan retrySpan;
    private DownloadDialog downloadDialog;
    private Animation animFadeIn, animFadeOut;

    // Widgets
    private Toolbar toolbar;
    private TextView retryTextView, scaleTextView, serialTextView, statusTextView, referenceHintTextView, referenceTextView, caseTextView, roomTextView, scoreTextView, closeTextView, generalTextView, prerequisiteTextView, testTextView;
    private ImageView retryImageView, statusImageView, referenceHintImageView, downloadImageView;
    private SquareImageView resultSquareImageView;
    private CheckBox editCheckbox;
    private RecyclerView generalRecyclerView, prerequisiteRecyclerView, testRecyclerView;
    private Dialog progressDialog;
    private FrameLayout mainLayout;
    private LinearLayout retryLayout, loadingLayout, referenceLinearLayout, caseLinearLayout, roomLinearLayout;
    private CardView loadingCardView, resultCardView, componentCardView;

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

        adapter = new DetailSampleAdapter(this);

        downloadDialog = new DownloadDialog(this);

        extras = getIntent().getExtras();
        sampleId = Objects.requireNonNull(extras).getString("id");

        animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        toolbar = findViewById(R.id.activity_detail_sample_toolbar);

        retryTextView = findViewById(R.id.activity_detail_sample_retry_textView);
        retryTextView.setMovementMethod(LinkMovementMethod.getInstance());

        scaleTextView = findViewById(R.id.activity_detail_sample_scale_textView);
        serialTextView = findViewById(R.id.activity_detail_sample_serial_textView);
        statusTextView = findViewById(R.id.activity_detail_sample_status_textView);
        referenceHintTextView = findViewById(R.id.activity_detail_sample_reference_hint_textView);
        referenceTextView = findViewById(R.id.activity_detail_sample_reference_textView);
        caseTextView = findViewById(R.id.activity_detail_sample_case_textView);
        roomTextView = findViewById(R.id.activity_detail_sample_room_textView);
        scoreTextView = findViewById(R.id.activity_detail_sample_score_textView);
        closeTextView = findViewById(R.id.activity_detail_sample_close_textView);
        generalTextView = findViewById(R.id.activity_detail_sample_general_textView);
        prerequisiteTextView = findViewById(R.id.activity_detail_sample_prerequisite_textView);
        testTextView = findViewById(R.id.activity_detail_sample_test_textView);

        retryImageView = findViewById(R.id.activity_detail_sample_retry_imageView);
        statusImageView = findViewById(R.id.activity_detail_sample_status_imageView);
        referenceHintImageView = findViewById(R.id.activity_detail_sample_reference_hint_imageView);
        downloadImageView = findViewById(R.id.activity_detail_sample_download_imageView);

        resultSquareImageView = findViewById(R.id.activity_detail_sample_result_squareImageView);

        editCheckbox = findViewById(R.id.activity_detail_sample_edit_checkbox);

        generalRecyclerView = findViewById(R.id.activity_detail_sample_general_recyclerView);
        generalRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", 0, (int) getResources().getDimension(R.dimen._4sdp), 0));
        generalRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        generalRecyclerView.setHasFixedSize(false);
        prerequisiteRecyclerView = findViewById(R.id.activity_detail_sample_prerequisite_recyclerView);
        prerequisiteRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", 0, (int) getResources().getDimension(R.dimen._4sdp), 0));
        prerequisiteRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        prerequisiteRecyclerView.setHasFixedSize(false);
        testRecyclerView = findViewById(R.id.activity_detail_sample_test_recyclerView);
        testRecyclerView.addItemDecoration(new ItemDecorator("verticalLayout", 0, (int) getResources().getDimension(R.dimen._4sdp), 0));
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
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            downloadImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        toolbar.setNavigationOnClickListener(v -> {
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

        downloadImageView.setOnClickListener(v -> {
            downloadDialog.show(this.getSupportFragmentManager(), "downloadBottomSheet");
            downloadDialog.getUrls(svgUrl, pngUrl, htmlUrl, pdfUrl);
        });

        resultSquareImageView.setOnClickListener(v -> {
            Intent intent = (new Intent(this, ImageActivity.class));

            intent.putExtra("title", scaleTitle);
            intent.putExtra("bitmap", false);
            intent.putExtra("image", pngUrl);

            startActivity(intent);
        });

        editCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                editCheckbox.setTextColor(getResources().getColor(R.color.Nero));
                adapter.setEditable(true);
            } else {
                editCheckbox.setTextColor(getResources().getColor(R.color.Mischka));
                adapter.setEditable(false);
            }
        });

        scoreTextView.setOnClickListener(v -> {
            statusTextView.setText(getResources().getString(R.string.DetailSampleStatusScoring));
            statusTextView.setTextColor(getResources().getColor(R.color.MoonYellow));
            ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.MoonYellow));

            setButton(scoreTextView, false);

            doWork("score");
        });

        closeTextView.setOnClickListener(v -> {
            setButton(closeTextView, false);

            doWork("close");
        });
    }

    private void setRetryLayout(String type) {
        if (type.equals("error")) {
            retryImageView.setImageResource(R.drawable.illu_error);
            retryTextView.setText(StringCustomizer.clickable(getResources().getString(R.string.AppError), 21, 30, retrySpan));
        } else if (type.equals("connection")) {
            retryImageView.setImageResource(R.drawable.illu_connection);
            retryTextView.setText(StringCustomizer.clickable(getResources().getString(R.string.AppConnection), 17, 26, retrySpan));
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

    private void setData() {
        try {
            JSONObject data = viewModel.readSampleDetailFromCache(sampleId);

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

                        setButton(scoreTextView, false);
                        setButton(closeTextView, true);

                        showLoading = false;
                        break;
                    case "open":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusOpen));
                        statusTextView.setTextColor(getResources().getColor(R.color.PrimaryDark));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.PrimaryDark));

                        setButton(scoreTextView, false);
                        setButton(closeTextView, true);

                        showLoading = false;
                        break;
                    case "closed":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusClosed));
                        statusTextView.setTextColor(getResources().getColor(R.color.PrimaryDark));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.PrimaryDark));

                        setButton(scoreTextView, true);
                        setButton(closeTextView, false);

                        showLoading = false;
                        break;
                    case "scoring":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusScoring));
                        statusTextView.setTextColor(getResources().getColor(R.color.MoonYellow));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.MoonYellow));

                        setButton(scoreTextView, false);
                        setButton(closeTextView, false);

                        showLoading = true;
                        break;
                    case "craeting_files":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusCreatingFiles));
                        statusTextView.setTextColor(getResources().getColor(R.color.MoonYellow));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.MoonYellow));

                        setButton(scoreTextView, false);
                        setButton(closeTextView, false);

                        showLoading = true;
                        break;
                    case "done":
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusDone));
                        statusTextView.setTextColor(getResources().getColor(R.color.Mischka));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.Mischka));

                        setButton(scoreTextView, false);
                        setButton(closeTextView, false);

                        if (viewModel.getSvgScore(sampleId) != null) {
                            svgUrl = viewModel.getSvgScore(sampleId);
                            showCardView = true;
                        }
                        if (viewModel.getPngScore(sampleId) != null) {
                            pngUrl = viewModel.getPngScore(sampleId);
                            Picasso.get().load(pngUrl).placeholder(R.color.Solitude).into(resultSquareImageView);
                            showCardView = true;
                        }
                        if (viewModel.getHtmlScore(sampleId) != null) {
                            htmlUrl = viewModel.getHtmlScore(sampleId);
                            showCardView = true;
                        }
                        if (viewModel.getPdfScore(sampleId) != null) {
                            pdfUrl = viewModel.getPdfScore(sampleId);
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

                        setButton(scoreTextView, false);
                        setButton(closeTextView, false);

                        if (viewModel.getSvgScore(sampleId) != null) {
                            svgUrl = viewModel.getSvgScore(sampleId);
                            showCardView = true;
                        }
                        if (viewModel.getPngScore(sampleId) != null) {
                            pngUrl = viewModel.getPngScore(sampleId);
                            Picasso.get().load(pngUrl).placeholder(R.color.Solitude).into(resultSquareImageView);
                            showCardView = true;
                        }
                        if (viewModel.getHtmlScore(sampleId) != null) {
                            htmlUrl = viewModel.getHtmlScore(sampleId);
                            showCardView = true;
                        }
                        if (viewModel.getPdfScore(sampleId) != null) {
                            pdfUrl = viewModel.getPdfScore(sampleId);
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
                        if (viewModel.readSampleDetailFromCache(sampleId) == null) {
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

                        setButton(scoreTextView, true);

                        loadingCardView.setVisibility(View.GONE);
                        loadingCardView.setAnimation(animFadeOut);
                        Toast.makeText(this, "aa" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "score":
                    if (integer == 1) {
                        setResult(RESULT_OK, null);
                    } else if (integer == 0) {
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusClosed));
                        statusTextView.setTextColor(getResources().getColor(R.color.PrimaryDark));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.PrimaryDark));

                        setButton(scoreTextView, true);

                        loadingCardView.setVisibility(View.GONE);
                        loadingCardView.setAnimation(animFadeOut);
                        Toast.makeText(this, "aa" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        statusTextView.setText(getResources().getString(R.string.DetailSampleStatusClosed));
                        statusTextView.setTextColor(getResources().getColor(R.color.PrimaryDark));
                        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.PrimaryDark));

                        setButton(scoreTextView, true);

                        loadingCardView.setVisibility(View.GONE);
                        loadingCardView.setAnimation(animFadeOut);
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    }
                    break;
                case "close":
                    if (integer == 1) {
                        setResult(RESULT_OK, null);

                        try {
                            JSONObject jsonObject = FileManager.readObjectFromCache(this, "sampleDetail", sampleId);
                            Objects.requireNonNull(jsonObject).put("status", "closed");
                            FileManager.writeObjectToCache(this, jsonObject, "sampleDetail", sampleId);

                            setData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    } else if (integer == 0) {
                        setButton(closeTextView, true);

                        progressDialog.dismiss();
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    } else if (integer == -2) {
                        setButton(closeTextView, true);

                        progressDialog.dismiss();
                        Toast.makeText(this, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    }
                    break;
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}