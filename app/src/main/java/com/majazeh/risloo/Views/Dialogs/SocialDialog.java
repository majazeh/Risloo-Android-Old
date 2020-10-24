package com.majazeh.risloo.Views.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Managers.IntentCaller;

public class SocialDialog extends BottomSheetDialogFragment {

    // Objects
    private Activity activity;
    private Handler handler;
    private IntentCaller intentCaller;

    // Widgets
    private LinearLayout telegramLinearLayout, instagramLinearLayout, facebookLinearLayout, twitterLinearLayout;
    private TextView closeTextView;

    public SocialDialog(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_social, viewGroup, false);

        initializer(view);

        detector();

        listener();

        return view;
    }

    private void initializer(View view) {
        handler = new Handler();

        intentCaller = new IntentCaller();

        telegramLinearLayout = view.findViewById(R.id.dialog_social_telegram_linearLayout);
        instagramLinearLayout = view.findViewById(R.id.dialog_social_instagram_linearLayout);
        facebookLinearLayout = view.findViewById(R.id.dialog_social_facebook_linearLayout);
        twitterLinearLayout = view.findViewById(R.id.dialog_social_twitter_linearLayout);

        closeTextView = view.findViewById(R.id.dialog_social_close_textView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            closeTextView.setBackgroundResource(R.drawable.draw_12sdp_solid_white_ripple_solitude);
        }
    }

    private void listener() {
        telegramLinearLayout.setOnClickListener(v -> {
            telegramLinearLayout.setClickable(false);
            handler.postDelayed(() -> telegramLinearLayout.setClickable(true), 300);
            dismiss();

            intentCaller.telegram(activity, activity.getResources().getString(R.string.SettingFollowTelegram));
        });

        instagramLinearLayout.setOnClickListener(v -> {
            instagramLinearLayout.setClickable(false);
            handler.postDelayed(() -> instagramLinearLayout.setClickable(true), 300);
            dismiss();

            intentCaller.instagram(activity, activity.getResources().getString(R.string.SettingFollowInstagram));
        });

        facebookLinearLayout.setOnClickListener(v -> {
            facebookLinearLayout.setClickable(false);
            handler.postDelayed(() -> facebookLinearLayout.setClickable(true), 300);
            dismiss();

            intentCaller.facebook(activity, activity.getResources().getString(R.string.SettingFollowFacebook));
        });

        twitterLinearLayout.setOnClickListener(v -> {
            twitterLinearLayout.setClickable(false);
            handler.postDelayed(() -> twitterLinearLayout.setClickable(true), 300);
            dismiss();

            intentCaller.twitter(activity, activity.getResources().getString(R.string.SettingFollowTwitter));
        });

        closeTextView.setOnClickListener(v -> {
            closeTextView.setClickable(false);
            handler.postDelayed(() -> closeTextView.setClickable(true), 300);
            dismiss();
        });
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        customizeDialog(dialog);
        return dialog;
    }

    @SuppressLint("InlinedApi")
    private void customizeDialog(@NonNull Dialog dialog) {
        if (dialog.getWindow() != null) {

            DisplayMetrics metrics = new DisplayMetrics();

            dialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            dialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);

            GradientDrawable dimDrawable = new GradientDrawable();
            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(activity.getResources().getColor(R.color.White));

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                windowBackground.setLayerInsetTop(1, metrics.heightPixels);
            }

            dialog.getWindow().setBackgroundDrawable(windowBackground);
        }
    }

}