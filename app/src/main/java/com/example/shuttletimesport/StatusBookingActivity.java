package com.example.shuttletimesport;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusBookingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StatusBookingAdapter adapter;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_booking);

        recyclerView = findViewById(R.id.rvStatusBooking);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Ambil flag admin dari intent
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        fetchBookings();
    }

    private void fetchBookings() {
        ApiService apiService = RetrofitClientInstance.getRetrofitInstance().create(ApiService.class);
        Call<List<Booking>> call = apiService.getBookings("getAll");

        call.enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Booking> bookings = response.body();

                    // 🔍 Jika bukan admin, filter yang statusnya bukan "selesai"
                    if (!isAdmin) {
                        List<Booking> filtered = new ArrayList<>();
                        for (Booking b : bookings) {
                            if (!"selesai".equalsIgnoreCase(b.getStatus())) {
                                filtered.add(b);
                            }
                        }
                        bookings = filtered;
                    }

                    adapter = new StatusBookingAdapter(bookings, isAdmin);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(StatusBookingActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                Toast.makeText(StatusBookingActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
