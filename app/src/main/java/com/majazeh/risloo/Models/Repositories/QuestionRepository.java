package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import androidx.annotation.NonNull;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Generators.JSONGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionRepository extends MainRepository {

    // Objects
    private JSONObject questionJson;
    private JSONArray questionItems;

    public QuestionRepository(@NonNull Application application) throws JSONException {
        super(application);

        questionJson = new JSONObject(JSONGenerator.getJSON(application.getApplicationContext(), "Question.json"));
        questionItems = questionJson.getJSONArray("items");
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() {
        ArrayList<Model> items = new ArrayList<>();
        for (int i = 0; i < questionItems.length(); i++) {
            try {
                items.add(new Model(questionItems.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return items;
    }

}