package com.majazeh.risloo.Workers;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Entities.Sample.Sample;
import com.majazeh.risloo.Models.Remotes.Apis.SampleApi;
import com.majazeh.risloo.Models.Remotes.Generators.RetroGenerator;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;

public class SampleWorker extends Worker {
    private SampleApi api;

    public SampleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        api = RetroGenerator.getRetrofit().create(SampleApi.class);
    }

    @NonNull
    @Override
    public Result doWork() {
        String work = getInputData().getString("work");
        if (work != null) {
            switch (work) {
                case "send_answers":
                    //send_answers(sending_data);
                    break;
            }
        }

        return Result.success();
    }

    public boolean send_answers(ArrayList sending_data) {
        try {
            Call<Sample> call = api.insert(sending_data);

            return call.execute().isSuccessful();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
