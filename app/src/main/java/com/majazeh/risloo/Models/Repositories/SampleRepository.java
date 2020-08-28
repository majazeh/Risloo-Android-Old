package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

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
import java.util.ArrayList;
import java.util.HashMap;

public class SampleRepository extends MainRepository {

    // Items
    private SampleItems sampleItems;

    // Vars
    public static ArrayList<ArrayList<Integer>> localData;
    public static ArrayList<ArrayList<Integer>> remoteData;
    public static HashMap prerequisiteData;
    public static ArrayList<String> scales;
    public static ArrayList<String> roomsTitle;
    public static ArrayList<String> roomsManager;
    public static ArrayList<String> cases;
    public static ArrayList<String> roomUsers;
    public static MutableLiveData<Integer> workStateSample;
    public static MutableLiveData<Integer> workStateAnswer;
    public static MutableLiveData<Integer> workStateCreate;
    public static String work = "";
    public static String theory = "sample";
    public static String sampleId = "";
    public static String roomId = "";
    public static boolean cache = false;

    // Objects
    private JSONObject sampleJson;

    public SampleRepository(Application application, String sampleId) throws JSONException {
        super(application);

        sample(sampleId);

        localData = new ArrayList<>();
        remoteData = new ArrayList<>();
        prerequisiteData = new HashMap();
        scales = new ArrayList<>();
        roomsTitle = new ArrayList<>();
        roomsManager = new ArrayList<>();
        cases = new ArrayList<>();
        roomUsers = new ArrayList<>();
        workStateSample = new MutableLiveData<>();
        workStateAnswer = new MutableLiveData<>();
        workStateCreate = new MutableLiveData<>();
        workStateSample.setValue(-1);
        workStateAnswer.setValue(-1);
        workStateCreate.setValue(-1);
    }

    public SampleRepository(Application application) {
        super(application);

        localData = new ArrayList<>();
        remoteData = new ArrayList<>();
        prerequisiteData = new HashMap();
        scales = new ArrayList<>();
        roomsTitle = new ArrayList<>();
        roomsManager = new ArrayList<>();
        cases = new ArrayList<>();
        roomUsers = new ArrayList<>();
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

    public void sample(String sampleId) throws JSONException {
        if (isNetworkConnected(application.getApplicationContext())) {
            SampleRepository.sampleId = sampleId;

            work = "getSingle";
            workStateSample.setValue(-1);
            workManager("getSingle");

            SampleRepository.workStateSample.observeForever(integer -> {
                if (integer == 1) {
                    try {
                        JSONObject jsonObject = readSampleFromCache(sampleId);
                        JSONObject data = jsonObject.getJSONObject("data");

                        sampleItems = new SampleItems(data.getJSONArray("items"));
                        checkSampleAnswerStorage(sampleId);

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
                        SampleRepository.workStateSample.removeObserver(integer1 -> {});
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
                    }
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

    public void samples() throws JSONException {
        work = "getAll";
        workStateSample.setValue(-1);
        workManager("getAll");
    }

    public void close(String sampleId) throws JSONException {
        SampleRepository.sampleId = sampleId;

        work = "close";
        workStateSample.setValue(-1);
        workManager("close");
    }

    public void delete(String sampleId) {
        FileManager.deleteCache(application.getApplicationContext(), "Answers", sampleId);
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

                SampleRepository.sampleId = sampleId;

                work = "sendAnswers";
                workStateAnswer.setValue(-1);
                workManager("sendAnswers");
            }
        } else {
            SampleRepository.cache = true;
        }
    }

    public void sendPrerequisite(String sampleId, HashMap prerequisites) throws JSONException {
        SampleRepository.prerequisiteData = prerequisites;
        SampleRepository.sampleId = sampleId;

        work = "sendPrerequisite";
        workStateAnswer.setValue(-1);
        workManager("sendPrerequisite");
    }

    public void scales() throws JSONException {
        work = "getScales";
        workStateCreate.setValue(-1);
        workManager("getScales");
    }

    public void rooms() throws JSONException {
        work = "getRooms";
        workStateCreate.setValue(-1);
        workManager("getRooms");
    }

    public void roomsUsers(String roomId) throws JSONException {
        SampleRepository.roomId = roomId;

        work = "getRoomsUsers";
        workStateCreate.setValue(-1);
        workManager("getRoomsUsers");
    }

    public void cases(String roomId) throws JSONException {
        SampleRepository.roomId = roomId;

        work = "getCases";
        workStateCreate.setValue(-1);
        workManager("getCases");
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
         ---------- Write ----------
    */

    public boolean writeSampleAnswerToExternal(JSONArray jsonArray, String fileName) {
        return FileManager.writeToExternal(application.getApplicationContext(), jsonArray, fileName);
    }

    public boolean writeSampleAnswerToCache(JSONArray jsonArray, String fileName) {
        return FileManager.writeArrayToCache(application.getApplicationContext(), jsonArray, "Answers", fileName);
    }

    public boolean writePrerequisiteAnswerToCache(JSONObject jsonObject, String fileName) {
        return FileManager.writeObjectToCache(application.getApplicationContext(), jsonObject, "prerequisiteAnswers", fileName);
    }

    /*
         ---------- Read ----------
    */

    public JSONObject readSampleFromCache(String fileName) {
        return FileManager.readObjectFromCache(application.getApplicationContext(), "Samples", fileName);
    }

    public JSONArray readSampleAnswerFromCache(String fileName) {
        return FileManager.readArrayFromCache(application.getApplicationContext(), "Answers", fileName);
    }

    public JSONObject readPrerequisiteAnswerFromCache(String fileName) {
        return FileManager.readObjectFromCache(application.getApplicationContext(), "prerequisiteAnswers", fileName);
    }

    /*
         ---------- Check ----------
    */

    public boolean hasSampleAnswerStorage(String fileName) {
        return FileManager.hasCache(application.getApplicationContext(), "Answers", fileName);
    }

    public boolean checkSampleAnswerStorage(String fileName) {
        if (!hasSampleAnswerStorage(fileName)) {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < sampleItems.size(); i++) {
                try {
                    jsonArray.put(new JSONObject().put("index", i).put("answer", ""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            writeSampleAnswerToCache(jsonArray, fileName);
            return true;
        }
        return false;
    }

    public boolean hasPrerequisiteAnswerStorage(String fileName) {
        return FileManager.hasCache(application.getApplicationContext(), "Prerequisite", fileName);
    }

    public boolean checkPrerequisiteAnswerStorage(String fileName) {
        try {
            int size = 0;
            for (int i = 1; i <= readPrerequisiteAnswerFromCache(fileName).length(); i++) {
                if (!readPrerequisiteAnswerFromCache(fileName).getString(String.valueOf(i)).equals("")) {
                    size++;
                }
            }
            return size == 0;
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }
    }

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
            return (JSONObject) getItem(index).get("answer");
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

    public String getType(int index) {
        try {
            return (String) getAnswer(index).get("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
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
         ---------- Ints ----------
    */

    public int answered(int index) {
        try {
            if (getItem(index).get("user_answered") != null) {
                return Integer.parseInt(String.valueOf(sampleItems.items().get(index).get("user_answered")));
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
            workStateCreate.setValue(-2);
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