package com.example.shuttletimesport;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

public class DashboardCustomerActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private MaterialCardView cardLapangan, cardBooking, cardHistory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_customer);


        // Inisialisasi toolbar dan set navigation click listener
        topAppBar = findViewById(R.id.topAppBar);
        setSupportActionBar(topAppBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        topAppBar.setNavigationOnClickListener(v -> {
            Toast.makeText(DashboardCustomerActivity.this, "Menu clicked", Toast.LENGTH_SHORT).show();
        });

        // Inisialisasi card views
        cardLapangan = findViewById(R.id.cardLapangan);
        cardBooking = findViewById(R.id.cardBooking);
        cardHistory = findViewById(R.id.cardStatusBooking);

        // Klik card Lapangan ➜ Pindah ke DaftarLapanganCustomerActivity
        cardLapangan.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardCustomerActivity.this, DaftarLapanganCustomerActivity.class);
            startActivity(intent);
        });

        // Klik card Booking
        cardBooking.setOnClickListener(v -> {
            Toast.makeText(DashboardCustomerActivity.this, "Booking clicked", Toast.LENGTH_SHORT).show();
            // Misal belum ada activity-nya
            // Intent intent = new Intent(DashboardCustomerActivity.this, BookingCustomerActivity.class);
            // startActivity(intent);
        });

        // Klik card Riwayat/Status Booking
        cardHistory.setOnClickListener(v -> {
            Toast.makeText(DashboardCustomerActivity.this, "Riwayat clicked", Toast.LENGTH_SHORT).show();
            // Misal belum ada activity-nya
            // Intent intent = new Intent(DashboardCustomerActivity.this, RiwayatBookingCustomerActivity.class);
            // startActivity(intent);
        });
    }
}
