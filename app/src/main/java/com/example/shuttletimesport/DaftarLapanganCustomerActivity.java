package com.example.shuttletimesport;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

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

public class DaftarLapanganCustomerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LapanganUserAdapter adapter;
    private List<Lapangan> lapanganList;
    private static final String URL = "https://shuttletime.my.id/get_lapangan.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_lapangan_customer);

        // Tombol Back
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // RecyclerView
        recyclerView = findViewById(R.id.recyclerViewLapangan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lapanganList = new ArrayList<>();
        adapter = new LapanganUserAdapter(this, lapanganList);
        recyclerView.setAdapter(adapter);

        // Ambil Data
        loadLapangan();
    }

    private void loadLapangan() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        JSONArray dataArray = response.getJSONArray("lapangan");
                        lapanganList.clear();
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject obj = dataArray.getJSONObject(i);
                            int id = obj.getInt("id");
                            String nama = obj.getString("nama_lapangan");
                            String jenis = obj.getString("jenis");
                            int harga = obj.getInt("harga_per_jam");
                            String fasilitas = obj.getString("fasilitas");
                            String gambar = obj.getString("gambar");

                            // Log untuk debug
                            Log.d("CEK_NAMA", nama);
                            Log.d("CEK_HARGA", String.valueOf(harga));

                            lapanganList.add(new Lapangan(id, nama, jenis, harga, fasilitas, gambar));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Gagal parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Gagal ambil data dari server", Toast.LENGTH_SHORT).show();
                });

        Volley.newRequestQueue(this).add(request);
    }
}
