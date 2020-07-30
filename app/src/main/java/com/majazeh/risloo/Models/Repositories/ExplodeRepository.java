package com.majazeh.risloo.Models.Repositories;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.majazeh.risloo.Models.Controller.ExplodeController;

import org.json.JSONException;

public class ExplodeRepository extends MainRepository {

    // Controllers
    private ExplodeController controller;

    public ExplodeRepository(Application application) throws JSONException {
        super(application);

        controller = new ExplodeController(application);
    }

    public void explode() throws JSONException {
        controller.work = "explode";
        controller.workState.setValue(-1);
        controller.workManager("explode");
    }

    public boolean newContent() {
        return false;
    }

    public boolean hasUpdate() {
        return false;
    }

    public boolean forceUpdate() {
        return false;
    }

    public String currentVersion() {
        try {
            PackageInfo packageInfo = application.getPackageManager().getPackageInfo(application.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } return null;
    }

    public String newVersion() {
        return "1.1.0";
    }

}