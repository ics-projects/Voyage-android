package com.example.voyage.data.network.retrofit;

import com.example.voyage.auth.VoyageUser;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface VoyageService {
    @POST("/login")
    @FormUrlEncoded
    Observable<VoyageUser> login(@Field("email") String email,
                                 @Field("password") String password);

    @POST("/register")
    @FormUrlEncoded
    Observable<VoyageUser> register(@Field("first_name") String firstName,
                                    @Field("last_name") String lastName,
                                    @Field("email") String email,
                                    @Field("password") String password,
                                    @Field("password_confirmation") String passwordConfirm);
}
