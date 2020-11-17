package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Workers.CaseWorker;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CaseRepository extends MainRepository {
    public static int page = 1;
    public static ArrayList<Model> cases;
    public static String caseId = "";
    public static String work = "";
    public static MutableLiveData<Integer> workState;
    public static HashMap createData;
    public static String Q = "";


    public CaseRepository(Application application) {
        super(application);
        cases = new ArrayList<>();
        workState = new MediatorLiveData<>();
        createData = new HashMap();
        workState.setValue(-1);
    }

    public void cases() throws JSONException {
        work = "getAll";
        workState.setValue(-1);
        workManager("getAll");
    }

    public void general(String caseId) throws JSONException {
        CaseRepository.caseId = caseId;
        work = "getGeneral";
        workState.setValue(-1);
        workManager("getGeneral");
    }

    public void create(String roomId,ArrayList<String> references,String chiefComplaint) throws JSONException {
    if (!roomId.equals(""))
        RoomRepository.roomId = roomId;
    if (!chiefComplaint.equals(""))
        createData.put("chief_complaint",chiefComplaint);
    if (references.size()!= 0)
        createData.put("client_id", references);
        work = "create";
        workState.setValue(-1);
        workManager("create");
    }

    public ArrayList<Model> getCases(){
        try {
            if (Q.equals("") && RoomRepository.roomId.equals("")) {
                if (FileManager.readObjectFromCache(application.getApplicationContext(), "cases") != null) {
                    ArrayList<Model> arrayList = new ArrayList<>();
                    JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "cases");
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        arrayList.add(new Model(data.getJSONObject(i)));
                    }
                    return arrayList;
                } else {
                    return null;
                }
            }else{
                if (cases.size() == 0){
                    return null;
                }else{
                    return cases;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    public JSONObject getGeneral(String caseId){
        if (FileManager.readObjectFromCache(application.getApplicationContext(), "caseDetail" + "/" + caseId) != null)
        return FileManager.readObjectFromCache(application.getApplicationContext(), "caseDetail" + "/" + caseId);
        else
            return null;
    }

    private void workManager(String work) throws JSONException {
        if (isNetworkConnected(application.getApplicationContext())) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(CaseWorker.class)
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
