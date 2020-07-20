package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Models.Repositories.ExplodeRepository;

public class ExplodeViewModel extends AndroidViewModel {
    ExplodeRepository repository;
    public ExplodeViewModel(@NonNull Application application) {
        super(application);
        repository = new ExplodeRepository(application);
    }
    public void explode(){
        repository.explode();
    }
}
