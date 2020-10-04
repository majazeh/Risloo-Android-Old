package com.majazeh.risloo.Models.Items;

import android.app.Application;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingItems {

    // Vars
    private ArrayList<Model> items = new ArrayList<>();

    // Objects
    private Application application;

    public SettingItems(Application application) {
        this.application = application;
    }

    public ArrayList<Model> items() throws JSONException {
        items.clear();

        for (int i = 0; i < data().length(); i++) {
            items.add(new Model(data().getJSONObject(i)));
        }

        return items;
    }

    private JSONArray data() throws JSONException {
        JSONArray data = new JSONArray();
        data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.SettingAboutUs)).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_info_light)).put("drawable", application.getApplicationContext().getResources().getDrawable(R.drawable.draw_oval_solid_snow)));
        data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.SettingQuestions)).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_question_light)).put("drawable", application.getApplicationContext().getResources().getDrawable(R.drawable.draw_oval_solid_snow)));
        data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.SettingCallUs)).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_headset_light)).put("drawable", application.getApplicationContext().getResources().getDrawable(R.drawable.draw_oval_solid_snow)));
        data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.SettingTermsConditions)).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_gavel_light)).put("drawable", application.getApplicationContext().getResources().getDrawable(R.drawable.draw_oval_solid_snow)));
        data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.SettingFollow)).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_follow_light)).put("drawable", application.getApplicationContext().getResources().getDrawable(R.drawable.draw_oval_solid_snow)));
        data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.SettingShare)).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_share_light)).put("drawable", application.getApplicationContext().getResources().getDrawable(R.drawable.draw_oval_solid_snow)));
        data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.SettingRate)).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_rate_light)).put("drawable", application.getApplicationContext().getResources().getDrawable(R.drawable.draw_oval_solid_snow)));
        data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.SettingVersion)).put("image", application.getApplicationContext().getResources().getDrawable(R.drawable.ic_mobile_light)).put("drawable", application.getApplicationContext().getResources().getDrawable(R.drawable.draw_oval_solid_snow)));
        return data;
    }

}