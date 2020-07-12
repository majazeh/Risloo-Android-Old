package com.majazeh.risloo.Models.Workers;

import android.content.Context;

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

    private AuthApi authApi;

    public AuthWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        authApi = RetroGenerator.getRetrofit().create(AuthApi.class);
    }

    @NonNull
    @Override
    public Result doWork() {
        String work = getInputData().getString("work");

        if (work != null) {
            switch (work) {
                case "auth":
                    try {
                        auth();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "authTheory":
                    try {
                        authTheory();
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                case "register":
                    try {
                        register();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "verification":
                    try {
                        verification();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        return Result.success();

    }

    public void auth() throws JSONException, IOException {
        String token = "";

        if (!AuthController.token.isEmpty()) {
            token = "Bearer " + AuthController.token;
        }

        Call<ResponseBody> call = authApi.auth(token, AuthController.callback, AuthController.authorizedKey);

        Response<ResponseBody> bodyResponse = call.execute();

        if (bodyResponse.isSuccessful()) {
            JSONObject object = new JSONObject(bodyResponse.body().string());
            if (object.has("key"))
                AuthController.key = object.getString("key");
            else
                AuthController.key = "";
            AuthController.preTheory = AuthController.theory;
            if (object.has("theory")) {
                AuthController.theory = object.getString("theory");
            } else
                AuthController.theory = "";
            if (object.has("callback"))
                AuthController.callback = object.getString("callback");
            else
                AuthController.callback = "";
            if (object.has("token"))
                AuthController.token = object.getString("token");
            else
                AuthController.token = "";
            AuthController.exception = "";
            AuthController.workState.postValue(1);
        } else {
            AuthController.exception = "";
            AuthController.workState.postValue(0);
            JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());
            AuthController.exception = errorBody.getString("message_text");
        }

    }

    public void authTheory() throws JSONException, IOException {
        String token = "";

        if (!AuthController.token.isEmpty()) {
            token = "Bearer" + AuthController.token;
        }

        Call<ResponseBody> call = authApi.authTheory(token, AuthController.key, AuthController.callback, AuthController.password, AuthController.code);

        Response<ResponseBody> bodyResponse = call.execute();
        if (bodyResponse.isSuccessful()) {
            JSONObject object = new JSONObject(bodyResponse.body().string());
            if (object.has("key"))
                AuthController.key = object.getString("key");
            else
                AuthController.key = "";
            AuthController.preTheory = AuthController.theory;
            if (object.has("theory")) {
                AuthController.theory = object.getString("theory");
            } else
                AuthController.theory = "";
            if (object.has("callback"))
                AuthController.callback = object.getString("callback");
            else
                AuthController.callback = "";
            if (object.has("token")) {
                AuthController.token = object.getString("token");
            } else
                AuthController.token = "";
            AuthController.exception = "";
            AuthController.workState.postValue(1);
        } else {
            AuthController.exception = "";
            AuthController.workState.postValue(0);
            JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());
            AuthController.exception = errorBody.getString("message_text");
        }
    }

    public void register() throws IOException, JSONException {
        Call<ResponseBody> call = authApi.register(AuthController.name, AuthController.mobile, AuthController.gender, AuthController.password);

        Response<ResponseBody> bodyResponse = call.execute();
        if (bodyResponse.isSuccessful()) {
            JSONObject object = new JSONObject(bodyResponse.body().string());
            if (object.has("key"))
                AuthController.key = object.getString("key");
            else
                AuthController.key = "";
            AuthController.preTheory = AuthController.theory;
            if (object.has("theory")) {
                AuthController.theory = object.getString("theory");
            } else
                AuthController.theory = "";
            if (object.has("callback"))
                AuthController.callback = object.getString("callback");
            else
                AuthController.callback = "";
            if (object.has("token"))
                AuthController.token = object.getString("token");
            else
                AuthController.token = "";
            AuthController.exception = "";
            AuthController.workState.postValue(1);
        } else {
            AuthController.exception = "";
            AuthController.workState.postValue(0);
            JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());
            AuthController.exception = errorBody.getString("message_text");
        }

    }

    private void verification() throws IOException, JSONException {
        Call<ResponseBody> call = authApi.verification(AuthController.mobile);

        Response<ResponseBody> bodyResponse = call.execute();
        if (bodyResponse.isSuccessful()) {
            JSONObject object = new JSONObject(bodyResponse.body().string());
            if (object.has("key"))
                AuthController.key = object.getString("key");
            else
                AuthController.key = "";
            AuthController.preTheory = AuthController.theory;
            if (object.has("theory")) {
                AuthController.theory = object.getString("theory");
            } else
                AuthController.theory = "";
            if (object.has("callback"))
                AuthController.callback = object.getString("callback");
            else
                AuthController.callback = "";
            if (object.has("token"))
                AuthController.token = object.getString("token");
            else
                AuthController.token = "";
            AuthController.exception = "";
            AuthController.workState.postValue(1);
        } else {
            AuthController.exception = "";
            AuthController.workState.postValue(0);
            JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());
            AuthController.exception = errorBody.getString("message_text");
        }

    }

}