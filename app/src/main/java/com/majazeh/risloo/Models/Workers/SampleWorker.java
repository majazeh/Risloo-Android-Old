package com.majazeh.risloo.Models.Workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Remotes.Apis.SampleApi;
import com.majazeh.risloo.Models.Remotes.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Repositories.Sample.SampleItems;
import com.majazeh.risloo.Models.Repositories.Sample.SampleRepository;
import com.majazeh.risloo.Models.Repositories.Sample.Samples;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class SampleWorker extends Worker {

    private SampleApi sampleApi;
    private Samples samples;

    public SampleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        sampleApi = RetroGenerator.getRetrofit().create(SampleApi.class);
        samples = new Samples();
    }

    @NonNull
    @Override
    public Result doWork() {
        String work = getInputData().getString("work");

        if (work != null) {
            switch (work) {
                case "send":
                    send();
                    break;
            }
        }

        return Result.success();
    }


    private void send() {
        try {
            Call<ResponseBody> call = sampleApi.send(SampleRepository.remoteData);
            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                SampleRepository.remoteData.clear();
            } else {
                for (int i = 0; i < SampleRepository.remoteData.size(); i++) {
                    SampleRepository.localData.getValue().add(SampleRepository.remoteData.get(i));
                    SampleRepository.remoteData.clear();
                }
            }
            SampleRepository.inProgress = false;

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}