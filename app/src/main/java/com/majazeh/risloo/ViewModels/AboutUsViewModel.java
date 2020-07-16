package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.List;
import com.majazeh.risloo.Models.Repositories.AboutUsRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class AboutUsViewModel extends AndroidViewModel {

    private AboutUsRepository repository;

    public AboutUsViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new AboutUsRepository(application);
    }

    public ArrayList<List> getAll() {
        return repository.getAll();
    }

    public ArrayList<List> getAllSubset(int index) throws JSONException {
        return repository.getAllSubset(index);
    }

}