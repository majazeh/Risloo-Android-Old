package com.majazeh.risloo.Models.Apis;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SessionApi {
    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("sessions")
    Call<ResponseBody> getAll(@Header("Authorization") String authorization, @Query("page") int page, @Query("q") String q);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("sessions/{sessionId}")
    Call<ResponseBody> getGeneral(@Header("Authorization") String authorization, @Path("sessionId") String sessionId);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @POST("rooms/{roomId}/sessions")
    Call<ResponseBody> create(@Header("Authorization") String authorization, @Path("roomId") String roomId, @Body HashMap body);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @PUT("sessions/{sessionId}")
    Call<ResponseBody> update(@Header("Authorization") String authorization, @Path("sessionId") String sessionId, @Body HashMap body);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("sessions")
    Call<ResponseBody> getSessionsOfCase(@Header("Authorization") String authorization, @Query("case") String caseId, @Query("q") String q);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @PUT("sessions/{sessionId}")
    Call<ResponseBody> Report(@Header("Authorization") String authorization, @Path("sessionId") String sessionId,@Body HashMap<String,Object> body);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("sessions/{session_id}/practices")
    Call<ResponseBody> getPractices(@Header("Authorization") String authorization,@Path("session_id") String sessionId, @Query("page") int page);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @Multipart
    @POST("sessions/{session_id}/practices")
    Call<ResponseBody> createPractices(@Header("Authorization") String authorization,@Path("session_id") String sessionId,@Part MultipartBody.Part file , @Part("title") RequestBody title,  @Part("content") RequestBody content);

}
