package com.example.shuttletimesport;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("booking.php")
    Call<List<Booking>> getBookings(@Query("action") String action);

    @GET("booking.php")
    Call<List<Booking>> getBookingsByUser(@Query("id_user") int userId);
}

