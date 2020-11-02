package com.majazeh.risloo.Utils.Managers;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import saman.zamani.persiandate.PersianDate;

public class DateManager {

    @SuppressLint("SimpleDateFormat")
    public static Date stringToDate(String pattern, String value) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        } return null;
    }

    @SuppressLint("SimpleDateFormat")
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

}