package com.majazeh.risloo.Models.Workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Remotes.Apis.SampleApi;
import com.majazeh.risloo.Models.Remotes.Generators.RetroGenerator;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class SampleWorker extends Worker {

    private SampleApi sampleApi;

    public SampleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        sampleApi = RetroGenerator.getRetrofit().create(SampleApi.class);
    }

    @NonNull
    @Override
    public Result doWork() {
        String work = getInputData().getString("work");

        if (work != null) {
            switch (work) {
                case "send":
                    send(data());
                    break;
            }
        }

        return Result.success();
    }

    private ArrayList data() {
        return new ArrayList();
    }

    private boolean send(ArrayList data) {
        try {
            Call<ResponseBody> call = sampleApi.send(data);

            return call.execute().isSuccessful();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}