package com.majazeh.risloo.Utils.Widgets;

import android.content.Context;
import android.util.AttributeSet;

public class CutCopyPasteEditText extends androidx.appcompat.widget.AppCompatEditText {

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
                if (onCutCopyPasteListener != null)
                    onCutCopyPasteListener.onCut();
                break;
            case android.R.id.copy:
                if (onCutCopyPasteListener != null)
                    onCutCopyPasteListener.onCopy();
                break;
            case android.R.id.paste:
                if (onCutCopyPasteListener != null)
                    onCutCopyPasteListener.onPaste();
                break;
        }
        return super.onTextContextMenuItem(id);
    }

    public interface OnCutCopyPasteListener {
        void onCut();
        void onCopy();
        void onPaste();
    }

}