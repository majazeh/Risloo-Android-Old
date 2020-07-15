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
        ArrayList<AboutUs> aboutUs = new ArrayList<>();
        for (int i = 0; i < aboutUsItems.length(); i++) {
            try {
                aboutUs.add(new AboutUs(aboutUsItems.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return aboutUs;
    }

    public ArrayList<String> getFacility() {
        ArrayList<String> facilities = new ArrayList<>();
        for (int i = 0; i < facilityItems.length(); i++) {
            try {
                facilities.add(facilityItems.getJSONObject(i).getString("facility"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return facilities;
    }

}