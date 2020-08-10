package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.ReservationRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReservationViewModel extends AndroidViewModel {
    private ReservationRepository repository;

    public ReservationViewModel(@NonNull Application application) {
        super(application);
        repository = new ReservationRepository(application);
    }

    public MutableLiveData<Integer> getWorkState() {
        return repository.getWorkState();
    }

    public String getException() {
        return repository.getException();
    }

    public void getReservation() throws JSONException {
        repository.getReservation();
    }

    public void getMyReservation() throws JSONException {
        repository.getMyReservation();
    }

    public void request(String clinic_id) throws JSONException {
        repository.request(clinic_id);
    }

    public JSONObject getReservationFromCache() {
        return repository.readSampleFromCache("reservation");
    }

    public JSONObject getMyReservationFromCache() {
        return repository.readSampleFromCache("myReservation");
    }

    public ArrayList<Model> getList() {
        ArrayList<Model> arrayList = new ArrayList<>();
        JSONObject jsonObject = repository.readSampleFromCache("reservation");
        try {
            JSONArray data = jsonObject.getJSONArray("data");
            if (data.length() == 0) {
                return null;
            }
            for (int i = 0; i < data.length(); i++) {
                Model model = new Model(data.getJSONObject(i));
                arrayList.add(model);
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Model> getMyList() {
        ArrayList<Model> arrayList = new ArrayList<>();
        JSONObject jsonObject = repository.readSampleFromCache("myReservation");
        try {
            JSONArray data = jsonObject.getJSONArray("data");
            if (data.length() == 0) {
                return null;
            }
            for (int i = 0; i < data.length(); i++) {
                Model model = new Model(data.getJSONObject(i));
                arrayList.add(model);
            }
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
