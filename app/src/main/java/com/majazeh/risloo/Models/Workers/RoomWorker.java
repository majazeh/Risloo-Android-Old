package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Apis.RoomApi;
import com.majazeh.risloo.Models.Apis.SampleApi;
import com.majazeh.risloo.Models.Repositories.RoomRepository;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
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

public class RoomWorker extends Worker {
    // Apis
    private RoomApi roomApi;

    // Repository
    private RoomRepository repository;

    // Objects
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public RoomWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;

        roomApi = RetroGenerator.getRetrofit().create(RoomApi.class);

        repository = new RoomRepository();

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
                case "getRooms":
                    getRooms();
                    break;
                case "getMyRooms":
                    getMyRooms();
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

    private void getRooms() {
        try {
            Call<ResponseBody> call = roomApi.getRooms(token(), RoomRepository.roomsPage, RoomRepository.roomQ);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONArray data = successBody.getJSONArray("data");
                JSONObject jsonObject = FileManager.readObjectFromCache(context, "rooms");

                if (data.length() != 0) {
                    if (RoomRepository.roomQ.equals("")){
                        if (RoomRepository.roomsPage == 1) {
                            FileManager.writeObjectToCache(context, successBody, "rooms");
                        } else {
                            for (int i = 0; i < data.length(); i++) {
                                JSONArray jsonArray = successBody.getJSONArray("data");
                                data.put(jsonArray.getJSONObject(i));
                            }
                            jsonObject.put("data", data);
                            FileManager.writeObjectToCache(context, jsonObject, "rooms");
                        }
                    }else{
                        if (RoomRepository.roomsPage == 1) {
                            RoomRepository.rooms.clear();
                        }
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            RoomRepository.rooms.add(new Model(object));
                        }
                    }

                }else if (SampleRepository.roomsPage == 1) {
                    RoomRepository.rooms.clear();
                }

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "rooms");
                RoomRepository.workStateCreate.postValue(1);
                RoomRepository.workStateSample.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "rooms");
                RoomRepository.workStateCreate.postValue(0);
                RoomRepository.workStateSample.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            RoomRepository.workStateCreate.postValue(0);
            RoomRepository.workStateSample.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            RoomRepository.workStateCreate.postValue(0);
            RoomRepository.workStateSample.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            RoomRepository.workStateCreate.postValue(0);
            RoomRepository.workStateSample.postValue(0);
        }
    }
    private void getMyRooms() {
        try {
            Call<ResponseBody> call = roomApi.getMyRooms(token(), RoomRepository.myRoomsPage, RoomRepository.roomQ);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                JSONArray data = successBody.getJSONArray("data");
                JSONObject jsonObject = FileManager.readObjectFromCache(context, "myRooms");

                if (data.length() != 0) {
                    if (RoomRepository.roomQ.equals("")){
                        if (RoomRepository.myRoomsPage == 1) {
                            FileManager.writeObjectToCache(context, successBody, "myRooms");
                        } else {
                            for (int i = 0; i < data.length(); i++) {
                                JSONArray jsonArray = successBody.getJSONArray("data");
                                data.put(jsonArray.getJSONObject(i));
                            }
                            jsonObject.put("data", data);
                            FileManager.writeObjectToCache(context, jsonObject, "myRooms");
                        }
                    }else{
                        if (RoomRepository.myRoomsPage == 1) {
                            RoomRepository.myRooms.clear();
                        }
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject object = data.getJSONObject(i);
                            RoomRepository.myRooms.add(new Model(object));
                        }
                    }

                }else if (RoomRepository.myRoomsPage == 1) {
                    RoomRepository.myRooms.clear();
                }

                ExceptionGenerator.getException(true, bodyResponse.code(), successBody, "rooms");
                RoomRepository.workStateCreate.postValue(1);
                RoomRepository.workStateSample.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                ExceptionGenerator.getException(true, bodyResponse.code(), errorBody, "rooms");
                RoomRepository.workStateCreate.postValue(0);
                RoomRepository.workStateSample.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "SocketTimeoutException");
            RoomRepository.workStateCreate.postValue(0);
            RoomRepository.workStateSample.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "IOException");
            RoomRepository.workStateCreate.postValue(0);
            RoomRepository.workStateSample.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            ExceptionGenerator.getException(false, 0, null, "JSONException");
            RoomRepository.workStateCreate.postValue(0);
            RoomRepository.workStateSample.postValue(0);
        }
    }

}