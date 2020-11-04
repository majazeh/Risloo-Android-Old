package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Utils.Generators.JSONGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AboutUsRepository extends MainRepository {

    // Objects
    private final JSONObject aboutUsJson;
    private final JSONArray aboutUsItems;

    public AboutUsRepository(Application application) throws JSONException {
        super(application);

        aboutUsJson = new JSONObject(JSONGenerator.getJSON(application.getApplicationContext(), "AboutUS.json"));
        aboutUsItems = aboutUsJson.getJSONArray("items");
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() throws JSONException {
        ArrayList<Model> items = new ArrayList<>();
        for (int i = 0; i < aboutUsItems.length(); i++) {
            items.add(new Model(aboutUsItems.getJSONObject(i)));
        }
        return items;
    }

    public ArrayList<Model> getSubset(int index) throws JSONException {
        JSONArray subsets = aboutUsItems.getJSONObject(index).getJSONArray("items");
        ArrayList<Model> items = new ArrayList<>();
        for (int i = 0; i < subsets.length(); i++) {
            items.add(new Model(subsets.getJSONObject(i)));
        }
        return items;
    }

}