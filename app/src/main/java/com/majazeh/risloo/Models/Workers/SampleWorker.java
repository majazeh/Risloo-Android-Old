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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

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

                sampleController.saveJsonToCache(context, succesBody, sharedPreferences.getString("sampleId", ""));

                SampleRepository.exception = "موفقیت آمیز";
                SampleRepository.workStateSample.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                SampleRepository.exception = errorBody.getString("message_text");
                SampleRepository.workStateSample.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            SampleRepository.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            SampleRepository.workStateSample.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            SampleRepository.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            SampleRepository.workStateSample.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            SampleRepository.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            SampleRepository.workStateSample.postValue(0);
        }
    }

    private void sendAnswers() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("items", SampleRepository.remoteData);

            Call<ResponseBody> call = sampleApi.send("Bearer " + sharedPreferences.getString("token", ""), sharedPreferences.getString("sampleId", ""), jsonObject);

            Response<ResponseBody> bodyResponse = call.execute();
            SampleRepository.cache = false;
            if (bodyResponse.isSuccessful()) {
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());

                SampleRepository.remoteData.clear();

                SampleRepository.exception = "موفقیت آمیز";
                SampleRepository.workStateAnswer.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                for (int i = 0; i < SampleRepository.remoteData.size(); i++) {
                    localData.add(SampleRepository.remoteData.get(i));
                }
                SampleRepository.remoteData.clear();

                SampleRepository.exception = errorBody.getString("message_text");
                SampleRepository.workStateAnswer.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            SampleRepository.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            SampleRepository.workStateAnswer.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            SampleRepository.exception = "مشکل ادریافت JSON! دوباره تلاش کنید.";
            SampleRepository.workStateAnswer.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            SampleRepository.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            SampleRepository.workStateAnswer.postValue(0);
        }
    }

}