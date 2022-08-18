package com.ktpm.vehiclebooking.api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ktpm.vehiclebooking.model.Booking;
import com.ktpm.vehiclebooking.model.ResponseTT;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface BookingAPI {
    Gson gson = new GsonBuilder()
            .setDateFormat("dd-MM-yyyy")
            .create();
    com.ktpm.vehiclebooking.api.BookingAPI apiService = new Retrofit.Builder()
            .baseUrl("http://34.72.120.102:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(BookingAPI.class);

    @POST("booking")
    Call<ResponseTT> login(@Body Booking booking);


    @POST("v1/revokeToken")
    Call<ResponseTT> revokeToken(@Header("Authorization") String Authorization);
}
