package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.SettingRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class SettingViewModel extends AndroidViewModel {

    // Repositories
    private SettingRepository repository;

    public SettingViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new SettingRepository(application);
    }

    public ArrayList<Model> getAll() {
        return repository.getAll();
    }

}