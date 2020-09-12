package com.majazeh.risloo.Views.Dialogs;

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

public class DownloadDialog extends BottomSheetDialogFragment {

    // Objects
    private Activity activity;
    private Handler handler;

    // Widgets
    private LinearLayout svgLinearLayout, pngLinearLayout, htmlLinearLayout, pdfLinearLayout;
    private TextView closeTextView;

    public DownloadDialog(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_download, viewGroup, false);

        initializer(view);

        detector();

        listener();

        return view;
    }

    private void initializer(View view) {
        handler = new Handler();

        svgLinearLayout = view.findViewById(R.id.dialog_download_svg_linearLayout);
        pngLinearLayout = view.findViewById(R.id.dialog_download_png_linearLayout);
        htmlLinearLayout = view.findViewById(R.id.dialog_download_html_linearLayout);
        pdfLinearLayout = view.findViewById(R.id.dialog_download_pdf_linearLayout);

        closeTextView = view.findViewById(R.id.dialog_download_close_textView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            closeTextView.setBackgroundResource(R.drawable.draw_12sdp_solid_white_ripple_solitude);
        }
    }

    private void listener() {
        svgLinearLayout.setOnClickListener(v -> {
            svgLinearLayout.setClickable(false);
            handler.postDelayed(() -> svgLinearLayout.setClickable(true), 500);
            dismiss();


        });

        pngLinearLayout.setOnClickListener(v -> {
            pngLinearLayout.setClickable(false);
            handler.postDelayed(() -> pngLinearLayout.setClickable(true), 500);
            dismiss();


        });

        htmlLinearLayout.setOnClickListener(v -> {
            htmlLinearLayout.setClickable(false);
            handler.postDelayed(() -> htmlLinearLayout.setClickable(true), 500);
            dismiss();


        });

        pdfLinearLayout.setOnClickListener(v -> {
            pdfLinearLayout.setClickable(false);
            handler.postDelayed(() -> pdfLinearLayout.setClickable(true), 500);
            dismiss();


        });

        closeTextView.setOnClickListener(v -> {
            closeTextView.setClickable(false);
            handler.postDelayed(() -> closeTextView.setClickable(true), 500);
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