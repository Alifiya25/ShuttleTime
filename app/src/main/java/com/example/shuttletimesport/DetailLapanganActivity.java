package com.example.shuttletimesport;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

public class DetailLapanganActivity extends AppCompatActivity {

    TextView tvNamaDetail, tvJenisDetail, tvHargaDetail, tvFasilitasDetail;
    ImageView ivGambarDetail;
    Button btnEdit, btnHapus;

    int idLapangan;
    String nama, jenis, fasilitas, gambar;
    int harga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lapangan);

        // Inisialisasi Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarDetail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Detail Lapangan");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inisialisasi view
        tvNamaDetail = findViewById(R.id.tvNamaDetail);
        tvJenisDetail = findViewById(R.id.tvJenisDetail);
        tvHargaDetail = findViewById(R.id.tvHargaDetail);
        tvFasilitasDetail = findViewById(R.id.tvFasilitasDetail);
        ivGambarDetail = findViewById(R.id.ivGambarDetail);
        btnEdit = findViewById(R.id.btnEdit);
        btnHapus = findViewById(R.id.btnHapus);

        // Ambil data dari intent
        Intent intent = getIntent();
        idLapangan = intent.getIntExtra("id", -1);
        nama = intent.getStringExtra("nama");
        jenis = intent.getStringExtra("jenis");
        harga = intent.getIntExtra("harga", 0);
        fasilitas = intent.getStringExtra("fasilitas");
        gambar = intent.getStringExtra("gambar");

        // Validasi
        if (idLapangan == -1) {
            Toast.makeText(this, "ID Lapangan tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Tampilkan data
        tampilkanData();

        // Tombol Edit
        btnEdit.setOnClickListener(view -> {
            Intent editIntent = new Intent(DetailLapanganActivity.this, EditLapanganActivity.class);
            editIntent.putExtra("id", idLapangan);
            editIntent.putExtra("nama", nama);
            editIntent.putExtra("jenis", jenis);
            editIntent.putExtra("harga", harga);
            editIntent.putExtra("fasilitas", fasilitas);
            editIntent.putExtra("gambar", gambar);
            startActivityForResult(editIntent, 1);
        });

        // Tombol Hapus
        btnHapus.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Konfirmasi Hapus")
                    .setMessage("Apakah Anda yakin ingin menghapus lapangan ini?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        hapusLapanganDariServer(idLapangan);
                    })
                    .setNegativeButton("Batal", null)
                    .show();
        });
    }

    private void tampilkanData() {
        tvNamaDetail.setText(nama);
        tvJenisDetail.setText("Jenis: " + jenis);
        tvHargaDetail.setText("Rp " + harga + " / jam");
        tvFasilitasDetail.setText("Fasilitas: " + fasilitas);

        Glide.with(this)
                .load("http://10.0.2.2/shuttletime_api/uploads/" + gambar)
                .into(ivGambarDetail);
    }

    private void hapusLapanganDariServer(int id) {
        String url = "http://10.0.2.2/shuttletime_api/delete_lapangan.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(DetailLapanganActivity.this, "Lapangan berhasil dihapus", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("deleted", true);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                },
                error -> {
                    Toast.makeText(DetailLapanganActivity.this, "Gagal menghapus lapangan", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            idLapangan = data.getIntExtra("id", idLapangan);
            nama = data.getStringExtra("nama");
            jenis = data.getStringExtra("jenis");
            harga = data.getIntExtra("harga", harga);
            fasilitas = data.getStringExtra("fasilitas");
            gambar = data.getStringExtra("gambar");

            tampilkanData();
            Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
