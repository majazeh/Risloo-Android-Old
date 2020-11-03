package com.majazeh.risloo.Models.Items;

import android.app.Application;

import androidx.core.content.res.ResourcesCompat;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SettingItems {

    // Vars
    private final ArrayList<Model> items = new ArrayList<>();

    // Objects
    private final Application application;

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
        data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.SettingAboutUs)).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_info_light, null)).put("drawable", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.draw_oval_solid_snow, null)));
        data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.SettingQuestions)).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_question_light, null)).put("drawable", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.draw_oval_solid_snow, null)));
        data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.SettingCallUs)).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_headset_light, null)).put("drawable", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.draw_oval_solid_snow, null)));
        data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.SettingTermsConditions)).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_gavel_light, null)).put("drawable", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.draw_oval_solid_snow, null)));
        data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.SettingFollow)).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_follow_light, null)).put("drawable", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.draw_oval_solid_snow, null)));
        data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.SettingShare)).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_share_light, null)).put("drawable", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.draw_oval_solid_snow, null)));
        data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.SettingRate)).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_rate_light, null)).put("drawable", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.draw_oval_solid_snow, null)));
        data.put(new JSONObject().put("title", application.getApplicationContext().getResources().getString(R.string.SettingVersion)).put("image", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.ic_mobile_light, null)).put("drawable", ResourcesCompat.getDrawable(application.getApplicationContext().getResources(), R.drawable.draw_oval_solid_snow, null)));
        return data;
    }

}