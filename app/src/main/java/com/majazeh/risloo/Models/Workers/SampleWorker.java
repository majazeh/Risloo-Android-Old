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
import com.majazeh.risloo.Models.Repositories.RoomRepository;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Models.Apis.SampleApi;
import com.majazeh.risloo.Utils.Generators.RetroGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;
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

                ////////// Samples
                JSONArray jsonArray2 = new JSONArray();
                JSONArray items = data.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONArray jsonArray1 = new JSONArray();
                    jsonArray1.put(i);
                    if (items.getJSONObject(i).has("user_answered")) {
                        jsonArray1.put(items.getJSONObject(i).getString("user_answered"));
                    } else {
                        jsonArray2.put("");
                    }
                }
                FileManager.writeObjectToCache(context, successBody, "Samples" + "/" + SampleRepository.sampleId);
                //////////

                JSONArray jsonArray = data.getJSONArray("prerequisites");

                ////////// Prerequisites
                JSONArray jsonArray1 = new JSONArray();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONArray jsonArray3 = new JSONArray();
                    if (jsonArray.getJSONObject(i).has("user_answered")) {
                        jsonArray3.put(String.valueOf(i + 1));
                        jsonArray3.put(jsonArray.getJSONObject(i).getString("user_answered"));
                    } else {
                        jsonArray3.put(String.valueOf(i + 1));
                        jsonArray3.put("");
                    }
                    jsonArray1.put(jsonArray3);
                }
                FileManager.writeArrayToCache(context, jsonArray1, "prerequisitesAnswers" + "/" + SampleRepository.sampleId);
                FileManager.writeArrayToCache(context, jsonArray, "Prerequisites" + "/" + SampleRepository.sampleId);
                //////////

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "single");
                if (data.getString("status").equals("closed")) {
                    SampleRepository.workStateSample.postValue(-3);
                } else {
                    SampleRepository.workStateSample.postValue(1);
                }
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "single");
                SampleRepository.workStateSample.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            SampleRepository.workStateSample.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            SampleRepository.workStateSample.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            SampleRepository.workStateSample.postValue(0);
        }
    }

    private void getAll() {
        try {
            Call<ResponseBody> call = sampleApi.getAll(token(), SampleRepository.samplesPage, SampleRepository.scalesQ, RoomRepository.roomQ, SampleRepository.statusQ);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                if (successBody.getJSONArray("data").length() != 0) {
                    if (!SampleRepository.filter()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            FileManager.deletePageFromCache(context, "samples" + "/" + "all", SampleRepository.samplesPage, 15);
                        }

                        if (SampleRepository.samplesPage == 1) {
                            FileManager.writeObjectToCache(context, successBody, "samples" + "/" + "all");

                        } else {
                            try {
                                JSONObject jsonObject = FileManager.readObjectFromCache(context, "samples" + "/" + "all");
                                JSONArray data = jsonObject.getJSONArray("data");
                                for (int i = 0; i < successBody.getJSONArray("data").length(); i++) {
                                    JSONArray jsonArray = successBody.getJSONArray("data");
                                    data.put(jsonArray.getJSONObject(i));
                                }
                                jsonObject.put("data", data);
                                FileManager.writeObjectToCache(context, jsonObject, "samples" + "/" + "all");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        if (SampleRepository.samplesPage == 1) {
                            SampleRepository.getAll.clear();
                        }
                        JSONArray data = successBody.getJSONArray("data");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject = data.getJSONObject(i);
                            SampleRepository.getAll.add(new Model(jsonObject));
                        }
                        SampleRepository.meta = successBody.getJSONObject("meta");
                    }
                } else if (SampleRepository.samplesPage == 1) {
                    SampleRepository.getAll.clear();
                    FileManager.deletePageFromCache(context, "samples" + "/" + "all");
                }

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "all");
                SampleRepository.workStateSample.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "all");
                SampleRepository.workStateSample.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            SampleRepository.workStateSample.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            SampleRepository.workStateSample.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
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

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "answers");
                SampleRepository.workStateAnswer.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                repository.insertRemoteToLocal();

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "answers");
                SampleRepository.workStateAnswer.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            SampleRepository.workStateAnswer.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            SampleRepository.workStateAnswer.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
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

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "prerequisite");
                SampleRepository.workStateAnswer.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "prerequisite");
                SampleRepository.workStateAnswer.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            SampleRepository.workStateAnswer.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            SampleRepository.workStateAnswer.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            SampleRepository.workStateAnswer.postValue(0);
        }
    }

    private void create() {
        try {
            Call<ResponseBody> call = sampleApi.create(token(), SampleRepository.createData);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "create");
                SampleRepository.workStateCreate.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "create");
                SampleRepository.workStateCreate.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            SampleRepository.workStateCreate.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            SampleRepository.workStateCreate.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            SampleRepository.workStateCreate.postValue(0);
        }
    }

    private void close() {
        try {
            Call<ResponseBody> call = sampleApi.close(token(), SampleRepository.sampleId);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "close");
                SampleRepository.workStateSample.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "close");
                SampleRepository.workStateSample.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            SampleRepository.workStateSample.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            SampleRepository.workStateSample.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
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

                            FileManager.writeObjectToCache(context, data, "sampleDetail" + "/" + SampleRepository.sampleId);

                            ExceptionGenerator.getException(true, response.code(), successBody, "score");
                            SampleRepository.workStateSample.postValue(1);
                        }
                    } else {
                        JSONObject errorBody = new JSONObject(response.errorBody().string());

                        ExceptionGenerator.getException(true, response.code(), errorBody, "score");
                        SampleRepository.workStateSample.postValue(0);
                    }
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();

                    ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
                    SampleRepository.workStateSample.postValue(0);
                } catch (IOException e) {
                    e.printStackTrace();

                    ExceptionGenerator.getException(false, 0, null, "IOException");
                    SampleRepository.workStateSample.postValue(0);
                } catch (JSONException e) {
                    e.printStackTrace();

                    ExceptionGenerator.getException(false, 0, null, "JSONException");
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
            Call<ResponseBody> call = sampleApi.getScales(token(), SampleRepository.scalesPage, SampleRepository.scalesQ);
            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                if (successBody.getJSONArray("data").length() != 0) {
                    if (SampleRepository.scalesQ.equals("")) {
                        if (SampleRepository.scalesPage == 1) {
                            FileManager.writeObjectToCache(context, successBody, "scales");
                        } else {
                            JSONObject jsonObject = FileManager.readObjectFromCache(context, "scales");
                            JSONArray data = jsonObject.getJSONArray("data");
                            for (int i = 0; i < successBody.getJSONArray("data").length(); i++) {
                                JSONArray jsonArray = successBody.getJSONArray("data");
                                data.put(jsonArray.getJSONObject(i));
                            }
                            FileManager.writeObjectToCache(context, jsonObject, "scales");
                        }
                    } else {
                        if (SampleRepository.scalesPage == 1) {
                            SampleRepository.scales.clear();
                        }
                        for (int i = 0; i < successBody.getJSONArray("data").length(); i++) {
                            JSONObject object = successBody.getJSONArray("data").getJSONObject(i);
                            SampleRepository.scales.add(new Model(object));
                        }
                    }
                } else if (SampleRepository.scalesPage == 1) {
                    SampleRepository.scales.clear();
                    FileManager.deletePageFromCache(context, "samples" + "/" + "all");
                }

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "scales");
                SampleRepository.workStateCreate.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "scales");
                SampleRepository.workStateCreate.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            SampleRepository.workStateCreate.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            SampleRepository.workStateCreate.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
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

                FileManager.writeObjectToCache(context, data, "sampleDetail" + "/" + SampleRepository.sampleId);

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "generals");
                SampleRepository.workStateSample.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "generals");
                SampleRepository.workStateSample.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            SampleRepository.workStateSample.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            SampleRepository.workStateSample.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
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

                            FileManager.writeObjectToCache(context, data, "sampleDetailFiles" + "/" + SampleRepository.sampleId);
                            ExceptionGenerator.getException(true, response.code(), successBody, "scores");
                            SampleRepository.workStateSample.postValue(1);
                        }
                    } else {
                        JSONObject errorBody = new JSONObject(response.errorBody().string());

                        ExceptionGenerator.getException(true, response.code(), errorBody, "scores");
                        SampleRepository.workStateSample.postValue(0);
                    }

                } catch (SocketTimeoutException e) {
                    e.printStackTrace();

                    ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
                    SampleRepository.workStateSample.postValue(0);
                } catch (IOException e) {
                    e.printStackTrace();

                    ExceptionGenerator.getException(false, 0, null, "IOException");
                    SampleRepository.workStateSample.postValue(0);
                } catch (JSONException e) {
                    e.printStackTrace();

                    ExceptionGenerator.getException(false, 0, null, "JSONException");
                    SampleRepository.workStateSample.postValue(0);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

}