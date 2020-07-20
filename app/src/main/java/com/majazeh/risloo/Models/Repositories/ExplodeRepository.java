package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Models.Workers.AuthWorker;
import com.majazeh.risloo.Models.Workers.ExplodeWorker;

import org.json.JSONException;

public class ExplodeRepository extends MainRepository {
    public static MutableLiveData<Integer> workState = new MutableLiveData<>();

    public ExplodeRepository(Application application) {
        super(application);
    }

    public void explode(){
        workState.setValue(-1);
        try {
            workManager("explode");
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
}
