package com.majazeh.risloo.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;

public class SampleViewModelFactory implements ViewModelProvider.Factory {

    // Objects
    private Application application;

    // Vars
    private String testUniqueId;

    public SampleViewModelFactory(Application application, String testUniqueId) {
        this.application = application;
        this.testUniqueId = testUniqueId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        try {
            return (T) new SampleViewModel(application, testUniqueId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}