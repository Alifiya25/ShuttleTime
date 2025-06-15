package com.example.shuttletimesport;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KelolaBookingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    StatusBookingAdapter adapter; // ✅ GANTI ke adapter admin
    List<Booking> bookingList;

    String URL_GET = "http://192.168.1.9/shuttletime_api/get_bookings.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_booking);

        // Tombol kembali
        Button btnKembali = findViewById(R.id.btnKembali);
        btnKembali.setOnClickListener(v -> finish());

        // Setup RecyclerView
        recyclerView = findViewById(R.id.rvBooking);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingList = new ArrayList<>();

        // ✅ Gunakan StatusBookingAdapter dan beri nilai isAdmin = true
        adapter = new StatusBookingAdapter(bookingList, true);
        recyclerView.setAdapter(adapter);

        loadBookings();
    }

    private void loadBookings() {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_GET, null,
                response -> {
                    Log.d("KelolaBooking", "Response: " + response.toString());
                    bookingList.clear();

                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Booking b = new Booking(
                                    obj.getInt("id"),
                                    obj.getString("nama"),
                                    obj.getString("tanggal"),
                                    obj.getString("jam_mulai"),
                                    obj.getString("jam_selesai"),
                                    obj.getInt("id_lapangan"),
                                    obj.getString("status"),
                                    obj.getString("nama_lapangan"),
                                    obj.getDouble("total_harga")
                            );
                            bookingList.add(b);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    adapter.notifyDataSetChanged();
                },
                error -> {
                    Toast.makeText(this, "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    Log.e("KelolaBooking_ERROR", error.toString());
                });

        Volley.newRequestQueue(this).add(request);
    }
}
