package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.CenterRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class CenterViewModel extends AndroidViewModel {

    // Repositories
    private CenterRepository repository;

    public CenterViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new CenterRepository(application);
    }

    public void centers() throws JSONException {
        repository.centers();
    }

    public void myCenters() throws JSONException {
        repository.myCenters();
    }

    public void request(String clinicId) throws JSONException {
        repository.request(clinicId);
    }

    public ArrayList<Model> getAll() {
        return repository.getAll();
    }

    public ArrayList<Model> getMy() {
       return repository.getMy();
    }

}