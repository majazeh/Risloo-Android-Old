package com.majazeh.risloo.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.Models.Repositories.CaseRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CaseViewModel extends AndroidViewModel {
    // Repositories
    private CaseRepository repository;

    public CaseViewModel(@NonNull Application application) {
        super(application);

        repository = new CaseRepository(application);

    }

    public void cases(String roomId, String Q) throws JSONException {
        repository.cases(roomId, Q);
    }

    public void general(String caseId) throws JSONException {
        repository.general(caseId);
    }

    public void create(String roomId,ArrayList<String> references,String chiefComplaint) throws JSONException {
        repository.create(roomId,references,chiefComplaint);
    }


    public ArrayList<Model> getCases() {
        return repository.getCases();
    }

    public JSONObject getGeneral(String caseId){return repository.getGeneral(caseId);}

}