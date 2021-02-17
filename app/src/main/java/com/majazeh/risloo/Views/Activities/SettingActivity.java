package com.majazeh.risloo.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Managers.ParamsManager;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Utils.Managers.WindowDecorator;
import com.majazeh.risloo.ViewModels.ExplodeViewModel;
import com.majazeh.risloo.ViewModels.SettingViewModel;
import com.majazeh.risloo.Views.Adapters.SettingAdapter;
import com.majazeh.risloo.Views.Dialogs.SocialDialog;

import org.json.JSONException;

public class SettingActivity extends AppCompatActivity {

    // ViewModels
    private SettingViewModel settingViewModel;
    public ExplodeViewModel explodeViewModel;

    // Adapters
    private SettingAdapter settingAdapter;

    // Objects
    private Handler handler;
    private SocialDialog socialDialog;

    // Widgets
    private ConstraintLayout toolbarLayout;
    private ImageView toolbarImageView;
    private TextView toolbarTextView;
    private RecyclerView settingRecyclerView;
    private Dialog noUpdateDialog, availableUpdateDialog;
    private TextView noUpdateDialogTitle, noUpdateDialogDescription, noUpdateDialogConfirm, availableUpdateDialogTitle, availableUpdateDialogDescription, availableUpdateDialogPositive, availableUpdateDialogNegative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        decorator();

        setContentView(R.layout.activity_setting);

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
        settingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
        explodeViewModel = new ViewModelProvider(this).get(ExplodeViewModel.class);

        settingAdapter = new SettingAdapter(this);

        handler = new Handler();

        socialDialog = new SocialDialog(this);

        toolbarLayout = findViewById(R.id.setting_toolbar);
        toolbarLayout.setBackgroundColor(getResources().getColor(R.color.Snow));

        toolbarImageView = findViewById(R.id.component_toolbar_primary_imageView);
        toolbarImageView.setImageResource(R.drawable.ic_chevron_right);
        ImageViewCompat.setImageTintList(toolbarImageView, AppCompatResources.getColorStateList(this, R.color.Gray900));

        toolbarTextView = findViewById(R.id.component_toolbar_textView);
        toolbarTextView.setText(getResources().getString(R.string.SettingTitle));

        settingRecyclerView = findViewById(R.id.setting_recyclerView);
        settingRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, 0, 0));
        settingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        settingRecyclerView.setHasFixedSize(true);

        noUpdateDialog = new Dialog(this, R.style.DialogTheme);
        noUpdateDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        noUpdateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        noUpdateDialog.setContentView(R.layout.dialog_note);
        noUpdateDialog.setCancelable(true);
        noUpdateDialog.getWindow().setAttributes(ParamsManager.set(noUpdateDialog));

        availableUpdateDialog = new Dialog(this, R.style.DialogTheme);
        availableUpdateDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        availableUpdateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        availableUpdateDialog.setContentView(R.layout.dialog_action);
        availableUpdateDialog.setCancelable(true);
        availableUpdateDialog.getWindow().setAttributes(ParamsManager.set(availableUpdateDialog));

        noUpdateDialogTitle = noUpdateDialog.findViewById(R.id.dialog_note_title_textView);
        noUpdateDialogDescription = noUpdateDialog.findViewById(R.id.dialog_note_description_textView);
        noUpdateDialogDescription.setText(getResources().getString(R.string.SettingNoUpdateDialogDescription));
        noUpdateDialogConfirm = noUpdateDialog.findViewById(R.id.dialog_note_confirm_textView);
        noUpdateDialogConfirm.setText(getResources().getString(R.string.SettingNoUpdateDialogConfirm));

        availableUpdateDialogTitle = availableUpdateDialog.findViewById(R.id.dialog_action_title_textView);
        availableUpdateDialogDescription = availableUpdateDialog.findViewById(R.id.dialog_action_description_textView);
        availableUpdateDialogDescription.setText(getResources().getString(R.string.SettingAvailableUpdateDialogDescription));
        availableUpdateDialogPositive = availableUpdateDialog.findViewById(R.id.dialog_action_positive_textView);
        availableUpdateDialogPositive.setText(getResources().getString(R.string.SettingAvailableUpdateDialogPositive));
        availableUpdateDialogPositive.setTextColor(getResources().getColor(R.color.Risloo800));
        availableUpdateDialogNegative = availableUpdateDialog.findViewById(R.id.dialog_action_negative_textView);
        availableUpdateDialogNegative.setText(getResources().getString(R.string.SettingAvailableUpdateDialogNegative));
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            toolbarImageView.setBackgroundResource(R.drawable.draw_oval_solid_snow_ripple_quartz);

            noUpdateDialogConfirm.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);

            availableUpdateDialogPositive.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
            availableUpdateDialogNegative.setBackgroundResource(R.drawable.draw_12sdp_solid_snow_ripple_quartz);
        }
    }

    private void listener() {
        toolbarImageView.setOnClickListener(v -> {
            toolbarImageView.setClickable(false);
            handler.postDelayed(() -> toolbarImageView.setClickable(true), 250);

            finish();
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        noUpdateDialogConfirm.setOnClickListener(v -> {
            noUpdateDialogConfirm.setClickable(false);
            handler.postDelayed(() -> noUpdateDialogConfirm.setClickable(true), 250);
            noUpdateDialog.dismiss();
        });

        availableUpdateDialogPositive.setOnClickListener(v -> {
            availableUpdateDialogPositive.setClickable(false);
            handler.postDelayed(() -> availableUpdateDialogPositive.setClickable(true), 250);
            availableUpdateDialog.dismiss();

            IntentManager.googlePlay(this);
        });

        availableUpdateDialogNegative.setOnClickListener(v -> {
            availableUpdateDialogNegative.setClickable(false);
            handler.postDelayed(() -> availableUpdateDialogNegative.setClickable(true), 250);
            availableUpdateDialog.dismiss();
        });

        noUpdateDialog.setOnCancelListener(dialog -> noUpdateDialog.dismiss());

        availableUpdateDialog.setOnCancelListener(dialog -> availableUpdateDialog.dismiss());
    }

    private void setData() {
        try {
            settingAdapter.setSettings(settingViewModel.getAll());
            settingRecyclerView.setAdapter(settingAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        if (explodeViewModel.hasUpdate()) {
            availableUpdateDialogTitle.setText(explodeViewModel.newVersionFa());
            availableUpdateDialog.show();
        } else {
            noUpdateDialogTitle.setText(explodeViewModel.currentVersionFa());
            noUpdateDialog.show();
        }
    }

    public void navigator(int position) {
        switch (position) {
            case 0:
                Intent aboutUsIntent = new Intent(new Intent(this, AboutUsActivity.class));
                startActivity(aboutUsIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 1:
                Intent faQuestionIntent = new Intent(new Intent(this, FAQuestionActivity.class));
                startActivity(faQuestionIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 2:
                Intent taConditionIntent = new Intent(new Intent(this, TAConditionActivity.class));
                startActivity(taConditionIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 3:
                Intent callUsIntent = new Intent(new Intent(this, CallUsActivity.class));
                startActivity(callUsIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
            case 4:
                socialDialog.show(this.getSupportFragmentManager(), "socialBottomSheet");
                break;
            case 5:
                IntentManager.share(this, getResources().getString(R.string.SettingShareLink), getResources().getString(R.string.SettingShareChooser));
                break;
            case 6:
                IntentManager.googlePlay(this);
                break;
            case 7:
                showDialog();
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}