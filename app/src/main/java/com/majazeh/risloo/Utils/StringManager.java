package com.majazeh.risloo.Utils;

import android.annotation.SuppressLint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import saman.zamani.persiandate.PersianDate;

@SuppressLint("SimpleDateFormat")
public class StringManager {

    public static Date stringToDate(String pattern, String value) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToString(String pattern, Date value) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(value);
    }

    public static String gregorianToJalali(String value) {
        int year = Integer.parseInt(dateToString("yyyy", stringToDate("yyyy-MM-dd", value)));
        int month = Integer.parseInt(dateToString("MM", stringToDate("yyyy-MM-dd", value)));
        int day = Integer.parseInt(dateToString("dd", stringToDate("yyyy-MM-dd", value)));

        PersianDate persianDate = new PersianDate();
        persianDate.initGrgDate(year, month, day);

        if (persianDate.getShMonth() < 10) {
            if (persianDate.getShDay() < 10)
                return persianDate.getShYear() + "-" + "0" + persianDate.getShMonth() + "-" + "0" + persianDate.getShDay();
            else
                return persianDate.getShYear() + "-" + "0" + persianDate.getShMonth() + "-" + persianDate.getShDay();
        } else {
            if (persianDate.getShDay() < 10)
                return persianDate.getShYear() + "-" + persianDate.getShMonth() + "-" + "0" + persianDate.getShDay();
            else
                return persianDate.getShYear() + "-" + persianDate.getShMonth() + "-" + persianDate.getShDay();
        }
    }

    public static String jalaliToGregorian(String value) {
        int year = Integer.parseInt(dateToString("yyyy", stringToDate("yyyy-MM-dd", value)));
        int month = Integer.parseInt(dateToString("MM", stringToDate("yyyy-MM-dd", value)));
        int day = Integer.parseInt(dateToString("dd", stringToDate("yyyy-MM-dd", value)));

        PersianDate persianDate = new PersianDate();
        persianDate.initJalaliDate(year, month, day);

        if (persianDate.getGrgMonth() < 10) {
            if (persianDate.getGrgDay() < 10)
                return persianDate.getGrgYear() + "-" + "0" + persianDate.getGrgMonth() + "-" + "0" + persianDate.getGrgDay();
            else
                return persianDate.getGrgYear() + "-" + "0" + persianDate.getGrgMonth() + "-" + persianDate.getGrgDay();
        } else {
            if (persianDate.getGrgDay() < 10)
                return persianDate.getGrgYear() + "-" + persianDate.getGrgMonth() + "-" + "0" + persianDate.getGrgDay();
            else
                return persianDate.getGrgYear() + "-" + persianDate.getGrgMonth() + "-" + persianDate.getGrgDay();
        }
    }

    public static String substring(String value, char character) {
        int position = 0;

        for(int i=0; i<value.length(); i++) {
            if (value.charAt(i) == character) {
                position = i;
            }
        }

        return value.substring(position + 1);
    }

    public static String separate(String value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        return decimalFormat.format(Double.parseDouble(value));
    }

    public static SpannableString lining(String value) {
        SpannableString spannableString = new SpannableString(value);
        spannableString.setSpan(new StrikethroughSpan(), 0, value.length(), Spanned.SPAN_MARK_MARK);
        return spannableString;
    }

    public static String persian(String value) {
        String[] persianNumbers = new String[] {"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "'۸", "۹"};
        StringBuilder output = new StringBuilder();
        if (value.length() == 0) {
            return "";
        } else {
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                if ('0' <= c && c <= '9') {
                    output.append(persianNumbers[Integer.parseInt(String.valueOf(c))]);
                } else if (c == '.' || c == ',' || c == 'و') {
                    output.append(",");
                } else {
                    output.append(c);
                }
            }
            return output.toString();
        }
    }

    public static SpannableString separateLining(String value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        String decimalFormatString = decimalFormat.format(Double.parseDouble(value));
        SpannableString spannableString = new SpannableString(decimalFormatString);
        spannableString.setSpan(new StrikethroughSpan(), 0, decimalFormatString.length(), Spanned.SPAN_MARK_MARK);
        return spannableString;
    }

    public static String separatePersian(String value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        String decimalFormatString = decimalFormat.format(Double.parseDouble(value));
        String[] persianNumbers = new String[] {"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "'۸", "۹"};
        StringBuilder output = new StringBuilder();
        if (decimalFormatString.length() == 0) {
            return "";
        } else {
            for (int i = 0; i < decimalFormatString.length(); i++) {
                char c = decimalFormatString.charAt(i);
                if ('0' <= c && c <= '9') {
                    output.append(persianNumbers[Integer.parseInt(String.valueOf(c))]);
                } else if (c == '.' || c == ',' || c == 'و') {
                    output.append(",");
                } else {
                    output.append(c);
                }
            }
            return output.toString();
        }
    }

    public static SpannableString persianLining(String value) {
        String[] persianNumbers = new String[] {"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "'۸", "۹"};
        StringBuilder output = new StringBuilder();
        if (value.length() == 0) {
            return null;
        } else {
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                if ('0' <= c && c <= '9') {
                    output.append(persianNumbers[Integer.parseInt(String.valueOf(c))]);
                } else if (c == '.' || c == ',' || c == 'و') {
                    output.append(",");
                } else {
                    output.append(c);
                }
            }
            SpannableString spannableString = new SpannableString(output.toString());
            spannableString.setSpan(new StrikethroughSpan(), 0, output.length(), Spanned.SPAN_MARK_MARK);
            return spannableString;
        }
    }

    public static SpannableString separatePersianLining(String value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        String decimalFormatString = decimalFormat.format(Double.parseDouble(value));
        String[] persianNumbers = new String[] {"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "'۸", "۹"};
        StringBuilder output = new StringBuilder();
        if (decimalFormatString.length() == 0) {
            return null;
        } else {
            for (int i = 0; i < decimalFormatString.length(); i++) {
                char c = decimalFormatString.charAt(i);
                if ('0' <= c && c <= '9') {
                    output.append(persianNumbers[Integer.parseInt(String.valueOf(c))]);
                } else if (c == '.' || c == ',' || c == 'و') {
                    output.append(",");
                } else {
                    output.append(c);
                }
            }
            SpannableString spannableString = new SpannableString(output.toString());
            spannableString.setSpan(new StrikethroughSpan(), 0, output.length(), Spanned.SPAN_MARK_MARK);
            return spannableString;
        }
    }

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