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
                case "getSample":
                    getSample();
                    break;
                case "closeSample":
                    closeSample();
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

    private void getSample() {
        try {
            Call<ResponseBody> call = sampleApi.get(token(), sharedPreferences.getString("sampleId", ""));

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());
                JSONObject data = succesBody.getJSONObject("data");

                repository.saveSampleToCache(context, succesBody, sharedPreferences.getString("sampleId", ""));
                repository.savePrerequisiteToCache(context,data.getJSONArray("prerequisite"),sharedPreferences.getString("sampleId",""));

                SampleController.exception = "نمونه با موفقیت دریافت شد.";
                SampleController.workStateSample.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                if (errorBody.getString("message_text").equals("This action is unauthorized.")) {
                    SampleController.exception = "این نمونه برای شما بسته شده است.";
                } else {
                    SampleController.exception = "وجود اشکال در باز کردن نمونه شما.";
                }
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

    private void closeSample() {
        try {
            Call<ResponseBody> call = sampleApi.close("Bearer " + sharedPreferences.getString("token", ""), sharedPreferences.getString("sampleId", ""));

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());

                SampleController.exception = "نمونه با موفقیت بسته شد";
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

            HashMap hashMap = new HashMap();
            hashMap.put("items", SampleRepository.remoteData);

            Call<ResponseBody> call = sampleApi.send("Bearer " + sharedPreferences.getString("token", ""), sharedPreferences.getString("sampleId", ""), hashMap);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());

                SampleRepository.remoteData.clear();

                SampleController.exception = "ارسال اطلاعات با موفقیت انجام شد.";
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

            SampleController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            SampleController.workStateAnswer.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            SampleController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            SampleController.workStateAnswer.postValue(0);
        }
    }

    private void sendPrerequisite() {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("prerequisite",SampleRepository.prerequisiteData);

            Call<ResponseBody> call = sampleApi.prerequisite("Bearer " + sharedPreferences.getString("token", ""), sharedPreferences.getString("sampleId", ""), hashMap);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
            SampleRepository.prerequisiteData = new HashMap();
                JSONObject succesBody = new JSONObject(bodyResponse.body().string());

                SampleRepository.remoteData.clear();

                SampleController.exception = "ارسال اطلاعات با موفقیت انجام شد.";
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

            SampleController.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            SampleController.workStateAnswer.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            SampleController.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            SampleController.workStateAnswer.postValue(0);
        }
    }

}