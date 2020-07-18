package com.majazeh.risloo.Utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;

public class ActivityAnimator {

    public void setAnimation(Bundle savedInstanceState, LinearLayout rootLayout) {
        if (savedInstanceState == null) {
            rootLayout.setVisibility(View.INVISIBLE);

            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            circularReveal(rootLayout);
                        }
                        rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void circularReveal(LinearLayout rootLayout) {
        int cx = rootLayout.getWidth() / 2;
        int cy = rootLayout.getHeight() / 2;

        float finalRadius = Math.max(rootLayout.getWidth(), rootLayout.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(rootLayout, cx, cy, 0, finalRadius);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(300);

        rootLayout.setVisibility(View.VISIBLE);

        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void circularUnReveal(LinearLayout rootLayout) {
        int cx = rootLayout.getWidth() / 2;
        int cy = rootLayout.getHeight() / 2;

        float initialRadius  = Math.max(rootLayout.getWidth(), rootLayout.getHeight());

        Animator anim = ViewAnimationUtils.createCircularReveal(rootLayout, cx, cy, initialRadius, 0);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.setDuration(300);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rootLayout.setVisibility(View.INVISIBLE);
            }
        });

        anim.start();
    }

}