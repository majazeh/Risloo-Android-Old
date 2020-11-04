package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.AboutUsRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class AboutUsViewModel extends AndroidViewModel {

    // Repositories
    private final AboutUsRepository repository;

    public AboutUsViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new AboutUsRepository(application);
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