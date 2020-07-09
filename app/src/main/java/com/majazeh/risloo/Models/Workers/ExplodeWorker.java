package com.majazeh.risloo.Models.Workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Remotes.Apis.SampleApi;
import com.majazeh.risloo.Models.Remotes.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Repositories.Sample.SampleRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ExplodeWorker extends Worker {
    private SampleApi sampleApi;

    public ExplodeWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        sampleApi = RetroGenerator.getRetrofit().create(SampleApi.class);

    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Call<ResponseBody> call = sampleApi.explode();
            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject object = new JSONObject(bodyResponse.body().string());
                JSONObject android = object.getJSONObject("android");
                if (Integer.parseInt(android.getString("current")) < Integer.parseInt(android.getString("force"))) {
                    // TODO: force update
                } else {
                    // TODO: not force update
                }
            } else {

            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();

        }
        return Result.success();
    }
}
