package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.More;
import com.majazeh.risloo.Models.Repositories.MoreRepository;

import java.util.ArrayList;

public class MoreViewModel extends AndroidViewModel {

    private MoreRepository repository;

    public MoreViewModel(@NonNull Application application) {
        super(application);

        repository = new MoreRepository(application);
    }

    public ArrayList<More> getAll() {
        return repository.getAll();
    }

}