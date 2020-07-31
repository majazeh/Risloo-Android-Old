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

import com.majazeh.risloo.Models.Workers.AuthWorker;

import org.json.JSONException;

public class AuthController {

    // Objects
    private Application application;

    // Vars
    public static MutableLiveData<Integer> workState;
    public static String work = "";
    public static String exception = "";
    public static String theory = "auth";
    public static String preTheory = "";
    public static String key = "";
    public static String authorizedKey = "";
    public static String callback = "";
    public static String token = "";
    public static String name = "";
    public static String mobile = "";
    public static String gender = "";
    public static String password = "";
    public static String code = "";
    public static String sampleId = "";

    public AuthController(Application application) {
        this.application = application;

        workState = new MutableLiveData<>();
        workState.setValue(-1);
    }

    public void workManager(String work) throws JSONException {
        if (isNetworkConnected(application.getApplicationContext())) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(AuthWorker.class)
                    .setConstraints(constraints)
                    .setInputData(data(work))
                    .build();

            WorkManager.getInstance(application).enqueue(workRequest);
        } else {
            exception = "انترنت شما وصل نیست! لطفا متصل شوید.";
            workState.postValue(-2);
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