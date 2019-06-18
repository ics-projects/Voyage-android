package com.example.voyage.data.network.retrofit;

import com.example.voyage.auth.VoyageUser;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface VoyageService {
    @POST("login")
    @FormUrlEncoded
    Observable<VoyageUser> login(@Field("email") String email,
                                 @Field("password") String password);

    @POST("register")
    Observable<VoyageUser> register(@Body JsonObject object);
}
