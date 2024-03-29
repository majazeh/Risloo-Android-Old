package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.majazeh.risloo.Models.Apis.AuthApi;
import com.majazeh.risloo.Utils.Generators.RetroGenerator;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.Models.Repositories.AuthRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class AuthWorker extends Worker {

    // Apis
    private final AuthApi api;

    // Objects
    private final Context context;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public AuthWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

        api = RetroGenerator.getRetrofit().create(AuthApi.class);

        sharedPreferences = context.getSharedPreferences("sharedPreference", Context.MODE_PRIVATE);

        editor = sharedPreferences.edit();
        editor.apply();
    }

    @NonNull
    @Override
    public Result doWork() {
        String work = getInputData().getString("work");

        if (work != null) {
            switch (work) {
                case "auth":
                    auth();
                    break;
                case "authTheory":
                    authTheory();
                    break;
                case "register":
                    register();
                    break;
                case "verification":
                    verification();
                    break;
                case "recovery":
                    recovery();
                    break;
                case "me":
                    me();
                    break;
                case "logOut":
                    logOut();
                    break;
                case "personal":
                    editPersonal();
                    break;
                case "password":
                    editPassword();
                    break;
                case "avatar":
                    editAvatar();
                    break;
            }
        }

        return Result.success();
    }

    private String token() {
        if (!sharedPreferences.getString("token", "").equals("")) {
            return "Bearer " + sharedPreferences.getString("token", "");
        }
        return "";
    }

    private String userId() {
        if (!sharedPreferences.getString("userId", "").equals("")) {
            return sharedPreferences.getString("userId", "");
        }
        return "";
    }

    private void auth() {
        try {
            Call<ResponseBody> call = api.auth(token(), AuthRepository.callback, AuthRepository.authorizedKey);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                if (successBody.has("key")) {
                    AuthRepository.key = successBody.getString("key");
                } else {
                    AuthRepository.key = "";
                }

                AuthRepository.preTheory = AuthRepository.theory;

                if (successBody.has("theory")) {
                    AuthRepository.theory = successBody.getString("theory");
                } else {
                    AuthRepository.theory = "";
                }

                if (successBody.has("callback")) {
                    AuthRepository.callback = successBody.getString("callback");
                } else {
                    AuthRepository.callback = "";
                }

                if (successBody.has("token")) {
                    editor.putString("token", successBody.getString("token"));
                    editor.apply();

                    me();
                }

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            AuthRepository.workState.postValue(0);
        }
    }

    private void authTheory() {
        try {
            Call<ResponseBody> call = api.authTheory(token(), AuthRepository.key, AuthRepository.callback, AuthRepository.password, AuthRepository.code);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                if (successBody.has("key")) {
                    AuthRepository.key = successBody.getString("key");
                } else {
                    AuthRepository.key = "";
                }

                AuthRepository.preTheory = AuthRepository.theory;

                if (successBody.has("theory")) {
                    AuthRepository.theory = successBody.getString("theory");
                } else {
                    AuthRepository.theory = "";
                }

                if (successBody.has("callback")) {
                    AuthRepository.callback = successBody.getString("callback");
                } else {
                    AuthRepository.callback = "";
                }

                if (!AuthRepository.password.equals("")) {
                    editor.putString("password", AuthRepository.password);
                    editor.apply();
                }

                if (successBody.has("token")) {
                    editor.putString("token", successBody.getString("token"));
                    editor.apply();

                    me();
                }

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "authTheory");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "authTheory");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            AuthRepository.workState.postValue(0);
        }
    }

    private void register() {
        try {
            Call<ResponseBody> call = api.register(AuthRepository.name, AuthRepository.mobile, AuthRepository.gender, AuthRepository.password);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                if (successBody.has("key")) {
                    AuthRepository.key = successBody.getString("key");
                } else {
                    AuthRepository.key = "";
                }

                AuthRepository.preTheory = AuthRepository.theory;

                if (successBody.has("theory")) {
                    AuthRepository.theory = successBody.getString("theory");
                } else {
                    AuthRepository.theory = "";
                }

                if (successBody.has("callback")) {
                    AuthRepository.callback = successBody.getString("callback");
                } else {
                    AuthRepository.callback = "";
                }

                if (!AuthRepository.password.equals("")) {
                    editor.putString("password", AuthRepository.password);
                    editor.apply();
                }

                if (successBody.has("token")) {
                    editor.putString("token", successBody.getString("token"));
                    editor.apply();

                    me();
                }

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "register");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "register");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            AuthRepository.workState.postValue(0);
        }
    }

    private void verification() {
        try {
            Call<ResponseBody> call = api.verification(AuthRepository.mobile);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                if (successBody.has("key")) {
                    AuthRepository.key = successBody.getString("key");
                } else {
                    AuthRepository.key = "";
                }

                AuthRepository.preTheory = AuthRepository.theory;

                if (successBody.has("theory")) {
                    AuthRepository.theory = successBody.getString("theory");
                } else {
                    AuthRepository.theory = "";
                }

                if (successBody.has("callback")) {
                    AuthRepository.callback = successBody.getString("callback");
                } else {
                    AuthRepository.callback = "";
                }

                if (successBody.has("token")) {
                    editor.putString("token", successBody.getString("token"));
                    editor.apply();

                    me();
                }

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "verification");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "verification");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            AuthRepository.workState.postValue(0);
        }
    }

    private void recovery() {
        try {
            Call<ResponseBody> call = api.recovery(AuthRepository.mobile);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                if (successBody.has("key")) {
                    AuthRepository.key = successBody.getString("key");
                } else {
                    AuthRepository.key = "";
                }

                AuthRepository.preTheory = AuthRepository.theory;

                if (successBody.has("theory")) {
                    AuthRepository.theory = successBody.getString("theory");
                } else {
                    AuthRepository.theory = "";
                }

                if (successBody.has("callback")) {
                    AuthRepository.callback = successBody.getString("callback");
                } else {
                    AuthRepository.callback = "";
                }

                if (successBody.has("token")) {
                    editor.putString("token", successBody.getString("token"));
                    editor.apply();

                    me();
                }

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "recovery");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "recovery");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            AuthRepository.workState.postValue(0);
        }
    }

    private void me() {
        try {
            Call<ResponseBody> call = api.me(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                JSONObject data = successBody.getJSONObject("data");
                JSONArray centers = data.getJSONArray("centers");

                FileManager.writeObjectToCache(context, new JSONObject().put("data", centers), "centers" + "/" + "my");

                boolean hasAccess = false;

                if (data.has("type") && data.getString("type").equals("admin")) {
                    hasAccess = true;
                } else {
                    for (int i = 0; i < centers.length(); i++) {
                        JSONObject center = centers.getJSONObject(i);
                        if (!center.isNull("acceptation")){
                            JSONObject acceptation = center.getJSONObject("acceptation");
                            if (center.getString("type").equals("counseling center")) {
                                JSONObject manager = center.getJSONObject("manager");
                                if (manager.getString("id").equals(data.getString("id"))) {
                                    editor.putString("createRoomAccess", "true");
                                    editor.putString("centerOwner", "true");
                                }
                            }
                            if (!acceptation.isNull("meta")){
                                if (!acceptation.getJSONObject("meta").isNull("room_id")){
                                    editor.putString("roomManager", "true");
                                }
                            }
                            if (acceptation.getString("position").equals("operator")) {
                                editor.putString("operator", "true");
                                hasAccess = true;
                            } else if (acceptation.getString("position").equals("manager")) {
                                editor.putString("centerManager", "true");
                                hasAccess = true;
                            } else if (acceptation.getString("position").equals("psychologist")) {
                                editor.putString("psychologist", "true");
                                hasAccess = true;
                            }
                        }
                    }
                }

                editor.putBoolean("hasAccess", hasAccess);

                if (data.has("id") && !data.isNull("id")) {
                    editor.putString("userId", data.getString("id"));
                } else {
                    editor.putString("userId", "");
                }

                if (data.has("name") && !data.isNull("name")) {
                    editor.putString("name", data.getString("name"));
                } else {
                    editor.putString("name", "");
                }

                if (data.has("username") && !data.isNull("username")) {
                    editor.putString("username", data.getString("username"));
                } else {
                    editor.putString("username", "");
                }

                if (data.has("mobile") && !data.isNull("mobile")) {
                    editor.putString("mobile", data.getString("mobile"));
                } else {
                    editor.putString("mobile", "");
                }

                if (data.has("email") && !data.isNull("email")) {
                    editor.putString("email", data.getString("email"));
                } else {
                    editor.putString("email", "");
                }

                if (data.has("birthday") && !data.isNull("birthday")) {
                    editor.putString("birthday", data.getString("birthday"));
                } else {
                    editor.putString("birthday", "");
                }

                if (data.has("gender") && !data.isNull("gender")) {
                    editor.putString("gender", data.getString("gender"));
                } else {
                    editor.putString("gender", "");
                }

                if (data.has("status") && !data.isNull("status")) {
                    editor.putString("status", data.getString("status"));
                } else {
                    editor.putString("status", "");
                }

                if (data.has("type") && !data.isNull("type")) {
                    editor.putString("type", data.getString("type"));
                } else {
                    editor.putString("type", "");
                }

                if (!data.isNull("avatar") && data.get("avatar").getClass().getName().equals("org.json.JSONObject")) {
                    JSONObject avatar = data.getJSONObject("avatar");
                    JSONObject medium = avatar.getJSONObject("medium");

                    editor.putString("avatar", medium.getString("url"));
                } else {
                    editor.putString("avatar", "");
                }

                if (data.has("public_key") && !data.isNull("public_key")) {
                    editor.putString("public_key", data.getString("public_key"));
                } else {
                    editor.putString("public_key", "");
                }

                editor.apply();

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "me");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "me");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            AuthRepository.workState.postValue(0);
        }
    }

    private void logOut() {
        try {
            Call<ResponseBody> call = api.logOut(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                editor.remove("hasAccess");

                editor.remove("token");
                editor.remove("userId");
                editor.remove("name");
                editor.remove("username");
                editor.remove("mobile");
                editor.remove("email");
                editor.remove("birthday");
                editor.remove("gender");
                editor.remove("type");
                editor.remove("password");
                editor.remove("avatar");
                editor.remove("public_key");
                editor.remove("private_key");

                editor.apply();

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "logOut");
                AuthRepository.workState.postValue(1);
            } else if (bodyResponse.code() == 401) {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                editor.remove("hasAccess");

                editor.remove("token");
                editor.remove("userId");
                editor.remove("name");
                editor.remove("username");
                editor.remove("mobile");
                editor.remove("email");
                editor.remove("birthday");
                editor.remove("gender");
                editor.remove("type");
                editor.remove("password");
                editor.remove("avatar");
                editor.remove("public_key");
                editor.remove("private_key");

                editor.apply();

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "logOut");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "logOut");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            AuthRepository.workState.postValue(0);
        }
    }

    private void editPersonal() {
        try {
            Call<ResponseBody> call = api.editPersonal(token(), AuthRepository.editData);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                editor.putString("name", AuthRepository.name);
                editor.putString("username", AuthRepository.username);
                editor.putString("mobile", AuthRepository.mobile);
                editor.putString("email", AuthRepository.email);
                editor.putString("birthday", AuthRepository.birthday);
                editor.putString("gender", AuthRepository.gender);
                editor.putString("status", AuthRepository.status);
                editor.putString("type", AuthRepository.type);

                editor.apply();

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "personal");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "personal");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            AuthRepository.workState.postValue(0);
        }
    }

    private void editPassword() {
        try {
            Call<ResponseBody> call = api.editPassword(token(), userId(), AuthRepository.password);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                editor.putString("password", AuthRepository.password);
                editor.apply();

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "password");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "password");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            AuthRepository.workState.postValue(0);
        }
    }

    private void editAvatar() {
        File avatar = new File(context.getCacheDir(), "image");

        AndroidNetworking.upload("https://bapi.risloo.ir/api/users/" + userId() + "/" + "avatar")
                .addHeaders("Authorization", token())
                .addMultipartFile("avatar", avatar)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject successBody = new JSONObject(response.toString());

                            JSONObject data = successBody.getJSONObject("data");

                            if (!data.isNull("avatar") && data.get("avatar").getClass().getName().equals("org.json.JSONObject")) {
                                JSONObject avatar = data.getJSONObject("avatar");
                                JSONObject medium = avatar.getJSONObject("medium");

                                editor.putString("avatar", medium.getString("url"));
                            } else {
                                editor.putString("avatar", "");
                            }

                            editor.apply();

                            FileManager.deleteFileFromCache(context, "image");

                            ExceptionGenerator.getException(true, 200, successBody, "avatar");
                            AuthRepository.workState.postValue(1);
                        } catch (JSONException e) {
                            e.printStackTrace();

                            ExceptionGenerator.getException(false, 0, null, "JSONException");
                            AuthRepository.workState.postValue(0);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        try {
                            JSONObject errorBody = new JSONObject(error.getErrorBody());

                            ExceptionGenerator.getException(true, error.getErrorCode(), errorBody, "avatar");
                            AuthRepository.workState.postValue(0);
                        } catch (JSONException e) {
                            e.printStackTrace();

                            ExceptionGenerator.getException(false, 0, null, "JSONException");
                            AuthRepository.workState.postValue(0);
                        }
                    }

                });
    }

}