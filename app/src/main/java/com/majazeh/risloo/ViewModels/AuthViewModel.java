package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.AuthRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class AuthViewModel extends AndroidViewModel {

    // Repositories
    private AuthRepository repository;

    public AuthViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new AuthRepository(application);
    }

    /*
         ---------- Voids ----------
    */

    public void auth(String authorizedKey) throws JSONException {
        repository.auth(authorizedKey);
    }

    public void authTheory(String password, String code) throws JSONException {
        repository.authTheory(password, code);
    }

    public void register(String name, String mobile, String gender, String password) throws JSONException {
        repository.register(name, mobile, gender, password);
    }

    public void verification() throws JSONException {
        repository.verification();
    }

    public void recovery(String mobile) throws JSONException {
        repository.recovery(mobile);
    }

    public void me() throws JSONException {
        repository.me();
    }

    public void edit(String name, String gender, String birthday) throws JSONException {
        repository.edit(name, gender, birthday);
    }

    public void avatar() throws JSONException {
        repository.avatar();
    }

    public void logOut() throws JSONException {
        repository.logOut();
    }

    public void sendDoc(String title, String description, String filePath) throws JSONException {
        repository.sendDoc(title, description, filePath);
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() throws JSONException {
        return repository.getAll();
    }

    /*
         ---------- Booleans ----------
    */

    public boolean hasAccess() {
        return repository.hasAccess();
    }

    /*
         ---------- Strings ----------
    */

    public String getToken() {
        return repository.getToken();
    }

    public String getUserId() {
        return repository.getUserId();
    }

    public String getName() {
        return repository.getName();
    }

    public String getType() {
        return repository.getType();
    }

    public String getMobile() {
        return repository.getMobile();
    }

    public String getEmail() {
        return repository.getEmail();
    }

    public String getGender() {
        return repository.getGender();
    }

    public String getBirthday() {
        return repository.getBirthday();
    }

    public String getAvatar() {
        return repository.getAvatar();
    }

}