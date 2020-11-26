package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import androidx.annotation.NonNull;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Utils.Generators.JSONGenerator;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class FAQuestionRepository extends MainRepository {

    // Objects
    private final JSONArray items;

    public FAQuestionRepository(@NonNull Application application) throws JSONException {
        super(application);

        items = new JSONArray(JSONGenerator.getJSON(application.getApplicationContext(), "FAQuestions.json"));
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() throws JSONException {
        ArrayList<Model> items = new ArrayList<>();
        for (int i = 0; i < this.items.length(); i++) {
            items.add(new Model(this.items.getJSONObject(i)));
        }
        return items;
    }

}