package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.SampleRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SampleViewModel extends AndroidViewModel {

    // Repositories
    private SampleRepository repository;

    public SampleViewModel(@NonNull Application application) {
        super(application);

        repository = new SampleRepository(application);
    }

    public void sendAnswers(String sampleId) throws JSONException {
        repository.sendAnswers(sampleId);
    }

    public String getTitle() throws JSONException {
        return repository.json().getString("title");
    }

    public String getDescription() throws JSONException {
        return repository.json().getString("description");
    }

    public ArrayList getItems() {
        return repository.items().items();
    }

    public Model getItem(int index) {
        return repository.items().item(index);
    }

    public Model getNext() {
        return repository.items().next();
    }

    public Model getPrev() {
        return repository.items().prev();
    }

    public Model goToIndex(int index) {
        return repository.items().goToIndex(index);
    }

    public void setIndex(int index) {
        repository.items().setIndex(index);
    }

    public int getIndex() {
        return repository.items().getIndex();
    }

    public int getSize() {
        return repository.items().size();
    }

    public ArrayList<String> getOptions(int index) throws JSONException {
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < getAnswer(index).getJSONArray("options").length(); i++) {
            arrayList.add((String) getAnswer(index).getJSONArray("options").get(i));
        }
        return arrayList;
    }

    public JSONObject getAnswer(int index) throws JSONException {
        return (JSONObject) repository.items().item(index).get("answer");
    }

    /*
         ---------- Insert ----------
    */

    public void insertToLocal(int index, int answer) {
        repository.insertToLocal(index, answer);
    }

    /*
         ---------- Save ----------
    */

    public void saveToExternal(JSONArray jsonArray, String fileName) {
        repository.saveToExternal(jsonArray, fileName);
    }

    public void saveAnswerToCache(JSONArray jsonArray, String fileName) {
        repository.saveAnswerToCache(jsonArray,  fileName);
    }

    /*
         ---------- Read ----------
    */

    public JSONArray readAnswerFromCache(String fileName) {
        return repository.readAnswerFromCache( fileName);
    }

    /*
         ---------- Storage ----------
    */

    public boolean hasStorage(String fileName) {
        return repository.hasStorage( fileName);
    }

    public void checkStorage(String fileName) {
        repository.checkStorage(fileName);
    }

    public void deleteStorage(String fileName) {
        repository.deleteStorage(fileName);
    }

    public ArrayList<Model> getStorageFiles() {
        return repository.storageFiles();
    }

    /*
         ---------- Answer ----------
    */

    public int answeredPosition(String fileName, int index) {
        return repository.answeredPosition(fileName, index);
    }

    public int answeredSize(String fileName) {
        return repository.answeredSize(fileName);
    }

    public int firstUnanswered(String fileName) {
        return repository.firstUnanswered( fileName);
    }

}