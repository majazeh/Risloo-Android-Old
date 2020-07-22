package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Controller.SampleController;
import com.majazeh.risloo.Models.Controller.SampleItems;
import com.majazeh.risloo.Models.Remotes.Generators.JSONGenerator;
import com.majazeh.risloo.Models.Workers.SampleWorker;

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

    // Generators
    private JSONGenerator jsonGenerator;

    // Controllers
    private SampleController sampleController;

    // Objects
    private JSONObject sampleJson;
    private SampleItems sampleItems;
    private SharedPreferences sharedPreferences;

    // Vars
    public static String exception = "";
    public static MutableLiveData<Integer> workStateSample = new MediatorLiveData<>();
    public static MutableLiveData<Integer> workStateAnswer = new MediatorLiveData<>();
    public static ArrayList<ArrayList<Integer>> localData = new ArrayList<>();
    public static ArrayList<ArrayList<Integer>> remoteData = new ArrayList<>();
    public static boolean cache = false;

    public SampleRepository(@NonNull Application application, String testUniqueId) throws JSONException {
        super(application);

        jsonGenerator = new JSONGenerator();

        sampleController = new SampleController(application, jsonGenerator, testUniqueId);

        sharedPreferences = application.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        if (isNetworkConnected()) {
            workStateSample = new MutableLiveData<>();
            sampleController.getSampleFromAPI("getSample", sharedPreferences.getString("sampleId", ""));
            workStateSample.observeForever(integer -> {
                if (integer == 1) {
                    try {
                        JSONObject jsonObject = sampleController.readJsonFromCache(application.getApplicationContext(), sharedPreferences.getString("sampleId", ""));
                        JSONObject data = jsonObject.getJSONObject("data");
                        sampleItems = new SampleItems(data.getJSONArray("items"));
                        checkStorage();
                        JSONArray jsonArray = readAnswerFromCache(sharedPreferences.getString("sampleId", ""));
                        for (int i = 0; i < sampleItems.size(); i++) {
                            if (sampleItems.userAnswer(i) != -1) {
                                jsonArray.getJSONObject(i).put("index", i);
                                jsonArray.getJSONObject(i).put("answer", sampleItems.userAnswer(i));
                                writeAnswerToCache(jsonArray, sharedPreferences.getString("sampleId", ""));
                            }
                        }
                        if (readAnswerFromCache(sharedPreferences.getString("sampleId", "")) != null)
                            sampleItems.setCurrentIndex(lastUnAnswer(sharedPreferences.getString("sampleId", "")));
                        workStateSample.removeObserver(integer1 -> {
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (integer == 0) {
                    if (sampleController.getSample() != null) {
                        try {
                            sampleJson = sampleController.getSample();
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
            if (sampleController.getSample() != null) {
                try {
                    sampleJson = sampleController.getSample();
                    JSONObject data = sampleJson.getJSONObject("data");
                    sampleItems = new SampleItems(data.getJSONArray("items"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public SampleRepository(Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

    }

    private Data data(String work, String UniqueId) throws JSONException {
        return new Data.Builder()
                .putString("work", work)
                .putString("UniqueId", UniqueId)
                .build();
    }

    public void insertToLocalData(int item, int answer) {
        ArrayList arrayList = new ArrayList<Integer>();
        arrayList.add(item);
        arrayList.add(answer);
        localData.add(arrayList);
    }

    public void insertRemoteDataToLocalData() {
        for (int i = 0; i < remoteData.size(); i++) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(remoteData.get(i));
            localData.add(arrayList);
        }
        remoteData.clear();
    }

    public void insertLocalDataToRemoteData() {
        for (int i = 0; i < localData.size(); i++) {
            remoteData.add(localData.get(i));
        }
        localData.clear();
    }

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

    public boolean saveToCSV(JSONArray jsonArray, String fileName) {
        try {
            FileOutputStream fos = application.getApplicationContext().openFileOutput(fileName + ".CSV", Context.MODE_PRIVATE);
            for (int i = 0; i < jsonArray.length(); i++) {
                fos.write(jsonArray.getJSONObject(i).getString("index").getBytes("UTF-8"));
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
        return new File(application.getApplicationContext().getCacheDir(), fileName + ".CSV");
    }

    public ArrayList<Model> files() {
        ArrayList<Model> arrayList = new ArrayList<Model>();
        File file = new File(application.getCacheDir(), "Samples");
        File[] list = file.listFiles();
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("serial", list[i].getName());
                    if (answerSize(list[i].getName()) < readAnswerFromCache(list[i].getName()).length()) {
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

    public JSONObject json() {
        return sampleJson;
    }

    public SampleItems items() {
        return sampleItems;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    //Answers
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void sendAnswers(String UniqueId) throws JSONException {
        if (isNetworkConnected()) {
            if (cache == true) {
                localData.clear();
                JSONArray jsonArray = readAnswerFromCache(sharedPreferences.getString("sampleId", ""));
                for (int i = 0; i < readAnswerFromCache(sharedPreferences.getString("sampleId", "")).length(); i++) {
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
                insertLocalDataToRemoteData();

                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SampleWorker.class)
                        .setInputData(data("sendAnswers", UniqueId))
                        .build();

                WorkManager.getInstance(application).enqueue(workRequest);
            }
        } else {
            cache = true;
        }
    }

    public void writeAnswerToCache(JSONArray jsonArray, String fileName) {
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


    public boolean hasStorage(String fileName) {
        return new File(application.getApplicationContext().getCacheDir(), "Answers/" + fileName).exists();
    }

    public void deleteStorage(String fileName) {
        File file = new File(application.getApplicationContext().getCacheDir(), "Answers/" + fileName);
        file.delete();
    }

    public int answerSize(String fileName) {
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

    public int answerPosition(String fileName, int index) {
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

    public int lastUnAnswer(String fileName) {
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

    public void checkStorage() {
        if (!hasStorage(sharedPreferences.getString("sampleId", ""))) {
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
            writeAnswerToCache(jsonArray, sharedPreferences.getString("sampleId", ""));
        }
    }

}