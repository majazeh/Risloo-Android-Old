package com.majazeh.risloo.Models.Remotes.Apis;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AuthApi {

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("auth")
    Call<ResponseBody> auth(@Header("Authorization") String authorization, @Field("callback") String callback, @Query("authorized_key") String authorizedKey);

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @FormUrlEncoded
    @POST("auth/theory/{LoginKey}")
    Call<ResponseBody> authTheory(@Header("Authorization") String authorization, @Path("LoginKey") String LoginKey, @Field("callback") String callback, @Query("password") String password, @Query("code") String code);

    @POST("register")
    Call<ResponseBody> register(@Query("name") String name, @Query("mobile") String mobile, @Query("gender") String gender, @Query("password") String password);

    @Headers({"content-type: application/x-www-form-urlencoded"})
    @POST("auth/verification")
    Call<ResponseBody> verification(@Query("mobile") String mobile);

}