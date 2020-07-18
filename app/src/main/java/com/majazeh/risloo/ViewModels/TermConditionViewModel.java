package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.List;
import com.majazeh.risloo.Models.Repositories.TermConditionRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class TermConditionViewModel extends AndroidViewModel {

    private TermConditionRepository repository;

    public TermConditionViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new TermConditionRepository(application);
    }

    public ArrayList<List> getAll() {
        return repository.getAll();
    }

    public ArrayList<List> getAllSubset(int index) throws JSONException {
        return repository.getAllSubset(index);
    }

}