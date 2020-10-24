package com.majazeh.risloo.Utils.Widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

@SuppressLint("AppCompatCustomView")
public class CutCopyPasteEditText extends EditText {

    // Objects
    private OnCutCopyPasteListener onCutCopyPasteListener;

    public void setOnCutCopyPasteListener(OnCutCopyPasteListener onCutCopyPasteListener) {
        this.onCutCopyPasteListener = onCutCopyPasteListener;
    }

    public CutCopyPasteEditText(Context context) {
        super(context);
    }

    public CutCopyPasteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CutCopyPasteEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTextContextMenuItem(int id) {
        switch (id) {
            case android.R.id.cut:
                onCut();
                break;
            case android.R.id.copy:
                onCopy();
                break;
            case android.R.id.paste:
                onPaste();
                break;
        }
        return super.onTextContextMenuItem(id);
    }

    public interface OnCutCopyPasteListener {
        void onCut();
        void onCopy();
        void onPaste();
    }

    private void onCut() {
        if (onCutCopyPasteListener != null) {
            onCutCopyPasteListener.onCut();
        }
    }

    private void onCopy() {
        if (onCutCopyPasteListener != null) {
            onCutCopyPasteListener.onCopy();
        }
    }

    private void onPaste() {
        if (onCutCopyPasteListener != null) {
            onCutCopyPasteListener.onPaste();
        }
    }

}