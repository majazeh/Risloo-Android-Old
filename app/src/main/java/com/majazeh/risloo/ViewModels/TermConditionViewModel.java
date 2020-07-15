package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.TermCondition;
import com.majazeh.risloo.Models.Repositories.TermConditionRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class TermConditionViewModel extends AndroidViewModel {

    private TermConditionRepository repository;

    public TermConditionViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new TermConditionRepository(application);
    }

    public ArrayList<TermCondition> getAll() {
        return repository.getAll();
    }

    public ArrayList<String> getAccounts() {
        return repository.getAccounts();
    }

    public ArrayList<String> getTerms() {
        return repository.getTerms();
    }

}