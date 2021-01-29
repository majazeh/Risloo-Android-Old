package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MediatorLiveData;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.CaseRepository;
import com.majazeh.risloo.Models.Repositories.RoomRepository;
import com.majazeh.risloo.Models.Repositories.SessionRepository;
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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SessionViewModel extends AndroidViewModel {

    // Repositories
    private SessionRepository repository;

    public SessionViewModel(@NonNull Application application) {
        super(application);
        repository = new SessionRepository(application);

    }

    public void sessions(String Q) throws JSONException {
        repository.sessions(Q);
    }

    public void general(String sessionId) throws JSONException {
        repository.general(sessionId);
    }

    public void practices(String sessionId) throws JSONException {
        repository.practices(sessionId);
    }

    public void create(String roomId, String caseId, String started_at, String duration, String status) throws JSONException {
        repository.create(roomId, caseId, started_at, duration, status);
    }

    public void createPractice(String sessionId, String title, String content, String fileAttachment) throws JSONException {
        repository.createPractice(sessionId, title, content, fileAttachment);
    }

    public void createHomework(String sessionId, String practiceId, String fileAttachment) throws JSONException {
        repository.createHomework(sessionId, practiceId, fileAttachment);
    }

    public void update(String sessionId, String caseId, String started_at, String duration, String status) throws JSONException {
        repository.update(sessionId, caseId, started_at, duration, status);
    }

    public void SessionsOfCase(String caseId, String q) throws JSONException {
        repository.SessionsOfCase(caseId, q);
    }

    public void Report(String sessionId, String report, String encryptionType) throws JSONException {
        repository.Report(sessionId, report, encryptionType);
    }

    public String encrypt(String text, String publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException {
        return repository.encrypt(text, publicKey);
    }

    public String decrypt(String result, String privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        return repository.decrypt(result, privateKey);
    }

    public ArrayList<Model> getLocalSessionStatus() {
        return repository.getLocalSessionStatus();
    }

    public ArrayList<Model> getPractices(String sessionId) {
        return repository.getPractices(sessionId);
    }

    public ArrayList<Model> getSessions() {
        return repository.getSessions();
    }

    public JSONObject getGeneral(String sessionId) {
        return repository.getGeneral(sessionId);
    }

    public ArrayList<Model> getSessionsOfCase() {
        return repository.getSessionsOfCase();
    }

    public ArrayList<Model> getReportType(String key) throws JSONException {
        return repository.getReportType(key);
    }

    public String getENStatus(String faStatus) {
        return repository.getENStatus(faStatus);
    }

    public String getFAStatus(String enStatus) {
        return repository.getFAStatus(enStatus);
    }


}
