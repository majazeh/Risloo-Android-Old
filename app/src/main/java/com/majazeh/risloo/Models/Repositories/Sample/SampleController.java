package com.majazeh.risloo.Models.Repositories.Sample;

import android.app.Application;
import android.content.Context;

import com.majazeh.risloo.Models.Remotes.Generators.JsonGenerator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

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
        } else if (readObjectFromCache(application, testUniqueId).length() != 0) {
            return readObjectFromCache(application, testUniqueId);
        } else {
            return getSampleFromAPI(testUniqueId);
        }
    }

    public JSONObject getSampleFromAPI(String UniqueId) {
        JSONObject jsonObject = new JSONObject();
        return jsonObject;
    }

    public JSONObject readObjectFromCache(Context context, String fileName) {
        JSONObject jsonObject;
        try {
            File file = new File(context.getCacheDir(), fileName);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            jsonObject = new JSONObject((String) ois.readObject());
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }

}