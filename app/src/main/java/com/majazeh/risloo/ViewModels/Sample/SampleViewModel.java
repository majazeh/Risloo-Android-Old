package com.majazeh.risloo.ViewModels.Sample;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.majazeh.risloo.Entities.Sample;
import com.majazeh.risloo.Models.Repositories.Sample.SampleRepository;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;

public class SampleViewModel extends AndroidViewModel {

    private SampleRepository repository;

    public SampleViewModel(@NonNull Application application, String testUniqueId) throws JSONException {
        super(application);

        repository = new SampleRepository(application, testUniqueId);
    }

    public void insertToLocalData(int item, int answer) {
        repository.insertToLocalData(item, answer);
    }

//    public void insertToRemoteData(int item, int answer) {
//        repository.insertToRemoteData(item, answer);
//    }

    public void insertRemoteDataToLocalData() {
        repository.insertRemoteDataToLocalData();
    }

    public void writeToCache(JSONArray jsonArray, String fileName) {
        repository.writeToCache(jsonArray, fileName);
    }

    public JSONArray readFromCache(String fileName) {
        return repository.readFromCache(fileName);
    }

    public boolean saveToCSV(JSONArray jsonArray, String fileName) {
        return repository.saveToCSV(jsonArray, fileName);
    }

    public File loadFromCSV(String fileName) {
        return repository.loadFromCSV(fileName);
    }

//    public ArrayList getLocalData(){
//        return repository.items().localData();
//    }

   // public ArrayList getRemoteData(){
//        return repository.items().remoteData();
//    }

    public String getTitle() throws JSONException {
        return repository.json().getString("title");
    }

    public String getDescription() throws JSONException {
        return repository.json().getString("description");
    }

    public int getVersion() throws JSONException {
        return repository.json().getInt("version");
    }

    public String getEdition() throws JSONException {
        return repository.json().getString("edition");
    }

    public String getEditionVersion() throws JSONException {
        return repository.json().getString("edition_version");
    }

    public String getFiller() throws JSONException {
        return repository.json().getString("filler");
    }

    public Sample getItem(int index) {
        return repository.items().item(index);
    }

    public Sample getNext() {
        return repository.items().next();
    }

    public Sample getPrev() {
        return repository.items().prev();
    }

    public Sample goToIndex(int index) {
        return repository.items().goToIndex(index);
    }

    public int getCurrentIndex() {
        return repository.items().currentIndex();
    }

    public int getSize() {
        return repository.items().size();
    }

    public MutableLiveData<ArrayList<ArrayList<Integer>>> getLocalData(){
        return repository.localData();
    }
    public ArrayList<ArrayList<Integer>> getRemoteData(){
        return repository.remoteData();
    }

    public boolean inProgress(){
        return repository.inProgress;
    }

    public void process() throws JSONException {
        repository.process();
    }

}