package com.example.voyage.data.network.retrofit;

import com.example.voyage.auth.VoyageUser;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface VoyageService {
    @POST("login")
    @FormUrlEncoded
    Observable<Response<VoyageUser>> login(@Field("email") String email,
                                           @Field("password") String password);

    @POST("register")
    Observable<Response<VoyageUser>> register(@Body JsonObject object);

    @Headers({
            "Content-Type: application/json",
            "Accept: application/json"
    })
    Observable<Response<VoyageUser>> getUser(@Header("Authorization") String authorization);
}
