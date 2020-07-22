package com.majazeh.risloo.Models.Controller;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Models.Remotes.Generators.JSONGenerator;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
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

    private Application application;
    JSONGenerator jsonGenerator;
    String testUniqueId;

    public SampleController(Application application, JSONGenerator jsonGenerator, String testUniqueId) {
        this.application = application;

        this.jsonGenerator = jsonGenerator;
        this.testUniqueId = testUniqueId;
    }

    public SampleController() {

    }

    public JSONObject getSample() {
        if (readJsonFromCache(application, testUniqueId) != null) {
            return readJsonFromCache(application, testUniqueId);
        }
        return null;
    }

    public void getSampleFromAPI(String work, String UniqueId) throws JSONException {


        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SampleWorker.class)
                .setInputData(data(work, UniqueId))
                .build();

        WorkManager.getInstance(application).enqueue(workRequest);
    }

    public void saveJsonToCache(Context context, JSONObject jsonObject, String fileName) {
        try {
            File file = new File(context.getCacheDir(), "Samples/" + fileName);
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            if (!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(jsonObject.toString());
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONObject readJsonFromCache(Context context, String fileName) {
        JSONObject jsonArray;
        try {
            FileInputStream fis = new FileInputStream(new File(context.getCacheDir(), "Samples/" + fileName));
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