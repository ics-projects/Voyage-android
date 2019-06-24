package com.example.voyage.data.network.retrofit;

import com.example.voyage.auth.VoyageUser;
import com.example.voyage.data.models.Schedule;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface VoyageService {
    @POST("login")
    Observable<Response<VoyageUser>> login(@Body JsonObject object);

    @GET("logout")
    Observable<Response<VoyageUser>> logout(@Header("Authorization") String authorization);

    @POST("register")
    Observable<Response<VoyageUser>> register(@Body JsonObject object);

    @Headers({
            "Accept: application/json"
    })
    @GET("user")
    Observable<Response<VoyageUser>> getUser(@Header("Authorization") String authorization);

    @Headers({
            "Accept: application/json"
    })
    @GET("schedule")
    Observable<Response<List<Schedule>>> schedules(@Header("Authorization") String authorization);
}
