package com.majazeh.risloo.Models.Apis;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ExplodeApi {

    @Headers({"content-type: application/x-www-form-urlencoded","Accept-Language:fa"})
    @GET("explode")
    Call<ResponseBody> explode();

}