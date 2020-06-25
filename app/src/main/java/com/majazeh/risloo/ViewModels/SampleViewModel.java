package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.majazeh.risloo.Entities.Sample.Sample;
import com.majazeh.risloo.Models.Repositories.SampleRepository;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SampleViewModel extends AndroidViewModel {
    SampleRepository repository;

    public SampleViewModel(@NonNull Application application, String testUniqueId) throws JSONException {
        super(application);
        repository = new SampleRepository(application, testUniqueId);
    }

    public String getTitle() throws JSONException {
        return repository.getTitle();
    }

    public String getDescription() throws JSONException {
        return repository.getDescription();
    }

    public int getVersion() throws JSONException {
        return repository.getVersion();
    }

    public String Edition() throws JSONException {
        return repository.getEdition();
    }

    public String getEdition_version() throws JSONException {
        return repository.getEdition_version();
    }

    public String getFiller() throws JSONException {
        return repository.getFiller();
    }

    public Sample getItem(int index) {
        return repository.items().getItem(index);
    }

    public Sample next() {
        return repository.items().next();
    }

    public Sample prev() {
        return repository.items().prev();
    }

    public Sample goToIndex(int index) {
        return repository.items().goToIndex(index);
    }

    public void writeToFile(JSONArray jsonArray, String file_name) {
        repository.writeToFile(jsonArray, file_name);
    }

    public JSONArray readFromFile(String file_name) {
        return repository.readFromFile(file_name);
    }

    public int getCurrentIndex() {
        return repository.items().getCurrentIndex();
    }

    public int getSize() {
        return repository.items().getSize();
    }

    public boolean saveToCSV(JSONArray jsonArray, String file_name) {
        return repository.saveToCSV(jsonArray, file_name);
    }

    public void setData(int item,int answer){
        repository.items().setData(item, answer);
    }

    public void setSending_data(int item,int answer){
        repository.items().setSending_data(item, answer);
    }

    public void putSDtoD(){
         repository.items().putSDtoD();
    }

    public ArrayList data(){
        return repository.items().data();
    }

    public ArrayList sending_data(){
        return repository.items().sending_data();
    }
}
