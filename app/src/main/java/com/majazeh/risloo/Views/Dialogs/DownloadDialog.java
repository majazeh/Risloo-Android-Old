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

public class DownloadDialog extends BottomSheetDialogFragment {

    // Vars
    private String svg, png, html, pdf;

    // Objects
    private Activity activity;
    private Handler handler;
    private IntentCaller intentCaller;

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

        intentCaller = new IntentCaller();

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
            handler.postDelayed(() -> svgLinearLayout.setClickable(true), 300);
            dismiss();

            intentCaller.download(activity, svg);
        });

        pngLinearLayout.setOnClickListener(v -> {
            pngLinearLayout.setClickable(false);
            handler.postDelayed(() -> pngLinearLayout.setClickable(true), 300);
            dismiss();

            intentCaller.download(activity, png);
        });

        htmlLinearLayout.setOnClickListener(v -> {
            htmlLinearLayout.setClickable(false);
            handler.postDelayed(() -> htmlLinearLayout.setClickable(true), 300);
            dismiss();

            intentCaller.download(activity, html);
        });

        pdfLinearLayout.setOnClickListener(v -> {
            pdfLinearLayout.setClickable(false);
            handler.postDelayed(() -> pdfLinearLayout.setClickable(true), 300);
            dismiss();

            intentCaller.download(activity, pdf);
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

    public void getUrls(String svg, String png, String html, String pdf) {
        if (svg == null)
            svgLinearLayout.setVisibility(View.GONE);
        else
            this.svg = svg;

        if (png == null)
            pngLinearLayout.setVisibility(View.GONE);
        else
            this.png = png;

        if (html == null)
            htmlLinearLayout.setVisibility(View.GONE);
        else
            this.html = html;

        if (pdf == null)
            pdfLinearLayout.setVisibility(View.GONE);
        else
            this.pdf = pdf;
    }

}