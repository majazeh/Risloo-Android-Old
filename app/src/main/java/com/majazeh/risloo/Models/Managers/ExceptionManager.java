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
//            try {
//                is_ok = errorBody.getString("is_ok");
//                message = errorBody.getString("message");
//                message_text = errorBody.getString("message_text");
//
//                if (errorBody.has("refer"))
//                    referer = errorBody.getString("referer");
//                else
//                    referer = "";
//
//                if (errorBody.has("error"))
//                    errors = errorBody.getJSONObject("errors");
//                else
//                    errors = new JSONObject();
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
            switch (errorCode) {
                case 200:
                    switch (module) {
                        case "auth":
                            switch (exception) {
                                case "auth":
                                case "authTheory":
                                    farsi_message = "درخواست ورود شما ارسال شد";
                                    break;
                                case "register":
                                    farsi_message = "درخواست ثبت نام شما ارسال شد";
                                    break;
                                case "verification":
                                    farsi_message = "کد پیامکی به شما ارسال شد";
                                    break;
                                case "recovery":
                                    farsi_message = "بازیابی رمز عبور انجام شد";
                                    break;
                                case "me":
                                    farsi_message = "دریافت اطلاعات با موفقیت انجام شد";
                                    break;
                                case "edit":
                                    farsi_message = "ویرایش حساب با موفقیت انجام شد";
                                    break;
                                case "logOut":
                                    farsi_message = "خروج با موفقیت انجام شد";
                                    break;
                                default:
                                    farsi_message = "auth";
                            }
                            break;
                        case "center":
                            switch (exception) {
                                case "all":
                                case "my":
                                    farsi_message = "دریافت اطلاعات با موفقیت انجام شد";
                                    break;
                                case "request":
                                    farsi_message = "درخواست پذیرش شما ارسال شد";
                                    break;
                                default:
                                    farsi_message = "";
                            }
                            break;
                        case "explode":
                            switch (exception) {
                                case "explode":
                                    farsi_message = "دریافت اطلاعات با موفقیت انجام شد";
                                    break;
                                default:
                                    farsi_message = "";
                            }
                            break;
                        case "sample":
                            switch (exception) {
                                case "get":
                                    farsi_message = "دریافت اطلاعات با موفقیت انجام شد";
                                    break;
                                case "close":
                                    farsi_message = "نمونه با موفقیت بسته شد";
                                    break;
                                case "send":
                                    farsi_message = "پاسخ ها با موفقیت ارسال شد";
                                    break;
                                case "prerequisite":
                                    farsi_message = "پیش نیاز ها با موفقیت تکمیل شد";
                                    break;
                                default:
                                    farsi_message = "";
                            }
                            break;
                        default:
                            farsi_message = "اطلاعات با موفقیت ارسال شد";
                    }
                    break;
                default:
                    try {
                        farsi_message = errorBody.getString("message_text");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
//                    break;
//                case 400:
//                    switch (module) {
//                        default:
//                            farsi_message = "درخواست ناصحیح";
//                    }
//                    break;
//                case 401:
//                    switch (module) {
//                        default:
//                            farsi_message = "مجاز نیست";
//                    }
//                    break;
//                case 403:
//                    switch (module) {
//                        case "auth":
//                            farsi_message = "ابتدا لاگین کنید";
//                            break;
//                        default:
//                            farsi_message = "نمونه بسته شد";
//                    }
//                    break;
//                case 404:
//                    switch (module) {
//                        default:
//                            farsi_message = "یافت نشد";
//                    }
//                    break;
//                case 408:
//                    switch (module) {
//                        default:
//                            farsi_message = "وقفه درخواست";
//                    }
//                    break;
//                case 422:
//                    switch (module) {
//                        default:
//                            farsi_message = "فیلد ها اشکال دارد";
//                    }
//                    break;
//                case 500:
//                    switch (module) {
//                        case "center":
//                            farsi_message = "درخواست قبلا ارسال شده است";
//                            break;
//                        default:
//                            farsi_message = "خطای داخلی سرور";
//                    }
//                    break;
//                default:
//                    farsi_message = "خطای ارسال اطلاعات";
//            }
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