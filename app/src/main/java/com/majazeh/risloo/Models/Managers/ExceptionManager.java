package com.majazeh.risloo.Models.Managers;

import org.json.JSONException;
import org.json.JSONObject;

public class ExceptionManager {

    // Vars
    public static String is_ok;
    public static String message;
    public static String message_text;
    public static String referer;
    public static String farsi_message;

    // Objects
    public static JSONObject errors;

    public static void getException(int errorCode, JSONObject errorBody, boolean serverSide, String exception, String module) {
        if (serverSide) {
            try {
                is_ok = errorBody.getString("is_ok");
                message = errorBody.getString("message");
                message_text = errorBody.getString("message_text");

                if (errorBody.has("refer"))
                    referer = errorBody.getString("referer");
                else
                    referer = "";

                if (errorBody.has("error"))
                    errors = errorBody.getJSONObject("errors");
                else
                    errors = new JSONObject();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            switch (errorCode) {
                case 200:
                    farsi_message = "درخواست صحیح";
                    break;
                case 400:
                    farsi_message = "درخواست ناصحیح";
                    break;
                case 401:
                    farsi_message = "مجاز نیست";
                    break;
                case 403:
                    farsi_message = "نمونه بسته شد";
                    break;
                case 404:
                    farsi_message = "یافت نشد";
                    break;
                case 408:
                    farsi_message = "وقفه درخواست";
                    break;
                case 422:
                    farsi_message = "فیلد ها اشکال دارد";
                    break;
                case 500:
                    farsi_message = "خطای داخلی سرور";
                    break;
                default:
                    farsi_message = "خطای ارسال اطلاعات";
            }

        } else {
            switch (exception) {
                case "SocketTimeoutException":
                    farsi_message = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
                    break;
                case "JSONException":
                    farsi_message = "مشکل دریافت JSON! دوباره تلاش کنید.";
                    break;
                case "IOException":
                    farsi_message = "مشکل دریافت IO! دوباره تلاش کنید.";
                    break;
                case "OffLine":
                    farsi_message = "انترنت وصل نیست! لطفا متصل شوید.";
                    break;
            }
        }
    }

}