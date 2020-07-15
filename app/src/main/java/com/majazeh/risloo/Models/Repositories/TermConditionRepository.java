package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import com.majazeh.risloo.Entities.TermCondition;
import com.majazeh.risloo.Models.Remotes.Generators.JsonGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TermConditionRepository extends MainRepository {

    private JsonGenerator jsonGenerator;
    private JSONObject termConditionJson;
    private JSONArray termConditionItems, accountItems, termItems;

    public TermConditionRepository(Application application) throws JSONException {
        super(application);

        jsonGenerator = new JsonGenerator();
        termConditionJson = new JSONObject(jsonGenerator.getJson(application.getApplicationContext(), "TermCondition.json"));
        termConditionItems = termConditionJson.getJSONArray("items");
        accountItems = termConditionItems.getJSONObject(2).getJSONArray("accounts");
        termItems = termConditionItems.getJSONObject(3).getJSONArray("terms");
    }

    public ArrayList<TermCondition> getAll() {
        ArrayList<TermCondition> items = new ArrayList<>();
        for (int i = 0; i < termConditionItems.length(); i++) {
            try {
                items.add(new TermCondition(termConditionItems.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return items;
    }

    public ArrayList<String> getAccounts() {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < accountItems.length(); i++) {
            try {
                items.add(accountItems.getJSONObject(i).getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return items;
    }

    public ArrayList<String> getTerms() {
        ArrayList<String> items = new ArrayList<>();
        for (int i = 0; i < termItems.length(); i++) {
            try {
                items.add(termItems.getJSONObject(i).getString("title"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return items;
    }

}