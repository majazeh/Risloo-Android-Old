package com.majazeh.risloo.Models.Repositories.Sample;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.majazeh.risloo.Models.Remotes.Generators.JsonGenerator;
import com.majazeh.risloo.Models.Repositories.MainRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
        try {
            File file = new File(application.getApplicationContext().getCacheDir(), fileName);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(jsonArray.toString());
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JSONArray readFromCache(String fileName) {
        JSONArray jsonArray;
        try {
            File file = new File(application.getApplicationContext().getCacheDir(), fileName);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            jsonArray = new JSONArray((String) ois.readObject());
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

    public boolean saveToCSV(JSONArray jsonArray, String fileName) {
        try {
            FileOutputStream fos = application.getApplicationContext().openFileOutput(fileName, Context.MODE_PRIVATE);
            for (int i = 0; i < jsonArray.length(); i++) {
                fos.write(String.valueOf(i).getBytes("UTF-8"));
                fos.write(",".getBytes("UTF-8"));
                fos.write(jsonArray.getJSONObject(i).getString("answer").getBytes("UTF-8"));
                fos.write("\n".getBytes("UTF-8"));
            }
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public File loadFromCSV(String fileName) {
        return new File(application.getApplicationContext().getCacheDir(), fileName);
    }

    public JSONObject json() {
        return sampleJson;
    }

    public SampleItems items() {
        return sampleItems;
    }

}