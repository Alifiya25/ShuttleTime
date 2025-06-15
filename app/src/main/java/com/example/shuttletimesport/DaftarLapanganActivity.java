package com.example.shuttletimesport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DaftarLapanganActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LapanganAdapter adapter;
    private List<Lapangan> lapanganList;
    private static final String URL = "http://10.0.2.2/shuttletime_api/get_lapangan.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_daftar_lapangan);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_daftar_lapangan), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Tombol kembali
        ImageView backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(v -> finish());

        // Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerLapangan);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        lapanganList = new ArrayList<>();
        adapter = new LapanganAdapter(this, lapanganList);
        recyclerView.setAdapter(adapter);

        // Listener klik item lapangan → buka detail
        adapter.setOnItemClickListener(lapangan -> {
            Intent intent = new Intent(DaftarLapanganActivity.this, DetailLapanganActivity.class);
            intent.putExtra("id", lapangan.getId());
            intent.putExtra("nama", lapangan.getNama());
            intent.putExtra("jenis", lapangan.getJenis());
            intent.putExtra("harga", lapangan.getHarga());
            intent.putExtra("fasilitas", lapangan.getFasilitas());
            intent.putExtra("gambar", lapangan.getGambar());
            startActivity(intent);
        });

        // Ambil data dari server
        loadLapanganData();

        // Tombol tambah lapangan
        FloatingActionButton btnTambahLapangan = findViewById(R.id.btnTambahLapangan);
        btnTambahLapangan.setOnClickListener(v -> {
            Intent intent = new Intent(DaftarLapanganActivity.this, TambahLapanganActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    private void loadLapanganData() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        JSONArray jsonArray = response.getJSONArray("lapangan");
                        lapanganList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            int id = obj.getInt("id");
                            String nama = obj.getString("nama_lapangan");
                            String jenis = obj.getString("jenis");
                            int harga = obj.getInt("harga_per_jam");
                            String fasilitas = obj.getString("fasilitas");
                            String gambar = obj.getString("gambar");

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

        queue.add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadLapanganData(); // Refresh data setelah tambah lapangan
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadLapanganData();
    }



}
