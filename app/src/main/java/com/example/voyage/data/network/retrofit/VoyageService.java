package com.example.voyage.data.network.retrofit;

import com.example.voyage.auth.VoyageUser;
import com.example.voyage.data.models.PayDetails;
import com.example.voyage.data.models.PayRequestBody;
import com.example.voyage.data.models.PickSeatBody;
import com.example.voyage.data.models.Schedule;
import com.example.voyage.data.models.Seat;
import com.example.voyage.data.models.Trip;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface VoyageService {
    @POST("login")
    Observable<Response<VoyageUser>> login(@Body JsonObject object);

    @GET("logout")
    Observable<Response<Void>> logout(@Header("Authorization") String authorization);

    @POST("register")
    Observable<Response<VoyageUser>> register(@Body JsonObject object);

    @GET("user")
    Observable<Response<VoyageUser>> getUser(@Header("Authorization") String authorization);

    @GET("schedule")
    Observable<Response<List<Schedule>>> schedules(@Header("Authorization") String authorization);

    @POST("trip")
    Observable<Response<List<Trip>>> trips(
            @Header("Authorization") String authorization, @Body JsonObject object);

    @GET("seat/{busId}")
    Observable<Response<List<Seat>>> seats(@Header("Authorization") String authToken,
                                           @Path("busId") int busId);

    @POST("bookingPhase/pickSeat")
    Observable<Response<PayDetails>> pickSeat(@Header("Authorization") String authToken,
                                              @Body PickSeatBody body);

    @POST
    Observable<Response<String>> pay(@Url String url, @Header("Authorization") String authToken,
                                     @Body PayRequestBody payRequestBody);

    @POST("fcmToken")
    Observable<Response<String>> sendFcmToken(@Header("Authorization") String authToken,
                                              @Body JsonObject fcmToken);
}
