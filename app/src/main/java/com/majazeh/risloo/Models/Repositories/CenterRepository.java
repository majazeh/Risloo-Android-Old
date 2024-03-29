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
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Generators.JSONGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;
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
    public static HashMap addUserData = new HashMap();
    public static ArrayList<Model> getAll;
    public static ArrayList<Model> getMy;
    public static ArrayList<Model> personalClinic;
    public static ArrayList<Model> counselingCenter;
    public static ArrayList<Model> users;
    public static MutableLiveData<Integer> workState;
    public static String work = "";
    public static String clinicId = "";
    public static String position = "";
    public static String usersQ = "";
    public static String personalClinicQ = "";
    public static String counselingCenterQ = "";
    public static int usersPage = 1;
    public static int allPage = 1;
    public static int myPage = 1;
    public static String search = "";
    public static String userId = "";
    public static String status = "";

    public CenterRepository(Application application) throws JSONException {
        super(application);

        createData = new HashMap();
        editData = new HashMap();
        getAll = new ArrayList<>();
        getMy = new ArrayList<>();
        personalClinic = new ArrayList<>();
        counselingCenter = new ArrayList<>();
        users = new ArrayList<>();
        workState = new MutableLiveData<>();
        workState.setValue(-1);
    }

    /*
         ---------- Voids ----------
    */

    public void centers(String q) throws JSONException {
        search = q;
        work = "getAll";
        workState.setValue(-1);
        workManager("getAll");
    }

    public void myCenters(String q) throws JSONException {
        search = q;
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

    public void users(String clinicId) throws JSONException {
        CenterRepository.clinicId = clinicId;
        work = "users";
        workState.setValue(-1);
        workManager("users");
    }

    public void userStatus(String clinicId, String userId, String status) throws JSONException {
        CenterRepository.clinicId = clinicId;
        CenterRepository.userId = userId;
        CenterRepository.status = status;
        work = "userStatus";
        workState.setValue(-1);
        workManager("userStatus");
    }

    public void userPosition(String clinicId, String userId, String position) throws JSONException {
        CenterRepository.clinicId = clinicId;
        CenterRepository.userId = userId;
        CenterRepository.position = position;
        work = "userPosition";
        workState.setValue(-1);
        workManager("userPosition");
    }

    public void references(String roomId, String q) throws JSONException {
        RoomRepository.roomId = roomId;
        CenterRepository.usersQ = q;
        work = "getReferences";
        workState.setValue(-1);
        workManager("getReferences");
    }

    public void addUser(String clinicId, String number, String roomId, String position, int create_case, String nickname) throws JSONException {
        if (!clinicId.equals(""))
            CenterRepository.clinicId = clinicId;
        if (!number.equals(""))
            addUserData.put("mobile", number);
        if (!roomId.equals(""))
            addUserData.put("room_id",roomId);
        if (!position.equals(""))
            addUserData.put("position", position);
        if (create_case != 0)
            addUserData.put("create_case", create_case);
        if (!nickname.equals(""))
            addUserData.put("nickname", nickname);
        work = "addUser";
        workState.setValue(-1);
        workManager("addUser");
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
        if (search.equals("")) {
            ArrayList<Model> arrayList = new ArrayList<>();
            if (FileManager.readObjectFromCache(application.getApplicationContext(), "centers" + "/" + "all") != null) {
                JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "centers" + "/" + "all");
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
        } else {
            if (isNetworkConnected(application.getApplicationContext())) {
                if (getAll.size() == 0) {
                    return null;
                } else {
                    return getAll;
                }
            } else {
                if (FileManager.readObjectFromCache(application.getApplicationContext(), "centers" + "/" + "all") != null) {
                    JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "centers" + "/" + "all");
                    try {
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data.length() == 0) {
                            return null;
                        }
                        for (int i = 0; i < data.length(); i++) {
                            getAll.add(new Model(data.getJSONObject(0)));
                        }
                        if (getAll.size() == 0) {
                            return null;
                        } else {
                            return getAll;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }
    }

    public ArrayList<Model> getMy() {
        if (search.equals("")) {
            ArrayList<Model> arrayList = new ArrayList<>();
            if (FileManager.readObjectFromCache(application.getApplicationContext(), "centers" + "/" + "my") != null) {
                JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "centers" + "/" + "my");
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
        } else {
            if (isNetworkConnected(application.getApplicationContext())) {
                if (getMy.size() == 0) {
                    return null;
                } else {
                    return getMy;
                }
            } else {
                if (FileManager.readObjectFromCache(application.getApplicationContext(), "centers" + "/" + "my") != null) {
                    JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "centers" + "/" + "my");
                    try {
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data.length() == 0) {
                            return null;
                        }
                        for (int i = 0; i < data.length(); i++) {
                            getMy.add(new Model(data.getJSONObject(0)));
                        }
                        if (getMy.size() == 0) {
                            return null;
                        } else {
                            return getMy;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    return null;
                }
            }
        }
    }

    public ArrayList<Model> getUsers(String clinicId) {
        ArrayList<Model> arrayList = new ArrayList<>();
        if (FileManager.readObjectFromCache(application.getApplicationContext(), "centerUsers" + "/" + clinicId) != null) {
            JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "centerUsers" + "/" + clinicId);
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
            ExceptionGenerator.getException(false, 0, null, "OffLineException");
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