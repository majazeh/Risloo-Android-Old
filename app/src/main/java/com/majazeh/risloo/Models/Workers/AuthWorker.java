package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Remotes.Apis.SampleApi;
import com.majazeh.risloo.Models.Remotes.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Repositories.Authentication.AuthController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class AuthWorker extends Worker {
    private SampleApi sampleApi;

    public AuthWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        sampleApi = RetroGenerator.getRetrofit().create(SampleApi.class);
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
                case "signIn":
                    try {
                        signIn();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
        return Result.success();

    }

    public void auth() throws JSONException, IOException {
        String token = "";
        if (token()) {
            token = "Bearer " + AuthController.token;
        }
        Call<ResponseBody> call = sampleApi.auth(token, AuthController.callback, AuthController.authorized_key);

        Response<ResponseBody> bodyResponse = call.execute();
        if (bodyResponse.isSuccessful()) {
            JSONObject object = new JSONObject(bodyResponse.body().string());
            if (object.has("key"))
                AuthController.key = object.getString("key");
            else
                AuthController.key = "";
            if (object.has("theory"))
                AuthController.theory = object.getString("theory");
            else
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
            AuthController.workState.postValue(0);
            AuthController.exception = bodyResponse.message();
        }

    }

    public void authTheory() throws JSONException, IOException {
        String token = "";
        if (token()) {
            token = "Bearer" + AuthController.token;
        }
        Call<ResponseBody> call = sampleApi.authTheory(token, AuthController.key, AuthController.callback, AuthController.password, AuthController.code);
        Response<ResponseBody> bodyResponse = call.execute();
        if (bodyResponse.isSuccessful()) {
            JSONObject object = new JSONObject(bodyResponse.body().string());
            if (object.has("key"))
                AuthController.key = object.getString("key");
            else
                AuthController.key = "";
            if (object.has("theory"))
                AuthController.theory = object.getString("theory");
            else
                AuthController.theory = "";
            if (object.has("callback"))
                AuthController.callback = object.getString("callback");
            else
                AuthController.callback = "";
            if (object.has("token")) {
                Log.e("token", object.getString("token"));
                AuthController.token = object.getString("token");
            }
            else
                AuthController.token = "";
            AuthController.exception = "";
            AuthController.workState.postValue(1);
        } else {
            AuthController.workState.postValue(0);
            AuthController.exception = bodyResponse.message();


        }
    }

    public void signIn() throws IOException, JSONException {
        Call<ResponseBody> call = sampleApi.signIn(AuthController.name, AuthController.gender, AuthController.mobile, AuthController.password);
        Response<ResponseBody> bodyResponse = call.execute();
        if (bodyResponse.isSuccessful()) {
            JSONObject object = new JSONObject(bodyResponse.body().string());
            if (object.has("key"))
                AuthController.key = object.getString("key");
            else
                AuthController.key = "";
            if (object.has("theory"))
                AuthController.theory = object.getString("theory");
            else
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
            AuthController.workState.postValue(0);
            AuthController.exception = bodyResponse.message();
        }

    }

    public boolean token() {
        return !AuthController.token.isEmpty();
    }
}
