package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Apis.CaseApi;
import com.majazeh.risloo.Models.Repositories.CaseRepository;
import com.majazeh.risloo.Models.Repositories.RoomRepository;
import com.majazeh.risloo.Utils.Generators.ExceptionGenerator;
import com.majazeh.risloo.Utils.Generators.RetroGenerator;
import com.majazeh.risloo.Utils.Managers.FileManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class CaseWorker extends Worker {
    // Apis
    private CaseApi caseApi;

    // Objects
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public CaseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

        caseApi = RetroGenerator.getRetrofit().create(CaseApi.class);

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
                case "getGeneral":
                    getGeneral();
                    break;
                case "create":
                    create();
                    break;
            }
        }

        return Result.success();
    }

    public void getAll() {
        try {
            Call<ResponseBody> call = caseApi.getAll(token(),RoomRepository.roomId,CaseRepository.page, CaseRepository.Q);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                try {
                    JSONObject successBody = new JSONObject(bodyResponse.body().string());
                    JSONObject jsonObject = FileManager.readObjectFromCache(context, "cases");
                    JSONArray data = jsonObject.getJSONArray("data");
                    if (successBody.getJSONArray("data").length() != 0) {
                        if (CaseRepository.Q.equals("") && RoomRepository.roomId.equals("")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                FileManager.deletePageFromCache(context, "cases", CaseRepository.page, 15);
                            }

                            if (CaseRepository.page == 1) {
                                FileManager.writeObjectToCache(context, successBody, "cases");

                            } else {
                                for (int i = 0; i < successBody.getJSONArray("data").length(); i++) {
                                    JSONArray jsonArray = successBody.getJSONArray("data");
                                    data.put(jsonArray.getJSONObject(i));
                                }
                                jsonObject.put("data", data);
                                FileManager.writeObjectToCache(context, jsonObject, "cases");

                            }
                        } else {
                            if (CaseRepository.page == 1) {
                                CaseRepository.cases.clear();
                            }

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                CaseRepository.cases.add(new Model(object));
                            }

                        }
                    } else if (CaseRepository.page == 1) {
                        CaseRepository.cases.clear();
                    }

                    ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "all");
                    CaseRepository.workState.postValue(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "cases");
                CaseRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            CaseRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            CaseRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            CaseRepository.workState.postValue(0);
        }
    }

    public void getGeneral() {
        try {
            Call<ResponseBody> call = caseApi.getGeneral(token(), CaseRepository.caseId);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONObject data = successBody.getJSONObject("data");

                FileManager.writeObjectToCache(context, data, "caseDetail" + "/" + CaseRepository.caseId);

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "generals");
                CaseRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "generals");
                CaseRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            CaseRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            CaseRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            CaseRepository.workState.postValue(0);
        }
    }

    private void create() {
        try {
            Call<ResponseBody> call = caseApi.create(token(), RoomRepository.roomId, CaseRepository.createData);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "create");
                CaseRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "create");
                CaseRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            CaseRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            CaseRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            CaseRepository.workState.postValue(0);
        }
    }

    private String token() {
        if (!sharedPreferences.getString("token", "").equals("")) {
            return "Bearer " + sharedPreferences.getString("token", "");
        }
        return "";
    }
}
