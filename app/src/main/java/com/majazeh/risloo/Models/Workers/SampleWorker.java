package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.majazeh.risloo.Models.Remotes.Apis.SampleApi;
import com.majazeh.risloo.Models.Remotes.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Controller.SampleController;
import com.majazeh.risloo.Models.Repositories.SampleRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static com.majazeh.risloo.Models.Repositories.SampleRepository.localData;

public class SampleWorker extends Worker {

    // Apis
    private SampleApi sampleApi;

    // Controllers
    private SampleController sampleController;

    // Objects
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SampleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

        sampleApi = RetroGenerator.getRetrofit().create(SampleApi.class);

        sampleController = new SampleController();

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
                case "getSample":
                    getSample();
                    break;
                case "sendAnswers":
                    sendAnswers();
                    break;
            }
        }

        return Result.success();
    }

    private void getSample() {

        try {
            Call<ResponseBody> call = sampleApi.get("Bearer " + sharedPreferences.getString("token", ""), sharedPreferences.getString("sampleId", ""));

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {

                JSONObject jsonObject = new JSONObject(bodyResponse.body().string());
                sampleController.saveJsonToCache(context, jsonObject, sharedPreferences.getString("sampleId", ""));
                SampleRepository.workStateSample.postValue(1);
            } else {
                SampleRepository.workStateSample.postValue(0);
                SampleRepository.exception = bodyResponse.message();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendAnswers() {
        try {
           JSONObject jsonObject = new JSONObject();
            JSONArray array = new JSONArray(SampleRepository.remoteData);
            jsonObject.put("items", SampleRepository.remoteData);
            JsonElement root = new JsonParser().parse(String.valueOf(jsonObject));

            Call<ResponseBody> call = sampleApi.send("Bearer " + sharedPreferences.getString("token", ""), sharedPreferences.getString("sampleId", ""), jsonObject);

            Response<ResponseBody> bodyResponse = call.execute();
            SampleRepository.cache = false;
            if (bodyResponse.isSuccessful()) {

                SampleRepository.remoteData.clear();
                SampleRepository.workStateAnswer.postValue(1);

            } else {
                for (int i = 0; i < SampleRepository.remoteData.size(); i++) {
                    localData.add(SampleRepository.remoteData.get(i));
                }
                SampleRepository.remoteData.clear();
                SampleRepository.workStateAnswer.postValue(-1);
            }
            SampleRepository.inProgress = false;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}