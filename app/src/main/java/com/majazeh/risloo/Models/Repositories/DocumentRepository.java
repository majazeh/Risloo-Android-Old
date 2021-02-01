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
import com.majazeh.risloo.Models.Items.AuthItems;
import com.majazeh.risloo.Models.Workers.AuthWorker;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class DocumentRepository extends MainRepository {

    // Objects
    private final AuthItems items;

    // Vars
    public static MutableLiveData<Integer> workState;
    public static String work = "";
    public static String fileTitle = "";
    public static String fileDescription = "";
    public static String fileAttachment = "";

    public DocumentRepository(@NonNull Application application) throws JSONException {
        super(application);

        items = new AuthItems(application);

        workState = new MutableLiveData<>();
        workState.setValue(-1);
    }

    /*
         ---------- Voids ----------
    */

    public void documents() throws JSONException {
        work = "documents";
        workState.setValue(-1);
        workManager("documents");
    }

    public void sendDoc(String title, String description, String attachment) throws JSONException {
        DocumentRepository.fileTitle = title;
        DocumentRepository.fileDescription = description;
        DocumentRepository.fileAttachment = attachment;
        work = "attachment";
        workState.setValue(-1);
        workManager("attachment");
    }

    public ArrayList<Model> getDocuments(){
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

            OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(AuthWorker.class)
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