package com.example.shuttletimesport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RiwayatBookingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RiwayatBookingAdapter adapter;
    private List<Booking> bookingList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_booking);

        MaterialButton btnKembali = findViewById(R.id.btnKembali);
        btnKembali.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewRiwayat);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingList = new ArrayList<>();
        adapter = new RiwayatBookingAdapter(this, bookingList);
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE);
        int userId = sharedPref.getInt("id_user", -1);

        if (userId == -1) {
            Toast.makeText(this, "Sesi login tidak valid. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String urlRiwayat = "https://shuttletime.my.id/get_bookings.php?status=selesai&id_user=" + userId;
        loadRiwayat(urlRiwayat);
    }

    private void loadRiwayat(String url) {
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    bookingList.clear();

                    try {
                        if (response.getString("status").equalsIgnoreCase("success")) {
                            JSONArray dataArray = response.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject obj = dataArray.getJSONObject(i);
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
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "Tidak ada data ditemukan", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RiwayatBookingActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(request);
    }
}
