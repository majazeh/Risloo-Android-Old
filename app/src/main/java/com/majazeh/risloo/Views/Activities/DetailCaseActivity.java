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

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.CaseRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.DateManager;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.ViewModels.CaseViewModel;
import com.majazeh.risloo.ViewModels.SessionViewModel;
import com.majazeh.risloo.Views.Adapters.DetailCaseReferencesAdapter;
import com.majazeh.risloo.Views.Adapters.DetailCaseSessionsAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailCaseActivity extends AppCompatActivity {

    // ViewModels
    private CaseViewModel caseViewModel;
    public SessionViewModel sessionViewModel;

    // Adapters
    private DetailCaseReferencesAdapter detailCaseReferencesAdapter;
    private DetailCaseSessionsAdapter detailCaseSessionsAdapter;

    // Vars
    public String caseId = "", caseName = "", roomId = "", roomName = "", roomTitle = "", roomUrl = "";

    // Objects
    private Bundle extras;
    private Handler handler;
    private ClickableSpan retrySpan;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private FrameLayout mainLayout;
    private LinearLayout infoLayout, loadingLayout;
    private ImageView infoImageView;
    private TextView infoTextView;
    private CircleImageView roomAvatarImageView;
    private TextView nameTextView, roomTitleTextView, roomSubTitleTextView, roomTypeTextView, complaintTextView, createdCaseDateTextView, lastSessionDateTextView, sessionsCountTextView, testsCountTextView, addReferenceTextView, addSessionTextView;
    private RecyclerView referencesRecyclerView, sessionsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_detail_case);

        initializer();

        detector();

        listener();

        launchProcess("getGeneral");
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        caseViewModel = new ViewModelProvider(this).get(CaseViewModel.class);
        sessionViewModel = new ViewModelProvider(this).get(SessionViewModel.class);

        detailCaseReferencesAdapter = new DetailCaseReferencesAdapter(this);
        detailCaseSessionsAdapter = new DetailCaseSessionsAdapter(this);

        handler = new Handler();

        extras = getIntent().getExtras();
        caseId = Objects.requireNonNull(extras).getString("id");

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.DetailCaseTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        mainLayout = findViewById(R.id.activity_detail_case_mainLayout);
        infoLayout = findViewById(R.id.layout_info_linearLayout);
        loadingLayout = findViewById(R.id.layout_loading_linearLayout);

        infoImageView = findViewById(R.id.layout_info_imageView);
        infoTextView = findViewById(R.id.layout_info_textView);

        roomAvatarImageView = findViewById(R.id.activity_detail_case_room_avatar_imageView);

        nameTextView = findViewById(R.id.activity_detail_case_name_textView);
        roomTitleTextView = findViewById(R.id.activity_detail_case_room_title_textView);
        roomSubTitleTextView = findViewById(R.id.activity_detail_case_room_subtitle_textView);
        roomTypeTextView = findViewById(R.id.activity_detail_case_room_type_textView);
        complaintTextView = findViewById(R.id.activity_detail_case_complaint_textView);
        createdCaseDateTextView = findViewById(R.id.activity_detail_case_created_case_date_textView);
        lastSessionDateTextView = findViewById(R.id.activity_detail_case_last_session_date_textView);
        sessionsCountTextView = findViewById(R.id.activity_detail_case_sessions_count_textView);
        testsCountTextView = findViewById(R.id.activity_detail_case_tests_count_textView);

        addReferenceTextView = findViewById(R.id.activity_detail_case_add_reference_textView);
        addSessionTextView = findViewById(R.id.activity_detail_case_add_session_textView);

        referencesRecyclerView = findViewById(R.id.activity_detail_case_references_recyclerView);
        referencesRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, (int) getResources().getDimension(R.dimen._4sdp), 0));
        referencesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        referencesRecyclerView.setHasFixedSize(false);
        sessionsRecyclerView = findViewById(R.id.activity_detail_case_sessions_recyclerView);
        sessionsRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, (int) getResources().getDimension(R.dimen._4sdp), 0));
        sessionsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        sessionsRecyclerView.setHasFixedSize(false);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            addReferenceTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_primary_ripple_primarydark);
            addSessionTextView.setBackgroundResource(R.drawable.draw_8sdp_solid_primary_ripple_primarydark);
        }
    }

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

        roomAvatarImageView.setOnClickListener(v -> {
            roomAvatarImageView.setClickable(false);
            handler.postDelayed(() -> roomAvatarImageView.setClickable(true), 300);

            if (!roomName.equals("") && !roomUrl.equals("")) {
                Intent intent = (new Intent(this, ImageActivity.class));

                intent.putExtra("title", roomName);
                intent.putExtra("bitmap", false);
                intent.putExtra("image", roomUrl);

                startActivity(intent);
            }
        });

        addReferenceTextView.setOnClickListener(v -> {
            addReferenceTextView.setClickable(false);
            handler.postDelayed(() -> addReferenceTextView.setClickable(true), 300);


        });

        addSessionTextView.setOnClickListener(v -> {
            addSessionTextView.setClickable(false);
            handler.postDelayed(() -> addSessionTextView.setClickable(true), 300);


        });
    }

    private void setInfoLayout(String type) {
        switch (type) {
            case "error":
                infoImageView.setImageResource(R.drawable.illu_error);
                infoTextView.setMovementMethod(LinkMovementMethod.getInstance());
                infoTextView.setText(StringManager.clickable(getResources().getString(R.string.AppError), 21, 30, retrySpan));
                break;
            case "connection":
                infoImageView.setImageResource(R.drawable.illu_connection);
                infoTextView.setMovementMethod(LinkMovementMethod.getInstance());
                infoTextView.setText(StringManager.clickable(getResources().getString(R.string.AppConnection), 17, 26, retrySpan));
                break;
        }
    }

    private void setData() {
        try {
            JSONObject data = FileManager.readObjectFromCache(this, "caseDetail" + "/" + caseId);

            if (data.has("room") && !data.isNull("room")) {
                JSONObject room = data.getJSONObject("room");
                roomId = room.get("id").toString();

                JSONObject manager = room.getJSONObject("manager");
                if (manager.has("name") && !manager.isNull("name")) {
                    roomName = manager.get("name").toString();
                    roomTitleTextView.setText(roomName);
                } else {
                    roomTitleTextView.setText("-");
                }

                JSONObject center = room.getJSONObject("center");
                if (center.has("detail") && !center.isNull("detail")) {
                    JSONObject centerDetail = center.getJSONObject("detail");

                    if (centerDetail.has("title") && !centerDetail.isNull("title")) {
                        roomTitle = centerDetail.get("title").toString();
                        roomTypeTextView.setText(roomTitle);
                    } else {
                        roomTypeTextView.setText("-");
                    }

                    if (centerDetail.has("avatar") && !centerDetail.isNull("avatar")) {
                        JSONObject avatar = centerDetail.getJSONObject("avatar");
                        JSONObject medium = avatar.getJSONObject("medium");

                        roomUrl = medium.get("url").toString();

                        Picasso.get().load(roomUrl).placeholder(R.color.Solitude).into(roomAvatarImageView);

                        roomSubTitleTextView.setVisibility(View.GONE);
                    } else {
                        Picasso.get().load(R.color.Solitude).placeholder(R.color.Solitude).into(roomAvatarImageView);

                        roomSubTitleTextView.setVisibility(View.VISIBLE);
                        roomSubTitleTextView.setText(roomName.charAt(0) + String.valueOf(roomName.substring(roomName.lastIndexOf(" ") + 1).charAt(0)));
                    }
                }

            }

            if (data.has("detail") && !data.isNull("detail")) {
                JSONObject detail = data.getJSONObject("detail");

                if (detail.has("chief_complaint") && !detail.isNull("chief_complaint")) {
                    complaintTextView.setText(detail.get("chief_complaint").toString());
                } else {
                    complaintTextView.setText("-");
                }
            }



            if (data.has("created_at") && !data.isNull("created_at")) {
                String date = DateManager.gregorianToJalali(DateManager.dateToString("yyyy-MM-dd", DateManager.timestampToDate(Long.parseLong(data.get("created_at").toString()))));
                String time = DateManager.dateToString("HH:mm", DateManager.timestampToDate(Long.parseLong(data.get("created_at").toString())));

                createdCaseDateTextView.setText(time + "  " + date);
            } else {
                createdCaseDateTextView.setText("-");
            }

            if (data.has("clients") && !data.isNull("clients")) {
                JSONArray clients = data.getJSONArray("clients");

                if (clients.length() != 0) {
                    StringBuilder reference = new StringBuilder();
                    reference.append(getResources().getString(R.string.DetailCaseName)).append(" ");

                    ArrayList<Model> references = new ArrayList<>();

                    for (int j = 0; j < clients.length(); j++) {
                        JSONObject client = clients.getJSONObject(j);
                        JSONObject user = client.getJSONObject("user");

                        references.add(new Model(client));

                        if (user.has("name") && !user.isNull("name")) {
                            if (j == clients.length() -1)
                                reference.append(user.get("name").toString());
                            else
                                reference.append(user.get("name").toString()).append(" - ");
                        }
                    }

                    caseName = reference.substring(14, reference.length());
                    nameTextView.setText(StringManager.foreground(reference.toString(), 14, reference.length(), getResources().getColor(R.color.PrimaryDark)));

                    detailCaseReferencesAdapter.setReference(references);
                    referencesRecyclerView.setAdapter(detailCaseReferencesAdapter);
                }
            }

            if (data.has("sessions") && !data.isNull("sessions")) {
                JSONArray sessions = data.getJSONArray("sessions");

                if (sessions.length() != 0) {
                    ArrayList<Model> meetings = new ArrayList<>();

                    for (int j = 0; j < sessions.length(); j++) {
                        JSONObject session = sessions.getJSONObject(j);

                        meetings.add(new Model(session));
                    }

                    detailCaseSessionsAdapter.setSession(meetings);
                    sessionsRecyclerView.setAdapter(detailCaseSessionsAdapter);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void launchProcess(String method) {
        try {
            if (method.equals("getGeneral")) {
                caseViewModel.general(caseId);
            }
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void relaunchProcess(String method) {
        loadingLayout.setVisibility(View.VISIBLE);
        infoLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);

        launchProcess(method);
    }

    private void observeWork() {
        CaseRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (CaseRepository.work.equals("getGeneral")) {
                if (integer == 1) {
                    // Show General Detail

                    loadingLayout.setVisibility(View.GONE);
                    infoLayout.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);

                    setData();

                    CaseRepository.workState.removeObservers((LifecycleOwner) this);
                } else if (integer != -1) {
                    if (FileManager.readObjectFromCache(this, "caseDetail" + "/" + caseId) == null) {
                        // Detail is Empty

                        loadingLayout.setVisibility(View.GONE);
                        infoLayout.setVisibility(View.VISIBLE);
                        mainLayout.setVisibility(View.GONE);

                        if (integer == 0) {
                            setInfoLayout("error"); // Show Error
                        } else if (integer == -2) {
                            setInfoLayout("connection"); // Show Connection
                        }

                        CaseRepository.workState.removeObservers((LifecycleOwner) this);
                    } else {
                        // Show General Detail

                        loadingLayout.setVisibility(View.GONE);
                        infoLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        setData();

                        CaseRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                relaunchProcess("getGeneral");
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}