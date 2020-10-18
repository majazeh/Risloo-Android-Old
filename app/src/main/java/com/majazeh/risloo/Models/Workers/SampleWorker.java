package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Apis.SampleApi;
import com.majazeh.risloo.Models.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Managers.FileManager;
import com.majazeh.risloo.Models.Repositories.SampleRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SampleWorker extends Worker {

    // Apis
    private SampleApi sampleApi;

    // Repository
    private SampleRepository repository;

    // Objects
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SampleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

        sampleApi = RetroGenerator.getRetrofit().create(SampleApi.class);

        repository = new SampleRepository();

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
                case "getSingle":
                    getSingle();
                    break;
                case "getAll":
                    getAll();
                    break;
                case "sendAnswers":
                    sendAnswers();
                    break;
                case "sendPrerequisite":
                    sendPrerequisite();
                    break;
                case "create":
                    create();
                    break;
                case "close":
                    close();
                    break;
                case "score":
                    score();
                    break;
                case "getScore":
                    getScore();
                    break;
                case "getScales":
                    getScales();
                    break;
                case "getScalesSearch":
                    getScalesSearch();
                    break;
                case "getRooms":
                    getRooms();
                    break;
                case "getRoomsSearch":
                    getRoomsSearch();
                    break;
                case "getReferences":
                    getReferences();
                    break;
                case "getReferencesSearch":
                    getReferencesSearch();
                    break;
                case "getCases":
                    getCases();
                    break;
                case "getCasesSearch":
                    getCasesSearch();
                    break;
                case "getGeneral":
                    getGeneral();
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

    private void getSingle() {
        try {
            Call<ResponseBody> call = sampleApi.getSingle(token(), SampleRepository.sampleId);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONObject data = successBody.getJSONObject("data");

                FileManager.writeSampleAnswerToCache(context, successBody, SampleRepository.sampleId);
                FileManager.writePrerequisiteAnswerToCache(context, data.getJSONArray("prerequisites"), SampleRepository.sampleId);

                ExceptionManager.getException(true, bodyResponse.code(), successBody, "single", "sample");
                if (data.getString("status").equals("closed")){
                    SampleRepository.workStateSample.postValue(-3);
                }else {
                    SampleRepository.workStateSample.postValue(1);
                }
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(true, bodyResponse.code(), errorBody, "single", "sample");
                SampleRepository.workStateSample.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
            SampleRepository.workStateSample.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "JSONException", "sample");
            SampleRepository.workStateSample.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "IOException", "sample");
            SampleRepository.workStateSample.postValue(0);
        }
    }

    private void getAll() {
        try {
            Call<ResponseBody> call = sampleApi.getAll(token(), SampleRepository.samplesPage);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                if (successBody.getJSONArray("data").length() != 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        FileManager.deletePage(context, "samples", "all", SampleRepository.samplesPage, 15);
                    }

                    if (SampleRepository.samplesPage == 1) {
                        FileManager.writeObjectToCache(context, successBody, "samples", "all");
                    } else {
                        JSONObject jsonObject = FileManager.readObjectFromCache(context, "samples", "all");
                        JSONArray data;
                        try {
                            data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < successBody.getJSONArray("data").length(); i++) {
                                JSONArray jsonArray = successBody.getJSONArray("data");
                                data.put(jsonArray.getJSONObject(i));
                            }
                            jsonObject.put("data", data);
                            FileManager.writeObjectToCache(context, jsonObject, "samples", "all");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                ExceptionManager.getException(true, bodyResponse.code(), successBody, "all", "sample");
                SampleRepository.workStateSample.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(true, bodyResponse.code(), errorBody, "all", "sample");
                SampleRepository.workStateSample.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
            SampleRepository.workStateSample.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "JSONException", "sample");
            SampleRepository.workStateSample.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "IOException", "sample");
            SampleRepository.workStateSample.postValue(0);
        }

    }

    private void sendAnswers() {
        try {
            SampleRepository.cache = false;

            HashMap hashMap = new HashMap();
            hashMap.put("items", SampleRepository.remoteData);

            Call<ResponseBody> call = sampleApi.send(token(), SampleRepository.sampleId, hashMap);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                SampleRepository.remoteData.clear();

                ExceptionManager.getException(true, bodyResponse.code(), successBody, "answers", "sample");
                SampleRepository.workStateAnswer.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                repository.insertRemoteToLocal();

                ExceptionManager.getException(true, bodyResponse.code(), errorBody, "answers", "sample");
                SampleRepository.workStateAnswer.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
            SampleRepository.workStateAnswer.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "JSONException", "sample");
            SampleRepository.workStateAnswer.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "IOException", "sample");
            SampleRepository.workStateAnswer.postValue(0);
        }
    }

    private void sendPrerequisite() {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("prerequisites", SampleRepository.prerequisiteData);

            Call<ResponseBody> call = sampleApi.send(token(), SampleRepository.sampleId, hashMap);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                SampleRepository.prerequisiteData = new ArrayList();
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                SampleRepository.remoteData.clear();

                ExceptionManager.getException(true, bodyResponse.code(), successBody, "prerequisite", "sample");
                SampleRepository.workStateAnswer.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(true, bodyResponse.code(), errorBody, "prerequisite", "sample");
                SampleRepository.workStateAnswer.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
            SampleRepository.workStateAnswer.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "JSONException", "sample");
            SampleRepository.workStateAnswer.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "IOException", "sample");
            SampleRepository.workStateAnswer.postValue(0);
        }
    }

    private void create() {
        try {
            Call<ResponseBody> call = sampleApi.create(token(), SampleRepository.createData);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                ExceptionManager.getException(true, bodyResponse.code(), successBody, "create", "sample");
                SampleRepository.workStateCreate.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(true, bodyResponse.code(), errorBody, "create", "sample");
                SampleRepository.workStateCreate.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "IOException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "JSONException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        }
    }

    private void close() {
        try {
            Call<ResponseBody> call = sampleApi.close(token(), SampleRepository.sampleId);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                ExceptionManager.getException(true, bodyResponse.code(), successBody, "close", "sample");
                SampleRepository.workStateSample.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(true, bodyResponse.code(), errorBody, "close", "sample");
                SampleRepository.workStateSample.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
            SampleRepository.workStateSample.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "JSONException", "sample");
            SampleRepository.workStateSample.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "IOException", "sample");
            SampleRepository.workStateSample.postValue(0);
        }
    }

    private void score() {
        Call<ResponseBody> call = sampleApi.score(token(), SampleRepository.sampleId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.code() == 202) {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(() -> getScore(), 3000);
                        } else {
                            JSONObject successBody = new JSONObject(response.body().string());
                            JSONObject data = successBody.getJSONObject("data");

                            FileManager.writeObjectToCache(context, data, "sampleDetail", SampleRepository.sampleId);

                            ExceptionManager.getException(true, response.code(), successBody, "score", "sample");
                            SampleRepository.workStateSample.postValue(1);
                        }
                    } else {
                        JSONObject errorBody = new JSONObject(response.errorBody().string());

                        ExceptionManager.getException(true, response.code(), errorBody, "score", "sample");
                        SampleRepository.workStateSample.postValue(0);
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();

                    ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
                    SampleRepository.workStateSample.postValue(0);
                } catch (IOException e) {
                    e.printStackTrace();

                    ExceptionManager.getException(false, 0, null, "IOException", "sample");
                    SampleRepository.workStateSample.postValue(0);
                } catch (JSONException e) {
                    e.printStackTrace();

                    ExceptionManager.getException(false, 0, null, "JSONException", "sample");
                    SampleRepository.workStateSample.postValue(0);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void getScales() {
        try {
            Call<ResponseBody> call = sampleApi.getScales(token(),"");

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONArray data = successBody.getJSONArray("data");

                if (data.length() != 0) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        SampleRepository.scales.add(new Model(object));
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("title", "");
                    SampleRepository.scales.add(new Model(jsonObject));
                }

                ExceptionManager.getException(true, bodyResponse.code(), successBody, "scales", "sample");
                SampleRepository.workStateCreate.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(true, bodyResponse.code(), errorBody, "scales", "sample");
                SampleRepository.workStateCreate.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "IOException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "JSONException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        }
    }

    private void getScalesSearch() {
        try {
            Call<ResponseBody> call = sampleApi.getScales(token(),SampleRepository.scalesSearch);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONArray data = successBody.getJSONArray("data");

                if (data.length() != 0) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        SampleRepository.scalesSearchArrayList.add(new Model(object));
                    }
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("title", "");
                    SampleRepository.scales.add(new Model(jsonObject));
                }

                ExceptionManager.getException(true, bodyResponse.code(), successBody, "scales", "sample");
                SampleRepository.workStateCreate.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(true, bodyResponse.code(), errorBody, "scales", "sample");
                SampleRepository.workStateCreate.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "IOException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "JSONException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        }
    }


    private void getRooms() {
        try {
            Call<ResponseBody> call = sampleApi.getRooms(token(),"");

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONArray data = successBody.getJSONArray("data");

                if (data.length() != 0) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        SampleRepository.rooms.add(new Model(object));
                    }
                }

                ExceptionManager.getException(true, bodyResponse.code(), successBody, "rooms", "sample");
                SampleRepository.workStateCreate.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(true, bodyResponse.code(), errorBody, "rooms", "sample");
                SampleRepository.workStateCreate.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "IOException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "JSONException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        }
    }

    private void getRoomsSearch(){
        try {
            Call<ResponseBody> call = sampleApi.getRooms(token(),SampleRepository.roomsSearch);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONArray data = successBody.getJSONArray("data");

                if (data.length() != 0) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        SampleRepository.roomsSearchArrayList.add(new Model(object));
                    }
                }

                ExceptionManager.getException(true, bodyResponse.code(), successBody, "rooms", "sample");
                SampleRepository.workStateCreate.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(true, bodyResponse.code(), errorBody, "rooms", "sample");
                SampleRepository.workStateCreate.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "IOException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "JSONException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        }
    }


    private void getReferences() {
        try {
            Call<ResponseBody> call = sampleApi.getReferences(token(), SampleRepository.roomId,"");

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONArray data = successBody.getJSONArray("data");

                if (data.length() != 0) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        SampleRepository.references.add(new Model(object));
                    }
                    JSONObject jsonObject = new JSONObject();
                    JSONObject user = new JSONObject();
                    user.put("name", "");
                    jsonObject.put("user",user);
                    SampleRepository.references.add(new Model(jsonObject));
                }

                ExceptionManager.getException(true, bodyResponse.code(), successBody, "references", "sample");
                SampleRepository.workStateCreate.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(true, bodyResponse.code(), errorBody, "references", "sample");
                SampleRepository.workStateCreate.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "IOException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "JSONException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        }
    }

    public void getReferencesSearch(){
        try {
            Call<ResponseBody> call = sampleApi.getReferences(token(), SampleRepository.roomId,SampleRepository.referencesSearch);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONArray data = successBody.getJSONArray("data");

                if (data.length() != 0) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        SampleRepository.referencesSearchArrayList.add(new Model(object));
                    }
                    JSONObject jsonObject = new JSONObject();
                    JSONObject user = new JSONObject();
                    user.put("name", "");
                    jsonObject.put("user",user);
                    SampleRepository.referencesSearchArrayList.add(new Model(jsonObject));
                }

                ExceptionManager.getException(true, bodyResponse.code(), successBody, "references", "sample");
                SampleRepository.workStateCreate.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(true, bodyResponse.code(), errorBody, "references", "sample");
                SampleRepository.workStateCreate.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "IOException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "JSONException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        }
    }

    private void getCases() {
        try {
            Call<ResponseBody> call = sampleApi.getCases(token(), SampleRepository.roomId,"");

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONArray data = successBody.getJSONArray("data");

                if (data.length() != 0) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        SampleRepository.cases.add(new Model(object));
                    }
                }

                ExceptionManager.getException(true, bodyResponse.code(), successBody, "cases", "sample");
                SampleRepository.workStateCreate.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(true, bodyResponse.code(), errorBody, "cases", "sample");
                SampleRepository.workStateCreate.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "IOException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "JSONException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        }
    }

    public void getCasesSearch(){
        try {
            Call<ResponseBody> call = sampleApi.getCases(token(), SampleRepository.roomId,SampleRepository.casesSearch);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONArray data = successBody.getJSONArray("data");

                if (data.length() != 0) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject object = data.getJSONObject(i);
                        SampleRepository.casesSearchArrayList.add(new Model(object));
                    }
                }

                ExceptionManager.getException(true, bodyResponse.code(), successBody, "cases", "sample");
                SampleRepository.workStateCreate.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(true, bodyResponse.code(), errorBody, "cases", "sample");
                SampleRepository.workStateCreate.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "IOException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "JSONException", "sample");
            SampleRepository.workStateCreate.postValue(0);
        }
    }

    private void getGeneral() {
        try {
            Call<ResponseBody> call = sampleApi.getGeneral(token(), SampleRepository.sampleId);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONObject data = successBody.getJSONObject("data");

                FileManager.writeObjectToCache(context, data, "sampleDetail", SampleRepository.sampleId);

                ExceptionManager.getException(true, bodyResponse.code(), successBody, "generals", "sample");
                SampleRepository.workStateSample.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionManager.getException(true, bodyResponse.code(), errorBody, "generals", "sample");
                SampleRepository.workStateSample.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
            SampleRepository.workStateSample.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "IOException", "sample");
            SampleRepository.workStateSample.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionManager.getException(false, 0, null, "JSONException", "sample");
            SampleRepository.workStateSample.postValue(0);
        }
    }

    private void getScore() {
        Call<ResponseBody> call = sampleApi.getScore(token(), SampleRepository.sampleId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.code() == 202) {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(() -> getScore(), 3000);
                        } else {
                            JSONObject successBody = new JSONObject(response.body().string());
                            JSONObject data = successBody.getJSONObject("data");

                            FileManager.writeObjectToCache(context, data, "sampleDetailFiles", SampleRepository.sampleId);
                            ExceptionManager.getException(true, response.code(), successBody, "scores", "sample");
                            SampleRepository.workStateSample.postValue(1);
                        }
                    } else {
                        JSONObject errorBody = new JSONObject(response.errorBody().string());

                        ExceptionManager.getException(true, response.code(), errorBody, "scores", "sample");
                        SampleRepository.workStateSample.postValue(0);
                    }

                } catch (SocketTimeoutException e) {
                    e.printStackTrace();

                    ExceptionManager.getException(false, 0, null, "SocketTimeoutException", "sample");
                    SampleRepository.workStateSample.postValue(0);
                } catch (IOException e) {
                    e.printStackTrace();

                    ExceptionManager.getException(false, 0, null, "IOException", "sample");
                    SampleRepository.workStateSample.postValue(0);
                } catch (JSONException e) {
                    e.printStackTrace();

                    ExceptionManager.getException(false, 0, null, "JSONException", "sample");
                    SampleRepository.workStateSample.postValue(0);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

}