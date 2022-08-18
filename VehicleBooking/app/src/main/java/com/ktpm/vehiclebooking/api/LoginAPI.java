package com.ktpm.vehiclebooking.api;
import com.ktpm.vehiclebooking.model.User;
import com.ktpm.vehiclebooking.model.UserTemp;
import com.ktpm.vehiclebooking.model.LoginModel;
import com.ktpm.vehiclebooking.model.ResponseTT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginAPI {
    Gson gson = new GsonBuilder()
            .setDateFormat("dd-MM-yyyy")
            .create();
    LoginAPI apiService = new Retrofit.Builder()
            .baseUrl("http://34.121.234.226:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(LoginAPI.class);

    @POST("login")
    Call<ResponseTT> login(@Body LoginModel loginModel);

    @POST("register")
    Call<ResponseTT> register(@Body User user);

    @POST("refreshToken")
    Call<ResponseTT> getNewToken(@Body UserTemp userTemp);

    @POST("v1/revokeToken")
    Call<ResponseTT> revokeToken(@Header("Authorization") String Authorization);
}
