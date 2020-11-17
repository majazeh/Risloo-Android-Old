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

    public ArrayList<Model> getAll() {
        return repository.getAll();
    }

    public ArrayList<Model> getMy() {
        return repository.getMy();
    }
}
