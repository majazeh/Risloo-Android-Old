package com.majazeh.risloo.Models.Repositories.Authentication;

import android.app.Application;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.majazeh.risloo.Models.Repositories.MainRepository;

import org.json.JSONException;

public class AuthRepository extends MainRepository {
    private AuthController controller;

    public AuthRepository(Application application) {
        super(application);
        controller = new AuthController(application);
    }

    public void auth(String authorized_key) throws JSONException {
        controller.authorized_key = authorized_key;
        controller.auth();
    }

    public void auth_theory(String password, String code) throws JSONException {
        controller.password = password;
        controller.code = code;
        controller.auth_theory();
        checkState();
    }

    public void start(String authorized_key) throws JSONException {
        controller.authorized_key = authorized_key;
        controller.theory = "auth";
        controller.auth();
        checkState();
    }

    public void checkState() {
        controller.workState().observeForever(integer -> {
            //auth
            if (controller.theory.equals("auth")) {
                if (controller.workState().getValue() == 1) {
                    if (!controller.key.equals("")) {
                        // TODO: go to auth theory with specific method
                    } else if (!controller.callback.equals("")) {
                        // TODO: go to auth with another authorized_key
                    }else{
                        // TODO: open panel
                    }
                } else if (controller.workState().getValue() == 0) {
                    controller.workState().removeObservers(() -> null);
                } else {

                }
                //auth theory
            } else {
                if (!controller.key.equals("")) {
                    // TODO: go to auth theory with specific method
                }else{
                    // TODO: open panel
                }
            }
        });
    }

}
