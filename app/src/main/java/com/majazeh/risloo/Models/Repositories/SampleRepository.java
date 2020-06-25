package com.majazeh.risloo.Models.Repositories;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;

import com.majazeh.risloo.Models.Controller.SampleController;
import com.majazeh.risloo.Models.Remotes.Generators.JsonGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SampleRepository extends MainRepository {
    public JsonGenerator jsonGenerator;
    private JSONObject json;
    private SampleItems items;
    SampleController sampleController;

    @SuppressLint("NewApi")
    public SampleRepository(@NonNull Application application, String testUniqueId) throws JSONException {
        super(application);
        jsonGenerator = new JsonGenerator();
        sampleController = new SampleController(application,jsonGenerator,testUniqueId);
        json = sampleController.getSample();
        items = new SampleItems(json.getJSONArray("items"));
    }

    public String getTitle() throws JSONException {
        return json.getString("title");
    }

    public int getVersion() throws JSONException {
        return json.getInt("version");

    }

    public String getEdition() throws JSONException {
        return json.getString("edition");

    }

    public String getEdition_version() throws JSONException {
        return json.getString("edition_version");

    }

    public String getFiller() throws JSONException {
        return json.getString("filler");

    }

    public String getDescription() throws JSONException {

        return json.getString("description");
    }

    public SampleItems items() {
        return items;
    }

    public void writeAnswerToFile(JSONArray jsonArray, String file_name) {
        jsonGenerator.saveJsonAnswerToCache(application.getApplicationContext(), jsonArray, file_name);
    }

    public JSONArray readAnswerFromFile(String file_name) {
        return jsonGenerator.readJsonAnswerFromCache(application.getApplicationContext(), file_name);
    }

    public boolean saveToCSV(JSONArray jsonArray, String file_name) {
        return jsonGenerator.saveToCSV(application.getApplicationContext(), jsonArray, file_name);
    }

}

