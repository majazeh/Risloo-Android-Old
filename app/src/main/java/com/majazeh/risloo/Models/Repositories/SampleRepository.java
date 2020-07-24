package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Environment;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Controller.SampleController;
import com.majazeh.risloo.Models.Items.SampleItems;

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
import java.util.ArrayList;

public class SampleRepository extends MainRepository {

    // Controllers
    private SampleController controller;

    // Vars
    public static ArrayList<ArrayList<Integer>> localData = new ArrayList<>();
    public static ArrayList<ArrayList<Integer>> remoteData = new ArrayList<>();

    // Objects
    private JSONObject sampleJson;
    private SampleItems sampleItems;
    private SharedPreferences sharedPreferences;

    public SampleRepository(Application application) {
        super(application);

        controller = new SampleController(application);

        sharedPreferences = application.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        try {
            getSample();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public SampleRepository() {

    }

    public JSONObject json() {
        return sampleJson;
    }

    public SampleItems items() {
        return sampleItems;
    }

    private void getSample() throws JSONException {
        if (isNetworkConnected()) {

            controller.work = "getSample";
            controller.workStateSample.setValue(-1);
            controller.workManager("getSample");

            SampleController.workStateSample.observeForever(integer -> {
                if (integer == 1) {
                    try {
                        JSONObject jsonObject = readSampleFromCache(sharedPreferences.getString("sampleId", ""));
                        JSONObject data = jsonObject.getJSONObject("data");
                        sampleItems = new SampleItems(data.getJSONArray("items"));
                        checkStorage(sharedPreferences.getString("sampleId", ""));
                        JSONArray jsonArray = readAnswerFromCache(sharedPreferences.getString("sampleId", ""));
                        for (int i = 0; i < sampleItems.size(); i++) {
                            if (answered(i) != -1) {
                                jsonArray.getJSONObject(i).put("index", i);
                                jsonArray.getJSONObject(i).put("answer", answered(i));
                                saveAnswerToCache( jsonArray, sharedPreferences.getString("sampleId", ""));
                            }
                        }
                        if (readAnswerFromCache(sharedPreferences.getString("sampleId", "")) != null) {
                            sampleItems.setIndex(firstUnanswered(sharedPreferences.getString("sampleId", "")));
                        }
                        SampleController.workStateSample.removeObserver(integer1 -> { });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (integer == 0) {
                    if (readSampleFromCache(sharedPreferences.getString("sampleId", "")) != null) {
                        try {
                            sampleJson = readSampleFromCache(sharedPreferences.getString("sampleId", ""));
                            JSONObject data = sampleJson.getJSONObject("data");
                            sampleItems = new SampleItems(data.getJSONArray("items"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        // you are offline
                    }
                } else {

                }
            });
        } else {
            if (readSampleFromCache(sharedPreferences.getString("sampleId", "")) != null) {
                try {
                    sampleJson = readSampleFromCache(sharedPreferences.getString("sampleId", ""));
                    JSONObject data = sampleJson.getJSONObject("data");
                    sampleItems = new SampleItems(data.getJSONArray("items"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendAnswers(String sampleId) throws JSONException {
        if (isNetworkConnected()) {
            if (SampleController.cache == true) {
                localData.clear();
                JSONArray jsonArray = readAnswerFromCache(sampleId);
                for (int i = 0; i < readAnswerFromCache(sampleId).length(); i++) {
                    if (!jsonArray.getJSONObject(i).getString("index").equals("")) {
                        if (!jsonArray.getJSONObject(i).getString("answer").equals("")) {
                            ArrayList arrayList = new ArrayList<Integer>();
                            arrayList.add(jsonArray.getJSONObject(i).getString("index"));
                            arrayList.add(jsonArray.getJSONObject(i).getString("answer"));
                            localData.add(arrayList);
                        }
                    }
                }
            }
            if (remoteData.size() == 0) {
                insertLocalToRemote();

                controller.work = "sendAnswers";
                controller.workStateAnswer.setValue(-1);
                controller.workManager("sendAnswers");
            }
        } else {
            SampleController.cache = true;
        }
    }

    /*
         ---------- Insert ----------
    */

    public void insertToLocal(int index, int answer) {
        ArrayList arrayList = new ArrayList<Integer>();
        arrayList.add(index);
        arrayList.add(answer);
        localData.add(arrayList);
    }

    public void insertRemoteToLocal() {
        localData.addAll(remoteData);
        remoteData.clear();
    }

    public void insertLocalToRemote() {
        remoteData.addAll(localData);
        localData.clear();
    }

    /*
         ---------- Save ----------
    */

    public void saveToExternal(JSONArray jsonArray, String fileName) {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + ".csv");
            FileOutputStream fos = new FileOutputStream(file);
            for (int i = 0; i < jsonArray.length(); i++) {
                fos.write(jsonArray.getJSONObject(i).getString("index").getBytes("UTF-8"));
                fos.write(",".getBytes("UTF-8"));
                fos.write(jsonArray.getJSONObject(i).getString("answer").getBytes("UTF-8"));
                fos.write("\n".getBytes("UTF-8"));
            }
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveSampleToCache(Context context, JSONObject jsonObject, String fileName) {
        try {
            File file = new File(context.getCacheDir(), "Samples/" + fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
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

    public void saveAnswerToCache(JSONArray jsonArray, String fileName) {
        try {
            File file = new File(application.getApplicationContext().getCacheDir(), "Answers/" + fileName);
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            if (!file.exists()) {
                file.createNewFile();
            }
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

    public void saveToCSV(JSONArray jsonArray, String fileName) {
        try {
            FileOutputStream fos = application.getApplicationContext().openFileOutput(fileName + ".CSV", Context.MODE_PRIVATE);
            for (int i = 0; i < jsonArray.length(); i++) {
                fos.write(jsonArray.getJSONObject(i).getString("index").getBytes("UTF-8"));
                fos.write(",".getBytes("UTF-8"));
                fos.write(jsonArray.getJSONObject(i).getString("answer").getBytes("UTF-8"));
                fos.write("\n".getBytes("UTF-8"));
            }
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
         ---------- Read ----------
    */

    public JSONObject readSampleFromCache(String fileName) {
        JSONObject jsonObject = null;
        try {
            File file = new File(application.getApplicationContext().getCacheDir(), "Samples/" + fileName);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                jsonObject = new JSONObject((String) ois.readObject());
                ois.close();
            }
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

    public JSONArray readAnswerFromCache(String fileName) {
        JSONArray jsonArray = null;
        try {
            File file = new File(application.getApplicationContext().getCacheDir(), "Answers/" + fileName);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                jsonArray = new JSONArray((String) ois.readObject());
                ois.close();
            }
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

    public File readFromCSV(String fileName) {
        return new File(application.getApplicationContext().getCacheDir(), fileName + ".CSV");
    }

    /*
         ---------- Storage ----------
    */

    public boolean hasStorage(String fileName) {
        return new File(application.getApplicationContext().getCacheDir(), "Answers/" + fileName).exists();
    }

    public void checkStorage(String fileName) {
        if (!hasStorage(fileName)) {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < items().size(); i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("index", i);
                    jsonObject.put("answer", "");
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            saveAnswerToCache(jsonArray, fileName);
        }
    }

    public void deleteStorage(String fileName) {
        File file = new File(application.getApplicationContext().getCacheDir(), "Answers/" + fileName);
        file.delete();
    }

    public ArrayList<Model> storageFiles() {
        ArrayList<Model> arrayList = new ArrayList<Model>();
        File file = new File(application.getApplicationContext().getCacheDir(), "Samples");
        File[] list = file.listFiles();
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("serial", list[i].getName());
                    if (answeredSize(list[i].getName()) < readAnswerFromCache(list[i].getName()).length()) {
                        jsonObject.put("status", "ناقص");
                    } else {
                        jsonObject.put("status", "کامل");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    arrayList.add(new Model(jsonObject));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return arrayList;
        } else {
            return null;
        }
    }

    /*
         ---------- Answer ----------
    */

    public int answered(int i) {
        try {
            if (sampleItems.items().get(i).get("user_answered") != null) {
                return Integer.parseInt(String.valueOf(sampleItems.items().get(i).get("user_answered")));
            } else {
                return -1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return  -1;
        }
    }

    public int answeredPosition(String fileName, int index) {
        JSONArray items = readAnswerFromCache(fileName);
        try {
            if (!items.getJSONObject(index).getString("answer").equals("")) {
                return items.getJSONObject(index).getInt("answer");
            } else {
                return -1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int answeredSize(String fileName) {
        JSONArray items = readAnswerFromCache(fileName);
        int size = 0;
        for (int i = 0; i < items.length(); i++) {
            try {
                if (!items.getJSONObject(i).getString("answer").equals("")) {
                    size++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    public int firstUnanswered(String fileName) {
        JSONArray items = readAnswerFromCache(fileName);
        for (int i = 0; i < items.length(); i++) {
            try {
                if (items.getJSONObject(i).getString("answer").equals("")) {
                    return i;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    /*
         ---------- Internet ----------
    */

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

}