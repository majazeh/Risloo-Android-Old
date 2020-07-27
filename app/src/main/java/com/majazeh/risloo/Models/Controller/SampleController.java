package com.majazeh.risloo.Models.Controller;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Models.Workers.SampleWorker;

import org.json.JSONException;

public class SampleController {

    // Objects
    private Application application;

    // Vars
    public static MutableLiveData<Integer> workStateSample;
    public static MutableLiveData<Integer> workStateAnswer;
    public static String work = "";
    public static String exception = "";
    public static boolean cache = false;

    public SampleController(Application application) {
        this.application = application;

        workStateSample = new MutableLiveData<>();
        workStateAnswer = new MutableLiveData<>();
        workStateSample.setValue(-1);
        workStateAnswer.setValue(-1);
    }

    public void workManager(String work) throws JSONException {
        if (isNetworkConnected(application.getApplicationContext())) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SampleWorker.class)
                    .setConstraints(constraints)
                    .setInputData(data(work))
                    .build();

            WorkManager.getInstance(application).enqueue(workRequest);
        } else {
            exception = "انترنت شما وصل نیست! لطفا متصل شوید.";
            workStateSample.setValue(-2);
            workStateAnswer.setValue(-2);
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

}