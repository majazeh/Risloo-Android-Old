package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
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
import com.majazeh.risloo.Utils.Generators.JSONGenerator;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class AuthRepository extends MainRepository {

    // Objects
    private final AuthItems items;
    private final JSONArray publicItems, privateItems;

    // Vars
    public static MutableLiveData<Integer> workState;
    public static String work = "";
    public static String theory = "auth";
    public static String preTheory = "";
    public static String key = "";
    public static String authorizedKey = "";
    public static String callback = "";
    public static String name = "";
    public static String username = "";
    public static String mobile = "";
    public static String email = "";
    public static String birthday = "";
    public static String gender = "";
    public static String status = "";
    public static String type = "";
    public static String password = "";
    public static String code = "";

    public AuthRepository(@NonNull Application application) throws JSONException {
        super(application);

        items = new AuthItems(application);

        workState = new MutableLiveData<>();
        workState.setValue(-1);

        publicItems = new JSONArray(JSONGenerator.getJSON(application.getApplicationContext(), "PublicKey.json"));
        privateItems = new JSONArray(JSONGenerator.getJSON(application.getApplicationContext(), "PrivateKey.json"));
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

    public void logOut() throws JSONException {
        work = "logOut";
        workState.setValue(-1);
        workManager("logOut");
    }

    public void editPersonal(String name, String username, String mobile, String email, String birthday, String gender, String status, String type) throws JSONException {
        AuthRepository.name = name;
        AuthRepository.username = username;
        AuthRepository.mobile = mobile;
        AuthRepository.email = email;
        AuthRepository.birthday = birthday;
        AuthRepository.gender = gender;
        AuthRepository.status = status;
        AuthRepository.type = type;

        work = "personal";
        workState.setValue(-1);
        workManager("personal");
    }

    public void editPassword(String password) throws JSONException {
        AuthRepository.password = password;

        work = "password";
        workState.setValue(-1);
        workManager("password");
    }

    public void editAvatar() throws JSONException {
        work = "avatar";
        workState.setValue(-1);
        workManager("avatar");
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() throws JSONException {
        return items.items();
    }

    public ArrayList<Model> getKeyAsset(String type) throws JSONException {
        ArrayList<Model> items = new ArrayList<>();
        if (type.equals("public")) {
            for (int i = 0; i < this.publicItems.length(); i++) {
                items.add(new Model(this.publicItems.getJSONObject(i)));
            }
        } else {
            for (int i = 0; i < this.privateItems.length(); i++) {
                items.add(new Model(this.privateItems.getJSONObject(i)));
            }
        }
        return items;
    }

    public ArrayList<Model> getKeyAssetSubset(String type, int index) throws JSONException {
        JSONArray subsets;

        if (type.equals("public")) {
            subsets = publicItems.getJSONObject(index).getJSONArray("items");
        } else {
            subsets = privateItems.getJSONObject(index).getJSONArray("items");
        }

        ArrayList<Model> items = new ArrayList<>();
        for (int i = 0; i < subsets.length(); i++) {
            items.add(new Model(subsets.getJSONObject(i)));
        }
        return items;
    }

    /*
         ---------- Policies ----------
    */

    public boolean auth() {
        return items.auth();
    }

    /*
         ---------- Gets ----------
    */

    public boolean getIntro() {
        return items.intro();
    }

    public boolean getCallUs() {
        return items.callUs();
    }

    public String getToken() {
        return items.token();
    }

    public String getId() {
        return items.id();
    }

    public String getName() {
        return items.name();
    }

    public String getUsername() {
        return items.username();
    }

    public String getMobile() {
        return items.mobile();
    }

    public String getEmail() {
        return items.email();
    }

    public String getBirthday() {
        return items.birthday();
    }

    public String getGender() {
        return items.gender();
    }

    public String getStatus() {
        return items.status();
    }

    public String getType() {
        return items.type();
    }

    public String getPassword() {
        return items.password();
    }

    public String getAvatar() {
        return items.avatar();
    }

    public String getPublicKey() {
        return items.publicKey();
    }

    public String getPrivateKey() {
        return items.privateKey();
    }

    /*
         ---------- Sets ----------
    */

    public void setIntro(boolean bool) {
        items.setIntro(bool);
    }

    public void setCallUs(boolean bool) {
        items.setCallUs(bool);
    }

    public void setPublicKey(String key) {
        items.setPublicKey(key);
    }

    public void setPrivateKey(String key) {
        items.setPrivateKey(key);
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