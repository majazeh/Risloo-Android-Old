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

import java.util.ArrayList;
import java.util.HashMap;

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

    public void create(String roomId, String caseId, String started_at, String duration, String status) throws JSONException {
        repository.create(roomId, caseId, started_at, duration, status);
    }

    public void update(String sessionId, String caseId, String started_at, String duration, String status) throws JSONException {
        repository.update(sessionId, caseId, started_at, duration, status);
    }

    public ArrayList<Model> getLocalSessionStatus() {
        return repository.getLocalSessionStatus();
    }

    public ArrayList<Model> getSessions() {
        return repository.getSessions();
    }

    public JSONObject getGeneral(String sessionId) {
        return repository.getGeneral(sessionId);
    }


}
