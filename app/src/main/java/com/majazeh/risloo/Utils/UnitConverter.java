package com.majazeh.risloo.Utils;

import android.annotation.SuppressLint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnitConverter {

    @SuppressLint("SimpleDateFormat")
    public static String dateToString(String pattern, Date value) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(value);
    }

    @SuppressLint("SimpleDateFormat")
    public static Date stringToDate(String pattern, String value) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String separation(String value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        return decimalFormat.format(Double.parseDouble(value));
    }

    public static SpannableString lineation(String value) {
        SpannableString spannableString = new SpannableString(value);
        spannableString.setSpan(new StrikethroughSpan(), 0, value.length(), Spanned.SPAN_MARK_MARK);
        return spannableString;
    }

    public static String persianization(String value) {
        String[] persianNumbers = new String[] {"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "'۸", "۹"};
        String output = "";
        if (value.length() == 0) {
            return "";
        }
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if ('0' <= c && c <= '9') {
                output += persianNumbers[Integer.parseInt(String.valueOf(c))];
            } else if (c == '.' || c == ',' || c == 'و') {
                output += ",";
            } else {
                output += c;
            }
        }
        return output;
    }

    public static SpannableString separationLineation(String value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        String decimalFormatString = decimalFormat.format(Double.parseDouble(value));
        SpannableString spannableString = new SpannableString(decimalFormatString);
        spannableString.setSpan(new StrikethroughSpan(), 0, decimalFormatString.length(), Spanned.SPAN_MARK_MARK);
        return spannableString;
    }

    public static String seperationPersianization(String value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        String decimalFormatString = decimalFormat.format(Double.parseDouble(value));
        String[] persianNumbers = new String[] {"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "'۸", "۹"};
        String output = "";
        if (decimalFormatString.length() == 0) {
            return "";
        }
        for (int i = 0; i < decimalFormatString.length(); i++) {
            char c = decimalFormatString.charAt(i);
            if ('0' <= c && c <= '9') {
                output += persianNumbers[Integer.parseInt(String.valueOf(c))];
            } else if (c == '.' || c == ',' || c == 'و') {
                output += ",";
            } else {
                output += c;
            }
        }
        return output;
    }

    public static SpannableString persianizationLineation(String value) {
        String[] persianNumbers = new String[] {"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "'۸", "۹"};
        String output = "";
        if (value.length() == 0) {
            return null;
        }
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if ('0' <= c && c <= '9') {
                output += persianNumbers[Integer.parseInt(String.valueOf(c))];
            } else if (c == '.' || c == ',' || c == 'و') {
                output += ",";
            } else {
                output += c;
            }
        }
        SpannableString spannableString = new SpannableString(output);
        spannableString.setSpan(new StrikethroughSpan(), 0, output.length(), Spanned.SPAN_MARK_MARK);
        return spannableString;
    }

    public static SpannableString seperationPersianizationLineation(String value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        String decimalFormatString = decimalFormat.format(Double.parseDouble(value));
        String[] persianNumbers = new String[] {"۰", "۱", "۲", "۳", "۴", "۵", "۶", "۷", "'۸", "۹"};
        String output = "";
        if (decimalFormatString.length() == 0) {
            return null;
        }
        for (int i = 0; i < decimalFormatString.length(); i++) {
            char c = decimalFormatString.charAt(i);
            if ('0' <= c && c <= '9') {
                output += persianNumbers[Integer.parseInt(String.valueOf(c))];
            } else if (c == '.' || c == ',' || c == 'و') {
                output += ",";
            } else {
                output += c;
            }
        }
        SpannableString spannableString = new SpannableString(output);
        spannableString.setSpan(new StrikethroughSpan(), 0, output.length(), Spanned.SPAN_MARK_MARK);
        return spannableString;
    }

}