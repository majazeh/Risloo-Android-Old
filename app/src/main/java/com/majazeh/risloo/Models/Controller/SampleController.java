package com.majazeh.risloo.Models.Controller;

import android.app.Application;

import com.majazeh.risloo.Models.Remotes.Generators.JsonGenerator;

import org.json.JSONException;
import org.json.JSONObject;

public class SampleController {

    Application application;
    JsonGenerator jsonGenerator;
    String testUniqueId;

    public SampleController(Application application, JsonGenerator jsonGenerator, String testUniqueId) {
        this.application = application;
        this.jsonGenerator = jsonGenerator;
        this.testUniqueId = testUniqueId;
    }

    public JSONObject getSample() throws JSONException {
        if (!jsonGenerator.getJson(application, testUniqueId).isEmpty()) {
            return new JSONObject(jsonGenerator.getJson(application, testUniqueId));
        } else if (jsonGenerator.readObjectFromCache(application, testUniqueId).length() != 0) {
            return jsonGenerator.readObjectFromCache(application, testUniqueId);
        } else {
            return getSampleFromAPI(testUniqueId);
        }
    }

    public JSONObject getSampleFromAPI(String UniqueId) {
        JSONObject jsonObject = new JSONObject();
        return jsonObject;
    }

}