package com.example.shuttletimesport;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.google.android.material.button.MaterialButton;


public class RiwayatBookingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RiwayatBookingAdapter adapter;
    private List<Booking> bookingList;
    private ProgressBar progressBar;

    private static final String URL_RIWAYAT = "http://10.0.2.2/shuttletime_api/get_bookings.php?status=selesai";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_booking);

        // Inisialisasi tombol kembali
        MaterialButton btnKembali = findViewById(R.id.btnKembali);
        btnKembali.setOnClickListener(v -> finish()); // Tutup activity saat tombol diklik

        recyclerView = findViewById(R.id.recyclerViewRiwayat);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingList = new ArrayList<>();
        adapter = new RiwayatBookingAdapter(this, bookingList);
        recyclerView.setAdapter(adapter);

        loadRiwayat();
    }

    private void loadRiwayat() {
        progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_RIWAYAT, null,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    bookingList.clear();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    adapter.notifyDataSetChanged();
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RiwayatBookingActivity.this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(request);
    }
}
