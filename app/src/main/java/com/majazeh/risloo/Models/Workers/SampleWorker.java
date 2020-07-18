package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Controller.AuthController;
import com.majazeh.risloo.Models.Remotes.Apis.SampleApi;
import com.majazeh.risloo.Models.Remotes.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.Models.Repositories.Sample.SampleController;
import com.majazeh.risloo.Models.Repositories.Sample.SampleRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SampleWorker extends Worker {

    private SampleApi sampleApi;
    SharedPreferences sharedpreferences;
    SampleController sampleController;
    Context context;


    public SampleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        sampleApi = RetroGenerator.getRetrofit().create(SampleApi.class);
        sharedpreferences = context.getSharedPreferences("STORE", Context.MODE_PRIVATE);
        sampleController = new SampleController();
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        String work = getInputData().getString("work");

        if (work != null) {
            switch (work) {

                case "getSample":
                    try {
                        getSample();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case "sendAnswers":
                    sendAnswers();
                    break;
            }
        }

        return Result.success();

    }

    private void getSample() throws IOException, JSONException {
        Call<ResponseBody> call = sampleApi.getSample("Bearer " + sharedpreferences.getString("token", ""), getInputData().getString("UniqueId"));
        Response<ResponseBody> bodyResponse = call.execute();
        if (bodyResponse.isSuccessful()) {

            JSONObject jsonObject = new JSONObject(bodyResponse.body().string());
            sampleController.saveJsonToCache(context, jsonObject, getInputData().getString("UniqueId"));
            SampleRepository.workStateSample.postValue(1);
        } else {
            SampleRepository.workStateSample.postValue(0);
            SampleRepository.exception = bodyResponse.message();
        }
    }

    private void sendAnswers() {
        try {
            Call<ResponseBody> call = sampleApi.send("Bearer " + sharedpreferences.getString("token", ""), AuthController.sampleId, SampleRepository.remoteData);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                SampleRepository.remoteData.clear();
                //SampleRepository.workStateAnswer.postValue(1);
            } else {
                for (int i = 0; i < SampleRepository.remoteData.size(); i++) {
                    SampleRepository.localData.getValue().add(SampleRepository.remoteData.get(i));
                    SampleRepository.remoteData.clear();
                    //SampleRepository.workStateAnswer.postValue(-1);
                }
            }
            SampleRepository.inProgress = false;
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}