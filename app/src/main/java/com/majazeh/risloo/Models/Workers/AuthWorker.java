package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Apis.AuthApi;
import com.majazeh.risloo.Models.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Repositories.AuthRepository;

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
            return "Bearer " + sharedPreferences.getString("token", "");
        }
        return "";
    }

    private void auth() {
        try {
            Call<ResponseBody> call = authApi.auth(token(), AuthRepository.callback, AuthRepository.authorizedKey);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                if (successBody.has("key")) {
                    AuthRepository.key = successBody.getString("key");
                } else {
                    AuthRepository.key = "";
                }

                AuthRepository.preTheory = AuthRepository.theory;

                if (successBody.has("theory")) {
                    AuthRepository.theory = successBody.getString("theory");
                } else {
                    AuthRepository.theory = "";
                }

                if (successBody.has("callback")) {
                    AuthRepository.callback = successBody.getString("callback");
                } else {
                    AuthRepository.callback = "";
                }

                if (successBody.has("token")) {
                    editor.putString("token", successBody.getString("token"));
                    editor.apply();

                    me();
                }

                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void authTheory() {
        try {
            Call<ResponseBody> call = authApi.authTheory(token(), AuthRepository.key, AuthRepository.callback, AuthRepository.password, AuthRepository.code);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                if (successBody.has("key")) {
                    AuthRepository.key = successBody.getString("key");
                } else {
                    AuthRepository.key = "";
                }

                AuthRepository.preTheory = AuthRepository.theory;

                if (successBody.has("theory")) {
                    AuthRepository.theory = successBody.getString("theory");
                } else {
                    AuthRepository.theory = "";
                }

                if (successBody.has("callback")) {
                    AuthRepository.callback = successBody.getString("callback");
                } else {
                    AuthRepository.callback = "";
                }

                if (successBody.has("token")) {
                    editor.putString("token", successBody.getString("token"));
                    editor.apply();

                    me();
                }

                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void register() {
        try {
            Call<ResponseBody> call = authApi.register(AuthRepository.name, AuthRepository.mobile, AuthRepository.gender, AuthRepository.password);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                if (successBody.has("key")) {
                    AuthRepository.key = successBody.getString("key");
                } else {
                    AuthRepository.key = "";
                }

                AuthRepository.preTheory = AuthRepository.theory;

                if (successBody.has("theory")) {
                    AuthRepository.theory = successBody.getString("theory");
                } else {
                    AuthRepository.theory = "";
                }

                if (successBody.has("callback")) {
                    AuthRepository.callback = successBody.getString("callback");
                } else {
                    AuthRepository.callback = "";
                }

                if (successBody.has("token")) {
                    editor.putString("token", successBody.getString("token"));
                    editor.apply();

                    me();
                }

                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void verification() {
        try {
            Call<ResponseBody> call = authApi.verification(AuthRepository.mobile);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                if (successBody.has("key")) {
                    AuthRepository.key = successBody.getString("key");
                } else {
                    AuthRepository.key = "";
                }

                AuthRepository.preTheory = AuthRepository.theory;

                if (successBody.has("theory")) {
                    AuthRepository.theory = successBody.getString("theory");
                } else {
                    AuthRepository.theory = "";
                }

                if (successBody.has("callback")) {
                    AuthRepository.callback = successBody.getString("callback");
                } else {
                    AuthRepository.callback = "";
                }

                if (successBody.has("token")) {
                    editor.putString("token", successBody.getString("token"));
                    editor.apply();

                    me();
                }

                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void recovery() {
        try {
            Call<ResponseBody> call = authApi.recovery(AuthRepository.mobile);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                if (successBody.has("key")) {
                    AuthRepository.key = successBody.getString("key");
                } else {
                    AuthRepository.key = "";
                }

                AuthRepository.preTheory = AuthRepository.theory;

                if (successBody.has("theory")) {
                    AuthRepository.theory = successBody.getString("theory");
                } else {
                    AuthRepository.theory = "";
                }

                if (successBody.has("callback")) {
                    AuthRepository.callback = successBody.getString("callback");
                } else {
                    AuthRepository.callback = "";
                }

                if (successBody.has("token")) {
                    editor.putString("token", successBody.getString("token"));
                    editor.apply();

                    me();
                }

                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void me() {
        try {
            Call<ResponseBody> call = authApi.me("Bearer " + sharedPreferences.getString("token", ""));

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONObject data = successBody.getJSONObject("data");

                editor.putString("name", data.getString("name"));
                editor.putString("type", data.getString("type"));
                editor.putString("mobile", data.getString("mobile"));
                editor.putString("email", data.getString("email"));
                editor.putString("gender", data.getString("gender"));
                editor.putString("birthday", data.getString("birthday"));

//                JSONArray avatar = data.getJSONArray("avatar");
//                editor.putString("avatar", String.valueOf(avatar.getJSONObject(0)));

                editor.apply();

                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void edit() {
        try {
            Call<ResponseBody> call = authApi.edit("Bearer " + sharedPreferences.getString("token", ""), AuthRepository.name, AuthRepository.gender, AuthRepository.birthday);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                editor.putString("name", AuthRepository.name);
                editor.putString("gender", AuthRepository.gender);
                editor.putString("birthday", AuthRepository.birthday);
                editor.apply();

                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void logOut() {
        try {
            Call<ResponseBody> call = authApi.logOut("Bearer " + sharedPreferences.getString("token", ""));

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                editor.remove("token");
                editor.remove("name");
                editor.remove("type");
                editor.remove("mobile");
                editor.remove("email");
                editor.remove("gender");
                editor.remove("birthday");
                editor.remove("avatar");
                editor.apply();

                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

}