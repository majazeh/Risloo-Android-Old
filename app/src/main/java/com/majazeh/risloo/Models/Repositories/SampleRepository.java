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
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.Models.Workers.SampleWorker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SampleRepository extends MainRepository {

    // Objects
    private JSONObject sampleJson;
    private SampleItems sampleItems;

    // Vars
    public static ArrayList<ArrayList<Integer>> localData;
    public static ArrayList<ArrayList<Integer>> remoteData;
    public static ArrayList prerequisiteData;
    public static HashMap createData;
    public static ArrayList<Model> getAll;
    public static ArrayList<Model> scales;
    public static ArrayList<Model> rooms;
    public static ArrayList<Model> cases;
    public static ArrayList<Model> references;
    public static ArrayList<Model> scaleFilter;
    public static ArrayList<Model> statusFilter;
    public static MutableLiveData<Integer> workStateSample;
    public static MutableLiveData<Integer> workStateAnswer;
    public static MutableLiveData<Integer> workStateCreate;
    public static String work = "";
    public static String theory = "sample";
    public static String sampleId = "";
    public static String roomId = "";
    public static String scalesQ = "";
    public static String roomQ = "";
    public static String casesQ = "";
    public static String referencesQ = "";
    public static String statusQ = "";
    public static boolean cache = false;
    public static int samplesPage = 1;
    public static int scalesPage = 1;

    public SampleRepository(Application application) throws JSONException {
        super(application);

        localData = new ArrayList<>();
        remoteData = new ArrayList<>();
        prerequisiteData = new ArrayList();
        createData = new HashMap();
        getAll = new ArrayList<>();
        scales = new ArrayList<>();
        rooms = new ArrayList<>();
        cases = new ArrayList<>();
        references = new ArrayList<>();
        workStateSample = new MutableLiveData<>();
        workStateAnswer = new MutableLiveData<>();
        workStateCreate = new MutableLiveData<>();
        workStateSample.setValue(-1);
        workStateAnswer.setValue(-1);
        workStateCreate.setValue(-1);
    }

    public SampleRepository() {

    }

    /*
         ---------- Voids ----------
    */

    public void sample(String sampleId) throws JSONException {
        SampleRepository.sampleId = sampleId;

        work = "getSingle";
        workStateSample.setValue(-1);
        workManager("getSingle");

        workStateSample.observeForever(integer -> {
            if (integer == 1) {
                try {
                    JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "Samples" + "/" + sampleId);
                    JSONObject data = jsonObject.getJSONObject("data");

                    sampleItems = new SampleItems(data.getJSONArray("items"));
                    checkSampleAnswerStorage(sampleId);

                    if (FileManager.readArrayFromCache(application.getApplicationContext(), "Answers" + "/" + sampleId) != null) {
                        sampleItems.setIndex(firstUnAnswered(sampleId));
                    }

                    sampleJson = FileManager.readObjectFromCache(application.getApplicationContext(), "Samples" + "/" + sampleId);

                    workStateSample.removeObserver(integer1 -> {
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                if (FileManager.readObjectFromCache(application.getApplicationContext(), "Samples" + "/" + sampleId) != null) {
                    try {
                        sampleJson = FileManager.readObjectFromCache(application.getApplicationContext(), "Samples" + "/" + sampleId);
                        JSONObject data = sampleJson.getJSONObject("data");
                        sampleItems = new SampleItems(data.getJSONArray("items"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                workStateSample.removeObserver(integer1 -> {
                });
            }
        });
    }

    public void samples(String scalesQ, String statusQ, String roomQ) throws JSONException {
        SampleRepository.scalesQ = scalesQ;
        SampleRepository.statusQ = statusQ;
        SampleRepository.roomQ = roomQ;

        work = "getAll";
        workStateSample.setValue(-1);
        workManager("getAll");
    }

    public void sendAnswers(String sampleId) throws JSONException {
        if (isNetworkConnected(application.getApplicationContext())) {
            cache = false;
            if (SampleRepository.cache) {
                localData.clear();
                JSONArray jsonArray = FileManager.readArrayFromCache(application.getApplicationContext(), "Answers" + "/" + sampleId);
                for (int i = 0; i < FileManager.readArrayFromCache(application.getApplicationContext(), "Answers" + "/" + sampleId).length(); i++) {
                    if (!jsonArray.getJSONObject(i).getString("index").equals("")) {
                        if (!jsonArray.getJSONObject(i).getString("answer").equals("")) {
                            ArrayList arrayList = new ArrayList<Integer>();

                            arrayList.add(jsonArray.getJSONObject(i).getString("index"));
                            arrayList.add(jsonArray.getJSONObject(i).getString("answer"));

                            localData.add(arrayList);
                        }
                    }
                }
            } else {
                if (remoteData.size() == 0) {
                    insertLocalToRemote();
                    SampleRepository.sampleId = sampleId;
                    work = "sendAnswers";
                    workStateAnswer.setValue(-1);
                    workManager("sendAnswers");
                }
            }
        } else {
            SampleRepository.cache = true;
            work = "sendAnswers";
            workStateAnswer.setValue(-2);
        }
    }

    public void sendAllAnswers(String sampleId) throws JSONException {
        JSONArray jsonArray = FileManager.readArrayFromCache(application.getApplicationContext(), "Answers" + "/" + sampleId);
        for (int i = 0; i < FileManager.readArrayFromCache(application.getApplicationContext(), "Answers" + "/" + sampleId).length(); i++) {
            if (!jsonArray.getJSONObject(i).getString("index").equals("")) {
                if (!jsonArray.getJSONObject(i).getString("answer").equals("")) {
                    ArrayList arrayList = new ArrayList<Integer>();

                    arrayList.add(jsonArray.getJSONObject(i).getString("index"));
                    arrayList.add(jsonArray.getJSONObject(i).getString("answer"));

                    remoteData.add(arrayList);
                }
            }
        }

        SampleRepository.sampleId = sampleId;

        work = "sendAnswers";
        workStateAnswer.setValue(-1);
        workManager("sendAnswers");
    }

    public void sendPrerequisite(String sampleId, ArrayList prerequisites) throws JSONException {
        SampleRepository.prerequisiteData = prerequisites;
        SampleRepository.sampleId = sampleId;

        work = "sendPrerequisite";
        workStateAnswer.setValue(-1);
        workManager("sendPrerequisite");
    }

    public void create(ArrayList scales, String room, String casse, ArrayList roomReferences, ArrayList caseReferences, String count) throws JSONException {
        if (scales.size() != 0)
            SampleRepository.createData.put("scale_id", scales);
        if (!room.equals(""))
            SampleRepository.createData.put("room_id", room);
        if (!casse.equals("")) {
            SampleRepository.createData.put("case_id", casse);
            if (caseReferences.size() != 0)
                SampleRepository.createData.put("client_id", caseReferences);
        } else {
            SampleRepository.createData.put("client_id", roomReferences);
        }
        if (!count.equals(""))
            SampleRepository.createData.put("count", count);

        work = "create";
        workStateCreate.setValue(-1);
        workManager("create");
    }

    public void close(String sampleId) throws JSONException {
        SampleRepository.sampleId = sampleId;

        work = "close";
        workStateSample.setValue(-1);
        workManager("close");
    }

    public void score(String sampleId) throws JSONException {
        SampleRepository.sampleId = sampleId;

        work = "score";
        workStateSample.setValue(-1);
        workManager("score");
    }

    public void delete(String sampleId) {
        FileManager.deleteFileFromCache(application.getApplicationContext(), "Answers" + "/" + sampleId);
    }

    public void scales(String q,int page) throws JSONException {
        SampleRepository.scalesQ = q;
        SampleRepository.scalesPage = page;

        work = "getScales";
        workStateCreate.setValue(-1);
        workManager("getScales");
    }

    public void rooms(String q) throws JSONException {
        SampleRepository.roomQ = q;

        work = "getRooms";
        workStateCreate.setValue(-1);
        workStateSample.setValue(-1);
        workManager("getRooms");
    }

    public void cases(String roomId, String q) throws JSONException {
        SampleRepository.roomId = roomId;
        SampleRepository.casesQ = q;

        work = "getCases";
        workStateCreate.setValue(-1);
        workManager("getCases");
    }

    public void references(String roomId, String q) throws JSONException {
        SampleRepository.roomId = roomId;
        SampleRepository.referencesQ = q;

        work = "getReferences";
        workStateCreate.setValue(-1);
        workManager("getReferences");
    }

    public void general(String sampleId) throws JSONException {
        SampleRepository.sampleId = sampleId;

        work = "getGeneral";
        workStateSample.setValue(-1);
        workManager("getGeneral");
    }

    public void scores(String sampleId) throws JSONException {
        SampleRepository.sampleId = sampleId;

        work = "getScore";
        workStateSample.setValue(-1);
        workManager("getScore");
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
         ---------- Check ----------
    */

    public boolean checkSampleAnswerStorage(String fileName) {
        if (!FileManager.hasFileInCache(application.getApplicationContext(), "Answers" + "/" + fileName)) {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < sampleItems.size(); i++) {
                try {
                    jsonArray.put(new JSONObject().put("index", i).put("answer", ""));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            FileManager.writeArrayToCache(application.getApplicationContext(), jsonArray, "Answers" + "/" + fileName);
            return true;
        }
        return false;
    }

    public boolean checkPrerequisiteAnswerStorage(String fileName) {
        JSONArray jsonArray = FileManager.readArrayFromCache(application.getApplicationContext(), "prerequisitesAnswers" + "/" + fileName);

        try {
            int size = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                if (!jsonArray.getJSONArray(i).getString(1).equals("")) {
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
            JSONArray jsonArray = sampleJson.getJSONObject("data").getJSONArray("prerequisites");
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
        JSONArray items = FileManager.readArrayFromCache(application.getApplicationContext(), "Answers" + "/" + fileName);
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
        JSONArray items = FileManager.readArrayFromCache(application.getApplicationContext(), "Answers" + "/" + fileName);
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
        JSONArray items = FileManager.readArrayFromCache(application.getApplicationContext(), "Answers" + "/" + fileName);
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
         ---------- Urls ----------
    */

    private String getUrlScore(String type) {
        if (FileManager.readObjectFromCache(application.getApplicationContext(), "sampleDetailFiles" + "/" + SampleRepository.sampleId) != null) {
            JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "sampleDetailFiles" + "/" + SampleRepository.sampleId);
            try {
                JSONObject profiles = jsonObject.getJSONObject("profiles");
                if (profiles.has(type)) {
                    JSONObject object = profiles.getJSONObject(type);
                    return object.getString("url");
                }
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public String getSvgScore(String sampleId) {
        SampleRepository.sampleId = sampleId;

        if (getUrlScore("profile_svg") != null) {
            return getUrlScore("profile_svg");
        }
        return null;
    }

    public String getPngScore(String sampleId) {
        SampleRepository.sampleId = sampleId;

        if (getUrlScore("profile_png") != null) {
            return getUrlScore("profile_png");
        }
        return null;
    }

    public String getHtmlScore(String sampleId) {
        SampleRepository.sampleId = sampleId;

        if (getUrlScore("profile_html") != null) {
            return getUrlScore("profile_html");
        }
        return null;
    }

    public String getPdfScore(String sampleId) {
        SampleRepository.sampleId = sampleId;

        if (getUrlScore("profile_pdf") != null) {
            return getUrlScore("profile_pdf");
        }
        return null;
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() {
        scaleFilter = new ArrayList<>();
        statusFilter = new ArrayList<>();
        if (!SampleRepository.filter()) {
            ArrayList<Model> arrayList = new ArrayList<>();
            if (FileManager.readObjectFromCache(application.getApplicationContext(), "samples" + "/" + "all") != null) {
                // not filter and show
                JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "samples" + "/" + "all");
                try {
                    JSONArray data = jsonObject.getJSONArray("data");
                    if (data.length() == 0) {
                        return null;
                    }
                    for (int i = 0; i < data.length(); i++) {
                        Model model = new Model(data.getJSONObject(i));
                        arrayList.add(model);
                    }
                    this.meta = jsonObject.getJSONObject("meta");
                    handlerFilters();
                    return arrayList;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                // not filter and not show
                return null;
            }
        } else {
            if (isNetworkConnected(application.getApplicationContext())) {
                // filter and show
                handlerFilters();
                if (getAll.size() == 0){
                    return null;
                }else {
                    return getAll;
                }
            } else {
                if (FileManager.readObjectFromCache(application.getApplicationContext(), "samples" + "/" + "all") != null) {
                    // filter and show
                    try {
                        getAll.clear();
                        JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "samples" + "/" + "all");
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            if (object.getString("status").equals(statusQ)) {
                                SampleRepository.getAll.add(new Model(object));
                            }
                            JSONObject scale = object.getJSONObject("scale");
                            if (scale.getString("id").equals(scalesQ)) {
                                SampleRepository.getAll.add(new Model(object));
                            }
                            if (object.has("room") && !object.isNull("room")) {
                                JSONObject room = object.getJSONObject("room");
                                if (room.getString("id").equals(roomQ)) {
                                    SampleRepository.getAll.add(new Model(object));
                                }
                            }
                        }
                        this.meta = jsonObject.getJSONObject("meta");
                        handlerFilters();
                        if (getAll.size() == 0){
                            return null;
                        }else {
                            return getAll;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }

                } else {
                    // filter and not show
                    return null;
                }
            }
        }
    }

    public ArrayList<Model> getArchive() {
        ArrayList<Model> arrayList = new ArrayList<>();
        File file = new File(application.getApplicationContext().getCacheDir(), "Samples");
        File[] list = file.listFiles();
        if (list != null) {
            for (int i = 0; i < list.length; i++) {
                if (FileManager.readArrayFromCache(application.getApplicationContext(), "Answers" + "/" + list[i].getName()) != null) {
                    JSONObject sample = FileManager.readObjectFromCache(application.getApplicationContext(), "Samples" + "/" + list[i].getName());
                    try {
                        if (sample.getJSONObject("data").getString("status").equals("open")) {
                            JSONObject jsonObject = new JSONObject();
                            if (!sample.getJSONObject("data").isNull("id"))
                                jsonObject.put("id", sample.getJSONObject("data").getString("id"));
                            if (!sample.getJSONObject("data").isNull("status"))
                                jsonObject.put("status", sample.getJSONObject("data").getString("status"));
                            if (!sample.getJSONObject("data").isNull("scale"))
                                jsonObject.put("scale", sample.getJSONObject("data").getJSONObject("scale"));
                            if (!sample.getJSONObject("data").isNull("client"))
                                jsonObject.put("client", sample.getJSONObject("data").getJSONObject("client"));
                            if (!sample.getJSONObject("data").isNull("code"))
                                jsonObject.put("code", sample.getJSONObject("data").getString("code"));
                            if (!sample.getJSONObject("data").isNull("case"))
                                jsonObject.put("case", sample.getJSONObject("data").getJSONObject("case"));
                            if (!sample.getJSONObject("data").isNull("room"))
                                jsonObject.put("room", sample.getJSONObject("data").getJSONObject("room"));
                            arrayList.add(new Model(jsonObject));
                        }
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

    public static boolean filter() {
        if (SampleRepository.scalesQ.equals("") && SampleRepository.statusQ.equals("") && SampleRepository.roomQ.equals(""))
            return false;
        else {
            return true;
        }
    }

    public static boolean filterArraysExist(){
        if (SampleRepository.scaleFilter.size() == 0){
            return false;
        }else{
            return true;
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
            ExceptionGenerator.getException(false, 0, null, "OffLineException");
            workStateSample.setValue(-2);
            workStateAnswer.setValue(-2);
            workStateCreate.setValue(-2);
        }
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null && Objects.requireNonNull(cm.getActiveNetworkInfo()).isConnected();
    }

    private Data data(String work) throws JSONException {
        return new Data.Builder()
                .putString("work", work)
                .build();
    }

}