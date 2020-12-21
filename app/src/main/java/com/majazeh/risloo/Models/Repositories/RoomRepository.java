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
import com.majazeh.risloo.Models.Workers.RoomWorker;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Generators.JSONGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class RoomRepository extends MainRepository {
    public static String work = "";
    public static String usage = "";
    public static String notInCase = "";
    public static HashMap addUserData = new HashMap();
    public static HashMap createData = new HashMap();
    public static ArrayList<Model> rooms;
    public static ArrayList<Model> myRooms;
    public static ArrayList<Model> myManagementRooms;
    public static ArrayList<Model> users;
    public static ArrayList<Model> counselingCenters;
    public static ArrayList<Model> psychologist;
    public static String roomQ = "";
    public static String psychologistsQ = "";
    public static int allPage = 1;
    public static int myPage = 1;
    public static int myManagementPage = 1;
    public static MutableLiveData<Integer> workState;
    public static String roomId = "";
    public static String referencesQ = "";
    public static int usersPage = 1;
    public static ArrayList<Model> references;
    public static ArrayList<Model> suggestRoom;
    public static ArrayList<Integer> suggestRoomCount;


    public RoomRepository(Application application) {
        super(application);
        rooms = new ArrayList<>();
        myRooms = new ArrayList<>();
        myManagementRooms = new ArrayList<>();
        suggestRoomCount = new ArrayList<>();
        users = new ArrayList<>();
        suggestRoom = new ArrayList<>();
        references = new ArrayList<>();
        counselingCenters = new ArrayList<>();
        psychologist = new ArrayList<>();
        workState = new MutableLiveData<>();

        workState.setValue(-1);
    }

    public RoomRepository() {
    }

    public void rooms(String q) throws JSONException {
        roomQ = q;

        work = "getAll";
        workState.setValue(-1);
        workManager("getAll");
    }

    public void myRooms(String q) throws JSONException {
        roomQ = q;

        work = "getMy";
        workState.setValue(-1);
        workManager("getMy");
    }

    public void myManagementRooms(String q) throws JSONException {
        roomQ = q;

        work = "getMyManagement";
        workState.setValue(-1);
        workManager("getMyManagement");
    }

    public void references(String roomId, String q, String usage, String notInCase) throws JSONException {
        RoomRepository.roomId = roomId;
        RoomRepository.referencesQ = q;
        RoomRepository.usage = usage;
        RoomRepository.notInCase = notInCase;
        work = "getReferences";
        workState.setValue(-1);
        workManager("getReferences");
    }

    public void create(String counselingCenterId, String psychologistId) throws JSONException {
        if (!counselingCenterId.equals(""))
            CenterRepository.createData.put("counseling_center_id", counselingCenterId);
        if (!psychologistId.equals(""))
            CenterRepository.createData.put("psychologist_id", psychologistId);

        work = "create";
        workState.setValue(-1);
        workManager("create");
    }

    public void references(String roomId, String q, String usage) throws JSONException {
        references(roomId, q, usage, "");
    }

    public void users(String roomId) throws JSONException {
        RoomRepository.roomId = roomId;
        work = "users";
        workState.setValue(-1);
        workManager("users");
    }

    public void addUser(String roomId, ArrayList<String> users) throws JSONException {
        if (!roomId.equals(""))
            RoomRepository.roomId = roomId;
        if (users.size() != 0)
            addUserData.put("user_id", users);
        work = "addUser";
        workState.setValue(-1);
        workManager("addUser");
    }

    public void getCounselingCenter() throws JSONException {
        work = "getCounselingCenter";
        workState.setValue(-1);
        workManager("getCounselingCenter");
    }

    public void getPsychologists(String clinicId,String q) throws JSONException {
        CenterRepository.clinicId = clinicId;
        RoomRepository.psychologistsQ = q;
        work = "getPsychologists";
        workState.setValue(-1);
        workManager("getPsychologists");
    }


    public void references(String roomId, String q) throws JSONException {
        references(roomId, q, "");
    }

    public ArrayList<Model> getAll() {
        try {
            if (roomQ.equals("")) {
                if (FileManager.readObjectFromCache(application.getApplicationContext(), "rooms") != null) {
                    ArrayList<Model> arrayList = new ArrayList<>();
                    JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "rooms");
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        arrayList.add(new Model(data.getJSONObject(i)));
                    }
                    return arrayList;
                } else {
                    return null;
                }
            } else {
                if (rooms.size() == 0) {
                    return null;
                } else {
                    return rooms;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Model> getMy() {
        try {
            if (roomQ.equals("")) {
                if (FileManager.readObjectFromCache(application.getApplicationContext(), "myRooms") != null) {
                    ArrayList<Model> arrayList = new ArrayList<>();
                    JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "myRooms");
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        arrayList.add(new Model(data.getJSONObject(i)));
                    }
                    return arrayList;
                } else {
                    return null;
                }
            } else {
                if (myRooms.size() == 0) {
                    return null;
                } else {
                    return myRooms;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Model> getUsers(String roomId) {
        ArrayList<Model> arrayList = new ArrayList<>();
        if (FileManager.readObjectFromCache(application.getApplicationContext(), "roomUsers" + "/" + roomId) != null) {
            JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "roomUsers" + "/" + roomId);
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

    public ArrayList<Model> getUsersCenters(String roomId) {
        ArrayList<Model> arrayList = new ArrayList<>();
        if (FileManager.readObjectFromCache(application.getApplicationContext(), "roomUsers" + "/" + roomId) != null) {
            JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "roomUsers" + "/" + roomId);
            try {
                JSONObject room = jsonObject.getJSONObject("room");
                Model model = new Model(room.getJSONObject("center"));
                arrayList.add(model);
                return arrayList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public ArrayList<Model> getLocalPosition() {
        try {
            JSONArray data = new JSONArray(JSONGenerator.getJSON(application.getApplicationContext(), "localPosition.json"));
            ArrayList<Model> arrayList = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                arrayList.add(new Model(data.getJSONObject(i)));
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getENStatus(String faStatus) {
        ArrayList<Model> arrayList = getLocalPosition();
        for (int i = 0; i < arrayList.size(); i++) {
            try {
                if (faStatus.equals(arrayList.get(i).get("fa_title")))
                    return (String) arrayList.get(i).get("en_title");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public String getFAStatus(String enStatus) {
        ArrayList<Model> arrayList = getLocalPosition();
        for (int i = 0; i < arrayList.size(); i++) {
            try {
                if (enStatus.equals(arrayList.get(i).get("en_title")))
                    return (String) arrayList.get(i).get("fa_title");
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }


    private void workManager(String work) throws JSONException {
        if (isNetworkConnected(application.getApplicationContext())) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(RoomWorker.class)
                    .setConstraints(constraints)
                    .setInputData(data(work))
                    .build();

            WorkManager.getInstance(application).enqueue(workRequest);
        } else {
            ExceptionGenerator.getException(false, 0, null, "OffLineException");
            workState.setValue(-2);
        }
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null && Objects.requireNonNull(cm.getActiveNetworkInfo()).isConnected();
    }

    private Data data(String work) {
        return new Data.Builder()
                .putString("work", work)
                .build();
    }
}
