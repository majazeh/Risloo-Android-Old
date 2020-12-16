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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.RoomRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.ViewModels.RoomViewModel;
import com.majazeh.risloo.Views.Adapters.SearchAdapter;
import com.majazeh.risloo.Views.Adapters.SpinnerAdapter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class CreateUserActivity extends AppCompatActivity {

    // ViewModels
    private RoomViewModel roomViewModel;

    // Adapters
    private SearchAdapter referenceDialogAdapter;
    public SpinnerAdapter referenceRecyclerViewAdapter;

    // Vars
    public String roomId = "";
    private boolean referenceException = false;

    // Objects
    private Bundle extras;
    private Handler handler;
    private ControlEditText controlEditText;
    private FlexboxLayoutManager referenceLayoutManager;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private FrameLayout referenceFrameLayout;
    public TextView referenceTextView;
    private RecyclerView referenceRecyclerView;
    private Button createButton;
    private Dialog referenceDialog, progressDialog;
    private TextView referenceDialogTitleTextView, referenceDialogConfirm;
    private CoordinatorLayout referenceDialogSearchLayout;
    private EditText referenceDialogEditText;
    private ImageView referenceDialogImageView;
    private ProgressBar referenceDialogProgressBar;
    private TextView referenceDialogTextView;
    private RecyclerView referenceDialogRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_create_user);

        initializer();

        detector();

        listener();

        setData();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);

        referenceDialogAdapter = new SearchAdapter(this);

        referenceRecyclerViewAdapter = new SpinnerAdapter(this);

        extras = getIntent().getExtras();

        handler = new Handler();

        controlEditText = new ControlEditText();

        referenceLayoutManager = new FlexboxLayoutManager(this);
        referenceLayoutManager.setFlexDirection(FlexDirection.ROW);
        referenceLayoutManager.setFlexWrap(FlexWrap.WRAP);

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.CreateUserTitle));
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        referenceFrameLayout = findViewById(R.id.activity_create_user_reference_frameLayout);

        referenceTextView = findViewById(R.id.activity_create_user_reference_textView);

        referenceRecyclerView = findViewById(R.id.activity_create_user_reference_recyclerView);
        referenceRecyclerView.setLayoutManager(referenceLayoutManager);
        referenceRecyclerView.setHasFixedSize(false);

        createButton = findViewById(R.id.activity_create_user_button);

        referenceDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(referenceDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        referenceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        referenceDialog.setContentView(R.layout.dialog_search);
        referenceDialog.setCancelable(true);
        progressDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(progressDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.dialog_progress);
        progressDialog.setCancelable(false);

        WindowManager.LayoutParams layoutParamsReferenceDialog = new WindowManager.LayoutParams();
        layoutParamsReferenceDialog.copyFrom(referenceDialog.getWindow().getAttributes());
        layoutParamsReferenceDialog.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsReferenceDialog.height = WindowManager.LayoutParams.WRAP_CONTENT;
        referenceDialog.getWindow().setAttributes(layoutParamsReferenceDialog);

        referenceDialogTitleTextView = referenceDialog.findViewById(R.id.dialog_search_title_textView);
        referenceDialogTitleTextView.setText(getResources().getString(R.string.CreateUserReferenceDialogTitle));

        referenceDialogConfirm = referenceDialog.findViewById(R.id.dialog_search_confirm_textView);

        referenceDialogSearchLayout = referenceDialog.findViewById(R.id.dialog_search_coordinatorLayout);
        referenceDialogSearchLayout.setVisibility(View.VISIBLE);

        referenceDialogEditText = referenceDialog.findViewById(R.id.dialog_search_editText);

        referenceDialogImageView = referenceDialog.findViewById(R.id.dialog_search_imageView);

        referenceDialogProgressBar = referenceDialog.findViewById(R.id.dialog_search_progressBar);

        referenceDialogTextView = referenceDialog.findViewById(R.id.dialog_search_textView);

        referenceDialogRecyclerView = referenceDialog.findViewById(R.id.dialog_search_recyclerView);
        referenceDialogRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._4sdp), 0, 0));
        referenceDialogRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        referenceDialogRecyclerView.setHasFixedSize(true);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            createButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);

            referenceDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
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

        referenceRecyclerView.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (referenceException) {
                    clearException("reference");
                }

                if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                    controlEditText.clear(this, controlEditText.input());
                }

                referenceDialog.show();
            }
            return false;
        });

        createButton.setOnClickListener(v -> {
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
            }

            if (referenceRecyclerViewAdapter.getValues().size() == 0) {
                errorException("reference");
            }

            if (referenceRecyclerViewAdapter.getValues().size() != 0) {
                clearException("reference");

                doWork();
            }
        });

        referenceDialogEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!referenceDialogEditText.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(referenceDialogEditText);
                    controlEditText.select(referenceDialogEditText);
                }
            }
            return false;
        });

        referenceDialogEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {
                    if (referenceDialogEditText.length() != 0) {
                        getData("getReferences", roomId, referenceDialogEditText.getText().toString().trim());
                    } else {
                        referenceDialogRecyclerView.setAdapter(null);

                        if (referenceDialogTextView.getVisibility() == View.VISIBLE) {
                            referenceDialogTextView.setVisibility(View.GONE);
                        }
                    }
                }, 750);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        referenceDialogConfirm.setOnClickListener(v -> {
            resetData("referenceDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            referenceDialog.dismiss();
        });

        referenceDialog.setOnCancelListener(dialog -> {
            resetData("referenceDialog");

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();

                handler.removeCallbacksAndMessages(null);
            }

            referenceDialog.dismiss();
        });
    }

    private void setData() {
        if (!Objects.requireNonNull(extras).getBoolean("loaded")) {
            setResult(RESULT_OK, null);
        }

        if (extras.getString("room_id") != null)
            roomId = extras.getString("room_id");
    }

    private void setRecyclerView(ArrayList<Model> arrayList, RecyclerView recyclerView, String method) {
        switch (method) {
            case "references":
                referenceRecyclerViewAdapter.setValues(referenceRecyclerViewAdapter.getValues(), referenceRecyclerViewAdapter.getIds(), method, "CreateUser");
                recyclerView.setAdapter(referenceRecyclerViewAdapter);
                break;
            case "getReferences":
                referenceDialogAdapter.setValues(arrayList, method, "CreateUser");
                recyclerView.setAdapter(referenceDialogAdapter);

                if (arrayList.size() == 0) {
                    referenceDialogTextView.setVisibility(View.VISIBLE);
                } else {
                    if (referenceDialogTextView.getVisibility() == View.VISIBLE) {
                        referenceDialogTextView.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    private void errorException(String type) {
        switch (type) {
            case "reference":
                referenceException = true;
                referenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
                break;
        }
    }

    private void clearException(String type) {
        switch (type) {
            case "reference":
                referenceException = false;
                referenceFrameLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
                break;
        }
    }

    private void resetData(String method) {
        switch (method) {
            case "referenceDialog":
                RoomRepository.references.clear();
                referenceDialogRecyclerView.setAdapter(null);

                if (referenceDialogConfirm.getVisibility() == View.VISIBLE) {
                    referenceDialogConfirm.setVisibility(View.GONE);
                }

                if (referenceDialogTextView.getVisibility() == View.VISIBLE) {
                    referenceDialogTextView.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void getData(String method, String roomId, String q) {
        try {
            switch (method) {
                case "getReferences":
                    referenceDialogProgressBar.setVisibility(View.VISIBLE);
                    referenceDialogImageView.setVisibility(View.GONE);

                    roomViewModel.references(roomId, q, "create_case");

                    observeWork("roomViewModel");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doWork() {
//        try {
//            progressDialog.show();
//
//            userViewModel.create(roomId, referenceRecyclerViewAdapter.getIds());
//            observeWork("userViewModel");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void observeWork(String method) {
        switch (method) {
            case "userViewModel":
//                UserRepository.workState.observe((LifecycleOwner) this, integer -> {
//                    if (UserRepository.work.equals("create")) {
//                        if (integer == 1) {
//                            setResult(RESULT_OK, null);
//                            finish();
//
//                            progressDialog.dismiss();
//                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
//                            UserRepository.workState.removeObservers((LifecycleOwner) this);
//                        } else if (integer == 0) {
//                            progressDialog.dismiss();
//                            observeException();
//                            UserRepository.workState.removeObservers((LifecycleOwner) this);
//                        } else if (integer == -2) {
//                            progressDialog.dismiss();
//                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
//                            UserRepository.workState.removeObservers((LifecycleOwner) this);
//                        }
//                    }
//                });
                break;

            case "roomViewModel":
                RoomRepository.workState.observe((LifecycleOwner) this, integer -> {
                    if (RoomRepository.work.equals("getReferences")) {
                        if (integer == 1) {
                            setRecyclerView(RoomRepository.references, referenceDialogRecyclerView, "getReferences");

                            referenceDialogProgressBar.setVisibility(View.GONE);
                            referenceDialogImageView.setVisibility(View.VISIBLE);
                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == 0) {
                            referenceDialogProgressBar.setVisibility(View.GONE);
                            referenceDialogImageView.setVisibility(View.VISIBLE);
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer == -2) {
                            referenceDialogProgressBar.setVisibility(View.GONE);
                            referenceDialogImageView.setVisibility(View.VISIBLE);
                            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
                        }
                    }
                });
                break;
        }
    }

    private void observeException() {
        if (ExceptionGenerator.current_exception.equals("create")) {
            String exceptionToast = "";

            if (!ExceptionGenerator.errors.isNull("roomId")) {
                exceptionToast = ExceptionGenerator.getErrorBody("roomId");
            }
            if (!ExceptionGenerator.errors.isNull("client_id")) {
                errorException("reference");
                if (exceptionToast.equals("")) {
                    exceptionToast = ExceptionGenerator.getErrorBody("client_id");
                } else {
                    exceptionToast += (" و " + ExceptionGenerator.getErrorBody("client_id"));
                }
            }
            Toast.makeText(this, exceptionToast, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, ExceptionGenerator.fa_message_text, Toast.LENGTH_SHORT).show();
        }
    }

    public void observeSearchAdapter(Model model, String method) {
        try {
            switch (method) {
                case "getReferences":
                    int referencePosition = referenceRecyclerViewAdapter.getIds().indexOf(model.get("id").toString());

                    if (referencePosition == -1) {
                        referenceRecyclerViewAdapter.getValues().add(model);
                        referenceRecyclerViewAdapter.getIds().add(model.get("id").toString());

                        setRecyclerView(null, referenceRecyclerView, "references");
                    } else {
                        referenceRecyclerViewAdapter.removeValue(referencePosition);

                        referenceDialogAdapter.notifyDataSetChanged();
                    }

                    if (referenceRecyclerViewAdapter.getValues().size() == 0) {
                        referenceTextView.setVisibility(View.VISIBLE);

                        referenceDialogConfirm.setVisibility(View.GONE);
                    } else {
                        if (referenceTextView.getVisibility() == View.VISIBLE) {
                            referenceTextView.setVisibility(View.GONE);
                        }

                        if (referenceDialogConfirm.getVisibility() == View.GONE) {
                            referenceDialogConfirm.setVisibility(View.VISIBLE);
                        }
                    }
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}