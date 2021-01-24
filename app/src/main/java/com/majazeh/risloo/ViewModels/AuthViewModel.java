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
    private final AuthRepository repository;

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

    public void attachment(String title, String description, String attachment) throws JSONException {
        repository.attachment(title, description, attachment);
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

    public String getUserName() {
        return repository.getUserName();
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

    public String getPublicKey() {
        return repository.getPublicKey();
    }

    public String getPrivateKey() {
        return repository.getPrivateKey();
    }

    public String getAvatar() {
        return repository.getAvatar();
    }


    public boolean admin() {
        return repository.admin();
    }

    public boolean roomManager(Model data) throws JSONException {
        return repository.roomManager(data);
    }

    public boolean client(Model data) throws JSONException {
        return repository.client(data);
    }

    public boolean centerManager(Model data) throws JSONException {
        return repository.centerManager(data);
    }

    public boolean operator(Model data) throws JSONException {
        return repository.operator(data);
    }

    public boolean openSample(Model data) throws JSONException {
        if (admin() || roomManager(data) || client(data) || centerManager(data) || operator(data)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean openSampleDetail(Model data) throws JSONException {
        if (admin() || roomManager(data) || centerManager(data) || operator(data)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean createSample() {
        if (hasAccess()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean createCenter() {
        if (admin()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean editCenter(Model data) throws JSONException {
        if (admin() ||centerManager(data)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean centerUsers(Model data) throws JSONException {
        if (admin() ||centerManager(data) || operator(data)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean roomUsers(Model data) throws JSONException {
        if (admin() ||roomManager(data) || centerUsers(data) || operator(data)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean addRoomUsers(Model data) throws JSONException {
        if (admin() ||centerManager(data) || operator(data)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean createRoom() {
        if (admin() ||repository.roomAccess()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean createCase() {
        if (hasAccess()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean  caseDetails(Model data) throws JSONException {
        if (operator(data) ||centerManager(data) || roomManager(data)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean  createSession() {
        if (repository.hasAccess()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean report(Model data) throws JSONException {
        if (roomManager(data)) {
            return true;
        } else {
            return false;
        }
    }


}
