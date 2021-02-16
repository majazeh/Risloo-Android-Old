package com.majazeh.risloo.Utils.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.core.content.res.ResourcesCompat;

import com.majazeh.risloo.R;

public class SingleNumberPicker extends NumberPicker {

    public SingleNumberPicker(Context context) {
        super(context);
    }

    public SingleNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SingleNumberPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void addView(View view) {
        super.addView(view);
        updateView(getContext(), view);
    }

    @Override
    public void addView(View view, android.view.ViewGroup.LayoutParams params) {
        super.addView(view, params);
        updateView(getContext(), view);
    }

    @Override
    public void addView(View view, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(view, index, params);
        updateView(getContext(), view);
    }

    private void updateView(Context context, View view) {
        if (view instanceof EditText) {
            ((EditText) view).setTypeface(ResourcesCompat.getFont(context, R.font.dana_bold));
            ((EditText) view).setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen._12ssp));
            ((EditText) view).setTextColor(getResources().getColor(R.color.Gray700));
        }
    }

}