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

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.majazeh.risloo.Models.Repositories.CenterRepository;
import com.majazeh.risloo.Models.Repositories.RoomRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.ViewModels.CenterViewModel;
import com.majazeh.risloo.ViewModels.RoomViewModel;
import com.majazeh.risloo.Views.Adapters.CentersAdapter;
import com.majazeh.risloo.Views.Adapters.RoomsAdapter;
import com.majazeh.risloo.Views.Adapters.UsersAdapter;

import org.json.JSONException;

import java.util.Objects;

public class UsersActivity extends AppCompatActivity {

    // ViewModels
    private AuthViewModel authViewModel;
    public CenterViewModel centerViewModel;
    public RoomViewModel roomViewModel;

    // Adapters
    private UsersAdapter usersRecyclerViewAdapter;
    private CentersAdapter centersRecyclerViewAdapter;
    private RoomsAdapter roomsRecyclerViewAdapter;

    // Vars
    private String search = "", type = "", clinicId = "", roomId = "", title = "";
    public boolean loading = false, finished = true;

    // Objects
    private Bundle extras;
    private Handler handler;
    private ControlEditText controlEditText;
    private LinearLayoutManager layoutManager;
    private ClickableSpan retrySpan;

    // Widgets
    private RelativeLayout toolbarLayout;
    private ImageView toolbarImageView, toolbarCreateImageView, toolbarSearchImageView;
    private TextView toolbarTextView;
    private LinearLayout searchLayout;
    private RelativeLayout mainLayout;
    private LinearLayout infoLayout, loadingLayout;
    private ImageView searchImageView, infoImageView;
    private TextView searchTextView, infoTextView;
    private RecyclerView usersRecyclerView, rcRecyclerView;
    public ProgressBar pagingProgressBar;
    private Dialog searchDialog;
    private TextView searchDialogTitle, searchDialogPositive, searchDialogNegative;
    private EditText searchDialogInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_users);

        initializer();

        detector();

        listener();

        setData();

        getData();
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        centerViewModel = new ViewModelProvider(this).get(CenterViewModel.class);
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);

        usersRecyclerViewAdapter = new UsersAdapter(this);
        centersRecyclerViewAdapter = new CentersAdapter(this);
        roomsRecyclerViewAdapter = new RoomsAdapter(this);

        extras = getIntent().getExtras();

        handler = new Handler();

        controlEditText = new ControlEditText();

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));
        toolbarCreateImageView = findViewById(R.id.layout_toolbar_secondary_imageView);
        toolbarCreateImageView.setVisibility(View.VISIBLE);
        toolbarCreateImageView.setImageResource(R.drawable.ic_plus_light);
        ImageViewCompat.setImageTintList(toolbarCreateImageView, AppCompatResources.getColorStateList(this, R.color.IslamicGreen));
        toolbarSearchImageView = findViewById(R.id.layout_toolbar_thirdly_imageView);
        toolbarSearchImageView.setImageResource(R.drawable.ic_search_light);
        ImageViewCompat.setImageTintList(toolbarSearchImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setTextColor(getResources().getColor(R.color.Nero));

        searchLayout = findViewById(R.id.activity_users_searchLayout);

        mainLayout = findViewById(R.id.activity_users_mainLayout);
        infoLayout = findViewById(R.id.layout_info_linearLayout);
        loadingLayout = findViewById(R.id.layout_loading_linearLayout);

        infoImageView = findViewById(R.id.layout_info_imageView);
        infoTextView = findViewById(R.id.layout_info_textView);

        searchImageView = findViewById(R.id.activity_users_search_imageView);
        searchTextView = findViewById(R.id.activity_users_search_textView);

        usersRecyclerView = findViewById(R.id.activity_users_recyclerView);
        usersRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._16sdp)));
        usersRecyclerView.setLayoutManager(layoutManager);
        usersRecyclerView.setHasFixedSize(true);
        rcRecyclerView = findViewById(R.id.activity_users_rc_recyclerView);
        rcRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._4sdp), (int) getResources().getDimension(R.dimen._16sdp)));
        rcRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rcRecyclerView.setHasFixedSize(true);

        pagingProgressBar = findViewById(R.id.activity_users_progressBar);

        searchDialog = new Dialog(this, R.style.DialogTheme);
        Objects.requireNonNull(searchDialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        searchDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        searchDialog.setContentView(R.layout.dialog_type);
        searchDialog.setCancelable(true);

        WindowManager.LayoutParams layoutParamsSearch = new WindowManager.LayoutParams();
        layoutParamsSearch.copyFrom(searchDialog.getWindow().getAttributes());
        layoutParamsSearch.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParamsSearch.height = WindowManager.LayoutParams.WRAP_CONTENT;
        searchDialog.getWindow().setAttributes(layoutParamsSearch);

        searchDialogTitle = searchDialog.findViewById(R.id.dialog_type_title_textView);
        searchDialogTitle.setText(getResources().getString(R.string.UsersSearchDialogTitle));
        searchDialogInput = searchDialog.findViewById(R.id.dialog_type_input_editText);
        searchDialogInput.setHint(getResources().getString(R.string.UsersSearchDialogInput));
        searchDialogInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        searchDialogPositive = searchDialog.findViewById(R.id.dialog_type_positive_textView);
        searchDialogPositive.setText(getResources().getString(R.string.UsersSearchDialogPositive));
        searchDialogPositive.setTextColor(getResources().getColor(R.color.PrimaryDark));
        searchDialogNegative = searchDialog.findViewById(R.id.dialog_type_negative_textView);
        searchDialogNegative.setText(getResources().getString(R.string.UsersSearchDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
            toolbarCreateImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);
            toolbarSearchImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            searchImageView.setBackgroundResource(R.drawable.draw_rectangle_solid_snow_ripple_violetred);
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

        toolbarCreateImageView.setOnClickListener(v -> {
            toolbarCreateImageView.setClickable(false);
            handler.postDelayed(() -> toolbarCreateImageView.setClickable(true), 250);

            Intent createUserActivity = (new Intent(this, CreateUserActivity.class));

            createUserActivity.putExtra("loaded", true);
            switch (type) {
                case "centers":
                    createUserActivity.putExtra("type", "center");
                    createUserActivity.putExtra("clinic_id", clinicId);
                    break;
                case "rooms":
                    createUserActivity.putExtra("type", "room");
                    createUserActivity.putExtra("room_id", roomId);
                    break;
            }

            startActivityForResult(createUserActivity, 100);
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.stay_still);
        });

        toolbarSearchImageView.setOnClickListener(v -> {
            toolbarSearchImageView.setClickable(false);
            handler.postDelayed(() -> toolbarSearchImageView.setClickable(true), 250);

            searchDialogInput.setText(search);

            searchDialog.show();
        });

        searchTextView.setOnClickListener(v -> {
            searchTextView.setClickable(false);
            handler.postDelayed(() -> searchTextView.setClickable(true), 250);

            searchDialogInput.setText(search);

            searchDialog.show();
        });

        searchImageView.setOnClickListener(v -> {
            searchImageView.setClickable(false);
            handler.postDelayed(() -> searchImageView.setClickable(true), 250);

            search = "";
            searchTextView.setText(search);

            relaunchData();

            searchDialog.dismiss();
        });

        retrySpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                relaunchData();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint textPaint) {
                textPaint.setColor(getResources().getColor(R.color.PrimaryDark));
                textPaint.setUnderlineText(false);
            }
        };

        usersRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {

                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                        try {
                            if (!loading) {
                                pagingProgressBar.setVisibility(View.VISIBLE);
                                switch (type) {
                                    case "centers":
                                        centerViewModel.users(clinicId);
                                        observeWork("centerViewModel");
                                        break;
                                    case "rooms":
                                        roomViewModel.users(roomId);
                                        observeWork("roomViewModel");
                                        break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        searchDialogInput.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!searchDialogInput.hasFocus()) {
                    if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                        controlEditText.clear(this, controlEditText.input());
                    }

                    controlEditText.focus(searchDialogInput);
                    controlEditText.select(searchDialogInput);
                }
            }
            return false;
        });

        searchDialogPositive.setOnClickListener(v -> {
            searchDialogPositive.setClickable(false);
            handler.postDelayed(() -> searchDialogPositive.setClickable(true), 250);

            if (searchDialogInput.length() != 0) {
                search = searchDialogInput.getText().toString().trim();

                searchTextView.setText(search);

                relaunchData();

                if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                    controlEditText.clear(this, controlEditText.input());
                    controlEditText.input().getText().clear();
                }

                searchDialog.dismiss();
            } else {
                errorException("searchDialog");
            }
        });

        searchDialogNegative.setOnClickListener(v -> {
            searchDialogNegative.setClickable(false);
            handler.postDelayed(() -> searchDialogNegative.setClickable(true), 250);

            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();
            }

            searchDialog.dismiss();
        });

        searchDialog.setOnCancelListener(dialog -> {
            if (controlEditText.input() != null && controlEditText.input().hasFocus()) {
                controlEditText.clear(this, controlEditText.input());
                controlEditText.input().getText().clear();
            }

            searchDialog.dismiss();
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
            case "empty":
                infoImageView.setImageResource(R.drawable.illu_empty);
                infoTextView.setMovementMethod(null);
                infoTextView.setText(getResources().getString(R.string.AppInfoEmpty));
                break;
            case "search":
                infoImageView.setImageResource(R.drawable.illu_empty);
                infoTextView.setMovementMethod(null);
                infoTextView.setText(getResources().getString(R.string.AppSearchEmpty));
                break;
        }
    }

    private void errorException(String type) {
        if (type.equals("searchDialog")) {
            searchDialogInput.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        }
    }

    private void resetData(String method) {
        if (method.equals("search")) {
            if (authViewModel.hasAccess()) {
                toolbarSearchImageView.setVisibility(View.GONE);
            } else {
                toolbarSearchImageView.setVisibility(View.GONE);
            }

            if (search.equals("")) {
                searchLayout.setVisibility(View.GONE);

                toolbarSearchImageView.setImageResource(R.drawable.ic_search_light);
                ImageViewCompat.setImageTintList(toolbarSearchImageView, AppCompatResources.getColorStateList(this, R.color.Nero));
            } else {
                if (searchLayout.getVisibility() == View.GONE) {
                    searchLayout.setVisibility(View.VISIBLE);

                    toolbarSearchImageView.setImageResource(R.drawable.ic_search_solid);
                    ImageViewCompat.setImageTintList(toolbarSearchImageView, AppCompatResources.getColorStateList(this, R.color.PrimaryDark));
                }
            }
        }
    }

    private void setData() {
        if (!Objects.requireNonNull(extras).getBoolean("loaded")) {
            setResult(RESULT_OK, null);
        }

        if (extras.getString("type") != null)
            type = extras.getString("type");
        if (extras.getString("clinic_id") != null)
            clinicId = extras.getString("clinic_id");
        if (extras.getString("clinic_name") != null)
            title = getResources().getString(R.string.UsersTitle) + " " + extras.getString("clinic_name");
        if (extras.getString("room_id") != null)
            roomId = extras.getString("room_id");
        if (extras.getString("room_name") != null)
            title = getResources().getString(R.string.UsersTitle) + " " + extras.getString("room_name");

        if (!title.equals("")) {
            toolbarTextView.setText(StringManager.foreground(title, 8, title.length(), getResources().getColor(R.color.PrimaryDark)));
        }
    }

    private void getData() {
        try {
            switch (type) {
                case "centers":
                    centerViewModel.users(clinicId);
                    CenterRepository.usersPage = 1;

                    observeWork("centerViewModel");
                    break;
                case "rooms":
                    roomViewModel.users(roomId);
                    RoomRepository.usersPage = 1;

                    observeWork("roomViewModel");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void relaunchData() {
        searchLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        infoLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);

        getData();
    }

    private void observeWork(String method) {
        switch (method) {
            case "centerViewModel":
                CenterRepository.workState.observe((LifecycleOwner) this, integer -> {
                    if (CenterRepository.work.equals("users")) {
                        finished = false;
                        loading = true;
                        if (integer == 1) {
                            if (centerViewModel.getUsers(clinicId) != null) {
                                // Show Users

                                loadingLayout.setVisibility(View.GONE);
                                infoLayout.setVisibility(View.GONE);
                                mainLayout.setVisibility(View.VISIBLE);

                                usersRecyclerViewAdapter.setUser(centerViewModel.getUsers(clinicId));
                                if (CenterRepository.usersPage == 1) {
                                    usersRecyclerView.setAdapter(usersRecyclerViewAdapter);
//                                    rcRecyclerView.setAdapter(centersRecyclerViewAdapter);
                                }
                            } else {
                                // User is Empty

                                loadingLayout.setVisibility(View.GONE);
                                infoLayout.setVisibility(View.VISIBLE);
                                mainLayout.setVisibility(View.GONE);

                                if (search.equals("")) {
                                    setInfoLayout("empty"); // Show Empty
                                } else {
                                    setInfoLayout("search"); // Show Search
                                }
                            }

                            if (pagingProgressBar.getVisibility() == View.VISIBLE) {
                                pagingProgressBar.setVisibility(View.GONE);
                            }

                            loading = false;
                            CenterRepository.usersPage++;

                            resetData("search");

                            finished = true;

                            CenterRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer != -1) {
                            if (centerViewModel.getUsers(clinicId) == null) {
                                // Users is Empty

                                loadingLayout.setVisibility(View.GONE);
                                infoLayout.setVisibility(View.VISIBLE);
                                mainLayout.setVisibility(View.GONE);

                                if (integer == 0) {
                                    setInfoLayout("error"); // Show Error
                                } else if (integer == -2) {
                                    setInfoLayout("connection"); // Show Connection
                                }

                                if (pagingProgressBar.getVisibility() == View.VISIBLE) {
                                    pagingProgressBar.setVisibility(View.GONE);
                                }

                                resetData("search");

                                finished = true;

                                CenterRepository.workState.removeObservers((LifecycleOwner) this);
                            } else {
                                // Show Users

                                loadingLayout.setVisibility(View.GONE);
                                infoLayout.setVisibility(View.GONE);
                                mainLayout.setVisibility(View.VISIBLE);

                                usersRecyclerViewAdapter.setUser(centerViewModel.getUsers(clinicId));
                                if (CenterRepository.usersPage == 1) {
                                    usersRecyclerView.setAdapter(usersRecyclerViewAdapter);
//                                    rcRecyclerView.setAdapter(centersRecyclerViewAdapter);
                                }

                                if (pagingProgressBar.getVisibility() == View.VISIBLE) {
                                    pagingProgressBar.setVisibility(View.GONE);
                                }

                                resetData("search");

                                finished = true;

                                CenterRepository.workState.removeObservers((LifecycleOwner) this);
                            }
                        }
                    }
                });
                break;

            case "roomViewModel":
                RoomRepository.workState.observe((LifecycleOwner) this, integer -> {
                    if (RoomRepository.work.equals("users")) {
                        finished = false;
                        loading = true;
                        if (integer == 1) {
                            if (roomViewModel.getUsers(roomId) != null) {
                                // Show Users

                                loadingLayout.setVisibility(View.GONE);
                                infoLayout.setVisibility(View.GONE);
                                mainLayout.setVisibility(View.VISIBLE);

                                usersRecyclerViewAdapter.setUser(roomViewModel.getUsers(roomId));
//                                roomsRecyclerViewAdapter.setRoom(roomViewModel.getUsers(roomId));
                                if (RoomRepository.usersPage == 1) {
                                    usersRecyclerView.setAdapter(usersRecyclerViewAdapter);
//                                    rcRecyclerView.setAdapter(roomsRecyclerViewAdapter);
                                }
                            } else {
                                // User is Empty

                                loadingLayout.setVisibility(View.GONE);
                                infoLayout.setVisibility(View.VISIBLE);
                                mainLayout.setVisibility(View.GONE);

                                if (search.equals("")) {
                                    setInfoLayout("empty"); // Show Empty
                                } else {
                                    setInfoLayout("search"); // Show Search
                                }
                            }

                            if (pagingProgressBar.getVisibility() == View.VISIBLE) {
                                pagingProgressBar.setVisibility(View.GONE);
                            }

                            loading = false;
                            RoomRepository.usersPage++;

                            resetData("search");

                            finished = true;

                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
                        } else if (integer != -1) {
                            if (roomViewModel.getUsers(roomId) == null) {
                                // Users is Empty

                                loadingLayout.setVisibility(View.GONE);
                                infoLayout.setVisibility(View.VISIBLE);
                                mainLayout.setVisibility(View.GONE);

                                if (integer == 0) {
                                    setInfoLayout("error"); // Show Error
                                } else if (integer == -2) {
                                    setInfoLayout("connection"); // Show Connection
                                }

                                if (pagingProgressBar.getVisibility() == View.VISIBLE) {
                                    pagingProgressBar.setVisibility(View.GONE);
                                }

                                resetData("search");

                                finished = true;

                                RoomRepository.workState.removeObservers((LifecycleOwner) this);
                            } else {
                                // Show Users

                                loadingLayout.setVisibility(View.GONE);
                                infoLayout.setVisibility(View.GONE);
                                mainLayout.setVisibility(View.VISIBLE);

                                usersRecyclerViewAdapter.setUser(roomViewModel.getUsers(roomId));
//                                roomsRecyclerViewAdapter.setRoom(roomViewModel.getUsers(roomId));
                                if (RoomRepository.usersPage == 1) {
                                    usersRecyclerView.setAdapter(usersRecyclerViewAdapter);
//                                    rcRecyclerView.setAdapter(roomsRecyclerViewAdapter);
                                }

                                if (pagingProgressBar.getVisibility() == View.VISIBLE) {
                                    pagingProgressBar.setVisibility(View.GONE);
                                }

                                resetData("search");

                                finished = true;

                                RoomRepository.workState.removeObservers((LifecycleOwner) this);
                            }
                        }
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                relaunchData();
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.stay_still, R.anim.slide_out_bottom);
    }

}