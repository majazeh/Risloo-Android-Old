package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import androidx.annotation.NonNull;

import com.majazeh.risloo.Entities.Question;
import com.majazeh.risloo.Models.Remotes.Generators.JsonGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionRepository extends MainRepository {

    private JsonGenerator jsonGenerator;
    private JSONObject questionJson;
    private JSONArray questionItems;

    public QuestionRepository(@NonNull Application application) throws JSONException {
        super(application);

        jsonGenerator = new JsonGenerator();
        questionJson = new JSONObject(jsonGenerator.getJson(application.getApplicationContext(), "Question.json"));
        questionItems = questionJson.getJSONArray("items");
    }

    public ArrayList<Question> getAll() {
        ArrayList<Question> items = new ArrayList<>();
        for (int i = 0; i < questionItems.length(); i++) {
            try {
                items.add(new Question(questionItems.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } return items;
    }

}