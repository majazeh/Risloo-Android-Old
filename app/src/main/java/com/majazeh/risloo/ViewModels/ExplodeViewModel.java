package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Models.Repositories.ExplodeRepository;
import com.majazeh.risloo.R;

import org.json.JSONException;

public class ExplodeViewModel extends AndroidViewModel {

    // Repositories
    private final ExplodeRepository repository;

    public ExplodeViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new ExplodeRepository(application);
    }

    /*
         ---------- Voids ----------
    */

    public void explode() throws JSONException {
        repository.explode();
    }

    /*
         ---------- Booleans ----------
    */

    public boolean hasUpdate(){
        return repository.hasUpdate();
    }

    public boolean forceUpdate(){
        return repository.forceUpdate();
    }

    /*
         ---------- Strings ----------
    */

    public String currentVersion(){
        return repository.currentVersion();
    }

    public String currentVersionFa() {
        return "نسخه" + " " + currentVersion();
    }

    public String newVersion(){
        return repository.newVersion();
    }

    public String newVersionFa() {
        return "نسخه" + " " + newVersion() + " " + "رسید";
    }

}