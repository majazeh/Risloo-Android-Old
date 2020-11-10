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
import com.majazeh.risloo.Utils.Managers.FileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class RoomRepository extends MainRepository {
    public static String work = "";
    public static ArrayList<Model> rooms;
    public static ArrayList<Model> myRooms;
    public static String roomQ = "";
    public static int roomsPage = 1;
    public static int myRoomsPage = 1;
    public static MutableLiveData<Integer> workStateSample;
    public static MutableLiveData<Integer> workStateCreate;

    public RoomRepository(Application application) {
        super(application);
        rooms = new ArrayList<>();
        myRooms = new ArrayList<>();
        workStateSample = new MutableLiveData<>();
        workStateCreate = new MutableLiveData<>();
        workStateSample.setValue(-1);
        workStateCreate.setValue(-1);
    }

    public RoomRepository() {
    }

    public void rooms(String q) throws JSONException {
        roomQ = q;

        work = "getRooms";
        workStateCreate.setValue(-1);
        workStateSample.setValue(-1);
        workManager("getRooms");
    }

    public void myRooms(String q) throws JSONException {
        roomQ = q;

        work = "getMyRooms";
        workStateCreate.setValue(-1);
        workStateSample.setValue(-1);
        workManager("getMyRooms");
    }

    public ArrayList<Model> getRooms(){
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
            }else{
                if (rooms.size() == 0){
                    return null;
                }else{
                    return rooms;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Model> getMyRooms(){
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
            }else{
                if (myRooms.size() == 0){
                    return null;
                }else{
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
            workStateSample.setValue(-2);
            workStateCreate.setValue(-2);
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
