package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import com.majazeh.risloo.Entities.List;
import com.majazeh.risloo.Models.Remotes.Generators.JsonGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AboutUsRepository extends MainRepository {

    private JsonGenerator jsonGenerator;
    private JSONObject aboutUsJson;
    private JSONArray aboutUsItems;

    public AboutUsRepository(Application application) throws JSONException {
        super(application);

        jsonGenerator = new JsonGenerator();
        aboutUsJson = new JSONObject(jsonGenerator.getJson(application.getApplicationContext(), "AboutUS.json"));
        aboutUsItems = aboutUsJson.getJSONArray("items");
    }

    public ArrayList<List> getAll() {
        ArrayList<List> items = new ArrayList<>();
        for (int i = 0; i < aboutUsItems.length(); i++) {
            try {
                items.add(new List(aboutUsItems.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return items;
    }

    public ArrayList<List> getAllSubset(int index) throws JSONException {
        JSONArray allSubset = aboutUsItems.getJSONObject(index).getJSONArray("items");
        ArrayList<List> items = new ArrayList<>();
        for (int i = 0; i < allSubset.length(); i++) {
            try {
                items.add(new List(allSubset.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return items;
    }

}