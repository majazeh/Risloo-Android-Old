package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.QuestionRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class QuestionViewModel extends AndroidViewModel {

    // Repositories
    private final QuestionRepository repository;

    public QuestionViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new QuestionRepository(application);
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() throws JSONException {
        return repository.getAll();
    }

}