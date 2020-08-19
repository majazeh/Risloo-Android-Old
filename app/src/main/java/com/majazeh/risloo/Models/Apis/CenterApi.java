package com.majazeh.risloo.Models.Apis;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CenterApi {

    @Headers({"content-type: application/x-www-form-urlencoded","Accept-Language:fa"})
    @GET("centers")
    Call<ResponseBody> getAll(@Header("Authorization") String authorization);

    @Headers({"content-type: application/x-www-form-urlencoded","Accept-Language:fa"})
    @GET("centers?my=1")
    Call<ResponseBody> getMy(@Header("Authorization") String authorization);

    @Headers({"content-type: application/x-www-form-urlencoded","Accept-Language:fa"})
    @POST("centers/{center}/request")
    Call<ResponseBody> request(@Header("Authorization") String authorization,@Path("center") String center);

}