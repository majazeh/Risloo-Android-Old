package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Controller.AuthController;
import com.majazeh.risloo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AuthRepository extends MainRepository {

    // Controllers
    private AuthController controller;

    // Objects
    private SharedPreferences sharedPreferences;

    public AuthRepository(Application application) {
        super(application);

        controller = new AuthController(application);

        sharedPreferences = application.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);
    }

    public void auth(String authorizedKey) throws JSONException {
        controller.authorizedKey = authorizedKey;
        controller.work = "auth";
        controller.workState.setValue(-1);
        controller.workManager("auth");
    }

    public void authTheory(String password, String code) throws JSONException {
        controller.password = password;
        controller.code = code;
        controller.work = "authTheory";
        controller.workState.setValue(-1);
        controller.workManager("authTheory");
    }

    public void register(String name, String mobile, String gender, String password) throws JSONException {
        controller.name = name;
        controller.mobile = mobile;
        controller.gender = gender;
        controller.password = password;
        controller.work = "register";
        controller.workState.setValue(-1);
        controller.workManager("register");
    }

    public void verification() throws JSONException {
        controller.work = "verification";
        controller.workState.setValue(-1);
        controller.workManager("verification");
    }

    public void recovery(String mobile) throws JSONException {
        controller.mobile = mobile;
        controller.work = "recovery";
        controller.workState.setValue(-1);
        controller.workManager("recovery");
    }

    public void me() throws JSONException {
        controller.work = "me";
        controller.workState.setValue(-1);
        controller.workManager("me");
    }

    public void logOut() throws JSONException {
        controller.work = "logOut";
        controller.workState.setValue(-1);
        controller.workManager("logOut");
    }

    public ArrayList<Model> getAll() {
        ArrayList<Model> items = new ArrayList<>();
        for (int i = 0; i < accountItems().length(); i++) {
            try {
                items.add(new Model(accountItems().getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return items;
    }

    public String getName() {
        if (!sharedPreferences.getString("name", "").equals("null")) {
            return sharedPreferences.getString("name", "");
        }
        return application.getApplicationContext().getResources().getString(R.string.AccountName);
    }

    private JSONArray accountItems() {
        JSONArray accountItems = new JSONArray();
        try {
            if (!sharedPreferences.getString("name", "").equals("null"))
                accountItems.put(new JSONObject().put("title", "نام").put("subTitle", sharedPreferences.getString("name", "")).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_user_light)));
            if (!sharedPreferences.getString("mobile", "").equals("null"))
                accountItems.put(new JSONObject().put("title", "شماره همراه").put("subTitle", sharedPreferences.getString("mobile", "")).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_mobile)));
            if (!sharedPreferences.getString("email", "").equals("null"))
                accountItems.put(new JSONObject().put("title", "ایمیل").put("subTitle", sharedPreferences.getString("email", "")).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_envelope)));
            if (!sharedPreferences.getString("gender", "").equals("null")) {
                if (sharedPreferences.getString("gender", "").equals("male")) {
                    accountItems.put(new JSONObject().put("title", "جنسیت").put("subTitle", "مرد").put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_male)));
                } else if (sharedPreferences.getString("gender", "").equals("female")) {
                    accountItems.put(new JSONObject().put("title", "جنسیت").put("subTitle", "زن").put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_female)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return accountItems;
    }

}