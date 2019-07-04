package com.example.voyage.data.network.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class VoyageClient {
    private static final String VOYAGE_API_BASE_URL = "https://voyageweb.tk/api/";

    private static VoyageClient instance;
    private VoyageService voyageService;

    private VoyageClient() {
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(VOYAGE_API_BASE_URL)
                .client(okHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        voyageService = retrofit.create(VoyageService.class);
    }

    private OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new CustomHeaderInterceptor());
        return builder.build();
    }

    public static VoyageClient getInstance() {
        if (instance == null) {
            instance = new VoyageClient();
        }
        return instance;
    }

    public VoyageService getVoyageService() {
        return voyageService;
    }
}
