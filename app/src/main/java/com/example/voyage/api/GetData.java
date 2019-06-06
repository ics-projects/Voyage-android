package com.example.voyage.api;

import com.example.voyage.models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface GetData {
    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> userlogin(
            @Field("email")String email,
            @Field("password") String password
    );
}
