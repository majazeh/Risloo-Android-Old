package com.majazeh.risloo.Models.Controller;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Models.Workers.AuthWorker;

import org.json.JSONException;

public class AuthController {

    private Application application;
    public static MutableLiveData<Integer> workState;

    public static String callback = "";
    public static String key = "";
    public static String theory = "";
    public static String preTheory = "";
    public static String authorizedKey = "";
    public static String code = "";
    public static String token = "";
    public static String exception = "";
    public static String work = "";
    public static String name = "";
    public static String mobile = "";
    public static String gender = "";
    public static String password = "";

    public AuthController(Application application) {
        this.application = application;

        workState = new MutableLiveData<>();
        workState.setValue(-1);
    }

    public void workManager(String work) throws JSONException {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(AuthWorker.class)
                .setInputData(data(work))
                .build();

        WorkManager.getInstance(application).enqueue(workRequest);
    }

    private Data data(String work) throws JSONException {
        return new Data.Builder()
                .putString("work", work)
                .build();
    }

    public LiveData<Integer> workState() {
        return workState;
    }

}