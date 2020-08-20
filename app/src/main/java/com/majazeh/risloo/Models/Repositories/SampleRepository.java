package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;

import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Items.SampleItems;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Managers.FileManager;
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
import java.util.HashMap;

public class SampleRepository extends MainRepository {

    // Items
    private SampleItems sampleItems;

    // Vars
    public static HashMap prerequisiteData;
    public static ArrayList<ArrayList<Integer>> localData;
    public static ArrayList<ArrayList<Integer>> remoteData;
    public static MutableLiveData<Integer> workStateSample;
    public static MutableLiveData<Integer> workStateAnswer;
    public static String work = "";
    public static String theory = "sample";
    public static String sampleId = "";
    public static boolean cache = false;

    // Objects
    private JSONObject sampleJson;

    public SampleRepository(Application application, String sampleId) throws JSONException {
        super(application);

        sample(sampleId);

        prerequisiteData = new HashMap();
        localData = new ArrayList<>();
        remoteData = new ArrayList<>();
        workStateSample = new MutableLiveData<>();
        workStateAnswer = new MutableLiveData<>();
        workStateSample.setValue(-1);
        workStateAnswer.setValue(-1);
    }

    public SampleRepository(Application application) {
        super(application);

        prerequisiteData = new HashMap();
        localData = new ArrayList<>();
        remoteData = new ArrayList<>();
        workStateSample = new MutableLiveData<>();
        workStateAnswer = new MutableLiveData<>();
        workStateSample.setValue(-1);
        workStateAnswer.setValue(-1);
    }

    public SampleRepository() {

    }

    /*
         ---------- Voids ----------
    */

    public void close(String sampleId) throws JSONException {
        SampleRepository.sampleId = sampleId;

        work = "close";
        workStateSample.setValue(-1);
        workManager("close");
    }

    public void samples() throws JSONException {
        work = "getAll";
        workStateSample.setValue(-1);
        workManager("getAll");
    }

























































    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void delete(String sampleId) {
        File file = new File(application.getApplicationContext().getCacheDir(), "Answers" + "/" + sampleId);
        file.delete();
    }

