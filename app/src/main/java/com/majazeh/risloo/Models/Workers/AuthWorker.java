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
import com.majazeh.risloo.Models.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Managers.FileManager;
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
    private AuthApi authApi;

    // Objects
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public AuthWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

        authApi = RetroGenerator.getRetrofit().create(AuthApi.class);

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
                case "edit":
                    edit();
                    break;
                case "avatar":
                    avatar();
                    break;
                case "logOut":
                    logOut();
                    break;
                case "sendDoc":
                    sendDoc();
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
            Call<ResponseBody> call = authApi.auth(token(), AuthRepository.callback, AuthRepository.authorizedKey);

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

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "auth", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "auth", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void authTheory() {
        try {
            Call<ResponseBody> call = authApi.authTheory(token(), AuthRepository.key, AuthRepository.callback, AuthRepository.password, AuthRepository.code);

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

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "authTheory", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "authTheory", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void register() {
        try {
            Call<ResponseBody> call = authApi.register(AuthRepository.name, AuthRepository.mobile, AuthRepository.gender, AuthRepository.password);

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

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "register", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "register", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void verification() {
        try {
            Call<ResponseBody> call = authApi.verification(AuthRepository.mobile);

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

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "verification", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "verification", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void recovery() {
        try {
            Call<ResponseBody> call = authApi.recovery(AuthRepository.mobile);

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

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "recovery", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "recovery", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void me() {
        try {
            Call<ResponseBody> call = authApi.me(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());
                JSONObject data = successBody.getJSONObject("data");

                boolean hasAccess = false;

                if (data.has("type") && data.getString("type").equals("admin")) {
                    hasAccess = true;
                } else {
                    JSONArray centers = data.getJSONArray("centers");
                    for (int i = 0; i < centers.length(); i++) {
                        JSONObject jsonObject = centers.getJSONObject(i);
                        JSONObject acceptation = jsonObject.getJSONObject("acceptation");

                        if (acceptation.getString("position").equals("operator") || acceptation.getString("position").equals("manager") || acceptation.getString("position").equals("psychologist")) {
                            hasAccess = true;
                        }
                    }
                }

                if (hasAccess) {
                    editor.putBoolean("hasAccess", true);
                } else {
                    editor.putBoolean("hasAccess", false);
                }

                FileManager.writeObjectToCache(context, new JSONObject().put("data", data.getJSONArray("centers")), "centers", "my");

                if (data.has("id"))
                    editor.putString("userId", data.getString("id"));
                else
                    editor.putString("userId", "");

                if (data.has("name"))
                    editor.putString("name", data.getString("name"));
                else
                    editor.putString("name", "");

                if (data.has("type"))
                    editor.putString("type", data.getString("type"));
                else
                    editor.putString("type", "");

                if (data.has("mobile"))
                    editor.putString("mobile", data.getString("mobile"));
                else
                    editor.putString("mobile", "");

                if (data.has("email"))
                    editor.putString("email", data.getString("email"));
                else
                    editor.putString("email", "");

                if (data.has("gender"))
                    editor.putString("gender", data.getString("gender"));
                else
                    editor.putString("gender", "");

                if (data.has("birthday"))
                    editor.putString("birthday", data.getString("birthday"));
                else
                    editor.putString("birthday", "");

                if (data.has("avatar")) {
                    JSONObject avatar = data.getJSONObject("avatar");
                    JSONObject medium = avatar.getJSONObject("medium");

                    editor.putString("avatar", medium.getString("url"));
                } else {
                    editor.putString("avatar", "");
                }

                editor.apply();

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "me", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "me", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void edit() {
        try {
            Call<ResponseBody> call = authApi.edit(token(), AuthRepository.name, AuthRepository.gender, AuthRepository.birthday);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                editor.putString("name", AuthRepository.name);
                editor.putString("gender", AuthRepository.gender);
                editor.putString("birthday", AuthRepository.birthday);
                editor.apply();

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "edit", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "edit", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void avatar() {
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

                            JSONObject avatar = data.getJSONObject("avatar");
                            JSONObject medium = avatar.getJSONObject("medium");

                            editor.putString("avatar", medium.getString("url"));
                            editor.apply();

                            FileManager.deleteBitmapFromCache(context, "image");

                            ExceptionManager.getException(200, successBody, true, "avatar", "auth");
                            AuthRepository.workState.postValue(1);
                        } catch (JSONException e) {
                            e.printStackTrace();

                            ExceptionManager.getException(0, null, false, "JSONException", "auth");
                            AuthRepository.workState.postValue(0);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        try {
                            JSONObject errorBody = new JSONObject(error.getErrorBody());

                            ExceptionManager.getException(error.getErrorCode(), errorBody, true, "avatar", "auth");
                            AuthRepository.workState.postValue(0);
                        } catch (JSONException e) {
                            e.printStackTrace();

                            ExceptionManager.getException(0, null, false, "JSONException", "auth");
                            AuthRepository.workState.postValue(0);
                        }
                    }

                });
    }

    private void logOut() {
        try {
            Call<ResponseBody> call = authApi.logOut(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(Objects.requireNonNull(bodyResponse.body()).string());

                editor.remove("hasAccess");

                editor.remove("token");
                editor.remove("userId");
                editor.remove("name");
                editor.remove("type");
                editor.remove("mobile");
                editor.remove("email");
                editor.remove("gender");
                editor.remove("birthday");
                editor.remove("avatar");

                editor.apply();

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "logOut", "auth");
                AuthRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(Objects.requireNonNull(bodyResponse.errorBody()).string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "logOut", "auth");
                AuthRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "auth");
            AuthRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "auth");
            AuthRepository.workState.postValue(0);
        }
    }

    private void sendDoc() {
        AndroidNetworking.upload("https://bapi.risloo.ir/api/documents")
                .addHeaders("Authorization", token())
                .addMultipartFile("attachment", new File(AuthRepository.filePath))
                .addMultipartParameter("title", AuthRepository.fileTitle)
                .addMultipartParameter("description", AuthRepository.fileDescription)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject successBody = new JSONObject(response.toString());

                            ExceptionManager.getException(200, successBody, true, "sendDoc", "auth");
                            AuthRepository.workState.postValue(1);
                        } catch (JSONException e) {
                            e.printStackTrace();

                            ExceptionManager.getException(0, null, false, "JSONException", "auth");
                            AuthRepository.workState.postValue(0);
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        try {
                            JSONObject errorBody = new JSONObject(error.getErrorBody());

                            ExceptionManager.getException(error.getErrorCode(), errorBody, true, "sendDoc", "auth");
                            AuthRepository.workState.postValue(0);
                        } catch (JSONException e) {
                            e.printStackTrace();

                            ExceptionManager.getException(0, null, false, "JSONException", "auth");
                            AuthRepository.workState.postValue(0);
                        }
                    }

                });
    }

}