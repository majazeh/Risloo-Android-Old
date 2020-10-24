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
import com.majazeh.risloo.Utils.ExceptionManager;
import com.majazeh.risloo.Utils.FileManager;
import com.majazeh.risloo.Models.Workers.CenterWorker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CenterRepository extends MainRepository {

    // Vars
    public static HashMap createData = new HashMap();
    public static HashMap editData = new HashMap();
    public static ArrayList<Model> personalClinic;
    public static ArrayList<Model> counselingCenter;
    public static ArrayList<Model> personalClinicSearch;
    public static ArrayList<Model> counselingCenterSearch;
    public static MutableLiveData<Integer> workState;
    public static String work = "";
    public static String clinicId = "";
    public static String personalClinicQ = "";
    public static String counselingCenterQ = "";
    public static int allPage = 1;
    public static int myPage = 1;

    public CenterRepository(Application application) throws JSONException {
        super(application);

        createData = new HashMap();
        editData = new HashMap();
        personalClinic = new ArrayList<>();
        counselingCenter = new ArrayList<>();
        personalClinicSearch = new ArrayList<>();
        counselingCenterSearch = new ArrayList<>();
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

    public void create(String type, String manager, String title, String avatar, String address, String description, ArrayList<String> phones) throws JSONException {
        if (!type.equals(""))
            CenterRepository.createData.put("type", type);
        if (!manager.equals(""))
            CenterRepository.createData.put("manager_id", manager);
        if (!title.equals(""))
            CenterRepository.createData.put("title", title);
        if (!avatar.equals(""))
            CenterRepository.createData.put("avatar", avatar);
        if (!description.equals(""))
            CenterRepository.createData.put("description", description);
        if (!address.equals(""))
            CenterRepository.createData.put("address", address);
        if (phones.size() != 0)
            CenterRepository.createData.put("phone_numbers", phones);

        work = "create";
        workState.setValue(-1);
        workManager("create");
    }

    public void edit(String id, String manager, String title, String description, String address, ArrayList phones) throws JSONException {
        CenterRepository.editData.put("id", id);

        if (!manager.equals(""))
            CenterRepository.editData.put("manager_id", manager);
        if (!title.equals(""))
            CenterRepository.editData.put("title", title);
        if (!description.equals(""))
            CenterRepository.editData.put("description", description);
        if (!address.equals(""))
            CenterRepository.editData.put("address", address);
        if (phones.size() != 0)
            CenterRepository.editData.put("phone_numbers", phones);

        work = "edit";
        workState.setValue(-1);
        workManager("edit");
    }

    public void personalClinic(String q) throws JSONException {
        CenterRepository.personalClinicQ = q;

        work = "getPersonalClinic";
        workState.setValue(-1);
        workManager("getPersonalClinic");
    }

    public void counselingCenter(String q) throws JSONException {
        CenterRepository.counselingCenterQ = q;

        work = "getCounselingCenter";
        workState.setValue(-1);
        workManager("getCounselingCenter");
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
            ExceptionManager.getException(false, 0, null, "OffLineException", "center");
            workState.postValue(-2);
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