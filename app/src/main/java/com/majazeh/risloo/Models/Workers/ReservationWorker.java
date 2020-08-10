package com.majazeh.risloo.Models.Workers;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.majazeh.risloo.Models.Remotes.Apis.ReservationApi;
import com.majazeh.risloo.Models.Remotes.Generators.RetroGenerator;
import com.majazeh.risloo.Models.Repositories.ReservationRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ReservationWorker extends Worker {
    ReservationRepository repository;
    private Context context;
    // Apis
    private ReservationApi reservationApi;

    // Objects
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public ReservationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        this.context = context;

        repository = new ReservationRepository();

        reservationApi = RetroGenerator.getRetrofit().create(ReservationApi.class);

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
                case "reservation":
                    reservation();
                    break;
                case "myReservation":
                    myReservation();
                    break;
                case "request":
                    request( getInputData().getString("id"));
            }
        }
        return Result.success();
    }

    private String token() {
//        if (!sharedPreferences.getString("token", "").equals("")) {
//            return "Bearer " + sharedPreferences.getString("token", "");
//        }
        return "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6IjY2NzAwNTE1OTYwZmY4OWEzYmVmMDM1ZjBlZGExOTM2MGVjNjVhZmNkNmMwNzY4NDEyYWVjZDM5MmVkY2YwZmE1ZWEzYzRmMzI2ZjNkNGYxIn0.eyJhdWQiOiIxIiwianRpIjoiNjY3MDA1MTU5NjBmZjg5YTNiZWYwMzVmMGVkYTE5MzYwZWM2NWFmY2Q2YzA3Njg0MTJhZWNkMzkyZWRjZjBmYTVlYTNjNGYzMjZmM2Q0ZjEiLCJpYXQiOjE1OTUzMjAxNTIsIm5iZiI6MTU5NTMyMDE1MiwiZXhwIjoxNjExMjIxMzUyLCJzdWIiOiI5Iiwic2NvcGVzIjpbXX0.VgWJxWpOxVY8IKQhpj25qoB8yyoQz-2Rto-LhoPoSmOOIvS3EheDr7TvwHIr-DC5hJdJASBvv3Ntpc-dp6XkyKKDQLoIsX3FUL5rMFvAj4QSbXWKOPhpfMM6zZHNjVIAtXv3XZmKw_lvpiEsXJS3F4d8QY4p7MSHZePabDiwhRV6hpHQ-QB-51RMPrjhP-7A_1VYaM7iXE9SXKnb5y2zt6m6PEQGSV8VhUjGJNZLijd65Kna39Kp2Mo2zcQFM3bU-uSwxegc3U9fnTatUlkjNYnlq7W7weHodCBaVniOuRXKJRp8OPeSlh2NIsNGQ0Q5woLToczqyzNZhS6Q3TjYvNcnYg2QmFhHtz_duVNK1EkbldqwcbY4NxwMNO0jNQ4-jPBeY3a7MWMt8KmVl7rXTCxzy2lb6V2hQGF_PmOgSAkbkRHPW8RUOWpRMXkkNZ29JOo65DUS9S86XKZzyL-XnoY-KQuJh6MfWavo_UrasYNebmsONh9KIlBkN3nlm9jhCfxN38whOiEQgghmIdtpwWlrd6CLCPmMOt61uiVO15JmZf7Hb2T9UpCMmoDD9CeUn02Uf3yuzmc-c6SAdGg83QKzGQxIQ6ZAeEbf_-wUp-IQagx8mQ6EZuR491n0Q2RfOKb2Of45FHZ9IbkoIrHw-uZjHtVnJwR7-DJ84aIQ93s";
    }

    public void reservation() {
        try {
            Call<ResponseBody> call = reservationApi.reservation(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                repository.writeReservationToCache(context, successBody, "reservation");
                repository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());
                repository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            repository.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            repository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            repository.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            repository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            repository.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            repository.workState.postValue(0);
        }
    }

    public void myReservation() {
        try {
            Call<ResponseBody> call = reservationApi.myReservation(token());

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                JSONObject successBody = new JSONObject(bodyResponse.body().string());
                repository.writeReservationToCache(context, successBody, "myReservation");
                repository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                repository.exception = errorBody.getString("message_text");
                repository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            repository.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            repository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            repository.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            repository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            repository.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            repository.workState.postValue(0);
        }
    }

    private void request(String id) {
        try {
            Call<ResponseBody> call = reservationApi.request(token(), id);

            Response<ResponseBody> bodyResponse = call.execute();
            if (bodyResponse.isSuccessful()) {
                repository.workState.postValue(1);
            } else {
                JSONObject errorBody = new JSONObject(bodyResponse.errorBody().string());

                repository.exception = errorBody.getString("message_text");
                repository.workState.postValue(0);
            }

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

            repository.exception = "مشکل ارتباط با سرور! دوباره تلاش کنید.";
            repository.workState.postValue(0);
        } catch (JSONException e) {
            e.printStackTrace();

            repository.exception = "مشکل دریافت JSON! دوباره تلاش کنید.";
            repository.workState.postValue(0);
        } catch (IOException e) {
            e.printStackTrace();

            repository.exception = "مشکل دریافت IO! دوباره تلاش کنید.";
            repository.workState.postValue(0);
        }
    }
}
