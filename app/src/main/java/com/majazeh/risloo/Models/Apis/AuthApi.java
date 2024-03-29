package com.majazeh.risloo.Models.Apis;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AuthApi {

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @FormUrlEncoded
    @POST("auth")
    Call<ResponseBody> auth(@Header("Authorization") String authorization, @Query("callback") String callback, @Field("authorized_key") String authorizedKey);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @FormUrlEncoded
    @POST("auth/theory/{LoginKey}")
    Call<ResponseBody> authTheory(@Header("Authorization") String authorization, @Path("LoginKey") String loginKey, @Query("callback") String callback, @Field("password") String password, @Field("code") String code);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @FormUrlEncoded
    @POST("register")
    Call<ResponseBody> register(@Field("name") String name, @Field("mobile") String mobile, @Field("gender") String gender, @Field("password") String password);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @FormUrlEncoded
    @POST("auth/verification")
    Call<ResponseBody> verification(@Field("mobile") String mobile);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @FormUrlEncoded
    @POST("auth/recovery")
    Call<ResponseBody> recovery(@Field("mobile") String mobile);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("me")
    Call<ResponseBody> me(@Header("Authorization") String authorization);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @POST("logout")
    Call<ResponseBody> logOut(@Header("Authorization") String authorization);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @PUT("me")
    Call<ResponseBody> editPersonal(@Header("Authorization") String authorization, @Body HashMap hashMap);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @FormUrlEncoded
    @PUT("users/{user_id}/change-password")
    Call<ResponseBody> editPassword(@Header("Authorization") String authorization, @Path("user_id") String user_id, @Field("new_password") String new_password);

}