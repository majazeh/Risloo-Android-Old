package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Managers.FileManager;
import com.majazeh.risloo.Models.Apis.CenterApi;
import com.majazeh.risloo.Models.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Repositories.CenterRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;

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
            Call<ResponseBody> call = centerApi.getAll(token(), CenterRepository.allPage);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                if (successBody.getJSONArray("data").length() != 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        FileManager.deletePage(context, "centers", "all", CenterRepository.allPage, 15);
                    }

                    if (CenterRepository.allPage == 1) {
                        FileManager.writeObjectToCache(context, successBody, "centers", "all");
                    } else {
                        JSONObject jsonObject = FileManager.readObjectFromCache(context, "centers", "all");
                        JSONArray data;
                        try {
                            data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < successBody.getJSONArray("data").length(); i++) {
                                JSONArray jsonArray = successBody.getJSONArray("data");
                                data.put(jsonArray.getJSONObject(i));
                            }
                            jsonObject.put("data", data);
                            FileManager.writeObjectToCache(context, jsonObject, "centers", "all");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "all", "center");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "all", "center");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "center");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "center");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "center");
            CenterRepository.workState.postValue(0);
        }

    }

    private void getMy() {
        try {
            Call<ResponseBody> call = centerApi.getMy(token(), CenterRepository.myPage);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                FileManager.writeObjectToCache(context, successBody, "centers", "my");

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "my", "center");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "my", "center");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "center");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "center");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "center");
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

                JSONObject data = FileManager.readObjectFromCache(context, "centers", "all");
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

                FileManager.writeObjectToCache(context, jsonObject, "centers", "all");

                if (acceptation.isNull("kicked_at")) {
                    if (!acceptation.isNull("accepted_at")) {
                        JSONObject data2 = FileManager.readObjectFromCache(context, "centers", "my");
                        JSONArray jsonArray = data2.getJSONArray("data");
                        jsonArray.put(response);

                        JSONObject json = new JSONObject();
                        json.put("data", jsonArray);

                        FileManager.writeObjectToCache(context, json, "centers", "my");
                    }
                }

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "request", "center");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "request", "center");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "center");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "center");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "center");
            CenterRepository.workState.postValue(0);
        }

    }

    private void create() {
        AndroidNetworking.upload("https://bapi.risloo.ir/api/centers")
                .addHeaders("Authorization", token())
                .addMultipartFile("avatar", new File(context.getCacheDir(), "createCenter"))
                .addMultipartParameter(CenterRepository.createData)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {

                    @Override
                    public void onResponse(JSONObject response) {
                        ExceptionManager.getException(200, response, true, "create", "center");
                        CenterRepository.workState.postValue(1);
                    }

                    @Override
                    public void onError(ANError error) {
                        try {
                            JSONObject errorBody = new JSONObject(error.getErrorBody());

                            ExceptionManager.getException(error.getErrorCode(), errorBody, true, "create", "center");
                            CenterRepository.workState.postValue(0);
                        } catch (JSONException e) {
                            e.printStackTrace();

                            ExceptionManager.getException(0, null, false, "JSONException", "center");
                            CenterRepository.workState.postValue(0);
                        }
                    }
                });
//
//        try {
//            Call<ResponseBody> call = centerApi.create(token(), CenterRepository.createData);
//
//            Response<ResponseBody> bodyResponse = call.execute();
//            if (bodyResponse.isSuccessful()) {
//                JSONObject successBody = new JSONObject(bodyResponse.body().string());
//
//                ExceptionManager.getException(bodyResponse.code(), successBody, true, "create", "center");
//                CenterRepository.workState.postValue(1);
//            } else {
//                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());
//
//                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "create", "center");
//                CenterRepository.workState.postValue(0);
//            }
//
//        } catch (SocketTimeoutException e) {
//            e.printStackTrace();
//
//            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "center");
//            CenterRepository.workState.postValue(0);
//        } catch (JSONException e) {
//            e.printStackTrace();
//
//            ExceptionManager.getException(0, null, false, "JSONException", "center");
//            CenterRepository.workState.postValue(0);
//        } catch (IOException e) {
//            e.printStackTrace();
//
//            ExceptionManager.getException(0, null, false, "IOException", "center");
//            CenterRepository.workState.postValue(0);
//        }

    }

    private void edit() {
        try {
            String id = (String) CenterRepository.editData.get("id");
            CenterRepository.editData.remove("id");

            Call<ResponseBody> call = centerApi.edit(token(), id, CenterRepository.editData);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "edit", "center");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "edit", "center");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "center");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "center");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "center");
            CenterRepository.workState.postValue(0);
        }

    }

    private void getPersonalClinic() {
        try {
            Call<ResponseBody> call = centerApi.getPersonalClinic(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONArray data = successBody.getJSONArray("data");

                if (data.length() != 0) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        CenterRepository.personalClinic.add(new Model(object));
                    }
                }

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "personalClinic", "center");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "personalClinic", "center");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "center");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "center");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "center");
            CenterRepository.workState.postValue(0);
        }

    }

    private void getCounselingCenter() {
        try {
            Call<ResponseBody> call = centerApi.getCounselingCenter(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONArray data = successBody.getJSONArray("data");

                if (data.length() != 0) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        CenterRepository.counselingCenter.add(new Model(object));
                    }
                }

                ExceptionManager.getException(bodyResponse.code(), successBody, true, "counselingCenter", "center");
                CenterRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(bodyResponse.code(), errorBody, true, "counselingCenter", "center");
                CenterRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "SocketTimeoutException", "center");
            CenterRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "JSONException", "center");
            CenterRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(0, null, false, "IOException", "center");
            CenterRepository.workState.postValue(0);
        }

    }

}