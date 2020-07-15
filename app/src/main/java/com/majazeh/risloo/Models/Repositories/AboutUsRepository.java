package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import com.majazeh.risloo.Entities.AboutUs;
import com.majazeh.risloo.Models.Remotes.Generators.JsonGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AboutUsRepository extends MainRepository {

    private JsonGenerator jsonGenerator;
    private JSONObject aboutUsJson;
    private JSONArray aboutUsItems, facilityItems;

    public AboutUsRepository(Application application) throws JSONException {
        super(application);

        jsonGenerator = new JsonGenerator();
        aboutUsJson = new JSONObject(jsonGenerator.getJson(application.getApplicationContext(), "AboutUS.json"));
        aboutUsItems = aboutUsJson.getJSONArray("items");
        facilityItems = aboutUsItems.getJSONObject(2).getJSONArray("facilities");
    }

    public ArrayList<AboutUs> getAll() {
        ArrayList<AboutUs> items = new ArrayList<>();
        for (int i = 0; i < aboutUsItems.length(); i++) {
            try {
                items.add(new AboutUs(aboutUsItems.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return items;
    }

    public ArrayList<String> getFacilities() {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < facilityItems.length(); i++) {
            try {
                items.add(facilityItems.getJSONObject(i).getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return items;
    }

}