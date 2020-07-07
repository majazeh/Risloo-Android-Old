package com.majazeh.risloo.Models.Repositories.Authentication;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Models.Workers.AuthWorker;

import org.json.JSONException;

public class AuthController {
    public static String callback = "";
    public static String key = "";
    public static String theory = "auth";
    public static String authorized_key = "";
    public static String password = "";
    public static String code = "";
    public static String token = "";
    public static String exception = "";
    public static String work = "";
    public static String name = "";
    public static String gender = "";
    public static String mobile = "";
    public static MutableLiveData<Integer> workState;
    private Application application;

    public AuthController(Application application) {
        this.application = application;
        workState = new MutableLiveData<>();
        workState.setValue(-1);
    }

    public AuthController() {

    }

    public void auth() throws JSONException {

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(AuthWorker.class)
                .setInputData(data("auth"))
                .build();
        WorkManager.getInstance(application).enqueue(workRequest);


    }

    public void auth_theory() throws JSONException {


        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(AuthWorker.class)
                .setInputData(data("authTheory"))
                .build();
        WorkManager.getInstance(application).enqueue(workRequest);

    }

    public void signIn() throws JSONException {

        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(AuthWorker.class)
                .setInputData(data("signIn"))
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
