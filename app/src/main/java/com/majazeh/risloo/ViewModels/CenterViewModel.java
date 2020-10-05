package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.CenterRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class CenterViewModel extends AndroidViewModel {

    // Repositories
    private CenterRepository repository;

    public CenterViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new CenterRepository(application);
    }

    /*
         ---------- Voids ----------
    */

    public void centers() throws JSONException {
        repository.centers();
    }

    public void myCenters() throws JSONException {
        repository.myCenters();
    }

    public void request(String clinicId) throws JSONException {
        repository.request(clinicId);
    }

    public void create(String type, String manager, String title, String avatar, String address, String description, ArrayList phones) throws JSONException {
        repository.create(type, manager, title, avatar, address, description, phones);
    }

    public void edit(String id, String manager, String title, String description, String address, ArrayList phones) throws JSONException {
        repository.edit(id, manager, title, description, address, phones);
    }

    public void personalClinic() throws JSONException {
        repository.personalClinic();
    }

    public void counselingCenter() throws JSONException {
        repository.counselingCenter();
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() {
        return repository.getAll();
    }

    public ArrayList<Model> getMy() {
        return repository.getMy();
    }

}