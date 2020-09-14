package com.majazeh.risloo.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class CustomEditText extends EditText {

    // Objects
    private OnCutCopyPasteListener onCutCopyPasteListener;

    public void setOnCutCopyPasteListener(OnCutCopyPasteListener listener) {
        onCutCopyPasteListener = listener;
    }

    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        boolean consumed = super.onTextContextMenuItem(id);
        switch (id) {
            case android.R.id.cut:
                onCut();
                break;
            case android.R.id.copy:
                onCopy();
                break;
            case android.R.id.paste:
                onPaste();
        }
        return consumed;
    }

    public void onCut() {
        if (onCutCopyPasteListener !=null) {
            onCutCopyPasteListener.onCut();
        }
    }

    public void onCopy(){
        if (onCutCopyPasteListener !=null) {
            onCutCopyPasteListener.onCopy();
        }
    }

    public void onPaste(){
        if (onCutCopyPasteListener !=null) {
            onCutCopyPasteListener.onPaste();
        }
    }

    public interface OnCutCopyPasteListener {
        void onCut();
        void onCopy();
        void onPaste();
    }

}