package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;

import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Workers.ExplodeWorker;

import org.json.JSONException;

import java.util.Objects;

public class ExplodeRepository extends MainRepository {

    // Vars
    public static MutableLiveData<Integer> workState;
    public static String work = "";

    public ExplodeRepository(Application application) throws JSONException {
        super(application);

        workState = new MutableLiveData<>();
        workState.setValue(-1);
    }

    /*
         ---------- Voids ----------
    */

    public void explode() throws JSONException {
        work = "explode";
        workState.setValue(-1);
        workManager("explode");
    }

    /*
         ---------- Booleans ----------
    */

    public boolean newContent() {
        return false;
    }

    public boolean hasUpdate() {
        return false;
    }

    public boolean forceUpdate() {
        return false;
    }

    /*
         ---------- Strings ----------
    */

    public String currentVersion() {
        try {
            PackageInfo packageInfo = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } return null;
    }

    public String newVersion() {
        return "1.1.0";
    }

    /*
         ---------- Work ----------
    */

    private void workManager(String work) throws JSONException {
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
            ExceptionManager.getException(0, null, false, "OffLine", "explode");
            workState.postValue(-2);
        }
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null && Objects.requireNonNull(cm.getActiveNetworkInfo()).isConnected();
    }

    private Data data(String work) throws JSONException {
        return new Data.Builder()
                .putString("work", work)
                .build();
    }

}