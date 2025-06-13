package com.example.shuttletimesport;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailLapanganCustomerActivity extends AppCompatActivity {

    TextView tvNama, tvJenis, tvHarga, tvFasilitas;
    ImageView ivGambar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lapangan_customer);

        tvNama = findViewById(R.id.tvNama);
        tvJenis = findViewById(R.id.tvJenis);
        tvHarga = findViewById(R.id.tvHarga);
        tvFasilitas = findViewById(R.id.tvFasilitas);
        ivGambar = findViewById(R.id.ivGambar);

        // Ambil objek Lapangan
        Lapangan lapangan = (Lapangan) getIntent().getSerializableExtra("lapangan");
        if (lapangan != null) {
            tvNama.setText(lapangan.getNama());
            tvJenis.setText("Jenis: " + lapangan.getJenis());
            tvHarga.setText("Harga: Rp " + lapangan.getHarga() + " / jam");
            tvFasilitas.setText("Fasilitas: " + lapangan.getFasilitas());

            Glide.with(this)
                    .load("http://10.0.2.2/shuttletime_api/uploads/" + lapangan.getGambar())
                    .placeholder(R.drawable.placeholder)
                    .into(ivGambar);
        }
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

    }
}
