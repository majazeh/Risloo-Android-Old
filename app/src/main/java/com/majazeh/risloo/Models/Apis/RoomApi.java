package com.majazeh.risloo.Models.Apis;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface RoomApi {
    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @GET("rooms")
    Call<ResponseBody> getRooms(@Header("Authorization") String authorization, @Query("page") int page, @Query("q") String q);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @GET("rooms?my=yes")
    Call<ResponseBody> getMyRooms(@Header("Authorization") String authorization, @Query("page") int page, @Query("q") String q);

}
