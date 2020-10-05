package com.majazeh.risloo.Models.Managers;

import org.json.JSONException;
import org.json.JSONObject;

public class ExceptionManager {

    // Vars
    public static String current_exception;
    public static String is_ok;
    public static String message;
    public static String message_text;
    public static String referer;
    public static String farsi_message;

    // Objects
    public static JSONObject errors;

    public static void getException(int code, JSONObject body, boolean serverSide, String exception, String module) {
        if (serverSide) {
            switch (code) {
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
                                    farsi_message = "ویرایش حساب شما انجام شد";
                                    break;
                                case "avatar":
                                    farsi_message = "ویرایش عکس شما انجام شد";
                                    break;
                                case "logOut":
                                    farsi_message = "خروج با موفقیت انجام شد";
                                    break;
                                default:
                                    farsi_message = ".";
                                    break;
                            }
                            break;
                        case "center":
                            switch (exception) {
                                case "all":
                                case "my":
                                case "personalClinic":
                                case "counselingCenter":
                                    farsi_message = "دریافت اطلاعات با موفقیت انجام شد";
                                    break;
                                case "request":
                                    farsi_message = "درخواست پذیرش شما ارسال شد";
                                    break;
                                case "create":
                                    farsi_message = "مرکز درمانی با موفقیت ساخته شد";
                                    break;
                                case "edit":
                                    farsi_message = "مرکز درمانی با موفقیت اصلاح شد";
                                    break;
                                default:
                                    farsi_message = ".";
                                    break;
                            }
                            break;
                        case "explode":
                            switch (exception) {
                                case "explode":
                                    farsi_message = "دریافت اطلاعات با موفقیت انجام شد";
                                    break;
                                default:
                                    farsi_message = ".";
                                    break;
                            }
                            break;
                        case "sample":
                            switch (exception) {
                                case "single":
                                case "all":
                                case "scores":
                                case "scales":
                                case "rooms":
                                case "references":
                                case "cases":
                                case "generals":
                                    farsi_message = "دریافت اطلاعات با موفقیت انجام شد";
                                    break;
                                case "answers":
                                    farsi_message = "پاسخ ها با موفقیت ارسال شد";
                                    break;
                                case "prerequisite":
                                    farsi_message = "پیش نیاز ها با موفقیت تکمیل شد";
                                    break;
                                case "create":
                                    farsi_message = "نمونه با موفقیت ساخته شد";
                                    break;
                                case "close":
                                    farsi_message = "نمونه با موفقیت بسته شد";
                                    break;
                                case "score":
                                    farsi_message = "نمونه با موفقیت نمره گذاری شد";
                                    break;
                                default:
                                    farsi_message = ".";
                                    break;
                            }
                            break;
                        default:
                            farsi_message = ".";
                            break;
                    }
                    break;
                default:
                    try {
                        current_exception = exception;

                        is_ok = body.getString("is_ok");
                        message = body.getString("message");
                        message_text = body.getString("message_text");

                        if (body.has("refer"))
                            referer = body.getString("referer");
                        else
                            referer = "";

                        if (!body.isNull("errors"))
                            errors = body.getJSONObject("errors");
                        else
                            errors = new JSONObject();

                        farsi_message = message_text;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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