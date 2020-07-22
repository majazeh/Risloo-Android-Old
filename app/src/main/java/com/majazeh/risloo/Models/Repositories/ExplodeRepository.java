package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import com.majazeh.risloo.Models.Controller.ExplodeController;

import org.json.JSONException;

public class ExplodeRepository extends MainRepository {

    // Controllers
    private ExplodeController controller;

    public ExplodeRepository(Application application) {
        super(application);

        controller = new ExplodeController(application);
    }

    public void explode() throws JSONException {
        controller.work = "explode";
        controller.workState.setValue(-1);
        controller.workManager("explode");
    }

    public boolean hasUpdate() {
        return false;
    }

    public String getVersion() {
        return "1.1.0";
    }

}