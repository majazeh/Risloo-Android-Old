package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Models.Workers.ExplodeWorker;

import org.json.JSONException;

public class ExplodeRepository extends MainRepository {

    public ExplodeRepository(Application application) {
        super(application);
    }

    public void workManager(String work) throws JSONException {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(ExplodeWorker.class)
                .setInputData(data(work))
                .build();

        WorkManager.getInstance(application).enqueue(workRequest);
    }

    private Data data(String work) throws JSONException {
        return new Data.Builder()
                .putString("work", work)
                .build();
    }

    public boolean hasUpdate() {
        return false;
    }

    public String version() {
        return "1.1.0";
    }

}