package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Apis.SampleApi;
import com.majazeh.risloo.Models.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Managers.FileManager;
import com.majazeh.risloo.Models.Repositories.SampleRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SampleWorker extends Worker {

    // Apis
    private SampleApi sampleApi;

    // Repository
    private SampleRepository repository;

    // Objects
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SampleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

        sampleApi = RetroGenerator.getRetrofit().create(SampleApi.class);

        repository = new SampleRepository();

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
                case "getSingle":
                    getSingle();
                    break;
                case "getAll":
                    getAll();
                    break;
                case "close":
                    close();
                    break;



                case "sendAnswers":
                    sendAnswers();
                    break;
                case "sendPrerequisite":
                    sendPrerequisite();
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

    private void getSingle() {
        try {
            Call<ResponseBody> call = sampleApi.getSingle(token(), sharedPreferences.getString("sampleId", ""));

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONObject data = successBody.getJSONObject("data");

                FileManager.writeSampleAnswerToCache(context, successBody, sharedPreferences.getString("sampleId", ""));
                FileManager.writePrerequisiteAnswerToCache(context,data.getJSONArray("prerequisite"),sharedPreferences.getString("sampleId",""));

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "get", "sample");
                SampleRepository.workStateSample.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "get", "sample");
                SampleRepository.workStateSample.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "sample");
            SampleRepository.workStateSample.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "sample");
            SampleRepository.workStateSample.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "sample");
            SampleRepository.workStateSample.postValue(0);
        }
    }

    private void getAll() {
        try {
            Call<ResponseBody> call = sampleApi.getAll(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                FileManager.writeObjectToCache(context, successBody, "samples", "all");

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "all", "sample");
                SampleRepository.workStateSample.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "all", "sample");
                SampleRepository.workStateSample.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "sample");
            SampleRepository.workStateSample.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "sample");
            SampleRepository.workStateSample.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "sample");
            SampleRepository.workStateSample.postValue(0);
        }
    }

    private void close() {
        try {
            Call<ResponseBody> call = sampleApi.close(token(), SampleRepository.sampleId);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "close", "sample");
                SampleRepository.workStateSample.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "close", "sample");
                SampleRepository.workStateSample.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "sample");
            SampleRepository.workStateSample.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "sample");
            SampleRepository.workStateSample.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "sample");
            SampleRepository.workStateSample.postValue(0);
        }
    }








    private void sendAnswers() {
        try {
            SampleRepository.cache = false;

            HashMap hashMap = new HashMap();
            hashMap.put("items", SampleRepository.remoteData);

            Call<ResponseBody> call = sampleApi.send("Bearer " + sharedPreferences.getString("token", ""), sharedPreferences.getString("sampleId", ""), hashMap);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                SampleRepository.remoteData.clear();

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "send", "sample");
                SampleRepository.workStateAnswer.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                repository.insertRemoteToLocal();

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "send", "sample");
                SampleRepository.workStateAnswer.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "sample");
            SampleRepository.workStateAnswer.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "sample");
            SampleRepository.workStateAnswer.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "sample");
            SampleRepository.workStateAnswer.postValue(0);
        }
    }

    private void sendPrerequisite() {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("prerequisite",SampleRepository.prerequisiteData);

            Call<ResponseBody> call = sampleApi.send("Bearer " + sharedPreferences.getString("token", ""), sharedPreferences.getString("sampleId", ""), hashMap);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                SampleRepository.prerequisiteData = new HashMap();
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                SampleRepository.remoteData.clear();

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "prerequisite", "sample");
                SampleRepository.workStateAnswer.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "prerequisite", "sample");
                SampleRepository.workStateAnswer.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "sample");
            SampleRepository.workStateAnswer.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "sample");
            SampleRepository.workStateAnswer.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "sample");
            SampleRepository.workStateAnswer.postValue(0);
        }
    }

}