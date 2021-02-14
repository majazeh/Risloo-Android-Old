package com.majazeh.risloo.Utils.Managers;

import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.majazeh.risloo.R;

public class ExceptionManager {

    public void error(EditText editText) {
        editText.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
    }

    public boolean error(TextView textView) {
        textView.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        return true;
    }

    public boolean error(TabLayout tabLayout) {
        tabLayout.setBackgroundResource(R.drawable.draw_16sdp_border_violetred);
        return true;
    }

    public void clear(EditText editText) {
        editText.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
    }

    public boolean clear(TextView textView) {
        textView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
        return false;
    }

    public boolean clear(TabLayout tabLayout) {
        tabLayout.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
        return false;
    }

}