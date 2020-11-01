package com.majazeh.risloo.Utils.Generators;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExceptionGenerator {

    // Vars
    public static String exception;
    public static String is_ok;
    public static String message;
    public static String message_text;
    public static String fa_message_text;

    // Objects
    public static JSONObject errors;

    public static void getException(boolean server, int code, JSONObject body, String exception, String module) {
        try {
            if (server) {
                is_ok = body.getString("is_ok");

                message = body.getString("message");
                message_text = body.getString("message_text");

                fa_message_text = message_text;

                if (code == 200) {
//                    switch (module) {
//                        case "auth":
//                            switch (exception) {
//                                case "auth":
//                                case "authTheory":
//                                    fa_message_text = "درخواست ورود شما ارسال شد.";
//                                    break;
//                                case "register":
//                                    fa_message_text = "درخواست ثبت نام شما ارسال شد.";
//                                    break;
//                                case "verification":
//                                    fa_message_text = "کد پیامکی به شما ارسال شد.";
//                                    break;
//                                case "recovery":
//                                    fa_message_text = "بازیابی رمز عبور انجام شد.";
//                                    break;
//                                case "me":
//                                    fa_message_text = "دریافت اطلاعات با موفقیت انجام شد.";
//                                    break;
//                                case "edit":
//                                    fa_message_text = "ویرایش حساب شما انجام شد.";
//                                    break;
//                                case "avatar":
//                                    fa_message_text = "ویرایش عکس شما انجام شد.";
//                                    break;
//                                case "logOut":
//                                    fa_message_text = "خروج با موفقیت انجام شد.";
//                                    break;
//                                case "sendDoc":
//                                    fa_message_text = "مدارک هویتی شما ارسال شد.";
//                                    break;
//                                default:
//                                    fa_message_text = ".";
//                                    break;
//                            }
//                            break;
//                        case "center":
//                            switch (exception) {
//                                case "all":
//                                case "my":
//                                case "personalClinic":
//                                case "counselingCenter":
//                                    fa_message_text = "دریافت اطلاعات با موفقیت انجام شد.";
//                                    break;
//                                case "request":
//                                    fa_message_text = "درخواست پذیرش شما ارسال شد.";
//                                    break;
//                                case "create":
//                                    fa_message_text = "مرکز درمانی با موفقیت ساخته شد.";
//                                    break;
//                                case "edit":
//                                    fa_message_text = "مرکز درمانی با موفقیت اصلاح شد.";
//                                    break;
//                                default:
//                                    fa_message_text = ".";
//                                    break;
//                            }
//                            break;
//                        case "explode":
//                            switch (exception) {
//                                case "explode":
//                                    fa_message_text = "دریافت اطلاعات با موفقیت انجام شد.";
//                                    break;
//                                default:
//                                    fa_message_text = ".";
//                                    break;
//                            }
//                            break;
//                        case "sample":
//                            switch (exception) {
//                                case "single":
//                                case "all":
//                                case "scores":
//                                case "scales":
//                                case "rooms":
//                                case "references":
//                                case "cases":
//                                case "generals":
//                                    fa_message_text = "دریافت اطلاعات با موفقیت انجام شد.";
//                                    break;
//                                case "answers":
//                                    fa_message_text = "پاسخ ها با موفقیت ارسال شد.";
//                                    break;
//                                case "prerequisite":
//                                    fa_message_text = "پیش نیاز ها با موفقیت تکمیل شد.";
//                                    break;
//                                case "create":
//                                    fa_message_text = "نمونه با موفقیت ساخته شد.";
//                                    break;
//                                case "close":
//                                    fa_message_text = "نمونه با موفقیت بسته شد.";
//                                    break;
//                                case "score":
//                                    fa_message_text = "نمونه با موفقیت نمره گذاری شد.";
//                                    break;
//                                default:
//                                    fa_message_text = ".";
//                                    break;
//                            }
//                            break;
//                        default:
//                            fa_message_text = message_text;
//                            break;
//                    }
                } else {
                    if (exception != null) {
                        ExceptionGenerator.exception = exception;
                    }

                    if (!body.isNull("errors"))
                        errors = body.getJSONObject("errors");
                    else
                        errors = new JSONObject();
                }
            } else {
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
                    case "FileException":
                        fa_message_text = "فایلی انتخاب نشده است.";
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
        StringBuilder exception = new StringBuilder();
        try {
            JSONArray data=errors.getJSONArray(type);
            for (int i = 0; i < data.length(); i++) {
                exception.append(data.get(i).toString());
                if (i>0) {
                    exception.append(" و ");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return exception.toString();
    }

}