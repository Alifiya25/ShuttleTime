package com.example.shuttletimesport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StatusBookingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StatusBookingAdapter adapter;
    private ProgressBar progressBar;
    private boolean isAdmin = false;

    private static final String BASE_URL = "https://shuttletime.my.id/get_bookings.php";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_booking);

        recyclerView = findViewById(R.id.rvStatusBooking);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        fetchBookings();
    }

    private void fetchBookings() {
        SharedPreferences prefs = getSharedPreferences("user_pref", MODE_PRIVATE);
        int userId = prefs.getInt("id_user", -1);

        if (userId == -1) {
            Toast.makeText(this, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        String url;
        if (isAdmin) {
            url = BASE_URL;
        } else {
            url = BASE_URL + "?id_user=" + userId;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    List<Booking> bookingList = new ArrayList<>();

                    try {
                        if (!response.getString("status").equals("success")) {
                            Toast.makeText(this, "Error: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                            return;
                        }

                        JSONArray dataArray = response.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject obj = dataArray.getJSONObject(i);

                            if (!isAdmin && obj.has("status") && "selesai".equalsIgnoreCase(obj.getString("status"))) {
                                continue;
                            }

                            Booking booking = new Booking();
                            booking.setId(obj.getInt("id"));
                            booking.setNama(obj.getString("nama"));
                            booking.setTanggal(obj.getString("tanggal"));
                            booking.setJamMulai(obj.getString("jam_mulai"));
                            booking.setJamSelesai(obj.getString("jam_selesai"));
                            booking.setStatus(obj.getString("status"));
                            booking.setNamaLapangan(obj.getString("nama_lapangan"));
                            booking.setTotalHarga(obj.getDouble("total_harga"));

                            bookingList.add(booking);
                        }

                        adapter = new StatusBookingAdapter(bookingList, isAdmin);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Format data tidak sesuai", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(StatusBookingActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(request);
    }
}
