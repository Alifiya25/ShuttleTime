package com.example.shuttletimesport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RiwayatBookingCustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ CEK SESI LOGIN
        SharedPreferences sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE);
        int userId = sharedPref.getInt("id_user", -1);
        String role = sharedPref.getString("role", null);

        if (userId == -1 || role == null || !role.equalsIgnoreCase("customer")) {
            Toast.makeText(this, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show();
            finish(); // tutup activity
            return;
        }

        // 👇 Tetap lanjut ke tampilan kalau login valid
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_riwayat_booking_customer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
