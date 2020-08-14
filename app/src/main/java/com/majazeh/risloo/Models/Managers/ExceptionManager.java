package com.majazeh.risloo.Models.Managers;

import org.json.JSONException;
import org.json.JSONObject;

public class ExceptionManager {
    public static String is_ok;
    public static String message;
    public static String message_text;
    public static String referer;
    public static JSONObject errors;
    public static String fa_message;

    public static void getError(int errorCode, JSONObject errorBody, boolean serverSide, String exception, String module) {
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
                    fa_message = "درخواست با موفقیت انجام شد";
                    break;
                case 400:
                    fa_message = "درخواست ناصحیح";
                    break;
                case 401:
                    fa_message = "مجاز نیست";
                    break;
                case 403:
                    fa_message = "این نمونه برای شما بسته شده است.";
                    break;
                case 404:
                    fa_message = "پیدا نشد";
                    break;
                case 408:
                    fa_message = "وقفه درخواست";
                    break;
                case 422:
                    fa_message = "فیلد های مورد نظر درست پر نشده اند";
                    break;
                case 500:
                    fa_message = "خطای داخلی سرور";
                    break;
                default:
                    fa_message = "مشکل در ارسال اطلاعات";
            }
        } else {
            switch (exception) {
                case "SocketTimeoutException":
                    fa_message = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
                    break;
                case "JSONException":
                    fa_message = "مشکل دریافت JSON! دوباره تلاش کنید.";
                    break;
                case "IOException":
                    fa_message = "مشکل دریافت IO! دوباره تلاش کنید.";
                    break;
                case "offline":
                    fa_message = "انترنت شما وصل نیست! لطفا متصل شوید.";
                    break;
            }
        }
    }
}
