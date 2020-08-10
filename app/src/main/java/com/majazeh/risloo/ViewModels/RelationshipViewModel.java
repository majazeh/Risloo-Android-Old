package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.RelationshipRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class RelationshipViewModel extends AndroidViewModel {

    // Repositories
    private RelationshipRepository repository;

    public RelationshipViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new RelationshipRepository(application);
    }

    public void request(String clinicId) throws JSONException {
        repository.request(clinicId);
    }

    public void relationships() throws JSONException {
        repository.relationships();
    }

    public void myRelationships() throws JSONException {
        repository.myRelationships();
    }

    public ArrayList<Model> getAll() {
        return repository.getAll();
    }

    public ArrayList<Model> getMy() {
       return repository.getMy();
    }

}