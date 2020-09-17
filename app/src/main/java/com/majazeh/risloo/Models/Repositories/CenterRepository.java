package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Managers.FileManager;
import com.majazeh.risloo.Models.Workers.CenterWorker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CenterRepository extends MainRepository {

    // Vars
    public static MutableLiveData<Integer> workState;
    public static String work = "";
    public static String clinicId = "";
    public static HashMap create = new HashMap();
    public static ArrayList<Model> personal_clinic = new ArrayList<>();
    public static ArrayList<Model> counseling_center = new ArrayList<>();
    public static int allPage = 1;
    public static int myPage = 1;

    public CenterRepository(Application application) throws JSONException {
        super(application);

        workState = new MutableLiveData<>();
        workState.setValue(-1);
    }

    /*
         ---------- Voids ----------
    */

    public void centers() throws JSONException {
        work = "getAll";
        workState.setValue(-1);
        workManager("getAll");
    }

    public void myCenters() throws JSONException {
        work = "getMy";
        workState.setValue(-1);
        workManager("getMy");
    }

    public void request(String clinicId) throws JSONException {
        CenterRepository.clinicId = clinicId;

        work = "request";
        workState.setValue(-1);
        workManager("request");
    }

    public void create(String type, String manager_id, String title, String avatar, String address, String description, ArrayList phone_numbers) throws JSONException {
        if (!type.equals("")) {
            CenterRepository.create.put("type", type);
        }
        if (!manager_id.equals("")) {
            CenterRepository.create.put("manager_id", manager_id);
        }
        if (!title.equals("")) {
            CenterRepository.create.put("title", title);
        }
        if (!avatar.equals("")) {
            CenterRepository.create.put("avatar", avatar);
        }
        if (!address.equals("")) {
            CenterRepository.create.put("address", address);
        }
        if (!description.equals("")) {
            CenterRepository.create.put("description", description);
        }
        if (phone_numbers.size() != 0) {
            CenterRepository.create.put("phone_numbers", phone_numbers);
        }

        work = "create";
        workState.setValue(-1);
        workManager("create");
    }

    public void personal_clinic() throws JSONException {
        work = "personal_clinic";
        workState.setValue(-1);
        workManager("personal_clinic");
    }

    public void counseling_center() throws JSONException {
        work = "counseling_center";
        workState.setValue(-1);
        workManager("counseling_center");
    }
    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() {
        ArrayList<Model> arrayList = new ArrayList<>();
        if (FileManager.readObjectFromCache(application.getApplicationContext(), "centers", "all") != null) {
            JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "centers", "all");
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

    public ArrayList<Model> getMy() {
        ArrayList<Model> arrayList = new ArrayList<>();
        if (FileManager.readObjectFromCache(application.getApplicationContext(), "centers", "my") != null) {
            JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "centers", "my");
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

    /*
         ---------- Work ----------
    */

    private void workManager(String work) throws JSONException {
        if (isNetworkConnected(application.getApplicationContext())) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(CenterWorker.class)
                    .setConstraints(constraints)
                    .setInputData(data(work))
                    .build();

            WorkManager.getInstance(application).enqueue(workRequest);
        } else {
            ExceptionManager.getException(0, null, false, "OffLine", "center");
            workState.postValue(-2);
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