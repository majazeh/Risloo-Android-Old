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

    public void centers(String q) throws JSONException {
        repository.centers(q);
    }

    public void myCenters(String q) throws JSONException {
        repository.myCenters(q);
    }

    public void request(String clinicId) throws JSONException {
        repository.request(clinicId);
    }

    public void users(String clinicId) throws JSONException {
        repository.users(clinicId);
    }

    public void create(String type, String manager, String title, String avatar, String address, String description, ArrayList phones) throws JSONException {
        repository.create(type, manager, title, avatar, address, description, phones);
    }

    public void edit(String id, String manager, String title, String description, String address, ArrayList phones) throws JSONException {
        repository.edit(id, manager, title, description, address, phones);
    }

    public void personalClinic(String q) throws JSONException {
        repository.personalClinic(q);
    }

    public void counselingCenter(String q) throws JSONException {
        repository.counselingCenter(q);
    }

    public void userStatus(String clinicId, String userId, String status) throws JSONException {
        repository.userStatus(clinicId, userId, status);
    }
    public void references(String roomId) throws JSONException {
        repository.references(roomId);
    }

        public void addUser(String clinicId, String number, String position) throws JSONException {
        repository.addUser(clinicId, number, position);
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

    public ArrayList<Model> getUsers(String clinicId) {
        return repository.getUsers(clinicId);
    }

    public ArrayList<Model> getLocalPosition() {
        return repository.getLocalPosition();
    }


    public String getENPosition(String faStatus) {
        return repository.getENStatus(faStatus);
    }

    public String getFAPosition(String enStatus){
        return repository.getFAStatus(enStatus);
    }
}