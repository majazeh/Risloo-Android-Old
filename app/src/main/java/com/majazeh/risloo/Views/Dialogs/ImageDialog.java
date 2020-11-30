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
import com.majazeh.risloo.Utils.Managers.IntentManager;
import com.majazeh.risloo.Utils.Managers.PermissionManager;
import com.majazeh.risloo.Views.Activities.CreateCenterActivity;
import com.majazeh.risloo.Views.Activities.EditAccountActivity;
import com.majazeh.risloo.Views.Activities.EditCenterActivity;

import java.util.Objects;

public class ImageDialog extends BottomSheetDialogFragment {

    // Vars
    private String type;

    // Objects
    private Activity activity;
    private Handler handler;

    // Widgets
    private LinearLayout galleryLinearLayout, cameraLinearLayout;
    private TextView closeTextView;

    public ImageDialog(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_image, viewGroup, false);

        initializer(view);

        detector();

        listener();

        return view;
    }

    private void initializer(View view) {
        handler = new Handler();

        galleryLinearLayout = view.findViewById(R.id.dialog_image_gallery_linearLayout);
        cameraLinearLayout = view.findViewById(R.id.dialog_image_camera_linearLayout);

        closeTextView = view.findViewById(R.id.dialog_image_close_textView);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            closeTextView.setBackgroundResource(R.drawable.draw_12sdp_solid_white_ripple_solitude);
        }
    }

    private void listener() {
        galleryLinearLayout.setOnClickListener(v -> {
            galleryLinearLayout.setClickable(false);
            handler.postDelayed(() -> galleryLinearLayout.setClickable(true), 300);
            dismiss();

            if (PermissionManager.galleryPermission(activity)) {
                IntentManager.gallery(activity);
            }
        });

        cameraLinearLayout.setOnClickListener(v -> {
            cameraLinearLayout.setClickable(false);
            handler.postDelayed(() -> cameraLinearLayout.setClickable(true), 300);
            dismiss();

            if (PermissionManager.cameraPermission(activity)) {
                switch (type) {
                    case "EditAccount":
                        ((EditAccountActivity) Objects.requireNonNull(activity)).imageFilePath = IntentManager.camera(activity);
                        break;
                    case "CreateCenter":
                        ((CreateCenterActivity) Objects.requireNonNull(activity)).imageFilePath = IntentManager.camera(activity);
                        break;
                    case "EditCenter":
                        ((EditCenterActivity) Objects.requireNonNull(activity)).imageFilePath = IntentManager.camera(activity);
                        break;
                }
            }
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

    public void setType(String type) {
        this.type = type;
    }

}