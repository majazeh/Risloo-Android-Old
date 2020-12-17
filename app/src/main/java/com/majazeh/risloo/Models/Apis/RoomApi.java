package com.majazeh.risloo.Models.Apis;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RoomApi {
    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @GET("rooms")
    Call<ResponseBody> getRooms(@Header("Authorization") String authorization, @Query("page") int page, @Query("q") String q);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @GET("rooms?my=yes")
    Call<ResponseBody> getMyRooms(@Header("Authorization") String authorization, @Query("page") int page, @Query("q") String q);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @GET("rooms?my_management=1")
    Call<ResponseBody> getMyRoomsManagement(@Header("Authorization") String authorization, @Query("page") int page, @Query("q") String q);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @GET("rooms/{room_id}/users?status=accepted")
    Call<ResponseBody> getReferences(@Header("Authorization") String authorization, @Path("room_id") String roomId, @Query("q") String q, @Query("usage") String usage, @Query("not_in_case") String notInCase);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("rooms/{room_id}/users")
    Call<ResponseBody> getUsers(@Header("Authorization") String authorization, @Path("room_id") String roomId, @Query("page") String page);

}
