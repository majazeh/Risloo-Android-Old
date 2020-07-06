package com.majazeh.risloo.Models.Repositories.Authentication;

import android.app.Application;
import android.util.Log;

import com.majazeh.risloo.Models.Repositories.MainRepository;

import org.json.JSONException;

import java.util.logging.Logger;

public class AuthRepository extends MainRepository {
    private AuthController controller;

    public AuthRepository(Application application) {
        super(application);
        controller = new AuthController(application);
    }

    public void auth(String authorized_key) throws JSONException {
        controller.authorized_key = authorized_key;
        controller.work = "auth";
        controller.auth();
    }

    public void auth_theory(String password, String code) throws JSONException {
        controller.password = password;
        controller.code = code;
        controller.work = "authTheory";
        controller.auth_theory();
        checkState();
    }

    public void signIn(String name, String gender, String mobile, String password) throws JSONException {
        controller.name = name;
        controller.gender = gender;
        controller.mobile = mobile;
        controller.password = password;
        controller.work = "signIn";
        controller.signIn();
        checkState();
    }

    public void start(String authorized_key) throws JSONException {
        controller.authorized_key = authorized_key;
        controller.auth();
        checkState();
    }

    public void checkState() {
        controller.workState().observeForever(integer -> {
            //auth
            if (controller.work.equals("auth")) {
                if (controller.workState().getValue() == 1) {
                    if (!controller.key.equals("")) {
                        switch (controller.theory) {
                            case "password":
                                //TODO: open password theory
                                break;
                            case "mobileCode":
                                //TODO: open PIN theory
                                break;
                        }
                    } else if (!controller.callback.equals("")) {
                        // TODO: go to auth with another authorized_key
                    } else {
                        // TODO: open panel
                        controller.workState().removeObservers(() -> null);
                    }
                } else if (controller.workState().getValue() == 0) {
                    //TODO: have an error
                    Log.e("error", controller.exception);
                } else {

                }
                //auth theory
            } else if (controller.work.equals("authTheory")) {
                if (controller.workState().getValue() == 1) {
                    if (!controller.key.equals("")) {
                        switch (controller.theory) {
                            case "password":
                                //TODO: open password theory
                                break;
                            case "mobileCode":
                                //TODO: open PIN theory
                        }
                    } else {
                        // TODO: open panel
                        controller.workState().removeObservers(() -> null);
                    }
                } else {
                    //TODO: have an error
                    Log.e("error", controller.exception);
                }
                //signIn
            } else if (controller.work.equals("signIn")) {
                if (controller.workState().getValue() == 1) {
                    if (!controller.key.equals("")) {
                        switch (controller.theory) {
                            case "password":
                                //TODO: open password theory
                                break;
                            case "mobileCode":
                                //TODO: open PIN theory
                        }
                    }
                } else {
                    //TODO: have an error
                    Log.e("error", controller.exception);
                }
            }
        });
    }

}
