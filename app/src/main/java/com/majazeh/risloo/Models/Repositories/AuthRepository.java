package com.majazeh.risloo.Models.Repositories;

import android.app.Application;

import com.majazeh.risloo.Models.Controller.AuthController;

import org.json.JSONException;

public class AuthRepository extends MainRepository {

    // Controllers
    private AuthController controller;

    public AuthRepository(Application application) {
        super(application);

        controller = new AuthController(application);
    }

    public void auth(String authorizedKey) throws JSONException {
        controller.authorizedKey = authorizedKey;
        controller.work = "auth";
        controller.workState.setValue(-1);
        controller.workManager("auth");
    }

    public void authTheory(String password, String code) throws JSONException {
        controller.password = password;
        controller.code = code;
        controller.work = "authTheory";
        controller.workState.setValue(-1);
        controller.workManager("authTheory");
    }

    public void register(String name, String mobile, String gender, String password) throws JSONException {
        controller.name = name;
        controller.mobile = mobile;
        controller.gender = gender;
        controller.password = password;
        controller.work = "register";
        controller.workState.setValue(-1);
        controller.workManager("register");
    }

    public void verification() throws JSONException {
        controller.work = "verification";
        controller.workState.setValue(-1);
        controller.workManager("verification");
    }

    public void recovery(String mobile) throws JSONException {
        controller.mobile = mobile;
        controller.work = "recovery";
        controller.workState.setValue(-1);
        controller.workManager("recovery");
    }

}