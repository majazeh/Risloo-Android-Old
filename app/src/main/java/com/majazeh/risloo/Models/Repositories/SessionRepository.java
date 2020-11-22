package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Workers.SessionWorker;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Generators.JSONGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SessionRepository extends MainRepository {
    public static int page = 1;
    public static ArrayList<Model> sessions;
    public static String sessionId = "";
    public static String work = "";
    public static MutableLiveData<Integer> workState;
    public static HashMap createData;
    public static HashMap updateData;
    public static String Q = "";

    public SessionRepository(Application application) {
        super(application);
        sessions = new ArrayList<>();
        workState = new MediatorLiveData<>();
        createData = new HashMap();
        updateData = new HashMap();
        workState.setValue(-1);
    }

    public void sessions( String Q) throws JSONException {
        SessionRepository.Q = Q;
        work = "getAll";
        workState.setValue(-1);
        workManager("getAll");
    }

    public void general(String sessionId) throws JSONException {
        SessionRepository.sessionId = sessionId;
        work = "getGeneral";
        workState.setValue(-1);
        workManager("getGeneral");
    }

    public void create(String roomId, String caseId, String started_at, String duration, String status) throws JSONException {
        if (!roomId.equals(""))
            RoomRepository.roomId = roomId;
        if (!caseId.equals(""))
            createData.put("case_id", caseId);
        if (!started_at.equals(""))
            createData.put("started_at", started_at);
        if (!duration.equals(""))
            createData.put("duration", duration);
        if (!status.equals(""))
            createData.put("status", status);

        work = "create";
        workState.setValue(-1);
        workManager("create");
    }

    public void update(String sessionId, String caseId, String started_at, String duration, String status) throws JSONException {
        if (!sessionId.equals(""))
            SessionRepository.sessionId = sessionId;
        if (!caseId.equals(""))
            updateData.put("case_id", caseId);
        if (!started_at.equals(""))
            updateData.put("started_at", started_at);
        if (!duration.equals(""))
            updateData.put("duration", duration);
        if (!status.equals(""))
            updateData.put("status", status);

        work = "update";
        workState.setValue(-1);
        workManager("update");
    }

    public ArrayList<Model> getLocalSessionStatus() {
        try {
            JSONArray data = new JSONArray(JSONGenerator.getJSON(application.getApplicationContext(), "localSessionStatus.json"));
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

    public ArrayList<Model> getSessions() {
        try {
            if (Q.equals("")) {
                if (FileManager.readObjectFromCache(application.getApplicationContext(), "sessions") != null) {
                    ArrayList<Model> arrayList = new ArrayList<>();
                    JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "sessions");
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        arrayList.add(new Model(data.getJSONObject(i)));
                    }
                    return arrayList;
                } else {
                    return null;
                }
            } else {
                if (sessions.size() == 0) {
                    return null;
                } else {
                    return sessions;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getGeneral(String sessionId) {
        if (FileManager.readObjectFromCache(application.getApplicationContext(), "sessionDetail" + "/" + sessionId) != null)
            return FileManager.readObjectFromCache(application.getApplicationContext(), "sessionDetail" + "/" + sessionId);
        else
            return null;
    }

    public String getENStatus(String faStatus){
        ArrayList<Model> arrayList = getLocalSessionStatus();
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

    public String getFAStatus(String enStatus){
        ArrayList<Model> arrayList = getLocalSessionStatus();
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

            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(SessionWorker.class)
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
