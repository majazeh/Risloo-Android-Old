package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;
import android.icu.text.Edits;
import android.net.ConnectivityManager;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Workers.RoomWorker;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;

public class RoomRepository extends MainRepository {
    public static String work = "";
    public static ArrayList<Model> rooms;
    public static ArrayList<Model> myRooms;
    public static ArrayList<Model> myManagementRooms;
    public static String roomQ = "";
    public static int allPage = 1;
    public static int myPage = 1;
    public static int myManagementPage = 1;
    public static MutableLiveData<Integer> workState;
    public static String roomId = "";
    public static String referencesQ = "";
    public static ArrayList<Model> references;
    public static ArrayList<Model> suggestRoom;
    public static ArrayList<Integer> suggestRoomCount;
    public static String usage= "";


    public RoomRepository(Application application) {
        super(application);
        rooms = new ArrayList<>();
        myRooms = new ArrayList<>();
        myManagementRooms = new ArrayList<>();
        suggestRoomCount = new ArrayList<>();
        suggestRoom = new ArrayList<>();
        references = new ArrayList<>();
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

    public void references(String roomId, String q , String usage) throws JSONException {
        RoomRepository.roomId = roomId;
        RoomRepository.referencesQ = q;
        RoomRepository.usage = usage;
        work = "getReferences";
        workState.setValue(-1);
        workManager("getReferences");
    }

    public void references(String roomId, String q ) throws JSONException {
    references(roomId,q,"");
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
