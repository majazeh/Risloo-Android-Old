package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.Models.Repositories.RoomRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.ViewModels.RoomViewModel;
import com.majazeh.risloo.Views.Adapters.SearchAdapter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class CreateRoomActivity extends AppCompatActivity {

//    // ViewModels
//    private RoomViewModel roomViewModel;
//    private CenterViewModel centerViewModel;
//
//    // Adapters
//    private SearchAdapter centerDialogAdapter, psychologyDialogAdapter;
//
//    // Vars
//    public String centerName = "", centerId = "", psychologyName = "", psychologyId = "";
//    private boolean centerException = false, psychologyException = false;
//
//    // Objects
//    private Bundle extras;
//    private Handler handler;
//    private ControlEditText controlEditText;
//
//    // Widgets
//    private RelativeLayout toolbarLayout;
//    private ImageView toolbarImageView;
//    private TextView toolbarTextView;
//    private FrameLayout centerFrameLayout, psychologyFrameLayout;
//    private LinearLayout centerLinearLayout, psychologyLinearLayout;
//    public TextView centerNameTextView, centerIdTextView, psychologyNameTextView, psychologyIdTextView;
//    private Button createButton;
//    private Dialog centerDialog, psychologyDialog, progressDialog;
//    private TextView centerDialogTitleTextView, psychologyDialogTitleTextView;
//    private CoordinatorLayout centerDialogSearchLayout, psychologyDialogSearchLayout;
//    private EditText centerDialogEditText, psychologyDialogEditText;
//    private ImageView centerDialogImageView, psychologyDialogImageView;
//    private ProgressBar centerDialogProgressBar, psychologyDialogProgressBar;
//    private TextView centerDialogTextView, psychologyDialogTextView;
//    private RecyclerView centerDialogRecyclerView, psychologyDialogRecyclerView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        decorator();
//
//        setContentView(R.layout.activity_create_room);
//
//        initializer();
//
//        detector();
//
//        listener();
//
//        setData();
//    }
//
//    private void decorator() {
//        WindowDecorator windowDecorator = new WindowDecorator();
//
//        windowDecorator.lightShowSystemUI(this);
//        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
//    }
//
//    private void initializer() {
//        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);
//        centerViewModel = new ViewModelProvider(this).get(CenterViewModel.class);
//
//        centerDialogAdapter = new SearchAdapter(this);
//        psychologyDialogAdapter = new SearchAdapter(this);
//
//        extras = getIntent().getExtras();
//
//        handler = new Handler();
//
//        controlEditText = new ControlEditText();
//
//        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
//        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));
//
//        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
//        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
//        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));
//
//        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
//        toolbarTextView.setText(getResources().getString(R.string.CreateRoomTitle));
//        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));
//
//        centerFrameLayout = findViewById(R.id.activity_create_room_center_frameLayout);
//        psychologyFrameLayout = findViewById(R.id.activity_create_room_psychology_frameLayout);
//
//        centerLinearLayout = findViewById(R.id.activity_create_room_center_linearLayout);
//        psychologyLinearLayout = findViewById(R.id.activity_create_room_psychology_linearLayout);
//
//        centerNameTextView = findViewById(R.id.activity_create_room_center_name_textView);
//        centerIdTextView = findViewById(R.id.activity_create_room_center_id_textView);
//        psychologyNameTextView = findViewById(R.id.activity_create_room_psychology_name_textView);
//        psychologyIdTextView = findViewById(R.id.activity_create_room_psychology_id_textView);
//
//        createButton = findViewById(R.id.activity_create_room_button);
//
//        centerDialog = new Dialog(this, R.style.DialogTheme);
//        Objects.requireNonNull(centerDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
//        centerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        centerDialog.setContentView(R.layout.dialog_search);
//        centerDialog.setCancelable(true);
//        psychologyDialog = new Dialog(this, R.style.DialogTheme);
//        Objects.requireNonNull(psychologyDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
//        psychologyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        psychologyDialog.setContentView(R.layout.dialog_search);
//        psychologyDialog.setCancelable(true);
//        progressDialog = new Dialog(this, R.style.DialogTheme);
//        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
//        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        progressDialog.setContentView(R.layout.dialog_progress);
//        progressDialog.setCancelable(false);
//
//        WindowManager.LayoutParams layoutParamsCenterDialog = new WindowManager.LayoutParams();
//        layoutParamsCenterDialog.copyFrom(centerDialog.getWindow().getAttributes());
//        layoutParamsCenterDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
//        layoutParamsCenterDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        centerDialog.getWindow().setAttributes(layoutParamsCenterDialog);
//        WindowManager.LayoutParams layoutParamsPsychologyDialog = new WindowManager.LayoutParams();
//        layoutParamsPsychologyDialog.copyFrom(psychologyDialog.getWindow().getAttributes());
//        layoutParamsPsychologyDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
//        layoutParamsPsychologyDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        psychologyDialog.getWindow().setAttributes(layoutParamsPsychologyDialog);
//
//        centerDialogTitleTextView = centerDialog.findViewById(R.id.dialog_search_title_textView);
//        centerDialogTitleTextView.setText(getResources().getString(R.string.CreateRoomCenterDialogTitle));
//        psychologyDialogTitleTextView = psychologyDialog.findViewById(R.id.dialog_search_title_textView);
//        psychologyDialogTitleTextView.setText(getResources().getString(R.string.CreateRoomPsychologyDialogTitle));
//
//        centerDialogSearchLayout = centerDialog.findViewById(R.id.dialog_search_coordinatorLayout);
//        centerDialogSearchLayout.setVisibility(View.VISIBLE);
//        psychologyDialogSearchLayout = psychologyDialog.findViewById(R.id.dialog_search_coordinatorLayout);
//        psychologyDialogSearchLayout.setVisibility(View.VISIBLE);
//
//        centerDialogEditText = centerDialog.findViewById(R.id.dialog_search_editText);
//        psychologyDialogEditText = psychologyDialog.findViewById(R.id.dialog_search_editText);
//
//        centerDialogImageView = centerDialog.findViewById(R.id.dialog_search_imageView);
//        psychologyDialogImageView = psychologyDialog.findViewById(R.id.dialog_search_imageView);
//
//        centerDialogProgressBar = centerDialog.findViewById(R.id.dialog_search_progressBar);
//        psychologyDialogProgressBar = psychologyDialog.findViewById(R.id.dialog_search_progressBar);
//
//        centerDialogTextView = centerDialog.findViewById(R.id.dialog_search_textView);
//        psychologyDialogTextView = psychologyDialog.findViewById(R.id.dialog_search_textView);
//
//        centerDialogRecyclerView = centerDialog.findViewById(R.id.dialog_search_recyclerView);
//        centerDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
//        centerDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        centerDialogRecyclerView.setHasFixedSize(true);
//
//        psychologyDialogRecyclerView = psychologyDialog.findViewById(R.id.dialog_search_recyclerView);
//        psychologyDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
//        psychologyDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//        psychologyDialogRecyclerView.setHasFixedSize(true);
//    }
//
//    private void detector() {
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
//
//            createButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
//        }
//    }
//
//    @SuppressLint("ClickableViewAccessibility")
//    private void listener() {
//        toolbarImageView.setOnClickListener(v -> {
//            toolbarImageView.setClickable(false);
//            handler.postDelayed(() -> toolbarImageView.setClickable(true), 250);
//
//            finish();
//            overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
//        });
//
//        centerLinearLayout.setOnClickListener(v -> {
//            centerLinearLayout.setClickable(false);
//            handler.postDelayed(() -> centerLinearLayout.setClickable(true), 250);
//
//            if (centerException) {
//                clearException("center");
//            }
//
//            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
//                controlEditText.clear(this, controlEditText.input());
//            }
//
//            centerDialog.show();
//        });
//
//        psychologyLinearLayout.setOnClickListener(v -> {
//            psychologyLinearLayout.setClickable(false);
//            handler.postDelayed(() -> psychologyLinearLayout.setClickable(true), 250);
//
//            if (psychologyException) {
//                clearException("psychology");
//            }
//
//            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
//                controlEditText.clear(this, controlEditText.input());
//            }
//
//            psychologyDialog.show();
//        });
//
//        createButton.setOnClickListener(v -> {
//            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
//                controlEditText.clear(this, controlEditText.input());
//            }
//
//            if (centerId.equals("")) {
//                errorException("center");
//            }
//            if (psychologyId.equals("")) {
//                errorException("psychology");
//            }
//
//            if (!centerId.equals("") && !psychologyId.equals("")) {
//                clearException("center");
//                clearException("psychology");
//
//                doWork();
//            }
//        });
//
//        centerDialogEditText.setOnTouchListener((v, event) -> {
//            if (MotionEvent.ACTION_UP == event.getAction()) {
//                if (!centerDialogEditText.hasFocus()) {
//                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
//                        controlEditText.clear(this, controlEditText.input());
//                    }
//
//                    controlEditText.focus(centerDialogEditText);
//                    controlEditText.select(centerDialogEditText);
//                }
//            }
//            return false;
//        });
//
//        psychologyDialogEditText.setOnTouchListener((v, event) -> {
//            if (MotionEvent.ACTION_UP == event.getAction()) {
//                if (!psychologyDialogEditText.hasFocus()) {
//                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
//                        controlEditText.clear(this, controlEditText.input());
//                    }
//
//                    controlEditText.focus(psychologyDialogEditText);
//                    controlEditText.select(psychologyDialogEditText);
//                }
//            }
//            return false;
//        });
//
//        centerDialogEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                handler.removeCallbacksAndMessages(null);
//                handler.postDelayed(() -> {
//                    if (centerDialogEditText.length() != 0) {
//                        getData("getCenters", centerDialogEditText.getText().toString().trim());
//                    } else {
//                        centerDialogRecyclerView.setAdapter(null);
//
//                        if (centerDialogTextView.getVisibility() == View.VISIBLE) {
//                            centerDialogTextView.setVisibility(View.GONE);
//                        }
//                    }
//                }, 750);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        psychologyDialogEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                handler.removeCallbacksAndMessages(null);
//                handler.postDelayed(() -> {
//                    if (psychologyDialogEditText.length() != 0) {
//                        getData("getPsychologies", psychologyDialogEditText.getText().toString().trim());
//                    } else {
//                        psychologyDialogRecyclerView.setAdapter(null);
//
//                        if (psychologyDialogTextView.getVisibility() == View.VISIBLE) {
//                            psychologyDialogTextView.setVisibility(View.GONE);
//                        }
//                    }
//                }, 750);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        centerDialog.setOnCancelListener(dialog -> {
//            resetData("centerDialog");
//
//            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
//                controlEditText.clear(this, controlEditText.input());
//                controlEditText.input().getText().clear();
//
//                handler.removeCallbacksAndMessages(null);
//            }
//
//            centerDialog.dismiss();
//        });
//
//        psychologyDialog.setOnCancelListener(dialog -> {
//            resetData("psychologyDialog");
//
//            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
//                controlEditText.clear(this, controlEditText.input());
//                controlEditText.input().getText().clear();
//
//                handler.removeCallbacksAndMessages(null);
//            }
//
//            psychologyDialog.dismiss();
//        });
//    }
//
//    private void setData() {
//        if (!Objects.requireNonNull(extras).getBoolean("loaded")) {
//            setResult(RESULT_OK, null);
//        }
//
//        if (extras.getString("center_id") != null)
//            centerId = extras.getString("center_id");
//        if (extras.getString("center_name") != null)
//            centerName = extras.getString("center_name");
//        if (extras.getString("psychology_id") != null)
//            psychologyId = extras.getString("psychology_id");
//        if (extras.getString("psychology_name") != null)
//            psychologyName = extras.getString("psychology_name");
//
//        if (!centerId.equals("")) {
//            centerNameTextView.setText(centerName);
//            centerNameTextView.setTextColor(getResources().getColor(R.color.Grey));
//
//            centerIdTextView.setText(centerId);
//            centerIdTextView.setVisibility(View.VISIBLE);
//        }
//
//        if (!psychologyId.equals("")) {
//            psychologyNameTextView.setText(psychologyName);
//            psychologyNameTextView.setTextColor(getResources().getColor(R.color.Grey));
//
//            psychologyIdTextView.setText(psychologyId);
//            psychologyIdTextView.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method) {
//        switch (method) {
//            case "getCenters":
//                centerDialogAdapter.setValues(arrayList, method, "CreateRoom");
//                recyclerView.setAdapter(centerDialogAdapter);
//
//                if (arrayList.size() == 0) {
//                    centerDialogTextView.setVisibility(View.VISIBLE);
//                } else {
//                    if (centerDialogTextView.getVisibility() == View.VISIBLE) {
//                        centerDialogTextView.setVisibility(View.GONE);
//                    }
//                }
//                break;
//            case "getPsychologies":
//                psychologyDialogAdapter.setValues(arrayList, method, "CreateRoom");
//                recyclerView.setAdapter(psychologyDialogAdapter);
//
//                if (arrayList.size() == 0) {
//                    psychologyDialogTextView.setVisibility(View.VISIBLE);
//                } else {
//                    if (psychologyDialogTextView.getVisibility() == View.VISIBLE) {
//                        psychologyDialogTextView.setVisibility(View.GONE);
//                    }
//                }
//                break;
//        }
//    }
//
//    private void errorException(String type) {
//        switch (type) {
//            case "center":
//                centerException = true;
//                centerFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
//                break;
//            case "psychology":
//                psychologyException = true;
//                psychologyFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
//                break;
//        }
//    }
//
//    private void clearException(String type) {
//        switch (type) {
//            case "center":
//                centerException = false;
//                centerFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
//                break;
//            case "psychology":
//                psychologyException = false;
//                psychologyFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
//                break;
//        }
//    }
//
//    private void resetData(String method) {
//        switch (method) {
//            case "centerDialog":
//                CenterRepository.centers.clear();
//                centerDialogRecyclerView.setAdapter(null);
//
//                if (centerDialogTextView.getVisibility() == View.VISIBLE) {
//                    centerDialogTextView.setVisibility(View.GONE);
//                }
//                break;
//            case "psychologyDialog":
//                RoomRepository.psychologies.clear();
//                psychologyDialogRecyclerView.setAdapter(null);
//
//                if (psychologyDialogTextView.getVisibility() == View.VISIBLE) {
//                    psychologyDialogTextView.setVisibility(View.GONE);
//                }
//                break;
//        }
//    }
//
//    private void getData(String method, String roomId, String q) {
//        try {
//            switch (method) {
//                case "getCenters":
//                    centerDialogProgressBar.setVisibility(View.VISIBLE);
//                    centerDialogImageView.setVisibility(View.GONE);
//
//                    CenterRepository.allPage = 1;
//                    centerViewModel.centers(q);
//
//                    observeWork("centerViewModel");
//                    break;
//                case "getPsychologies":
//                    psychologyDialogProgressBar.setVisibility(View.VISIBLE);
//                    psychologyDialogImageView.setVisibility(View.GONE);
//
//                    roomViewModel.getPsychologists(centerId);
//
//                    observeWork("roomViewModel");
//                    break;
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void doWork() {
//        try {
//            progressDialog.show();
//
//            roomViewModel.create(centerId, psychologyId);
//            observeWork("roomViewModel");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void observeWork(String method) {
//        switch (method) {
//            case "roomViewModel":
//                RoomRepository.workState.observe((LifecycleOwner) this, integer -> {
//                    if (RoomRepository.work.equals("create")) {
//                        if (integer == 1) {
//                            setResult(RESULT_OK, null);
//                            finish();
//
//                            progressDialog.dismiss();
//                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
//                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
//                        } else if (integer == 0) {
//                            progressDialog.dismiss();
//                            observeException();
//                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
//                        } else if (integer == -2) {
//                            progressDialog.dismiss();
//                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
//                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
//                        }
//                    }
//                });
//                break;
//        }
//    }
//
//    private void observeException() {
//        if (ExceptionGenerator.current_exception.equals("create")) {
//            String exceptionToast = "";
//
//            if (!ExceptionGenerator.errors.isNull("counseling_center_id")) {
//                errorException("center");
//                exceptionToast = ExceptionGenerator.getErrorBody("counseling_center_id");
//            }
//            if (!ExceptionGenerator.errors.isNull("psychologist_id")) {
//                errorException("psychology");
//                if (exceptionToast.equals("")) {
//                    exceptionToast = ExceptionGenerator.getErrorBody("psychologist_id");
//                } else {
//                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("psychologist_id"));
//                }
//            }
//            Toast.makeText(this, exceptionToast, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void observeSearchAdapter(Model model, String method) {
//        try {
//            switch (method) {
//                case "getCenters":
//                    if (!centerId.equals(model.get("id").toString())) {
//                        centerId = model.get("id").toString();
//
//                        centerName = model.get("name").toString();
//
//                        centerNameTextView.setText(centerName);
//                        centerNameTextView.setTextColor(getResources().getColor(R.color.Grey));
//
//                        centerIdTextView.setText(centerId);
//                        centerIdTextView.setVisibility(View.VISIBLE);
//                    } else if (centerId.equals(model.get("id").toString())) {
//                        centerId = "";
//                        centerName = "";
//
//                        centerNameTextView.setText(getResources().getString(R.string.CreateRoomCenter));
//                        centerNameTextView.setTextColor(getResources().getColor(R.color.Mischka));
//
//                        centerIdTextView.setText(centerId);
//                        centerIdTextView.setVisibility(View.GONE);
//                    }
//
//                    resetData("centerDialog");
//
//                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
//                        controlEditText.clear(this, controlEditText.input());
//                        controlEditText.input().getText().clear();
//
//                        handler.removeCallbacksAndMessages(null);
//                    }
//
//                    centerDialog.dismiss();
//                    break;
//
//                case "getPsychologies":
//                    if (!psychologyId.equals(model.get("id").toString())) {
//                        psychologyId = model.get("id").toString();
//
//                        psychologyName = model.get("name").toString();
//
//                        psychologyNameTextView.setText(psychologyName);
//                        psychologyNameTextView.setTextColor(getResources().getColor(R.color.Grey));
//
//                        psychologyIdTextView.setText(psychologyId);
//                        psychologyIdTextView.setVisibility(View.VISIBLE);
//                    } else if (psychologyId.equals(model.get("id").toString())) {
//                        psychologyId = "";
//                        psychologyName = "";
//
//                        psychologyNameTextView.setText(getResources().getString(R.string.CreateRoomPsychology));
//                        psychologyNameTextView.setTextColor(getResources().getColor(R.color.Mischka));
//
//                        psychologyIdTextView.setText(psychologyId);
//                        psychologyIdTextView.setVisibility(View.GONE);
//                    }
//
//                    resetData("psychologyDialog");
//
//                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
//                        controlEditText.clear(this, controlEditText.input());
//                        controlEditText.input().getText().clear();
//
//                        handler.removeCallbacksAndMessages(null);
//                    }
//
//                    psychologyDialog.dismiss();
//                    break;
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
//    }

}