package com.majazeh.risloo.Models.Items;

import android.app.Application;
import android.content.SharedPreferences;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AuthItems {

    // Vars
    private ArrayList<Model> items = new ArrayList<>();

    // Objects
    private Application application;
    private SharedPreferences sharedPreferences;

    public AuthItems(Application application, SharedPreferences sharedPreferences) throws JSONException {
        this.application = application;
        this.sharedPreferences = sharedPreferences;

        for (int i = 0; i < data().length(); i++) {
            items.add(new Model(data().getJSONObject(i)));
        }
    }

    public ArrayList<Model> items(){
        return items;
    }

    public String name() {
        if (!sharedPreferences.getString("name", "").equals("null")) {
            return sharedPreferences.getString("name", "");
        }
        return application.getApplicationContext().getResources().getString(R.string.AccountName);
    }

    public String type() {
        if (!sharedPreferences.getString("type", "").equals("null")) {
            return sharedPreferences.getString("type", "");
        }
        return application.getApplicationContext().getResources().getString(R.string.AccountType);
    }

    private JSONArray data() throws JSONException {
        JSONArray data = new JSONArray();
        if (!sharedPreferences.getString("name", "").equals("null")) {
            data.put(new JSONObject().put("title", "نام").put("subTitle", sharedPreferences.getString("name", "")).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_user)));
        }
        if (!sharedPreferences.getString("type", "").equals("null")) {
            if (sharedPreferences.getString("type", "").equals("psychologist")) {
                data.put(new JSONObject().put("title", "نوع حساب").put("subTitle", "مشاور").put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_stethoscope)));
            } else if (sharedPreferences.getString("type", "").equals("counseling_center")) {
                data.put(new JSONObject().put("title", "نوع حساب").put("subTitle", "مرکز درمانی").put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_hospital)));
            } else if (sharedPreferences.getString("type", "").equals("operator")) {
                data.put(new JSONObject().put("title", "نوع حساب").put("subTitle", "اپراتور").put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_headset)));
            } else {
                data.put(new JSONObject().put("title", "نوع حساب").put("subTitle", "مراجع").put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_pills)));
            }
        }
        if (!sharedPreferences.getString("mobile", "").equals("null")) {
            data.put(new JSONObject().put("title", "شماره همراه").put("subTitle", sharedPreferences.getString("mobile", "")).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_mobile)));
        }
        if (!sharedPreferences.getString("email", "").equals("null")) {
            data.put(new JSONObject().put("title", "ایمیل").put("subTitle", sharedPreferences.getString("email", "")).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_message)));
        }
        if (!sharedPreferences.getString("gender", "").equals("null")) {
            if (sharedPreferences.getString("gender", "").equals("male")) {
                data.put(new JSONObject().put("title", "جنسیت").put("subTitle", "مرد").put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_male)));
            } else if (sharedPreferences.getString("gender", "").equals("female")) {
                data.put(new JSONObject().put("title", "جنسیت").put("subTitle", "زن").put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_female)));
            }
        }
        return data;
    }

}