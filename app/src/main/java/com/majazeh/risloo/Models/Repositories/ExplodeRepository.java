package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Models.Workers.ExplodeWorker;

import org.json.JSONException;

public class ExplodeRepository extends MainRepository {

    // Vars
    public static MutableLiveData<Integer> workState;
    public static String work = "";

    public ExplodeRepository(Application application) {
        super(application);

        workState = new MutableLiveData<>();
        workState.setValue(-1);
    }

    public void workManager(String work) throws JSONException {
        if (isNetworkConnected(application.getApplicationContext())) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(ExplodeWorker.class)
                    .setConstraints(constraints)
                    .setInputData(data(work))
                    .build();

            WorkManager.getInstance(application).enqueue(workRequest);
        } else {
            workState.setValue(-2);
        }
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private Data data(String work) throws JSONException {
        return new Data.Builder()
                .putString("work", work)
                .build();
    }

    public boolean hasUpdate() {
        return false;
    }

    public String getVersion() {
        return "1.1.0";
    }

}