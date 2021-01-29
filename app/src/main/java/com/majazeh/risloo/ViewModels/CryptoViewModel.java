package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.CryptoRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class CryptoViewModel extends AndroidViewModel {

    // Repositories
    private final CryptoRepository repository;

    public CryptoViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new CryptoRepository(application);
    }

    /*
         ---------- Arrays ----------
    */

    public ArrayList<Model> getAll(String type) throws JSONException {
        return repository.getAll(type);
    }

    public ArrayList<Model> getSubset(String type, int index) throws JSONException {
        return repository.getSubset(type, index);
    }

}