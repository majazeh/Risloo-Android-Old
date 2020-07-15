package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.AboutUs;
import com.majazeh.risloo.Models.Repositories.AboutUsRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class AboutUsViewModel extends AndroidViewModel {

    private AboutUsRepository repository;

    public AboutUsViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new AboutUsRepository(application);
    }

    public ArrayList<AboutUs> getAll() {
        return repository.getAll();
    }

    public ArrayList<String> getFacilities() {
        return repository.getFacilities();
    }

}