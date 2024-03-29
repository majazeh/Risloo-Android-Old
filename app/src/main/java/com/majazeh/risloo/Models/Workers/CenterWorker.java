package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Repositories.AuthRepository;
import com.majazeh.risloo.Models.Repositories.RoomRepository;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;
import com.majazeh.risloo.Models.Apis.CenterApi;
import com.majazeh.risloo.Utils.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Repositories.CenterRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class CenterWorker extends Worker {

    // Apis
    private CenterApi centerApi;

    // Objects
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public CenterWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

        centerApi = RetroGenerator.getRetrofit().create(CenterApi.class);

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
                case "getAll":
                    getAll();
                    break;
                case "getMy":
                    getMy();
                    break;
                case "request":
                    request();
                    break;
                case "create":
                    create();
                    break;
                case "edit":
                    edit();
                    break;
                case "getPersonalClinic":
                    getPersonalClinic();
                    break;
                case "getCounselingCenter":
                    getCounselingCenter();
                    break;
                case "users":
                    getUsers();
                    break;
                case "userStatus":
                    userStatus();
                    break;
                case "addUser":
                    addUser();
                    break;
                case "getReferences":
                    getReferences();
                    break;
                case "userPosition":
                    userPosition();
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

    private void getAll() {
        try {
            Call<ResponseBody> call = centerApi.getAll(token(), CenterRepository.allPage, CenterRepository.search);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                if (successBody.getJSONArray("data").length() != 0) {
                    if (CenterRepository.search.equals("")) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            FileManager.deletePageFromCache(context, "centers" + "/" + "all", CenterRepository.allPage, 15);
                        }

                        if (CenterRepository.allPage == 1) {
                            FileManager.writeObjectToCache(context, successBody, "centers" + "/" + "all");
                        } else {
                            JSONObject jsonObject = FileManager.readObjectFromCache(context, "centers" + "/" + "all");
                            JSONArray data;
                            try {
                                data = jsonObject.getJSONArray("data");
                                for (int i = 0; i < successBody.getJSONArray("data").length(); i++) {
                                    JSONArray jsonArray = successBody.getJSONArray("data");
                                    data.put(jsonArray.getJSONObject(i));
                                }
                                jsonObject.put("data", data);
                                FileManager.writeObjectToCache(context, jsonObject, "centers" + "/" + "all");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {

                        if (CenterRepository.allPage == 1) {
                            CenterRepository.getAll.clear();
                        }
                        JSONArray data = successBody.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            CenterRepository.getAll.add(new Model(data.getJSONObject(i)));
                        }
                    }

                } else if (CenterRepository.allPage == 1) {
                    CenterRepository.getAll.clear();
                    FileManager.deletePageFromCache(context, "centers" + "/" + "all");

                }

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "all");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "all");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            CenterRepository.workState.postValue(0);
        }

    }

    private void getMy() {
        try {
            Call<ResponseBody> call = centerApi.getMy(token(), CenterRepository.myPage, CenterRepository.search);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                if (successBody.getJSONArray("data").length() != 0) {
                    if (CenterRepository.search.equals("")) {
                        if (CenterRepository.myPage == 1) {
                            FileManager.writeObjectToCache(context, successBody, "centers" + "/" + "my");
                        } else {
                            JSONObject jsonObject = FileManager.readObjectFromCache(context, "centers" + "/" + "my");
                            JSONArray data;
                            try {
                                data = jsonObject.getJSONArray("data");
                                for (int i = 0; i < successBody.getJSONArray("data").length(); i++) {
                                    JSONArray jsonArray = successBody.getJSONArray("data");
                                    data.put(jsonArray.getJSONObject(i));
                                }
                                jsonObject.put("data", data);
                                FileManager.writeObjectToCache(context, jsonObject, "centers" + "/" + "my");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {

                        JSONArray data = successBody.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            CenterRepository.getMy.add(new Model(data.getJSONObject(i)));
                        }
                    }
                } else if (CenterRepository.myPage == 1) {
                    CenterRepository.getMy.clear();
                    FileManager.deletePageFromCache(context, "centers" + "/" + "my");

                }
                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "my");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "my");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            CenterRepository.workState.postValue(0);
        }

    }

    private void request() {
        try {
            Call<ResponseBody> call = centerApi.request(token(), CenterRepository.clinicId);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                JSONObject response = successBody.getJSONObject("data");
                JSONObject acceptation = response.getJSONObject("acceptation");

                JSONObject data = FileManager.readObjectFromCache(context, "centers" + "/" + "all");
                JSONArray items = data.getJSONArray("data");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = (JSONObject) items.get(i);
                    if (response.getString("id").equals(item.getString("id"))) {
                        items.put(i, response);
                        break;
                    }
                }

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("data", items);

                FileManager.writeObjectToCache(context, jsonObject, "centers" + "/" + "all");

                if (acceptation.isNull("kicked_at")) {
                    if (!acceptation.isNull("accepted_at")) {
                        JSONObject data2 = FileManager.readObjectFromCache(context, "centers" + "/" + "my");
                        JSONArray jsonArray = data2.getJSONArray("data");
                        jsonArray.put(response);

                        JSONObject json = new JSONObject();
                        json.put("data", jsonArray);

                        FileManager.writeObjectToCache(context, json, "centers" + "/" + "my");
                    }
                }

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "request");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "request");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            CenterRepository.workState.postValue(0);
        }

    }

    private void create() {
        try {
            Call<ResponseBody> call = centerApi.create(token(), CenterRepository.createData);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "create");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "create");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            CenterRepository.workState.postValue(0);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        File avatar = new File(context.getCacheDir(), "image");
