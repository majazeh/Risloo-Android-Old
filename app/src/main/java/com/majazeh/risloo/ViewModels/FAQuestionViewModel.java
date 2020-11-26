package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.FAQuestionRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class FAQuestionViewModel extends AndroidViewModel {

    // Repositories
    private final FAQuestionRepository repository;

    public FAQuestionViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new FAQuestionRepository(application);
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() throws JSONException {
        return repository.getAll();
    }

}