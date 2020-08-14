package com.majazeh.risloo.Models.Apis;

import okhttp3.ResponseBody;
import retrofit2.Call;
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

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language: fa"})
    @FormUrlEncoded
    @POST("auth")
    Call<ResponseBody> auth(@Header("Authorization") String authorization, @Field("callback") String callback, @Query("authorized_key") String authorizedKey);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language: fa"})
    @FormUrlEncoded
    @POST("auth/theory/{LoginKey}")
    Call<ResponseBody> authTheory(@Header("Authorization") String authorization, @Path("LoginKey") String loginKey, @Field("callback") String callback, @Query("password") String password, @Query("code") String code);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language: fa"})
    @POST("register")
    Call<ResponseBody> register(@Query("name") String name, @Query("mobile") String mobile, @Query("gender") String gender, @Query("password") String password);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language: fa"})
    @POST("auth/verification")
    Call<ResponseBody> verification(@Query("mobile") String mobile);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language: fa"})
    @POST("auth/recovery")
    Call<ResponseBody> recovery(@Query("username") String username);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language: fa"})
    @GET("me")
    Call<ResponseBody> me(@Header("Authorization") String authorization);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language: fa"})
    @PUT("me")
    Call<ResponseBody> edit(@Header("Authorization") String authorization, @Query("name") String name, @Query("gender") String gender, @Query("birthday") String birthday);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language: fa"})
    @POST("logout")
    Call<ResponseBody> logOut(@Header("Authorization") String authorization);

}