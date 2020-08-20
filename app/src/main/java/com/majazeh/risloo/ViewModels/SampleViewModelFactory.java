package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;

public class SampleViewModelFactory implements ViewModelProvider.Factory {

    // Vars
    private String sampleId;

    // Objects
    private Application application;

    public SampleViewModelFactory(Application application, String sampleId) {
        this.application = application;
        this.sampleId = sampleId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return (T) new SampleViewModel(application, sampleId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}