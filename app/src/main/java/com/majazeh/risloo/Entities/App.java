package com.majazeh.risloo.Entities;

import android.content.res.Configuration;

import androidx.multidex.MultiDexApplication;

import org.jetbrains.annotations.NotNull;

public class App extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onConfigurationChanged(@NotNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}