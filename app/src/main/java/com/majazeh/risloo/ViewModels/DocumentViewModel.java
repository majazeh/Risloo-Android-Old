package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.DocumentRepository;

import org.json.JSONException;

import java.util.ArrayList;

public class DocumentViewModel extends AndroidViewModel {

    // Repositories
    private final DocumentRepository repository;

    public DocumentViewModel(@NonNull Application application) throws JSONException {
        super(application);

        repository = new DocumentRepository(application);
    }

    /*
         ---------- Voids ----------
    */

    public void documents(String Q) throws JSONException {
        repository.documents(Q);
    }

    public void send(String title, String description, String attachment) throws JSONException {
        repository.send(title, description, attachment);
    }

    public String getENStatus(String faStatus) {
        return repository.getENStatus(faStatus);
    }

    public String getFAStatus(String enStatus) {
        return repository.getFAStatus(enStatus);
    }


    public ArrayList<Model> getDocuments(){
        return repository.getDocuments();
    }

}