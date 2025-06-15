package com.example.shuttletimesport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailLapanganCustomerActivity extends AppCompatActivity {

    TextView tvNama, tvJenis, tvHarga, tvFasilitas;
    ImageView ivGambar;
    Button btnBooking; // Tambahan

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_lapangan_customer);

        tvNama = findViewById(R.id.tvNama);
        tvJenis = findViewById(R.id.tvJenis);
        tvHarga = findViewById(R.id.tvHarga);
        tvFasilitas = findViewById(R.id.tvFasilitas);
        ivGambar = findViewById(R.id.ivGambar);
        btnBooking = findViewById(R.id.btnBooking); // Ambil tombol booking dari layout

        // Ambil objek Lapangan dari intent
        Lapangan lapangan = (Lapangan) getIntent().getSerializableExtra("lapangan");

        if (lapangan != null) {
            tvNama.setText(lapangan.getNama());
            tvJenis.setText("Jenis: " + lapangan.getJenis());
            tvHarga.setText("Harga: Rp " + lapangan.getHarga() + " / jam");
            tvFasilitas.setText("Fasilitas: " + lapangan.getFasilitas());

            Glide.with(this)
                    .load("http://shuttletime.rf.gd/uploads/" + lapangan.getGambar())
                    .placeholder(R.drawable.placeholder)
                    .into(ivGambar);



            // Booking Button Logic
            btnBooking.setOnClickListener(v -> {
                Intent intent = new Intent(DetailLapanganCustomerActivity.this, BookingActivity.class);
                intent.putExtra("lapangan", lapangan); // Kirim data lapangan

                SharedPreferences sharedPref = getSharedPreferences("user_session", MODE_PRIVATE);
                String namaUser = sharedPref.getString("nama", "");
                intent.putExtra("nama_user", namaUser); // Kirim nama user juga

                startActivity(intent);
            });

        }

        // Tombol kembali
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
}
