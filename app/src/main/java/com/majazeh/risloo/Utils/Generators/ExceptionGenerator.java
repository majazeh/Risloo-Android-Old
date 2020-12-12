package com.majazeh.risloo.Utils.Generators;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExceptionGenerator {

    // Vars
    public static String is_ok;
    public static String message;
    public static String message_text;
    public static String fa_message_text;
    public static String current_exception;

    // Objects
    public static JSONObject errors;

    public static void getException(boolean server, int code, JSONObject body, String exception) {
        try {
            if (server) {
                is_ok = body.getString("is_ok");

                message = body.getString("message");
                message_text = body.getString("message_text");

                fa_message_text = message_text;

                if (code != 200) {
                    current_exception = exception;

                    if (!body.isNull("errors")) {
                        errors = body.getJSONObject("errors");
                    } else {
                        errors = new JSONObject();
                    }
                }
            } else {
                current_exception = "";

                switch (exception) {
                    case "SocketTimeoutException":
                        fa_message_text = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
                        break;
                    case "JSONException":
                        fa_message_text = "مشکل دریافت JSON! دوباره تلاش کنید.";
                        break;
                    case "IOException":
                        fa_message_text = "مشکل دریافت IO! دوباره تلاش کنید.";
                        break;
                    case "OffLineException":
                        fa_message_text = "انترنت وصل نیست! لطفا متصل شوید.";
                        break;
                    case "FillOneException":
                        fa_message_text = "لطفا یک پارامتری را پر نمایید.";
                        break;
                    case "SelectRoomFirstException":
                        fa_message_text = "لطفا اول اتاق درمانی انتخاب کنید.";
                        break;
                    case "EmptyScalesForFilterException":
                        fa_message_text = "آزمونی برای انتخاب موجود نیست.";
                        break;
                    case "EmptyStatusForFilterException":
                        fa_message_text = "وضعیتی برای انتخاب موجود نیست.";
                        break;
                    case "SavedToDownloadException":
                        fa_message_text = "جواب ها در پوشه Download ذخیره شد.";
                        break;
                    case "NoSuffixException":
                        fa_message_text = "این مورد برای دریافت موجود نیست.";
                        break;
                    case "CantRequestException":
                        fa_message_text = "درخواست پذیرش برای مراکز درمانی من امکان پذیر نیست.";
                        break;
                    case "FileException":
                        fa_message_text = "فایلی انتخاب نشده است.";
                        break;
                    case "SendToException":
                        fa_message_text = "فایلی ارسال نشده است.";
                        break;
                    case "GalleryException":
                        fa_message_text = "عکسی انتخاب نشده است.";
                        break;
                    case "CameraException":
                        fa_message_text = "عکسی گرفته نشده است.";
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getErrorBody(String type) {
        try {
            StringBuilder exception = new StringBuilder();
            JSONArray data=errors.getJSONArray(type);

            for (int i = 0; i < data.length(); i++) {
                exception.append(data.get(i).toString());
                if (i>0) {
                    exception.append(" و ");
                }
            }

            return exception.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}