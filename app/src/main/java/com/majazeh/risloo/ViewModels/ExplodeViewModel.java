package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.majazeh.risloo.Models.Repositories.ExplodeRepository;

import org.json.JSONException;

public class ExplodeViewModel extends AndroidViewModel {

    // Repositories
    private ExplodeRepository repository;

    public ExplodeViewModel(@NonNull Application application) {
        super(application);

        repository = new ExplodeRepository(application);
    }

    public void explode() throws JSONException {
        repository.workManager("explode");
    }

    public boolean hasUpdate(){
        return repository.hasUpdate();
    }

    public String version(){
        return repository.version();
    }

}