//
//        AndroidNetworking.upload("https://bapi.risloo.ir/api/centers")
//                .addHeaders("Authorization", token())
//                .addMultipartFile("avatar", avatar)
//                .addMultipartParameter(CenterRepository.createData)
//                .setPriority(Priority.HIGH)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONObject successBody = new JSONObject(response.toString());
//
//                            FileManager.deleteBitmapFromCache(context, "image");
//
//                            ExceptionManager.getException(true, 200, successBody, "create");
//                            CenterRepository.workState.postValue(1);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//
//                            ExceptionManager.getException(false, 0, null, "JSONException");
//                            CenterRepository.workState.postValue(0);
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        try {
//                            JSONObject errorBody = new JSONObject(error.getErrorBody());
//
//                            ExceptionManager.getException(true, error.getErrorCode(), errorBody, "create");
//                            CenterRepository.workState.postValue(0);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//
//                            ExceptionManager.getException(false, 0, null, "JSONException");
//                            CenterRepository.workState.postValue(0);
//                        }
//                    }
//                });
    }

    private void edit() {
        try {
            String id = (String) CenterRepository.editData.get("id");
            CenterRepository.editData.remove("id");

            Call<ResponseBody> call = centerApi.edit(token(), id, CenterRepository.editData);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "edit");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "edit");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            CenterRepository.workState.postValue(0);
        }

    }

    private void getPersonalClinic() {
        try {
            Call<ResponseBody> call = centerApi.getPersonalClinic(token(), CenterRepository.personalClinicQ);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONArray data = successBody.getJSONArray("data");

                if (CenterRepository.personalClinic.size() != 0) {
                    CenterRepository.personalClinic.clear();
                }

                if (data.length() != 0) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        CenterRepository.personalClinic.add(new Model(object));
                    }
                }

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "personalClinic");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "personalClinic");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            CenterRepository.workState.postValue(0);
        }

    }

    private void getCounselingCenter() {
        try {
            Call<ResponseBody> call = centerApi.getCounselingCenter(token(), CenterRepository.counselingCenterQ);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONArray data = successBody.getJSONArray("data");

                if (CenterRepository.counselingCenter.size() != 0) {
                    CenterRepository.counselingCenter.clear();
                }

                if (data.length() != 0) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        CenterRepository.counselingCenter.add(new Model(object));
                    }
                }

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "counselingCenter");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "counselingCenter");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            CenterRepository.workState.postValue(0);
        }

    }

    private void getUsers() {
        try {
            Call<ResponseBody> call = centerApi.getUsers(token(), CenterRepository.clinicId, CenterRepository.usersPage);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                if (successBody.getJSONArray("data").length() != 0) {
                    if (CenterRepository.usersPage == 1) {
                        FileManager.writeObjectToCache(context, successBody, "centerUsers" + "/" + CenterRepository.clinicId);
                    } else {
                        JSONObject jsonObject = FileManager.readObjectFromCache(context, "centerUsers" + "/" + CenterRepository.clinicId);
                        JSONArray data;
                        try {
                            data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < successBody.getJSONArray("data").length(); i++) {
                                JSONArray jsonArray = successBody.getJSONArray("data");
                                data.put(jsonArray.getJSONObject(i));
                            }
                            jsonObject.put("data", data);
                            FileManager.writeObjectToCache(context, jsonObject, "centerUsers" + "/" + CenterRepository.clinicId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (CenterRepository.usersPage == 1) {
                    FileManager.deleteFileFromCache(context, "centerUsers" + "/" + CenterRepository.clinicId);
                }
                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "users");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "users");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            CenterRepository.workState.postValue(0);
        }

    }

    private void userStatus() {
        try {
            Call<ResponseBody> call = centerApi.userStatus(token(), CenterRepository.clinicId, CenterRepository.userId, CenterRepository.status);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONObject jsonObject = FileManager.readObjectFromCache(context, "centerUsers" + "/" + CenterRepository.clinicId);
                JSONArray data = jsonObject.getJSONArray("data");
                JSONArray newData = new JSONArray();
                for (int i = 0; i < data.length(); i++) {
                    if (data.getJSONObject(i).getString("id").equals(successBody.getJSONObject("data").getString("id"))) {
                        newData.put(successBody.getJSONObject("data"));
                    } else {
                        newData.put(data.getJSONObject(i));
                    }
                }
                jsonObject.put("data", newData);
                FileManager.writeObjectToCache(context, jsonObject, "centerUsers" + "/" + CenterRepository.clinicId);

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "userStatus");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "userStatus");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            CenterRepository.workState.postValue(0);
        }

    }

    private void userPosition() {
        try {
            Call<ResponseBody> call = centerApi.userPosition(token(), CenterRepository.clinicId, CenterRepository.userId, CenterRepository.position);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONObject jsonObject = FileManager.readObjectFromCache(context, "centerUsers" + "/" + CenterRepository.clinicId);
                JSONArray data = jsonObject.getJSONArray("data");
                JSONArray newData = new JSONArray();
                for (int i = 0; i < data.length(); i++) {
                    if (data.getJSONObject(i).getString("id").equals(successBody.getJSONObject("data").getString("id"))) {
                        newData.put(successBody.getJSONObject("data"));
                    } else {
                        newData.put(data.getJSONObject(i));
                    }
                }
                jsonObject.put("data", newData);
                FileManager.writeObjectToCache(context, jsonObject, "centerUsers" + "/" + CenterRepository.clinicId);

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "userPosition");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "userPosition");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            CenterRepository.workState.postValue(0);
        }

    }

    private void addUser() {
        try {
            Call<ResponseBody> call = centerApi.addUser(token(), CenterRepository.clinicId, CenterRepository.addUserData);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

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

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "addUser");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "addUser");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            CenterRepository.workState.postValue(0);
        }
    }

    private void getReferences() {
        try {
            Call<ResponseBody> call = centerApi.getReferences(token(), RoomRepository.roomId, RoomRepository.roomId, CenterRepository.usersQ);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONArray data = successBody.getJSONArray("data");

                if (CenterRepository.users.size() != 0) {
                    CenterRepository.users.clear();
                }

                if (data.length() != 0) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        CenterRepository.users.add(new Model(object));
                    }
                }

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "getReferences");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "getReferences");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            CenterRepository.workState.postValue(0);
        }

    }

}