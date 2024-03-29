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
import com.majazeh.risloo.Views.Activities.SamplesActivity;

import java.util.Objects;

public class FilterDialog extends BottomSheetDialogFragment {

    // Objects
    private Activity activity;
    private Handler handler;

    // Widgets
    private LinearLayout scaleLinearLayout, roomLinearLayout, statusLinearLayout;
    private TextView closeTextView;

    public FilterDialog(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_filter, viewGroup, false);

        initializer(view);

        detector();

        listener();

        return view;
    }

    private void initializer(View view) {
        handler = new Handler();

        scaleLinearLayout = view.findViewById(R.id.dialog_filter_scale_linearLayout);
        roomLinearLayout = view.findViewById(R.id.dialog_filter_room_linearLayout);
        statusLinearLayout = view.findViewById(R.id.dialog_filter_status_linearLayout);

        closeTextView = view.findViewById(R.id.dialog_filter_close_textView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            closeTextView.setBackgroundResource(R.drawable.draw_12sdp_solid_white_ripple_solitude);
        }
    }

    private void listener() {
        scaleLinearLayout.setOnClickListener(v -> {
            scaleLinearLayout.setClickable(false);
            handler.postDelayed(() -> scaleLinearLayout.setClickable(true), 300);
            dismiss();

            ((SamplesActivity) Objects.requireNonNull(getActivity())).setFilter("scale");
        });

        roomLinearLayout.setOnClickListener(v -> {
            roomLinearLayout.setClickable(false);
            handler.postDelayed(() -> roomLinearLayout.setClickable(true), 300);
            dismiss();

            ((SamplesActivity) Objects.requireNonNull(getActivity())).setFilter("room");
        });

        statusLinearLayout.setOnClickListener(v -> {
            statusLinearLayout.setClickable(false);
            handler.postDelayed(() -> statusLinearLayout.setClickable(true), 300);
            dismiss();

            ((SamplesActivity) Objects.requireNonNull(getActivity())).setFilter("status");
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