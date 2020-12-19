package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.RoomRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class RoomViewModel extends AndroidViewModel {
    // Repositories
    private RoomRepository repository;

    public RoomViewModel(@NonNull Application application) {
        super(application);
        repository = new RoomRepository(application);
    }

    public void rooms(String q) throws JSONException {
        repository.rooms(q);
    }

    public void myRooms(String q) throws JSONException {
        repository.myRooms(q);
    }

    public void getMyManagementRooms(String q) throws JSONException {
        repository.myManagementRooms(q);
    }

    public void references(String roomId, String q) throws JSONException {
        repository.references(roomId, q);
    }

    public void references(String roomId, String q, String usage, String notInCase) throws JSONException {
        repository.references(roomId, q, usage, notInCase);
    }

    public void references(String roomId, String q, String usage) throws JSONException {
        repository.references(roomId, q, usage);
    }

    public void users(String roomId) throws JSONException {
        repository.users(roomId);
    }

    public void addUser(String roomId, ArrayList<String> users) throws JSONException {
        repository.addUser(roomId, users);
    }

        public ArrayList<Model> getAll() {
        return repository.getAll();
    }

    public ArrayList<Model> getMy() {
        return repository.getMy();
    }

    public void addSuggestRoom(Model model) throws JSONException {
        repository.addSuggest(model, getClass().getSimpleName());
    }

    public void addSuggestRoom(Model model, Integer rank) throws JSONException {
        repository.addSuggest(model, rank, getClass().getSimpleName());
    }

    public ArrayList<Model> getSuggestRoom() throws JSONException {
        return repository.getSuggest(getClass().getSimpleName());
    }

    public ArrayList<Model> getUsers(String roomId) {
        return repository.getUsers(roomId);
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

    public ArrayList<Model> getUsersCenters(String roomId) {
        return repository.getUsersCenters(roomId);
    }
}
