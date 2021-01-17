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
import com.majazeh.risloo.Utils.CryptoUtil;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Generators.JSONGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SessionRepository extends MainRepository {
    public static int page = 1;
    public static int practicesPage = 1;
    public static ArrayList<Model> sessions;
    public static ArrayList<Model> practices;
    public static String sessionId = "";
    public static String practiceId = "";
    public static String work = "";
    public static MutableLiveData<Integer> workState;
    public static HashMap createData;
    public static HashMap updateData;
    public static String Q = "";
    public static String report = "";
    public static String encryptionType = "";
    public static String fileTitle = "";
    public static String fileContent = "";
    public static String fileAttachment = "";


    public SessionRepository(Application application) {
        super(application);
        sessions = new ArrayList<>();
        practices = new ArrayList<>();
        workState = new MediatorLiveData<>();
        createData = new HashMap();
        updateData = new HashMap();
        workState.setValue(-1);
    }

    public void sessions(String Q) throws JSONException {
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

    public void practices(String sessionId) throws JSONException {
        SessionRepository.sessionId = sessionId;
        work = "getPractices";
        workState.setValue(-1);
        workManager("getPractices");
    }

    public void create(String roomId, String caseId, String started_at, String duration, String status) throws JSONException {
        createData.clear();
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

    public void SessionsOfCase(String caseId, String Q) throws JSONException {
        CaseRepository.caseId = caseId;
        SessionRepository.Q = Q;
        work = "getSessionsOfCase";
        workState.setValue(-1);
        workManager("getSessionsOfCase");
    }

    public void Report(String sessionId, String report, String encryptionType) throws JSONException {
        SessionRepository.sessionId = sessionId;
        SessionRepository.report = report;
        SessionRepository.encryptionType = encryptionType;
        work = "createReport";
        workState.setValue(-1);
        workManager("createReport");
    }

    public void createPractice(String sessionId, String title, String content, String fileAttachment) throws JSONException {
        SessionRepository.sessionId = sessionId;
        SessionRepository.fileTitle = title;
        SessionRepository.fileContent = content;
        SessionRepository.fileAttachment = fileAttachment;
        work = "createPractice";
        workState.setValue(-1);
        workManager("createPractice");
    }

    public void createHomework(String sessionId, String practiceId, String fileAttachment) throws JSONException {
        SessionRepository.sessionId = sessionId;
        SessionRepository.practiceId = practiceId;
        SessionRepository.fileAttachment = fileAttachment;
        work = "createHomework";
        workState.setValue(-1);
        workManager("createHomework");
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

    public ArrayList<Model> getPractices(String sessionId) {
        try {
            if (FileManager.readObjectFromCache(application.getApplicationContext(), "practices" + "/" + sessionId) != null) {
                ArrayList<Model> arrayList = new ArrayList<>();
                JSONObject jsonObject = FileManager.readObjectFromCache(application.getApplicationContext(), "practices" + "/" + sessionId);
                JSONArray data = jsonObject.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    arrayList.add(new Model(data.getJSONObject(i)));
                }
                return arrayList;
            } else {
                return null;
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

    public ArrayList<Model> getReportType(String key) throws JSONException {
        ArrayList arrayList = new ArrayList<Model>();
        if (!key.equals("")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("fa_title", "رمزگذاری توسط کلید عمومی من");
            jsonObject.put("en_title", "publicKey");
            arrayList.add(new Model(jsonObject));
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fa_title", "بدون رمزنگاری");
        jsonObject.put("en_title", "none");
        arrayList.add(new Model(jsonObject));
        return arrayList;
    }

    public ArrayList<Model> getSessionsOfCase() {
        return sessions;
    }

    public String getENStatus(String faStatus) {
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

    public String getFAStatus(String enStatus) {
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

    public String encrypt(String text, String publicKey) throws InvalidKeySpecException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException {
        return CryptoUtil.encrypt(text, publicKey);
    }

    public String decrypt(String result, String privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        return CryptoUtil.decrypt(result, privateKey);
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
