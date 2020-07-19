package com.majazeh.risloo.Models.Repositories.Sample;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Models.Controller.AuthController;
import com.majazeh.risloo.Models.Remotes.Generators.JsonGenerator;
import com.majazeh.risloo.Models.Repositories.MainRepository;
import com.majazeh.risloo.Models.Workers.AuthWorker;
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

    private JsonGenerator jsonGenerator;
    private JSONObject sampleJson;
    private SampleItems sampleItems;
    private SampleController sampleController;
    public static boolean inProgress = false;
    public static String exception = "";
    public static MutableLiveData<Integer> workStateSample = new MediatorLiveData<>();
    public static MutableLiveData<Integer> workStateAnswer = new MediatorLiveData<>();
    public static ArrayList<ArrayList<Integer>> localData = new ArrayList<>();
    public static ArrayList<ArrayList<Integer>> remoteData = new ArrayList<>();
    public static boolean cache = false;
    private SharedPreferences sharedPreferences;

    public SampleRepository(@NonNull Application application, String testUniqueId) throws JSONException {
        super(application);

        sharedPreferences = application.getSharedPreferences("STORE", Context.MODE_PRIVATE);


        jsonGenerator = new JsonGenerator();
        sampleController = new SampleController(application, jsonGenerator, testUniqueId);

        if (isNetworkConnected()) {
            sampleController.getSampleFromAPI("getSample", AuthController.sampleId);

            workStateSample.observeForever(integer -> {

                if (integer == 1) {
                    try {
                        JSONObject jsonObject = sampleController.readJsonFromCache(application.getApplicationContext(), sharedPreferences.getString("sampleId", ""));
                        JSONObject data = jsonObject.getJSONObject("data");
                        sampleItems = new SampleItems(data.getJSONArray("items"));
                        workStateSample.removeObserver(integer1 -> {
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (integer == 0) {
                    try {
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
                    } catch (JSONException e) {
                        e.printStackTrace();
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


    public void insertToLocalData(int item, int answer) {
        ArrayList arrayList = new ArrayList<Integer>();
        arrayList.add(item);
        arrayList.add(answer);
        localData.add(arrayList);
    }

//    public void insertToRemoteData(int item, int answer) {
//        ArrayList arrayList = new ArrayList();
//        arrayList.add(item);
//        arrayList.add(answer);
//
//        remoteData.add(arrayList);
//    }

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

    public void writeAnswersToCache(JSONArray jsonArray, String fileName) {
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

    public JSONArray readAnswersFromCache(String fileName) {
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

    public boolean hasStorage(String fileName) {
        String path = application.getApplicationContext().getCacheDir() + "/" + fileName;
        File file = new File(path);
        return file.exists();
    }

    public JSONObject json() {
        return sampleJson;
    }

    public SampleItems items() {
        return sampleItems;
    }

    public void sendAnswers(String UniqueId) throws JSONException {
        if (isNetworkConnected()) {
            if (cache == true) {
                localData.clear();
                JSONArray jsonArray = readAnswersFromCache(sharedPreferences.getString("sampleId", "") + "Answers");
                for (int i = 0; i < readAnswersFromCache(sharedPreferences.getString("sampleId", "") + "Answers").length(); i++) {
                    if (!jsonArray.getJSONObject(i).getString("index").equals("")) {
                        ArrayList arrayList = new ArrayList<Integer>();
                        arrayList.add(jsonArray.getJSONObject(i).getString("index"));
                        arrayList.add(jsonArray.getJSONObject(i).getString("answer"));
                        localData.add(arrayList);
                    }
                }
            }
            if (remoteData.size() == 0) {
                insertLocalDataToRemoteData();
                inProgress = true;
                OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SampleWorker.class)
                        .setInputData(data("sendAnswers", UniqueId))
                        .build();
                WorkManager.getInstance(application).enqueue(workRequest);
            }
        } else {
            cache = true;
        }
    }

    private Data data(String work, String UniqueId) throws JSONException {
        return new Data.Builder()
                .putString("work", work)
                .putString("UniqueId", UniqueId)
                .build();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public int answerSize(String fileName){
        JSONArray jsonArray = readAnswersFromCache(fileName);
        int size = 0;
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                if (!jsonArray.getJSONObject(i).getString("index").equals("")){
                    size++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return size;
    }
}