    public void sample(String sampleId) throws JSONException {
        if (isNetworkConnected(application.getApplicationContext())) {

            work = "getSingle";
            workStateSample.setValue(-1);
            workManager("getSingle");

            SampleRepository.workStateSample.observeForever(integer -> {
                if (integer == 1) {
                    try {
                        JSONObject jsonObject = readSampleFromCache(sampleId);
                        JSONObject data = jsonObject.getJSONObject("data");
                        sampleItems = new SampleItems(data.getJSONArray("items"));
                        checkAnswerStorage(sampleId);
                        JSONArray jsonArray = readSampleAnswerFromCache(sampleId);
                        for (int i = 0; i < sampleItems.size(); i++) {
                            if (answered(i) != -1) {
                                jsonArray.getJSONObject(i).put("index", i);
                                jsonArray.getJSONObject(i).put("answer", answered(i));
                                writeSampleAnswerToCache(jsonArray, sampleId);
                            }
                        }
                        if (readSampleAnswerFromCache(sampleId) != null) {
                            sampleItems.setIndex(firstUnAnswered(sampleId));
                        }
                        sampleJson = readSampleFromCache(sampleId);
                        SampleRepository.workStateSample.removeObserver(integer1 -> {
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (integer == 0) {
                    if (readSampleFromCache(sampleId) != null) {
                        try {
                            sampleJson = readSampleFromCache(sampleId);
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
            if (readSampleFromCache(sampleId) != null) {
                try {
                    sampleJson = readSampleFromCache(sampleId);
                    JSONObject data = sampleJson.getJSONObject("data");
                    sampleItems = new SampleItems(data.getJSONArray("items"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendAnswers(String sampleId) throws JSONException {
        if (isNetworkConnected(application.getApplicationContext())) {
            if (SampleRepository.cache == true) {
                localData.clear();
                JSONArray jsonArray = readSampleAnswerFromCache(sampleId);
                for (int i = 0; i < readSampleAnswerFromCache(sampleId).length(); i++) {
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

                work = "sendAnswers";
                workStateAnswer.setValue(-1);
                workManager("sendAnswers");
            }
        } else {
            SampleRepository.cache = true;
        }
    }

    public void sendPrerequisite(HashMap parameters) throws JSONException {
        prerequisiteData = parameters;

        work = "sendPrerequisite";
        workStateAnswer.setValue(-1);
        workManager("sendPrerequisite");
    }

    public void writeSampleAnswerToExternal(JSONArray jsonArray, String fileName) {
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

    public boolean saveSampleToCache(Context context, JSONObject jsonObject, String fileName) {
        try {
            File file = new File(context.getCacheDir(), "Samples/" + fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            JSONArray jsonArray = new JSONArray();
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray items = data.getJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                JSONArray jsonArray1 = new JSONArray();
                jsonArray1.put(i);
                if (items.getJSONObject(i).has("user_answered")) {
                    jsonArray1.put(items.getJSONObject(i).getString("user_answered"));
                } else {
                    jsonArray.put("");
                }
            }
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(jsonObject.toString());
            oos.close();
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

    public boolean writeSampleAnswerToCache(JSONArray jsonArray, String fileName) {
        return FileManager.writeArrayToCache(application.getApplicationContext(), jsonArray, "Answers", fileName);
    }

    public void savePrerequisiteToCache(Context context, JSONArray jsonArray, String fileName) {
        try {
            File file = new File(context.getCacheDir(), "Prerequisite/" + fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).has("user_answered")) {
                    jsonObject.put(String.valueOf(i + 1), jsonArray.getJSONObject(i).getString("user_answered"));
                } else {
                    jsonObject.put(String.valueOf(i + 1), "");
                }
            }
            writePrerequisiteAnswerToCache(jsonObject, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(jsonArray.toString());
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean writePrerequisiteAnswerToCache(JSONObject jsonObject, String fileName) {
        return FileManager.writeObjectToCache(application.getApplicationContext(), jsonObject, "prerequisiteAnswers", fileName);
    }

    public JSONObject readSampleFromCache(String fileName) {
        return FileManager.readObjectFromCache(application.getApplicationContext(), "Samples", fileName);
    }

    public JSONArray readSampleAnswerFromCache(String fileName) {
        return FileManager.readArrayFromCache(application.getApplicationContext(), "Answers", fileName);
    }

    public JSONObject readPrerequisiteAnswerFromCache(String fileName) {
        return FileManager.readObjectFromCache(application.getApplicationContext(), "prerequisiteAnswers", fileName);
    }

    public boolean hasAnswerStorage(String fileName) {
        return FileManager.hasCache(application.getApplicationContext(), "Answers", fileName);
    }

    public boolean hasPrerequisiteStorage(String fileName) {
        return FileManager.hasCache(application.getApplicationContext(), "Prerequisite", fileName);
    }

    public void checkAnswerStorage(String fileName) {
        if (!hasAnswerStorage(fileName)) {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < sampleItems.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("index", i);
                    jsonObject.put("answer", "");
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            writeSampleAnswerToCache(jsonArray, fileName);
        }
    }

    public int answered(int i) {
        try {
            if (sampleItems.items().get(i).get("user_answered") != null) {
                return Integer.parseInt(String.valueOf(sampleItems.items().get(i).get("user_answered")));
            } else {
                return -1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public int answeredPosition(String fileName, int index) {
        JSONArray items = readSampleAnswerFromCache(fileName);
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
        JSONArray items = readSampleAnswerFromCache(fileName);
        int size = 0;
        if (items != null) {
            for (int i = 0; i < items.length(); i++) {
                try {
                    if (!items.getJSONObject(i).getString("answer").equals("")) {
                        size++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return size;
    }

    public int firstUnAnswered(String fileName) {
        JSONArray items = readSampleAnswerFromCache(fileName);
        if (items != null) {
            for (int i = 0; i < items.length(); i++) {
                try {
                    if (items.getJSONObject(i).getString("answer").equals("")) {
                        return i;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    public boolean showPrerequisite(String fileName) {
        try {
            int size = 0;
            for (int i = 1; i <= readPrerequisiteAnswerFromCache(fileName).length(); i++) {
                if (!readPrerequisiteAnswerFromCache(fileName).getString(String.valueOf(i)).equals(""))
                    size++;
            }
            if (size != 0) {
                return false;
            } else {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
















































    /*
         ---------- Items ----------
    */

    public String getDescription() {
        try {
            return sampleJson.getJSONObject("data").getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList getPrerequisite() {
        ArrayList arrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = sampleJson.getJSONObject("data").getJSONArray("prerequisite");
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList.add(jsonArray.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public ArrayList getItems() {
        if (sampleItems != null)
            return sampleItems.items();
        else
            return null;
    }

    public Model getItem(int index) {
        return sampleItems.item(index);
    }

    public JSONObject getAnswer(int index) {
        try {
            return (JSONObject) sampleItems.item(index).get("answer");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList getOptions(int index) {
        ArrayList arrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = getAnswer(index).getJSONArray("options");
            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList.add(jsonArray.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }





    public Model getNext() {
        return sampleItems.next();
    }

    public Model getPrev() {
        return sampleItems.prev();
    }

    public Model goToIndex(int index) {
        return sampleItems.goToIndex(index);
    }

    public void setIndex(int index) {
        sampleItems.setIndex(index);
    }

    public int getIndex() {
        return sampleItems.getIndex();
    }

    public int getSize() {
        return sampleItems.size();
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() {
        ArrayList<Model> arrayList = new ArrayList<>();
        if (FileManager.readObjectFromCache(application.getApplicationContext(), "samples", "all") != null) {
            JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "samples", "all");
            try {
                JSONArray data = jsonObject.getJSONArray("data");
                if (data.length() == 0) {
                    return null;
                }
                for (int i = 0; i < data.length(); i++) {
                    Model model = new Model(data.getJSONObject(i));
                    arrayList.add(model);
                }
                return arrayList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public ArrayList<Model> getArchive() {
        ArrayList<Model> arrayList = new ArrayList<>();
        File file = new File(application.getApplicationContext().getCacheDir(), "Samples");
        File[] list = file.listFiles();
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                if (readSampleAnswerFromCache(list[i].getName()) != null) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("serial", list[i].getName());
                        if (answeredSize(list[i].getName()) < readSampleAnswerFromCache(list[i].getName()).length()) {
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
            }
            if (arrayList.size() == 0) {
                return null;
            }
            return arrayList;
        } else {
            return null;
        }
    }

    /*
         ---------- Insert ----------
    */

    public void insertToLocal(int index, int answer) {
        ArrayList<Integer> arrayList = new ArrayList<>();
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
         ---------- Work ----------
    */

    private void workManager(String work) throws JSONException {
        if (isNetworkConnected(application.getApplicationContext())) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SampleWorker.class)
                    .setConstraints(constraints)
                    .setInputData(data(work))
                    .build();

            WorkManager.getInstance(application).enqueue(workRequest);
        } else {
            ExceptionManager.getException(0, null, false, "OffLine", "sample");
            workStateSample.setValue(-2);
            workStateAnswer.setValue(-2);
        }
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    private Data data(String work) throws JSONException {
        return new Data.Builder()
                .putString("work", work)
                .build();
    }

}