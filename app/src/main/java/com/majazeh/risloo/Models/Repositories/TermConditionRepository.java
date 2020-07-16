package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import com.majazeh.risloo.Entities.List;
import com.majazeh.risloo.Models.Remotes.Generators.JsonGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TermConditionRepository extends MainRepository {

    private JsonGenerator jsonGenerator;
    private JSONObject termConditionJson;
    private JSONArray termConditionItems;

    public TermConditionRepository(Application application) throws JSONException {
        super(application);

        jsonGenerator = new JsonGenerator();
        termConditionJson = new JSONObject(jsonGenerator.getJson(application.getApplicationContext(), "TermCondition.json"));
        termConditionItems = termConditionJson.getJSONArray("items");
    }

    public ArrayList<List> getAll() {
        ArrayList<List> items = new ArrayList<>();
        for (int i = 0; i < termConditionItems.length(); i++) {
            try {
                items.add(new List(termConditionItems.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return items;
    }

    public ArrayList<List> getAllSubset(int index) throws JSONException {
        JSONArray teallSubsetms = termConditionItems.getJSONObject(index).getJSONArray("items");
        ArrayList<List> items = new ArrayList<>();
        for (int i = 0; i < teallSubsetms.length(); i++) {
            try {
                items.add(new List(teallSubsetms.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return items;
    }

}