package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Utils.Generators.JSONGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TermConditionRepository extends MainRepository {

    // Objects
    private JSONObject termConditionJson;
    private JSONArray termConditionItems;

    public TermConditionRepository(Application application) throws JSONException {
        super(application);

        termConditionJson = new JSONObject(JSONGenerator.getJSON(application.getApplicationContext(), "TermCondition.json"));
        termConditionItems = termConditionJson.getJSONArray("items");
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() throws JSONException {
        ArrayList<Model> items = new ArrayList<>();
        for (int i = 0; i < termConditionItems.length(); i++) {
            items.add(new Model(termConditionItems.getJSONObject(i)));
        }
        return items;
    }

    public ArrayList<Model> getSubset(int index) throws JSONException {
        JSONArray subsets = termConditionItems.getJSONObject(index).getJSONArray("items");
        ArrayList<Model> items = new ArrayList<>();
        for (int i = 0; i < subsets.length(); i++) {
            items.add(new Model(subsets.getJSONObject(i)));
        }
        return items;
    }

}