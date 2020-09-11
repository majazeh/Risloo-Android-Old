package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
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
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Managers.FileManager;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.ItemDecorator;
import com.majazeh.risloo.Utils.StringCustomizer;
import com.majazeh.risloo.Utils.WindowDecorator;
import com.majazeh.risloo.ViewModels.SampleViewModel;
import com.majazeh.risloo.Views.Adapters.DetailSampleAdapter;
import com.majazeh.risloo.Views.Dialogs.DownloadDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailSampleActivity extends AppCompatActivity {

    // ViewModels
    private SampleViewModel viewModel;

    // Adapters
    private DetailSampleAdapter adapter;

    // Objects
    private ClickableSpan retrySpan;
    private DownloadDialog downloadDialog;
    private Bundle extras;
    String sampleId;

    // Widgets
    private Toolbar toolbar;
    private TextView retryTextView, scaleTextView, serialTextView, statusTextView, referenceTextView, caseTextView, roomTextView, scoreTextView, closeTextView, generalTextView, prerequisiteTextView, testTextView;
    private ImageView retryImageView, statusImageView, downloadImageView, resultImageView;
    private CheckBox editCheckbox;
    private RecyclerView generalRecyclerView, prerequisiteRecyclerView, testRecyclerView;
    private Dialog progressDialog;
    private LinearLayout mainLayout, retryLayout, loadingLayout, infoLinearLayout, referenceLinearLayout, caseLinearLayout, roomLinearLayout;
    private CardView resultCardView, componentCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_detail_sample);

        initializer();

        detector();

        listener();

        launchProcess("getGeneral");

        ///////////////////////////////////

        loadingLayout.setVisibility(View.GONE);
        retryLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);

        ImageViewCompat.setImageTintList(statusImageView, AppCompatResources.getColorStateList(this, R.color.MoonYellow));

        Picasso.get().load(R.drawable.example).placeholder(R.color.Solitude).into(resultImageView);

        generalRecyclerView.setAdapter(adapter);
        prerequisiteRecyclerView.setAdapter(adapter);
        testRecyclerView.setAdapter(adapter);

        setButton(scoreTextView, true);
        setButton(closeTextView, false);
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();
        windowDecorator.lightWindow(this, R.color.Snow, R.color.Snow);
    }

    private void initializer() {
        viewModel = ViewModelProviders.of(this).get(SampleViewModel.class);

        extras = getIntent().getExtras();
        sampleId = extras.getString("id");

        downloadDialog = new DownloadDialog(this);

        adapter = new DetailSampleAdapter(this, viewModel);

        toolbar = findViewById(R.id.activity_detail_sample_toolbar);

        retryTextView = findViewById(R.id.activity_detail_sample_retry_textView);
        retryTextView.setMovementMethod(LinkMovementMethod.getInstance());

        scaleTextView = findViewById(R.id.activity_detail_sample_scale_textView);
        serialTextView = findViewById(R.id.activity_detail_sample_serial_textView);
        statusTextView = findViewById(R.id.activity_detail_sample_status_textView);
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
        downloadImageView = findViewById(R.id.activity_detail_sample_download_imageView);
        resultImageView = findViewById(R.id.activity_detail_sample_result_imageView);

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
        progressDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        mainLayout = findViewById(R.id.activity_detail_sample_mainLayout);
        retryLayout = findViewById(R.id.activity_detail_sample_retryLayout);
        loadingLayout = findViewById(R.id.activity_detail_sample_loadingLayout);
        infoLinearLayout = findViewById(R.id.activity_detail_sample_info_linearLayout);
        referenceLinearLayout = findViewById(R.id.activity_detail_sample_reference_linearLayout);
        caseLinearLayout = findViewById(R.id.activity_detail_sample_case_linearLayout);
        roomLinearLayout = findViewById(R.id.activity_detail_sample_room_linearLayout);

        resultCardView = findViewById(R.id.activity_detail_sample_result_cardView);
        componentCardView = findViewById(R.id.activity_detail_sample_component_cardView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            scoreTextView.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
            closeTextView.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);

            downloadImageView.setBackgroundResource(R.drawable.draw_oval_snow_ripple);
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
                loadingLayout.setVisibility(View.VISIBLE);
                retryLayout.setVisibility(View.GONE);
                mainLayout.setVisibility(View.GONE);

                launchProcess("getGeneral");
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.PrimaryDark));
                textPaint.setUnderlineText(false);
            }
        };

        downloadImageView.setOnClickListener(v -> {
            downloadDialog.show(this.getSupportFragmentManager(), "downloadBottomSheet");
        });

        resultImageView.setOnClickListener(v -> {

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

        scoreTextView.setOnClickListener(v -> doWork("score"));

        closeTextView.setOnClickListener(v -> doWork("close"));
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

    private void setData(String method, boolean isOk) {
//        if (method.equals("getGeneral") && isOk) {
//            adapter.setValues(viewModel.getGeneral());
//            generalRecyclerView.setAdapter(adapter);
//        } else {
//            componentCardView.setVisibility(View.GONE);
//        }
    }

    private void setButton(TextView button, boolean clickable) {
        if (clickable) {
            button.setClickable(true);
            button.setTextColor(getResources().getColor(R.color.White));

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP)
                button.setBackgroundResource(R.drawable.draw_18sdp_primary_ripple);
            else
                button.setBackgroundResource(R.drawable.draw_18sdp_primary);
        } else {
            button.setClickable(false);
            button.setTextColor(getResources().getColor(R.color.Mischka));
            button.setBackgroundResource(R.drawable.draw_18sdp_quartz_border);
        }
    }

    private void launchProcess(String method) {
        try {
            viewModel.getGeneral(sampleId);
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doWork(String method) {
        progressDialog.show();
        try {
            switch (method) {
                case "score":
                    viewModel.score(sampleId);
                    break;
                case "close":
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
            if (SampleRepository.work == "getGeneral") {
                if (integer == 1) {
                    if (viewModel.readSampleDetail(sampleId) != null) {
                        try {
                        JSONObject data = viewModel.readSampleDetail(sampleId);
                            JSONObject scale = data.getJSONObject("scale");
                            if (data.has("client")&& !data.isNull("client")) {
                                JSONObject client = data.getJSONObject("client");
                                referenceTextView.setText(client.getString("name"));
                            }else{
                                referenceTextView.setText(data.getString("code"));

                            }
                            JSONObject room = data.getJSONObject("room");
                            JSONObject center = room.getJSONObject("center");
                            JSONObject detail = center.getJSONObject("detail");

                        // Show General Detail
                        scaleTextView.setText(scale.getString("title"));
                        serialTextView.setText(scale.getString("id"));
                        statusTextView.setText(data.getString("status"));
                        // TODO: no need
//                        caseTextView.setText();
                        roomTextView.setText(detail.getString("title"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        Log.e("data", String.valueOf(integer));
                        loadingLayout.setVisibility(View.GONE);
                        retryLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        setData(SampleRepository.work, true);

                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    } else {
                        // General Detail is Empty

                        loadingLayout.setVisibility(View.GONE);
                        retryLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        setData(SampleRepository.work, false);

                        SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                    }
                } else {
                    if (viewModel.readSampleDetail(sampleId) == null) {
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
                        if (integer != -1) {
                            // Show General Detail

                            loadingLayout.setVisibility(View.GONE);
                            retryLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);

                            setData(SampleRepository.work, true);

                            SampleRepository.workStateSample.removeObservers((LifecycleOwner) this);
                        }
                    }
                }
            } else if (SampleRepository.work == "getPrerequisite") {

            } else if (SampleRepository.work == "getTest") {

            } else if (SampleRepository.work == "score") {
                if (integer == 1) {
                    setResult(RESULT_OK, null);

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
            } else if (SampleRepository.work == "close") {
                if (integer == 1) {
                    setResult(RESULT_OK, null);
                    try {
                   JSONObject jsonObject = FileManager.readObjectFromCache(this,"sampleDetail",SampleRepository.sampleId);
                        jsonObject.put("status", "closed");
                        FileManager.writeObjectToCache(this, jsonObject,"sampleDetail",SampleRepository.sampleId);
                        statusTextView.setText("closed");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}