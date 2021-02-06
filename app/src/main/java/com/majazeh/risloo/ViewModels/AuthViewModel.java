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

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll() throws JSONException {
        return repository.getAll();
    }

    /*
         ---------- Policies ----------
    */

    public boolean auth() {
        return repository.auth();
    }

    public boolean currentAuth(String id) {
        return repository.currentAuth(id);
    }

    public boolean admin() {
        return repository.admin();
    }

    public boolean centerOwner() {
        return repository.centerOwner();
    }

    public boolean centerOwner(Model model) throws JSONException {
        return repository.centerOwner(model);
    }

    public boolean centerOperator() {
        return repository.centerOperator();
    }

    public boolean centerOperator(Model model) throws JSONException {
        return repository.centerOperator(model);
    }

    public boolean centerManager() {
        return repository.centerManager();
    }

    public boolean centerManager(Model model) throws JSONException {
        return repository.centerManager(model);
    }

    public boolean roomManager() {
        return repository.roomManager();
    }

    public boolean roomManager(Model model) throws JSONException {
        return repository.roomManager(model);
    }

//    public boolean sessionClient() {
//        return repository.sessionClient();
//    }

    public boolean sessionClient(Model model) throws JSONException {
        return repository.sessionClient(model);
    }

    /*
         ---------- Extra Policies ----------
    */

    public boolean psychologist() {
        return repository.psychologist();
    }

    public boolean psychologist(Model model) throws JSONException {
        return repository.psychologist(model);
    }

    public boolean roomAccess() {
        return repository.roomAccess();
    }

    /*
         ---------- Booleans ----------
    */

    public boolean account() {
        return auth();
    }

    public boolean editAccount() {
        return auth();
    }

    public boolean indexCenter() {
        return auth();
    }

    public boolean detailCenter() {
        return auth();
    }

    public boolean createCenter() {
        return admin();
    }

    public boolean editCenter(Model model) throws JSONException {
        return admin() || centerOwner(model);
    }

    public boolean indexCenterUsers(Model model) throws JSONException {
        return admin() || centerOperator(model) || centerManager(model);
    }

    public boolean createCenterUsers(Model model) throws JSONException {
        return admin() || centerOperator(model) || centerManager(model);
    }

    public boolean indexRoom() {
        return auth();
    }

    public boolean detailRoom() {
        return auth();
    }

    public boolean createRoom() {
        return admin() || centerOperator() || centerManager();
    }

    public boolean createRoom(Model model) throws JSONException {
        return admin() || centerOperator(model) || centerManager(model);
    }

    public boolean indexRoomCases(Model model) throws JSONException {
        return true;
    }

    public boolean indexRoomUsers(Model model) throws JSONException {
        return admin() || centerOperator(model) || centerManager(model);
    }

    public boolean createRoomUsers(Model model) throws JSONException {
        return admin() || centerOperator(model) || centerManager(model);
    }

    public boolean indexCase() {
        return auth();
    }

    public boolean detailCase(String id, Model model) throws JSONException {
        return currentAuth(id) || admin() || centerOperator(model) || centerManager(model) || roomManager(model);
    }

    public boolean createCase() {
        return admin() || centerOperator() || centerManager() || roomManager();
    }

    public boolean createCase(Model model) throws JSONException {
        return admin() || centerOperator(model) || centerManager(model) || roomManager(model);
    }

    public boolean editCase(Model model) throws JSONException {
        return admin() || centerOperator(model) || centerManager(model) || roomManager(model);
    }

    public boolean indexCaseUsers(Model model) throws JSONException {
        return admin() || centerOperator(model) || centerManager(model) || roomManager(model);
    }

    public boolean createCaseUsers(Model model) throws JSONException {
        return admin() || centerOperator(model) || centerManager(model) || roomManager(model);
    }

    public boolean indexSession() {
        return auth();
    }

    public boolean detailSession(Model model) throws JSONException {
        return admin() || centerOperator(model) || centerManager(model) || roomManager(model) || sessionClient(model);
    }

    public boolean createSession() {
        return admin() || centerOperator() || centerManager() || roomManager();
    }

    public boolean createSession(Model model) throws JSONException {
        return admin() || centerOperator(model) || centerManager(model) || roomManager(model);
    }

    public boolean editSession(Model model) throws JSONException {
        return admin() || centerOperator(model) || centerManager(model) || roomManager(model) || sessionClient(model);
    }

    /*
         ---------- Extra Booleans ----------
    */

    public boolean indexScale() {
        return auth();
    }

    public boolean indexSample() {
        return auth();
    }

    public boolean detailSample(Model model) throws JSONException {
        return admin() || centerOperator(model) || centerManager(model) || roomManager(model);
    }

    public boolean startSample(Model model) throws JSONException {
        return admin() || centerOperator(model) || centerManager(model) || roomManager(model) || sessionClient(model);
    }

    public boolean createSample(Model model) throws JSONException {
        return admin() || centerOperator(model) || psychologist(model) || centerManager(model) || auth();
    }

    public boolean indexDocument() {
        return auth();
    }

    public boolean sendDocument() {
        return true;
    }

    public boolean indexReport(Model model) throws JSONException {
        return roomManager(model);
    }

    public boolean createReport(Model model) throws JSONException {
        return roomManager(model);
    }

    public boolean indexPractice(Model model) throws JSONException {
        return admin() || roomManager(model) || sessionClient(model);
    }

    public boolean createPractice(Model model) throws JSONException {
        return admin() || roomManager(model);
    }

    /*
         ---------- Strings ----------
    */

    public boolean getIntro() {
        return repository.getIntro();
    }

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

    public void setIntro(boolean intro) {
        repository.setIntro(intro);
    }

    public void setPublicKey(String key) {
        repository.setPublicKey(key);
    }

    public void setPrivateKey(String key) {
        repository.setPrivateKey(key);
    }

}