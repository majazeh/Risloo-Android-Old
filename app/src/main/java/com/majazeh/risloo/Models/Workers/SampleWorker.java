package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Remotes.Apis.SampleApi;
import com.majazeh.risloo.Models.Remotes.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Controller.SampleController;
import com.majazeh.risloo.Models.Repositories.SampleRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

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

    private String token() {
        if (!sharedPreferences.getString("token", "").equals("")) {
            return  "Bearer " + sharedPreferences.getString("token", "");
        }
        return "";
    }

    private void getSample() {
        try {
            Call<ResponseBody> call = sampleApi.get(token(), sharedPreferences.getString("sampleId", ""));

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());

                repository.saveSampleToCache(context, succesBody, sharedPreferences.getString("sampleId", ""));

                SampleController.exception = "موفقیت آمیز";
                SampleController.workStateSample.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                SampleController.exception = errorBody.getString("message_text");
                SampleController.workStateSample.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            SampleController.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            SampleController.workStateSample.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            SampleController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            SampleController.workStateSample.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            SampleController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            SampleController.workStateSample.postValue(0);
        }
    }

    private void sendAnswers() {
        try {


            SampleController.cache = false;
            JSONArray jsonArray = new JSONArray(SampleRepository.remoteData);
            Call<ResponseBody> call = sampleApi.send("Bearer " + sharedPreferences.getString("token", ""), sharedPreferences.getString("sampleId", ""), jsonArray);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());

                SampleRepository.remoteData.clear();

                SampleController.exception = "موفقیت آمیز";
                SampleController.workStateAnswer.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                repository.insertRemoteToLocal();

                SampleController.exception = errorBody.getString("message_text");
                SampleController.workStateAnswer.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            SampleController.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            SampleController.workStateAnswer.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            SampleController.exception = "مشکل ادریافت JSON! دوباره تلاش کنید.";
            SampleController.workStateAnswer.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            SampleController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            SampleController.workStateAnswer.postValue(0);
        }
    }

}