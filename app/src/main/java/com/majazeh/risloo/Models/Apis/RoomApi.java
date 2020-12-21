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
    @POST("rooms")
    Call<ResponseBody> create(@Header("Authorization") String authorization, @Body HashMap body);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @GET("rooms/{room_id}/users?status=accepted")
    Call<ResponseBody> getReferences(@Header("Authorization") String authorization, @Path("room_id") String roomId, @Query("q") String q, @Query("usage") String usage, @Query("not_in_case") String notInCase);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("rooms/{room_id}/users")
    Call<ResponseBody> getUsers(@Header("Authorization") String authorization, @Path("room_id") String roomId, @Query("page") int page);

    @Headers({"content-type: application/json", "Accept-Language:fa"})
    @POST("rooms/{room_id}/users")
    Call<ResponseBody> addUser(@Header("Authorization") String authorization, @Path("room_id") String roomId, @Body HashMap body);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("centers?my_position=manager&type=counseling_center")
    Call<ResponseBody> getCounselingCenters(@Header("Authorization") String authorization,@Query("q") String q);

    @Headers({"content-type: application/x-www-form-urlencoded", "Accept-Language:fa"})
    @GET("centers/{center_id}/users?position=manager,operator,psychologist,under_supervision&has_room=no")
    Call<ResponseBody> getPsychologists(@Header("Authorization") String authorization,@Path("center_id") String centerId,@Query("q") String q);
}
