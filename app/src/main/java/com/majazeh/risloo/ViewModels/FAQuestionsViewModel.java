package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.FAQuestionsRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class FAQuestionsViewModel extends AndroidViewModel {

    // Repositories
    private final FAQuestionsRepository repository;

    public FAQuestionsViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new FAQuestionsRepository(application);
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() throws JSONException {
        return repository.getAll();
    }

}