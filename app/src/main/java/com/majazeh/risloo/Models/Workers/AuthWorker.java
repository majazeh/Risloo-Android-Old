package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Remotes.Apis.AuthApi;
import com.majazeh.risloo.Models.Remotes.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Controller.AuthController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class AuthWorker extends Worker {

    // Apis
    private AuthApi authApi;

    // Objects
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    // Vars
    private String token;

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
                case "signOut":
                    signOut();
                    break;
            }
        }

        return Result.success();
    }

    public void auth() {
        try {

            if (!AuthController.token.isEmpty()) {
                token = "Bearer " + AuthController.token;
            } else {
                token = "";
            }

            Call<ResponseBody> call = authApi.auth(token, AuthController.callback, AuthController.authorizedKey);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {

                JSONObject jsonObject = new JSONObject(bodyResponse.body().string());

                if (jsonObject.has("key")) {
                    AuthController.key = jsonObject.getString("key");
                } else {
                    AuthController.key = "";
                }
                AuthController.preTheory = AuthController.theory;
                if (jsonObject.has("theory")) {
                    AuthController.theory = jsonObject.getString("theory");
                } else {
                    AuthController.theory = "";
                }
                if (jsonObject.has("callback")) {
                    AuthController.callback = jsonObject.getString("callback");
                } else {
                    AuthController.callback = "";
                }
                if (jsonObject.has("token")) {
                    AuthController.token = jsonObject.getString("token");
                    editor.putString("token", AuthController.token);
                    editor.apply();
                }
                AuthController.exception = "";
                AuthController.workState.postValue(1);

            } else {
                AuthController.exception = "";
                AuthController.workState.postValue(0);
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());
                AuthController.exception = errorBody.getString("message_text");
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void authTheory() {
        try {

            if (!AuthController.token.isEmpty()) {
                token = "Bearer " + AuthController.token;
            } else {
                token = "";
            }

            Call<ResponseBody> call = authApi.authTheory(token, AuthController.key, AuthController.callback, AuthController.password, AuthController.code);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {

                JSONObject jsonObject = new JSONObject(bodyResponse.body().string());

                if (jsonObject.has("key")) {
                    AuthController.key = jsonObject.getString("key");
                } else {
                    AuthController.key = "";
                }
                AuthController.preTheory = AuthController.theory;
                if (jsonObject.has("theory")) {
                    AuthController.theory = jsonObject.getString("theory");
                } else {
                    AuthController.theory = "";
                }
                if (jsonObject.has("callback")) {
                    AuthController.callback = jsonObject.getString("callback");
                } else {
                    AuthController.callback = "";
                }
                if (jsonObject.has("token")) {
                    AuthController.token = jsonObject.getString("token");
                    editor.putString("token", AuthController.token);
                    editor.apply();
                }
                AuthController.exception = "";
                AuthController.workState.postValue(1);

            } else {
                AuthController.exception = "";
                AuthController.workState.postValue(0);
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());
                AuthController.exception = errorBody.getString("message_text");
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    public void register() {
        try {

            Call<ResponseBody> call = authApi.register(AuthController.name, AuthController.mobile, AuthController.gender, AuthController.password);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {

                JSONObject jsonObject = new JSONObject(bodyResponse.body().string());

                if (jsonObject.has("key")) {
                    AuthController.key = jsonObject.getString("key");
                } else {
                    AuthController.key = "";
                }
                AuthController.preTheory = AuthController.theory;
                if (jsonObject.has("theory")) {
                    AuthController.theory = jsonObject.getString("theory");
                } else {
                    AuthController.theory = "";
                }
                if (jsonObject.has("callback")) {
                    AuthController.callback = jsonObject.getString("callback");
                } else {
                    AuthController.callback = "";
                }
                if (jsonObject.has("token")) {
                    AuthController.token = jsonObject.getString("token");
                    editor.putString("token", AuthController.token);
                    editor.apply();
                }
                AuthController.exception = "";
                AuthController.workState.postValue(1);

            } else {
                AuthController.exception = "";
                AuthController.workState.postValue(0);
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());
                AuthController.exception = errorBody.getString("message_text");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void verification() {
        try {
            Call<ResponseBody> call = authApi.verification(AuthController.mobile);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {

                JSONObject jsonObject = new JSONObject(bodyResponse.body().string());

                if (jsonObject.has("key")) {
                    AuthController.key = jsonObject.getString("key");
                } else {
                    AuthController.key = "";
                }
                AuthController.preTheory = AuthController.theory;
                if (jsonObject.has("theory")) {
                    AuthController.theory = jsonObject.getString("theory");
                } else {
                    AuthController.theory = "";
                }
                if (jsonObject.has("callback")) {
                    AuthController.callback = jsonObject.getString("callback");
                } else {
                    AuthController.callback = "";
                }
                if (jsonObject.has("token")) {
                    AuthController.token = jsonObject.getString("token");
                    editor.putString("token", AuthController.token);
                    editor.apply();
                }
                AuthController.exception = "";
                AuthController.workState.postValue(1);

            } else {
                AuthController.exception = "";
                AuthController.workState.postValue(0);
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());
                AuthController.exception = errorBody.getString("message_text");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void recovery() {
        try {
            Call<ResponseBody> call = authApi.recovery(AuthController.mobile);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {

                JSONObject jsonObject = new JSONObject(bodyResponse.body().string());

                if (jsonObject.has("key")) {
                    AuthController.key = jsonObject.getString("key");
                } else {
                    AuthController.key = "";
                }
                AuthController.preTheory = AuthController.theory;
                if (jsonObject.has("theory")) {
                    AuthController.theory = jsonObject.getString("theory");
                } else {
                    AuthController.theory = "";
                }
                if (jsonObject.has("callback")) {
                    AuthController.callback = jsonObject.getString("callback");
                } else {
                    AuthController.callback = "";
                }
                if (jsonObject.has("token")) {
                    AuthController.token = jsonObject.getString("token");
                    editor.putString("token", AuthController.token);
                    editor.apply();
                }
                AuthController.exception = "";
                AuthController.workState.postValue(1);

            } else {
                AuthController.exception = "";
                AuthController.workState.postValue(0);
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());
                AuthController.exception = errorBody.getString("message_text");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void me() {
        try {
            Call<ResponseBody> call = authApi.me("Bearer " + sharedPreferences.getString("token", ""));

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject jsonObject = new JSONObject(bodyResponse.body().string());
                JSONObject data = jsonObject.getJSONObject("data");
                editor.putString("name", data.getString("name"));
                editor.putString("email", data.getString("email"));
                editor.putString("mobile", data.getString("mobile"));
                editor.putString("gender", data.getString("gender"));
                editor.apply();
                AuthController.workState.postValue(1);

            } else {
                AuthController.workState.postValue(0);
                editor.putString("name", "");
                editor.putString("email", "");
                editor.putString("mobile", "");
                editor.putString("gender", "");
                editor.apply();
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void signOut() {
        Log.e("hee", "hoo");
        try {
            Call<ResponseBody> call = authApi.logout("Bearer " + sharedPreferences.getString("token", ""));

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                AuthController.workState.postValue(1);

            } else {
                AuthController.workState.postValue(0);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}