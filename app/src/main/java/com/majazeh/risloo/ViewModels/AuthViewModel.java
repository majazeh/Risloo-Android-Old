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

    public void documents() throws JSONException {
        repository.documents();
    }

    public void logOut() throws JSONException {
        repository.logOut();
    }

    public void sendDocument(String title, String description, String attachment) throws JSONException {
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

    public boolean admin() {
        return repository.admin();
    }

    public boolean operator(Model model) throws JSONException {
        return repository.operator(model);
    }

    public boolean psychologist(Model model) throws JSONException {
        return repository.psychologist(model);
    }

    public boolean centerManager(Model model) throws JSONException {
        return repository.centerManager(model);
    }

    public boolean centerOwner(Model model) throws JSONException {
        return repository.centerOwner(model);
    }

    public boolean roomManager(Model model) throws JSONException {
        return repository.roomManager(model);
    }

    public boolean client(Model model) throws JSONException {
        return repository.client(model);
    }

    public boolean roomAccess() {
        return repository.roomAccess();
    }

    public boolean counselingCenter() {
        return true;
    }

    public boolean personalClinic() {
        return true;
    }

    public boolean openSample(Model model) throws JSONException {
        return admin() || operator(model) || centerManager(model) || roomManager(model) || client(model);
    }

    public boolean openDetailSample(Model model) throws JSONException {
        return admin() || operator(model) || centerManager(model) || roomManager(model);
    }

    public boolean openDetailCase(Model model) throws JSONException {
        return admin() || operator(model) || centerManager(model) || roomManager(model);
    }

    public boolean openDetailSession(Model model) throws JSONException {
        return admin() || true;
    }

    public boolean showDrawerItems() {
        return hasAccess();
    }

    public boolean showCenterUsers(Model model) throws JSONException {
        return admin() || operator(model) || centerManager(model);
    }

    public boolean showRoomUsers(Model model) throws JSONException {
        return admin() || operator(model) || centerManager(model) || roomManager(model);
    }

    public boolean showReports(Model model) throws JSONException {
        return roomManager(model);
    }

    public boolean showPractices(Model model) throws JSONException {
        return admin() || roomManager(model) || client(model);
    }

    public boolean createSample(Model model) throws JSONException {
        return admin() || operator(model) || psychologist(model) || centerManager(model) || hasAccess();
    }

    public boolean createCenter() {
        return admin();
    }

    public boolean createRoom() {
        return admin() || roomAccess() || counselingCenter();
    }

    public boolean createCase(Model model) throws JSONException {
        return admin() || operator(model) || psychologist(model) || centerManager(model);
    }

    public boolean createSession() {
        return admin() || hasAccess();
    }

    public boolean createReport(Model model) throws JSONException {
        return roomManager(model);
    }

    public boolean createPractice(Model model) throws JSONException {
        return admin() || roomManager(model);
    }

    public boolean createCenterUsers(Model model) throws JSONException {
        return admin() || true;
    }

    public boolean createRoomUsers(Model model) throws JSONException {
        return admin() || operator(model) || centerManager(model);
    }

    public boolean createCaseUsers(Model model) throws JSONException {
        return admin() || true;
    }

    public boolean editCenter(Model model) throws JSONException {
        return admin() || centerManager(model);
    }

    public boolean editCase(Model model) throws JSONException {
        return admin() || true;
    }

    public boolean editSession(Model model) throws JSONException {
        return admin() || operator(model) || centerManager(model) || roomManager(model);
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

    public String getAvatar() {
        return repository.getAvatar();
    }

    public String getPublicKey() {
        return repository.getPublicKey();
    }

    public String getPrivateKey() {
        return repository.getPrivateKey();
    }

    public void setPublicKey(String key) {
        repository.setPublicKey(key);
    }

    public void setPrivateKey(String key) {
        repository.setPrivateKey(key);
    }

}