package com.majazeh.risloo.Models.Workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Remotes.Apis.ExplodeApi;
import com.majazeh.risloo.Models.Remotes.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Repositories.ExplodeRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ExplodeWorker extends Worker {

    // Apis
    private ExplodeApi explodeApi;

    public ExplodeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        explodeApi = RetroGenerator.getRetrofit().create(ExplodeApi.class);
    }

    @NonNull
    @Override
    public Result doWork() {
        String work = getInputData().getString("work");

        if (work != null) {
            switch (work) {
                case "explode":
                    explode();
                    break;
            }
        }

        return Result.success();
    }

    private void explode() {
        try {
            Call<ResponseBody> call = explodeApi.explode();

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {

                ExplodeRepository.workState.postValue(1);

                JSONObject jsonObject = new JSONObject(bodyResponse.body().string());
                JSONObject android = jsonObject.getJSONObject("android");

                if (Integer.parseInt(android.getString("current")) < Integer.parseInt(android.getString("force"))) {
                    // TODO: Force Update
                } else {
                    // TODO: Normal Update
                }

            } else {
                ExplodeRepository.workState.postValue(0);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

}