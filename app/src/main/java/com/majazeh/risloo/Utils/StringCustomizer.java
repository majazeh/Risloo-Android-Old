package com.majazeh.risloo.Utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

public class StringCustomizer {

    public static SpannableString clickable(String value, int startIndex, int endIndex, ClickableSpan clickableSpan) {
        SpannableString spannableString = new SpannableString(value);
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString foreground(String value, int startIndex, int endIndex, int color) {
        SpannableString spannableString = new SpannableString(value);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(color);
        spannableString.setSpan(foregroundColorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString background(String value, int startIndex, int endIndex, int color) {
        SpannableString spannableString = new SpannableString(value);
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(color);
        spannableString.setSpan(backgroundColorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString size(String value, int startIndex, int endIndex, int size) {
        SpannableString spannableString = new SpannableString(value);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(size);
        spannableString.setSpan(sizeSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString style(String value, int startIndex, int endIndex, int typeface) {
        SpannableString spannableString = new SpannableString(value);
        StyleSpan styleSpan = new StyleSpan(typeface);
        spannableString.setSpan(styleSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString foregroundBackground(String value, int startIndex, int endIndex, int foregroundColor, int backgroundColor) {
        SpannableString spannableString = new SpannableString(value);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(foregroundColor);
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(backgroundColor);
        spannableString.setSpan(foregroundColorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(backgroundColorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString foregroundSize(String value, int startIndex, int endIndex, int foregroundColor, int size) {
        SpannableString spannableString = new SpannableString(value);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(foregroundColor);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(size);
        spannableString.setSpan(foregroundColorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString foregroundStyle(String value, int startIndex, int endIndex, int foregroundColor, int typeface) {
        SpannableString spannableString = new SpannableString(value);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(foregroundColor);
        StyleSpan styleSpan = new StyleSpan(typeface);
        spannableString.setSpan(foregroundColorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(styleSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString backgroundStyle(String value, int startIndex, int endIndex, int backgroundColor, int typeface) {
        SpannableString spannableString = new SpannableString(value);
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(backgroundColor);
        StyleSpan styleSpan = new StyleSpan(typeface);
        spannableString.setSpan(backgroundColorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(styleSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static SpannableString foregroundBackgroundStyle(String value, int startIndex, int endIndex, int foregroundColor, int backgroundColor, int typeface) {
        SpannableString spannableString = new SpannableString(value);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(foregroundColor);
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(backgroundColor);
        StyleSpan styleSpan = new StyleSpan(typeface);
        spannableString.setSpan(foregroundColorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(backgroundColorSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(styleSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

}