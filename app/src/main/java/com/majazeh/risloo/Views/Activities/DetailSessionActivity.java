package com.majazeh.risloo.Views.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.SessionRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Managers.PathManager;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.ViewModels.SessionViewModel;
import com.majazeh.risloo.Views.Adapters.DetailSessionPracticesAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailSessionActivity extends AppCompatActivity {

    // ViewModels
    public SessionViewModel sessionViewModel;

    // Adapters
    private DetailSessionPracticesAdapter detailSessionPracticesAdapter;

    // Vars
    public String sessionId = "", roomId = "", roomName = "", roomTitle = "", roomUrl = "", report = "", encryptionType = "";

    // Objects
    private Bundle extras;
    private Handler handler;
    private ClickableSpan retrySpan;
    private PathManager pathManager;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private FrameLayout mainLayout;
    private LinearLayout infoLayout, loadingLayout;
    private ImageView infoImageView;
    private TextView infoTextView;
    private CircleImageView roomAvatarImageView;
    private TextView roomTitleTextView, roomSubTitleTextView, roomTypeTextView, reportTextView, addReportTextView, emptyReportTextView, addPracticeTextView, emptyPracticesTextView;
    private RecyclerView practicesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_detail_session);

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
        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);

        detailSessionPracticesAdapter = new DetailSessionPracticesAdapter(this);

        handler = new Handler();

        pathManager = new PathManager();

        extras = getIntent().getExtras();
        sessionId = Objects.requireNonNull(extras).getString("id");

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.DetailSessionTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        mainLayout = findViewById(R.id.activity_detail_session_mainLayout);
        infoLayout = findViewById(R.id.layout_info_linearLayout);
        loadingLayout = findViewById(R.id.layout_loading_linearLayout);

        infoImageView = findViewById(R.id.layout_info_imageView);
        infoTextView = findViewById(R.id.layout_info_textView);

        roomAvatarImageView = findViewById(R.id.activity_detail_session_room_avatar_imageView);

        roomTitleTextView = findViewById(R.id.activity_detail_session_room_title_textView);
        roomSubTitleTextView = findViewById(R.id.activity_detail_session_room_subtitle_textView);
        roomTypeTextView = findViewById(R.id.activity_detail_session_room_type_textView);

        reportTextView = findViewById(R.id.activity_detail_session_report_textView);

        addReportTextView = findViewById(R.id.activity_detail_session_add_report_textView);
        addPracticeTextView = findViewById(R.id.activity_detail_session_add_practice_textView);

        emptyReportTextView = findViewById(R.id.activity_detail_session_report_empty_textView);
        emptyPracticesTextView = findViewById(R.id.activity_detail_session_practices_empty_textView);

        practicesRecyclerView = findViewById(R.id.activity_detail_session_practices_recyclerView);
        practicesRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, (int) getResources().getDimension(R.dimen._4sdp), 0));
        practicesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        practicesRecyclerView.setHasFixedSize(false);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            addReportTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_primary_ripple_primarydark);
            addPracticeTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_primary_ripple_primarydark);
        }
    }

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
                textPaint.setColor(getResources().getColor(R.color.PrimaryDark));
                textPaint.setUnderlineText(false);
            }
        };

        roomAvatarImageView.setOnClickListener(v -> {
            roomAvatarImageView.setClickable(false);
            handler.postDelayed(() -> roomAvatarImageView.setClickable(true), 250);

            if (!roomName.equals("") && !roomUrl.equals("")) {
                Intent intent = (new Intent(this, ImageActivity.class));

                intent.putExtra("title", roomName);
                intent.putExtra("bitmap", false);
                intent.putExtra("image", roomUrl);

                startActivity(intent);
            }
        });

        addReportTextView.setOnClickListener(v -> {
            addReportTextView.setClickable(false);
            handler.postDelayed(() -> addReportTextView.setClickable(true), 250);

            Intent createReportActivity = (new Intent(this, CreateReportActivity.class));

            createReportActivity.putExtra("loaded", true);
            createReportActivity.putExtra("session_id", sessionId);

            startActivityForResult(createReportActivity, 200);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });

        addPracticeTextView.setOnClickListener(v -> {
            addPracticeTextView.setClickable(false);
            handler.postDelayed(() -> addPracticeTextView.setClickable(true), 250);

            Intent createPracticeActivity = (new Intent(this, CreatePracticeActivity.class));

            createPracticeActivity.putExtra("loaded", true);
            createPracticeActivity.putExtra("session_id", sessionId);

            startActivityForResult(createPracticeActivity, 200);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });
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

    private void setData() {
        try {
            JSONObject data = FileManager.readObjectFromCache(this, "sessionDetail" + "/" + sessionId);

            // Case
            if (data.has("case") && !data.isNull("case")) {
                JSONObject casse = (JSONObject) data.get("case");

                // Room
                if (casse.has("room") && !casse.isNull("room")) {
                    JSONObject room = (JSONObject) casse.get("room");
                    roomId = room.get("id").toString();

                    JSONObject manager = (JSONObject) room.get("manager");
                    roomName = manager.get("name").toString();

                    roomTitleTextView.setText(roomName);

                    JSONObject center = (JSONObject) room.get("center");
                    JSONObject detail = (JSONObject) center.get("detail");
                    roomTitle = detail.get("title").toString();

                    roomTypeTextView.setText(roomTitle);

                    // Avatar
                    if (detail.has("avatar") && !detail.isNull("avatar")) {
                        JSONObject avatar = detail.getJSONObject("avatar");
                        JSONObject medium = avatar.getJSONObject("medium");
                        roomUrl = medium.get("url").toString();

                        Picasso.get().load(roomUrl).placeholder(R.color.Solitude).into(roomAvatarImageView);

                        roomSubTitleTextView.setVisibility(View.GONE);
                    } else {
                        Picasso.get().load(R.color.Solitude).placeholder(R.color.Solitude).into(roomAvatarImageView);

                        roomSubTitleTextView.setVisibility(View.VISIBLE);
                        roomSubTitleTextView.setText(StringManager.firstChars(roomName));
                    }
                }
            }

            // Report
            if (data.has("report") && !data.isNull("report")) {
                report = data.get("report").toString();

                reportTextView.setText(report);
                reportTextView.setVisibility(View.VISIBLE);
            } else {
                emptyReportTextView.setVisibility(View.VISIBLE);
            }

            // EncryptionType
            if (data.has("encryption_type") && !data.isNull("encryption_type")) {
                encryptionType = data.get("encryption_type").toString();
            }

            // Practices
            if (sessionViewModel.getPractices(sessionId) != null) {
                setRecyclerView(sessionViewModel.getPractices(sessionId), practicesRecyclerView, "practices");
            } else {
                emptyPracticesTextView.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method) {
        switch (method) {
            case "practices":
                detailSessionPracticesAdapter.setPractice(arrayList);
                recyclerView.setAdapter(detailSessionPracticesAdapter);
                break;
        }
    }

    private void getData(String method) {
        try {
            switch (method) {
                case "getGeneral":
                    sessionViewModel.general(sessionId);
                    break;
                case "getPractices":
                    sessionViewModel.practices(sessionId);
                    break;
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

    private void observeWork() {
        SessionRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (SessionRepository.work.equals("getGeneral")) {
                if (integer == 1) {
                    // Continue Get Practices
                    SessionRepository.workState.removeObservers((LifecycleOwner) this);

                    getData("getPractices");
                } else if (integer != -1) {
                    if (FileManager.readObjectFromCache(this, "sessionDetail" + "/" + sessionId) == null) {
                        // General Detail is Empty

                        loadingLayout.setVisibility(View.GONE);
                        infoLayout.setVisibility(View.VISIBLE);
                        mainLayout.setVisibility(View.GONE);

                        if (integer == 0) {
                            setInfoLayout("error"); // Show Error
                        } else if (integer == -2) {
                            setInfoLayout("connection"); // Show Connection
                        }

                        SessionRepository.workState.removeObservers((LifecycleOwner) this);
                    } else {
                        // Continue Get Practices
                        SessionRepository.workState.removeObservers((LifecycleOwner) this);

                        getData("getPractices");
                    }
                }
            } else if (SessionRepository.work.equals("getPractices")) {
                if (integer != -1) {
                    // Show Detail Session

                    loadingLayout.setVisibility(View.GONE);
                    infoLayout.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);

                    setData();

                    SessionRepository.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                IntentManager.file(this);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                Uri uri = Objects.requireNonNull(data).getData();

                String attachment = pathManager.getLocalPath(this, uri);

                detailSessionPracticesAdapter.doWork(sessionId, attachment);

            } else if (requestCode == 200) {
                relaunchData("getGeneral");
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (requestCode == 100) {
                ExceptionGenerator.getException(false, 0, null, "FileException");
                Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);

        FileManager.deleteFolderFromCache(this, "documents");
    }

}