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
    StatusBookingAdapter adapter; // Adapter untuk menampilkan list booking
    List<Booking> bookingList;

    String URL_GET = "https://shuttletime.my.id/get_bookings.php";

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

                            Booking b = new Booking(); // Constructor kosong
                            b.setId(obj.getInt("id"));
                            b.setNama(obj.getString("nama"));
                            b.setTanggal(obj.getString("tanggal"));
                            b.setJamMulai(obj.getString("jam_mulai"));
                            b.setJamSelesai(obj.getString("jam_selesai"));
                            b.setIdLapangan(obj.getInt("id_lapangan"));
                            b.setStatus(obj.getString("status"));
                            b.setNamaLapangan(obj.getString("nama_lapangan"));
                            b.setTotalHarga(obj.getDouble("total_harga"));

                            // Jika field id_user ada di JSON, bisa di-set juga
                            if (obj.has("id_user")) {
                                b.setIdUser(obj.getInt("id_user"));
                            }

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
