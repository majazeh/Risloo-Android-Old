package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import androidx.annotation.NonNull;

import com.majazeh.risloo.Models.Items.SampleItems;
import com.majazeh.risloo.Models.Controller.SampleController;
import com.majazeh.risloo.Models.Remotes.Generators.JsonGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class SampleRepository extends MainRepository {

    private JsonGenerator jsonGenerator;
    private JSONObject sampleJson;
    private SampleItems sampleItems;
    private SampleController sampleController;

    public SampleRepository(@NonNull Application application, String testUniqueId) throws JSONException {
        super(application);

        jsonGenerator = new JsonGenerator();
        sampleController = new SampleController(application, jsonGenerator, testUniqueId);
        sampleJson = sampleController.getSample();
        sampleItems = new SampleItems(sampleJson.getJSONArray("items"));
    }

    public void insertToLocalData(int item, int answer) {
        sampleItems.insertToLocalData(item, answer);
    }

    public void insertToRemoteData(int item, int answer) {
        sampleItems.insertToRemoteData(item, answer);
    }

    public void insertRemoteDataToLocalData() {
        sampleItems.insertRemoteDataToLocalData();
    }

    public void writeToCache(JSONArray jsonArray, String fileName) {
        jsonGenerator.writeArrayToCache(application.getApplicationContext(), jsonArray, fileName);
    }

    public JSONArray readFromCache(String fileName) {
        return jsonGenerator.readArrayFromCache(application.getApplicationContext(), fileName);
    }

    public boolean saveToCSV(JSONArray jsonArray, String fileName) {
        return jsonGenerator.saveToCSV(application.getApplicationContext(), jsonArray, fileName);
    }

    public File loadFromCSV(String fileName) {
        return jsonGenerator.loadFromCSV(application.getApplicationContext(), fileName);
    }

    public JSONObject json() {
        return sampleJson;
    }

    public SampleItems items() {
        return sampleItems;
    }

}