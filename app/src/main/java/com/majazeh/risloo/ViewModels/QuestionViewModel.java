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
    private QuestionRepository repository;

    public QuestionViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new QuestionRepository(application);
    }

    public ArrayList<Model> getAll() {
        return repository.getAll();
    }

}