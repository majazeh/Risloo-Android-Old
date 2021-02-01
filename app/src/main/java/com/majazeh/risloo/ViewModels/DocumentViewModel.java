package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
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
    public void documents() throws JSONException {
        repository.documents();
    }
    public void sendDocument(String title, String description, String attachment) throws JSONException {
        repository.sendDoc(title, description, attachment);
    }

    public ArrayList<Model> getDocuments(){
        return repository.getDocuments();
    }
}