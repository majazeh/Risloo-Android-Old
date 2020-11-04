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

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Items.AuthItems;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Models.Workers.AuthWorker;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class AuthRepository extends MainRepository {

    // Objects
    private AuthItems authItems;

    // Vars
    public static MutableLiveData<Integer> workState;
    public static String work = "";
    public static String theory = "auth";
    public static String preTheory = "";
    public static String key = "";
    public static String authorizedKey = "";
    public static String callback = "";
    public static String name = "";
    public static String mobile = "";
    public static String gender = "";
    public static String birthday = "";
    public static String password = "";
    public static String code = "";
    public static String fileTitle = "";
    public static String fileDescription = "";
    public static String filePath = "";

    public AuthRepository(Application application) throws JSONException {
        super(application);

        authItems = new AuthItems(application);

        workState = new MutableLiveData<>();
        workState.setValue(-1);
    }

    /*
         ---------- Voids ----------
    */

    public void auth(String authorizedKey) throws JSONException {
        AuthRepository.authorizedKey = authorizedKey;

        work = "auth";
        workState.setValue(-1);
        workManager("auth");
    }

    public void authTheory(String password, String code) throws JSONException {
        AuthRepository.password = password;
        AuthRepository.code = code;

        work = "authTheory";
        workState.setValue(-1);
        workManager("authTheory");
    }

    public void register(String name, String mobile, String gender, String password) throws JSONException {
        AuthRepository.name = name;
        AuthRepository.mobile = mobile;
        AuthRepository.gender = gender;
        AuthRepository.password = password;

        work = "register";
        workState.setValue(-1);
        workManager("register");
    }

    public void verification() throws JSONException {
        work = "verification";
        workState.setValue(-1);
        workManager("verification");
    }

    public void recovery(String mobile) throws JSONException {
        AuthRepository.mobile = mobile;

        work = "recovery";
        workState.setValue(-1);
        workManager("recovery");
    }

    public void me() throws JSONException {
        work = "me";
        workState.setValue(-1);
        workManager("me");
    }

    public void edit(String name, String gender, String birthday) throws JSONException {
        AuthRepository.name = name;
        AuthRepository.gender = gender;
        AuthRepository.birthday = birthday;

        work = "edit";
        workState.setValue(-1);
        workManager("edit");
    }

    public void avatar() throws JSONException {
        work = "avatar";
        workState.setValue(-1);
        workManager("avatar");
    }

    public void logOut() throws JSONException {
        work = "logOut";
        workState.setValue(-1);
        workManager("logOut");
    }

    public void sendDoc(String title, String description, String filePath) throws JSONException {
        AuthRepository.fileTitle = title;
        AuthRepository.fileDescription = description;
        AuthRepository.filePath = filePath;

        work = "sendDoc";
        workState.setValue(-1);
        workManager("sendDoc");
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() throws JSONException {
        return authItems.items();
    }

    /*
         ---------- Booleans ----------
    */

    public boolean hasAccess() {
        return authItems.hasAccess();
    }

    /*
         ---------- Strings ----------
    */

    public String getToken() {
        return authItems.token();
    }

    public String getUserId() {
        return authItems.userId();
    }

    public String getName() {
        return authItems.name();
    }

    public String getType() {
        return authItems.type();
    }

    public String getMobile() {
        return authItems.mobile();
    }

    public String getEmail() {
        return authItems.email();
    }

    public String getGender() {
        return authItems.gender();
    }

    public String getBirthday() {
        return authItems.birthday();
    }

    public String getAvatar() {
        return authItems.avatar();
    }

    /*
         ---------- Work ----------
    */

    private void workManager(String work) throws JSONException {
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
            ExceptionGenerator.getException(false, 0, null, "OffLineException");
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