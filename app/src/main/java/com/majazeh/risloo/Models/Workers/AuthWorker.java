package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Remotes.Apis.AuthApi;
import com.majazeh.risloo.Models.Remotes.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Controller.AuthController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class AuthWorker extends Worker {

    // Apis
    private AuthApi authApi;

    // Objects
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public AuthWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        authApi = RetroGenerator.getRetrofit().create(AuthApi.class);

        sharedPreferences = context.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();
    }

    @NonNull
    @Override
    public Result doWork() {
        String work = getInputData().getString("work");

        if (work != null) {
            switch (work) {
                case "auth":
                    auth();
                    break;
                case "authTheory":
                    authTheory();
                    break;
                case "register":
                    register();
                    break;
                case "verification":
                    verification();
                    break;
                case "recovery":
                    recovery();
                    break;
                case "me":
                    me();
                    break;
                case "edit":
                    edit();
                    break;
                case "logOut":
                    logOut();
                    break;
            }
        }

        return Result.success();
    }

    private String token() {
        if (!sharedPreferences.getString("token", "").equals("")) {
            return  "Bearer " + sharedPreferences.getString("token", "");
        }
        return "";
    }

    private void auth() {
        try {
            Call<ResponseBody> call = authApi.auth(token(), AuthController.callback, AuthController.authorizedKey);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());

                if (succesBody.has("key")) {
                    AuthController.key = succesBody.getString("key");
                } else {
                    AuthController.key = "";
                }

                AuthController.preTheory = AuthController.theory;

                if (succesBody.has("theory")) {
                    AuthController.theory = succesBody.getString("theory");
                } else {
                    AuthController.theory = "";
                }

                if (succesBody.has("callback")) {
                    AuthController.callback = succesBody.getString("callback");
                } else {
                    AuthController.callback = "";
                }

                if (succesBody.has("token")) {
                    AuthController.token = succesBody.getString("token");

                    editor.putString("token", AuthController.token);
                    editor.apply();

                    me();
                }

                AuthController.exception = "دریافت اطلاعات با موفقیت انجام شد.";
                AuthController.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                AuthController.exception = errorBody.getString("message_text");
                AuthController.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        }
    }

    private void authTheory() {
        try {
            Call<ResponseBody> call = authApi.authTheory(token(), AuthController.key, AuthController.callback, AuthController.password, AuthController.code);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());

                if (succesBody.has("key")) {
                    AuthController.key = succesBody.getString("key");
                } else {
                    AuthController.key = "";
                }

                AuthController.preTheory = AuthController.theory;

                if (succesBody.has("theory")) {
                    AuthController.theory = succesBody.getString("theory");
                } else {
                    AuthController.theory = "";
                }

                if (succesBody.has("callback")) {
                    AuthController.callback = succesBody.getString("callback");
                } else {
                    AuthController.callback = "";
                }

                if (succesBody.has("token")) {
                    AuthController.token = succesBody.getString("token");

                    editor.putString("token", AuthController.token);
                    editor.apply();

                    me();
                }

                AuthController.exception = "دریافت اطلاعات با موفقیت انجام شد.";
                AuthController.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                AuthController.exception = errorBody.getString("message_text");
                AuthController.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        }
    }

    private void register() {
        try {
            Call<ResponseBody> call = authApi.register(AuthController.name, AuthController.mobile, AuthController.gender, AuthController.password);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());

                if (succesBody.has("key")) {
                    AuthController.key = succesBody.getString("key");
                } else {
                    AuthController.key = "";
                }

                AuthController.preTheory = AuthController.theory;

                if (succesBody.has("theory")) {
                    AuthController.theory = succesBody.getString("theory");
                } else {
                    AuthController.theory = "";
                }

                if (succesBody.has("callback")) {
                    AuthController.callback = succesBody.getString("callback");
                } else {
                    AuthController.callback = "";
                }

                if (succesBody.has("token")) {
                    AuthController.token = succesBody.getString("token");

                    editor.putString("token", AuthController.token);
                    editor.apply();

                    me();
                }

                AuthController.exception = "ثبت نام با موفقیت انجام شد.";
                AuthController.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                AuthController.exception = errorBody.getString("message_text");
                AuthController.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        }
    }

    private void verification() {
        try {
            Call<ResponseBody> call = authApi.verification(AuthController.mobile);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());

                if (succesBody.has("key")) {
                    AuthController.key = succesBody.getString("key");
                } else {
                    AuthController.key = "";
                }

                AuthController.preTheory = AuthController.theory;

                if (succesBody.has("theory")) {
                    AuthController.theory = succesBody.getString("theory");
                } else {
                    AuthController.theory = "";
                }

                if (succesBody.has("callback")) {
                    AuthController.callback = succesBody.getString("callback");
                } else {
                    AuthController.callback = "";
                }

                if (succesBody.has("token")) {
                    AuthController.token = succesBody.getString("token");

                    editor.putString("token", AuthController.token);
                    editor.apply();

                    me();
                }

                AuthController.exception = "احراز هویت با موفقیت انجام شد.";
                AuthController.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                AuthController.exception = errorBody.getString("message_text");
                AuthController.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        }
    }

    private void recovery() {
        try {
            Call<ResponseBody> call = authApi.recovery(AuthController.mobile);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());

                if (succesBody.has("key")) {
                    AuthController.key = succesBody.getString("key");
                } else {
                    AuthController.key = "";
                }

                AuthController.preTheory = AuthController.theory;

                if (succesBody.has("theory")) {
                    AuthController.theory = succesBody.getString("theory");
                } else {
                    AuthController.theory = "";
                }

                if (succesBody.has("callback")) {
                    AuthController.callback = succesBody.getString("callback");
                } else {
                    AuthController.callback = "";
                }

                if (succesBody.has("token")) {
                    AuthController.token = succesBody.getString("token");

                    editor.putString("token", AuthController.token);
                    editor.apply();

                    me();
                }

                AuthController.exception = "رمز عبور با موفقیت بازیابی شد.";
                AuthController.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                AuthController.exception = errorBody.getString("message_text");
                AuthController.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        }
    }

    private void me() {
        try {
            Call<ResponseBody> call = authApi.me("Bearer " + sharedPreferences.getString("token", ""));

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());
                JSONObject data = succesBody.getJSONObject("data");

                editor.putString("name", data.getString("name"));
                editor.putString("type", data.getString("type"));
                editor.putString("mobile", data.getString("mobile"));
                editor.putString("email", data.getString("email"));
                editor.putString("gender", data.getString("gender"));
                editor.putString("birthday", data.getString("birthday"));

//                JSONArray avatar = data.getJSONArray("avatar");
//                editor.putString("avatar", String.valueOf(avatar.getJSONObject(0)));

                editor.apply();

                AuthController.exception = "دریافت اطلاعات با موفقیت انجام شد.";
                AuthController.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                AuthController.exception = errorBody.getString("message_text");
                AuthController.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        }
    }

    private void edit() {
        try {
            Call<ResponseBody> call = authApi.edit("Bearer " + sharedPreferences.getString("token", ""), AuthController.name, AuthController.gender, AuthController.birthday);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());

                editor.putString("name", AuthController.name);
                editor.putString("gender", AuthController.gender);
                editor.putString("birthday", AuthController.birthday);
                editor.apply();

                AuthController.exception = "اصلاح پروفایل با موفقیت انجام شد.";
                AuthController.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                AuthController.exception = errorBody.getString("message_text");
                AuthController.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        }
    }

    private void logOut() {
        try {
            Call<ResponseBody> call = authApi.logOut("Bearer " + sharedPreferences.getString("token", ""));

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());

                editor.remove("token");
                editor.remove("name");
                editor.remove("type");
                editor.remove("mobile");
                editor.remove("email");
                editor.remove("gender");
                editor.remove("birthday");
                editor.remove("avatar");
                editor.apply();

                AuthController.exception = "خروج با موفقیت انجام شد.";
                AuthController.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                AuthController.exception = errorBody.getString("message_text");
                AuthController.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            AuthController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            AuthController.workState.postValue(0);
        }
    }

}