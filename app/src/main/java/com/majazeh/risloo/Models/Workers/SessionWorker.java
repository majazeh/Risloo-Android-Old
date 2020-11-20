package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Apis.CaseApi;
import com.majazeh.risloo.Models.Apis.SessionApi;
import com.majazeh.risloo.Models.Repositories.CaseRepository;
import com.majazeh.risloo.Models.Repositories.RoomRepository;
import com.majazeh.risloo.Models.Repositories.SessionRepository;
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

public class SessionWorker extends Worker {
    // Apis
    private SessionApi sessionApi;

    // Objects
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SessionWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.context = context;

        sessionApi = RetroGenerator.getRetrofit().create(SessionApi.class);

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
                case "update":
                    update();
                    break;
            }
        }

        return Result.success();
    }

    public void getAll() {
        try {
            Call<ResponseBody> call = sessionApi.getAll(token(), SessionRepository.page, SessionRepository.Q);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                try {
                    JSONObject successBody = new JSONObject(bodyResponse.body().string());
                    if (successBody.getJSONArray("data").length() != 0) {
                        if (SessionRepository.Q.equals("")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                FileManager.deletePageFromCache(context, "sessions", SessionRepository.page, 15);
                            }

                            if (SessionRepository.page == 1) {
                                FileManager.writeObjectToCache(context, successBody, "sessions");

                            } else {
                    JSONObject jsonObject = FileManager.readObjectFromCache(context, "sessions");
                    JSONArray data = jsonObject.getJSONArray("data");
                                for (int i = 0; i < successBody.getJSONArray("data").length(); i++) {
                                    JSONArray jsonArray = successBody.getJSONArray("data");
                                    data.put(jsonArray.getJSONObject(i));
                                }
                                jsonObject.put("data", data);
                                FileManager.writeObjectToCache(context, jsonObject, "sessions");

                            }
                        } else {
                            if (SessionRepository.page == 1) {
                                SessionRepository.sessions.clear();
                            }
                            JSONArray data = successBody.getJSONArray("data");

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject object = data.getJSONObject(i);
                                SessionRepository.sessions.add(new Model(object));
                            }

                        }
                    } else if (SessionRepository.page == 1) {
                        SessionRepository.sessions.clear();
                    }

                    ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "all");
                    SessionRepository.workState.postValue(1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "sessions");
                SessionRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            SessionRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            SessionRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            SessionRepository.workState.postValue(0);
        }
    }

    public void getGeneral() {
        try {
            Call<ResponseBody> call = sessionApi.getGeneral(token(), SessionRepository.sessionId);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONObject data = successBody.getJSONObject("data");

                FileManager.writeObjectToCache(context, data, "sessionDetail" + "/" + SessionRepository.sessionId);

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "generals");
                SessionRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "generals");
                SessionRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            SessionRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            SessionRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            SessionRepository.workState.postValue(0);
        }
    }

    private void create() {
        try {
            Call<ResponseBody> call = sessionApi.create(token(), RoomRepository.roomId, SessionRepository.createData);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "create");
                SessionRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "create");
                SessionRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            SessionRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            SessionRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            SessionRepository.workState.postValue(0);
        }
    }

    private void update() {
        try {
            Call<ResponseBody> call = sessionApi.update(token(), SessionRepository.sessionId, SessionRepository.updateData);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "update");
                SessionRepository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "update");
                SessionRepository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            SessionRepository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            SessionRepository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            SessionRepository.workState.postValue(0);
        }
    }

    private String token() {
        if (!sharedPreferences.getString("token", "").equals("")) {
            return "Bearer " + sharedPreferences.getString("token", "");
        }
        return "";
    }
}
