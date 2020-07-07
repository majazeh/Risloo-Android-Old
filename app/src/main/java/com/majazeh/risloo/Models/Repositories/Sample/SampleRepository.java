package com.majazeh.risloo.Models.Repositories.Sample;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Models.Remotes.Generators.JsonGenerator;
import com.majazeh.risloo.Models.Repositories.MainRepository;
import com.majazeh.risloo.Models.Workers.AuthWorker;

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
    public static MutableLiveData<ArrayList<ArrayList<Integer>>> localData = new MutableLiveData<>();
    public static ArrayList<ArrayList<Integer>> remoteData = new ArrayList<>();

    public SampleRepository(@NonNull Application application, String testUniqueId) throws JSONException {
        super(application);

        jsonGenerator = new JsonGenerator();
        sampleController = new SampleController(application, jsonGenerator, testUniqueId);
        sampleJson = sampleController.getSample();
        sampleItems = new SampleItems(sampleJson.getJSONArray("items"));
    }



    public void insertToLocalData(int item, int answer) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(item);
        arrayList.add(answer);

        localData.setValue(arrayList);
    }

    public void insertToRemoteData(int item, int answer) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(item);
        arrayList.add(answer);

        remoteData.add(arrayList);
    }

    public void insertRemoteDataToLocalData() {
        for (int i = 0; i < remoteData.size(); i++) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(remoteData.get(i));

            localData.setValue(arrayList);
        }
        remoteData.clear();
    }

    public void insertLocalDataToRemoteData() {
        for (int i = 0; i < localData.getValue().size(); i++) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(localData.getValue().get(i));

            remoteData.add(arrayList);
        }
        localData.getValue().clear();
    }

    public MutableLiveData<ArrayList<ArrayList<Integer>>> localData() {
        return localData;
    }

    public ArrayList remoteData() {
        return remoteData;
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

    public void process() throws JSONException {
        if (remoteData().size() == 0){
            insertLocalDataToRemoteData();
            inProgress = true;
            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(AuthWorker.class)
                    .setInputData(data("sendAnswers"))
                    .build();
            WorkManager.getInstance(application).enqueue(workRequest);
            localData().getValue().clear();
        }
    }
    private Data data(String work) throws JSONException {
        return new Data.Builder()
                .putString("work", work)
                .build();
    }

}