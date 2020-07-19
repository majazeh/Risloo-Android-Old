package com.majazeh.risloo.Models.Repositories.Sample;

import android.app.Application;
import android.content.Context;

import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Models.Remotes.Generators.JsonGenerator;
import com.majazeh.risloo.Models.Workers.SampleWorker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SampleController {

    Application application;
    JsonGenerator jsonGenerator;
    String testUniqueId;

    public SampleController(Application application, JsonGenerator jsonGenerator, String testUniqueId) {
        this.application = application;
        this.jsonGenerator = jsonGenerator;
        this.testUniqueId = testUniqueId;
    }

    public SampleController() {
    }

    public JSONObject getSample() throws JSONException {
        if (readJsonFromCache(application, testUniqueId) != null) {
            return readJsonFromCache(application, testUniqueId);
        }
//        else if (jsonGenerator.getJson(application, testUniqueId) != null) {
//            return new JSONObject(jsonGenerator.getJson(application, testUniqueId));
//        }
        return null;
    }


    public void getSampleFromAPI(String work, String UniqueId) throws JSONException {
        SampleRepository.workStateSample.postValue(-1);
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SampleWorker.class)
                .setInputData(data(work, UniqueId))
                .build();

        WorkManager.getInstance(application).enqueue(workRequest);
    }

    public void saveJsonToCache(Context context, JSONObject jsonObject, String fileName) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(context.getCacheDir(), "") + File.separator + fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(jsonObject.toString());
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject readJsonFromCache(Context context, String answer_file) {
        JSONObject jsonArray;
        try {
            FileInputStream fis = new FileInputStream(new File(context.getCacheDir() + File.separator + answer_file));
            ObjectInputStream ois = new ObjectInputStream(fis);
            jsonArray = new JSONObject((String) ois.readObject());
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
        return jsonArray;
    }

    private Data data(String work, String UniqueId) throws JSONException {
        return new Data.Builder()
                .putString("work", work)
                .putString("UniqueId", UniqueId)
                .build();
    }

}