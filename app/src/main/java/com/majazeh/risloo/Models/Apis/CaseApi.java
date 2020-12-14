package com.majazeh.risloo.Models.Apis;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface CaseApi {
    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("cases")
    Call<ResponseBody> getAll(@Header("Authorization") String authorization,@Query("room") String room, @Query("page") int page, @Query("q") String q,@Query("usage")String usage);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("cases/{caseId}")
    Call<ResponseBody> getGeneral(@Header("Authorization") String authorization, @Path("caseId") String caseId,@Query("usage")String usage);


    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @POST("rooms/{roomId}/cases")
    Call<ResponseBody> create(@Header("Authorization") String authorization, @Path("roomId") String roomId, @Body HashMap body);

}
