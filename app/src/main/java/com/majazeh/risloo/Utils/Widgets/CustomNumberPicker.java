package com.majazeh.risloo.Utils.Widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.core.content.res.ResourcesCompat;

import com.majazeh.risloo.R;

public class CustomNumberPicker extends NumberPicker {

    // Objects
    private Typeface danaBold;

    public CustomNumberPicker(Context context) {
        super(context);
    }

    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void addView(View child) {
        super.addView(child);

        updateView(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);

        danaBold = ResourcesCompat.getFont(getContext(), R.font.dana_bold);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);

        danaBold = ResourcesCompat.getFont(getContext(), R.font.dana_bold);
        updateView(child);
    }

    private void updateView(View view) {
        if (view instanceof EditText) {
            ((EditText) view).setTypeface(danaBold);
            ((EditText) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._12ssp));
            ((EditText) view).setTextColor(getResources().getColor(R.color.Grey));
        }
    }

}