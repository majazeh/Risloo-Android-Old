package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.TAConditionRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class TAConditionViewModel extends AndroidViewModel {

    // Repositories
    private final TAConditionRepository repository;

    public TAConditionViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new TAConditionRepository(application);
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() throws JSONException {
        return repository.getAll();
    }

    public ArrayList<Model> getSubset(int index) throws JSONException {
        return repository.getSubset(index);
    }

}