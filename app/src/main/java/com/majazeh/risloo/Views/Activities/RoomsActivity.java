package com.majazeh.risloo.Views.Activities;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.duolingo.open.rtlviewpager.RtlViewPager;
import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.Models.Repositories.RoomRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.StringManager;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.Utils.Widgets.ControlEditText;
import com.majazeh.risloo.ViewModels.AuthViewModel;
import com.majazeh.risloo.ViewModels.RoomViewModel;
import com.majazeh.risloo.Views.Adapters.TabRoomsAdapter;
import com.majazeh.risloo.Views.Fragments.AllRoomsFragment;
import com.majazeh.risloo.Views.Fragments.MyRoomsFragment;

import org.json.JSONException;

import java.util.Objects;

public class RoomsActivity extends AppCompatActivity {

    // ViewModels
    public AuthViewModel authViewModel;
    public RoomViewModel roomViewModel;

    // Adapters
    public TabRoomsAdapter tabRoomsAdapter;

    // Vars
    public String search = "";
    public boolean finished = false, loadingAll = false, loadingMy = false;

    // Objects
    private Handler handler;
    private ControlEditText controlEditText;
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
    private TabLayout tabLayout;
    private RtlViewPager rtlViewPager;
    private Dialog searchDialog;
    private TextView searchDialogTitle, searchDialogPositive, searchDialogNegative;
    private EditText searchDialogInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_rooms);

        initializer();

        detector();

        listener();

        setData();

        getData("getAll");
    }

    private void decorator() {
        WindowDecorator windowDecorator = new WindowDecorator();

        windowDecorator.lightShowSystemUI(this);
        windowDecorator.lightSetSystemUIColor(this, getResources().getColor(R.color.Snow), getResources().getColor(R.color.Snow));
    }

    private void initializer() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        roomViewModel = new ViewModelProvider(this).get(RoomViewModel.class);

        if (!authViewModel.getToken().equals("")) {
            tabRoomsAdapter = new TabRoomsAdapter(getSupportFragmentManager(), 0, this, true);
        } else {
            tabRoomsAdapter = new TabRoomsAdapter(getSupportFragmentManager(), 0, this, false);
        }

        handler = new Handler();

        controlEditText = new ControlEditText();

        toolbarLayout = findViewById(R.id.layout_toolbar_linearLayout);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.layout_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Nero));
        toolbarCreateImageView = findViewById(R.id.layout_toolbar_secondary_imageView);
        toolbarCreateImageView.setImageResource(R.drawable.ic_plus_light);
        ImageViewCompat.setImageTintList(toolbarCreateImageView, AppCompatResources.getColorStateList(this, R.color.MountainMeadow));
        toolbarSearchImageView = findViewById(R.id.layout_toolbar_thirdly_imageView);
        toolbarSearchImageView.setImageResource(R.drawable.ic_search_light);
        ImageViewCompat.setImageTintList(toolbarSearchImageView, AppCompatResources.getColorStateList(this, R.color.Nero));

        toolbarTextView = findViewById(R.id.layout_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.RoomsTitle));

        searchLayout = findViewById(R.id.activity_rooms_searchLayout);

        mainLayout = findViewById(R.id.activity_rooms_mainLayout);
        infoLayout = findViewById(R.id.layout_info_linearLayout);
        loadingLayout = findViewById(R.id.layout_loading_linearLayout);

        infoImageView = findViewById(R.id.layout_info_imageView);
        infoTextView = findViewById(R.id.layout_info_textView);

        searchImageView = findViewById(R.id.activity_rooms_search_imageView);
        searchTextView = findViewById(R.id.activity_rooms_search_textView);

        tabLayout = findViewById(R.id.activity_rooms_tabLayout);

        rtlViewPager = findViewById(R.id.activity_rooms_rtlViewPager);

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
        searchDialogTitle.setText(getResources().getString(R.string.RoomsSearchDialogTitle));
        searchDialogInput = searchDialog.findViewById(R.id.dialog_type_input_editText);
        searchDialogInput.setHint(getResources().getString(R.string.RoomsSearchDialogInput));
        searchDialogInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        searchDialogPositive = searchDialog.findViewById(R.id.dialog_type_positive_textView);
        searchDialogPositive.setText(getResources().getString(R.string.ScalesSearchDialogPositive));
        searchDialogPositive.setTextColor(getResources().getColor(R.color.PrimaryDark));
        searchDialogNegative = searchDialog.findViewById(R.id.dialog_type_negative_textView);
        searchDialogNegative.setText(getResources().getString(R.string.ScalesSearchDialogNegative));
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
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        toolbarCreateImageView.setOnClickListener(v -> {
            toolbarCreateImageView.setClickable(false);
            handler.postDelayed(() -> toolbarCreateImageView.setClickable(true), 250);

            if (finished) {
                Fragment allFragment = tabRoomsAdapter.allFragment;
                if (((AllRoomsFragment) allFragment).pagingProgressBar.isShown()) {
                    loadingAll = false;
                    ((AllRoomsFragment) allFragment).pagingProgressBar.setVisibility(View.GONE);
                }
                Fragment myFragment = tabRoomsAdapter.myFragment;
                if (((MyRoomsFragment) myFragment).pagingProgressBar.isShown()) {
                    loadingMy = false;
                    ((MyRoomsFragment) myFragment).pagingProgressBar.setVisibility(View.GONE);
                }
            }

            startActivityForResult(new Intent(this, CreateRoomActivity.class).putExtra("loaded", finished), 100);
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

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                rtlViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        rtlViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
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

    private void setData() {
//        if (authViewModel.createRoom()) {
//            toolbarCreateImageView.setVisibility(View.VISIBLE);
//        } else {
//            toolbarCreateImageView.setVisibility(View.GONE);
//        }
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

    private void errorException(String type) {
        if (type.equals("searchDialog")) {
            searchDialogInput.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        }
    }

    private void resetData(String method) {
        if (method.equals("search")) {
            if (authViewModel.auth()) {
                toolbarSearchImageView.setVisibility(View.VISIBLE);
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

    private void getData(String method) {
        try {
            switch (method) {
                case "getAll":
                    roomViewModel.rooms(search);
                    RoomRepository.allPage = 1;
                    break;
                case "getMy":
                    roomViewModel.myRooms(search);
                    RoomRepository.myPage = 1;
                    break;
            }
            observeWork();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void relaunchData() {
        searchLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        infoLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);

        getData("getAll");
    }

    public void observeWork() {
        RoomRepository.workState.observe((LifecycleOwner) this, integer -> {
            if (RoomRepository.work.equals("getAll")) {
                finished = false;
                loadingAll = true;
                if (integer == 1) {
                    if (RoomRepository.allPage == 1) {
                        if (!authViewModel.getToken().equals("")) {
                            // Continue Get MyRooms

                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
                            getData("getMy");
                        } else {
                            // Show Rooms And Just AllRooms

                            loadingLayout.setVisibility(View.GONE);
                            infoLayout.setVisibility(View.GONE);
                            mainLayout.setVisibility(View.VISIBLE);

                            tabLayout.setVisibility(View.GONE);
                            rtlViewPager.setAdapter(tabRoomsAdapter);

                            resetData("search");

                            finished = true;

                            RoomRepository.workState.removeObservers((LifecycleOwner) this);
                        }

                        loadingAll = false;
                        RoomRepository.allPage++;

                    } else {
                        Fragment allFragment = tabRoomsAdapter.allFragment;
                        ((AllRoomsFragment) allFragment).notifyRecycler();

                        resetData("search");

                        RoomRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                } else if (integer != -1) {
                    if (roomViewModel.getAll() == null) {
                        // Rooms is Empty

                        loadingLayout.setVisibility(View.GONE);
                        infoLayout.setVisibility(View.VISIBLE);
                        mainLayout.setVisibility(View.GONE);

                        if (integer == 0) {
                            setInfoLayout("error"); // Show Error
                        } else if (integer == -2) {
                            setInfoLayout("connection"); // Show Connection
                        }

                        resetData("search");

                        finished = true;

                        RoomRepository.workState.removeObservers((LifecycleOwner) this);
                    } else {
                        // Show Rooms

                        loadingLayout.setVisibility(View.GONE);
                        infoLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        if (!authViewModel.getToken().equals("")) {
                            tabLayout.setVisibility(View.VISIBLE); // Both AllRooms And MyRooms
                            rtlViewPager.setAdapter(tabRoomsAdapter);
                        } else {
                            tabLayout.setVisibility(View.GONE); // Just AllRooms
                            rtlViewPager.setAdapter(tabRoomsAdapter);
                        }

                        resetData("search");

                        finished = true;

                        RoomRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                }
            } else if (RoomRepository.work.equals("getMy")) {
                loadingMy = true;
                if (integer == 1) {
                    if (RoomRepository.myPage == 1) {
                        // Show Rooms And Both AllRooms And MyRooms

                        loadingLayout.setVisibility(View.GONE);
                        infoLayout.setVisibility(View.GONE);
                        mainLayout.setVisibility(View.VISIBLE);

                        tabLayout.setVisibility(View.VISIBLE);
                        rtlViewPager.setAdapter(tabRoomsAdapter);

                        loadingMy = false;
                        RoomRepository.myPage++;

                        resetData("search");

                        finished = true;

                        RoomRepository.workState.removeObservers((LifecycleOwner) this);
                    } else {
                        Fragment myFragment = tabRoomsAdapter.myFragment;
                        ((MyRoomsFragment) myFragment).notifyRecycler();

                        resetData("search");

                        RoomRepository.workState.removeObservers((LifecycleOwner) this);
                    }
                } else if (integer != -1) {
                    // Show Rooms And Both AllRooms And MyRooms

                    loadingLayout.setVisibility(View.GONE);
                    infoLayout.setVisibility(View.GONE);
                    mainLayout.setVisibility(View.VISIBLE);

                    tabLayout.setVisibility(View.VISIBLE);
                    rtlViewPager.setAdapter(tabRoomsAdapter);

                    resetData("search");

                    finished = true;

                    RoomRepository.workState.removeObservers((LifecycleOwner) this);
                }
            }
        });
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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}