package com.majazeh.risloo.Utils.Managers;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

public class WindowDecorator {

    public void darkShowSystemUI(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                // Codes For Making The Content Appear Under System Bars.
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void darkNavShowSystemUI(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                // Codes For Making The Content Appear Under System Bars.
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void darkSetSystemUIColor(Activity activity, int statusColor, int navColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(statusColor);
            activity.getWindow().setNavigationBarColor(navColor);
        }
    }

    public void lightShowSystemUI(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    // Codes For Making The Content Appear Under System Bars.
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Codes For Making The Status And Navigation Bars Icons Light.
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    // Codes For Making The Content Appear Under System Bars.
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Codes For Making The Status Bars Icons Light.
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    // Codes For Making The Content Appear Under System Bars.
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    public void lightNavShowSystemUI(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    // Codes For Making The Content Appear Under System Bars.
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Codes For Making The Status And Navigation Bars Icons Light.
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    // Codes For Making The Content Appear Under System Bars.
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Codes For Making The Status Bars Icons Light.
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    // Codes For Making The Content Appear Under System Bars.
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
    }

    public void lightSetSystemUIColor(Activity activity, int statusColor, int navColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

            activity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            activity.getWindow().setStatusBarColor(statusColor);
            activity.getWindow().setNavigationBarColor(navColor);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            activity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            activity.getWindow().setStatusBarColor(statusColor);
            activity.getWindow().setNavigationBarColor(Color.BLACK);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.BLACK);
            activity.getWindow().setNavigationBarColor(Color.BLACK);
        }
    }

    public void leanBackHideSystemUI(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                // Codes For Making The Content Appear Under System Bars.
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        // Codes For Hiding Status And Navigation.
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    public void immersiveHideSystemUI(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                // Codes For Making The Content Appear Under System Bars.
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        // Codes For Hiding Status And Navigation.
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        // Codes For Enabling Immersive Mode.
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public void immersiveStickyHideSystemUI(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                // Codes For Making The Content Appear Under System Bars.
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        // Codes For Hiding Status And Navigation.
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        // Codes For Enabling Immersive Sticky Mode.
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}