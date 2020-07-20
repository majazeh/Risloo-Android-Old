package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Remotes.Generators.JSONGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TermConditionRepository extends MainRepository {

    // Generators
    private JSONGenerator jsonGenerator;

    // Objects
    private JSONObject termConditionJson;
    private JSONArray termConditionItems;

    public TermConditionRepository(Application application) throws JSONException {
        super(application);

        jsonGenerator = new JSONGenerator();

        termConditionJson = new JSONObject(jsonGenerator.getJSON(application.getApplicationContext(), "TermCondition.json"));
        termConditionItems = termConditionJson.getJSONArray("items");
    }

    public ArrayList<Model> getAll() {
        ArrayList<Model> items = new ArrayList<>();
        for (int i = 0; i < termConditionItems.length(); i++) {
            try {
                items.add(new Model(termConditionItems.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return items;
    }

    public ArrayList<Model> getAllSubset(int index) throws JSONException {
        JSONArray subsets = termConditionItems.getJSONObject(index).getJSONArray("items");
        ArrayList<Model> items = new ArrayList<>();
        for (int i = 0; i < subsets.length(); i++) {
            try {
                items.add(new Model(subsets.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return items;
    }

}