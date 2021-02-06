package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Workers.DocumentWorker;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Generators.JSONGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class DocumentRepository extends MainRepository {

    // Vars
    public static MutableLiveData<Integer> workState;
    public static HashMap<String, String> documentData;
    public static String work = "";
    public static String documentId = "";
    public static String fileTitle = "";
    public static String fileDescription = "";
    public static String fileAttachment = "";

    public DocumentRepository(@NonNull Application application) throws JSONException {
        super(application);

        workState = new MutableLiveData<>();
        documentData = new HashMap<String, String>();
        workState.setValue(-1);
    }

    /*
         ---------- Voids ----------
    */

    public void documents(String Q) throws JSONException {
        work = "getAll";
        workState.setValue(-1);
        workManager("getAll");
    }

    public void send(String title, String description, String attachment) throws JSONException {
        DocumentRepository.fileTitle = title;
        DocumentRepository.fileDescription = description;
        DocumentRepository.fileAttachment = attachment;
        work = "send";
        workState.setValue(-1);
        workManager("send");
    }

    public void docStatus(String documentId, String status) throws JSONException {
        DocumentRepository.documentId = documentId;
        if (!status.equals(""))
            documentData.put("status", status);
        work = "docStatus";
        workState.setValue(-1);
        workManager("docStatus");
    }

    public ArrayList<Model> getDocuments() {
        ArrayList<Model> arrayList = new ArrayList<>();
        if (FileManager.readObjectFromCache(application.getApplicationContext(), "documents") != null) {
            JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "documents");
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

    private void workManager(String work) throws JSONException {
        if (isNetworkConnected(application.getApplicationContext())) {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(DocumentWorker.class)
                    .setConstraints(constraints)
                    .setInputData(data(work))
                    .build();

            WorkManager.getInstance(application).enqueue(workRequest);
        } else {
            ExceptionGenerator.getException(false, 0, null, "OffLineException");
            workState.postValue(-2);
        }
    }

    public ArrayList<Model> getLocalDocumentStatus() {
        try {
            JSONArray data = new JSONArray(JSONGenerator.getJSON(application.getApplicationContext(), "localDocumentStatus.json"));
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
        ArrayList<Model> arrayList = getLocalDocumentStatus();
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
        ArrayList<Model> arrayList = getLocalDocumentStatus();
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