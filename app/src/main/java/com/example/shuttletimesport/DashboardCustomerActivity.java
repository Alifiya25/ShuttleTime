package com.example.shuttletimesport;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

public class DashboardCustomerActivity extends AppCompatActivity {

    private MaterialToolbar topAppBar;
    private MaterialCardView cardLapangan, cardBooking, cardHistory;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_customer);

        // Inisialisasi view
        topAppBar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // Atur toolbar sebagai ActionBar
        setSupportActionBar(topAppBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Tombol hamburger buka drawer
        topAppBar.setNavigationOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Listener menu drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    Toast.makeText(DashboardCustomerActivity.this, "Beranda", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {
                    Toast.makeText(DashboardCustomerActivity.this, "Keluar dari aplikasi", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DashboardCustomerActivity.this, LoginActivity.class));
                    finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Inisialisasi card view menu
        cardLapangan = findViewById(R.id.cardLapangan);
        cardBooking = findViewById(R.id.cardBooking);
        cardHistory = findViewById(R.id.cardStatusBooking);

        // Navigasi antar activity dari card menu
        cardLapangan.setOnClickListener(v -> {
            startActivity(new Intent(DashboardCustomerActivity.this, DaftarLapanganCustomerActivity.class));
        });

        cardBooking.setOnClickListener(v -> {
            startActivity(new Intent(DashboardCustomerActivity.this, RiwayatBookingActivity.class));
        });

        cardHistory.setOnClickListener(v -> {
            startActivity(new Intent(DashboardCustomerActivity.this, StatusBookingActivity.class));
        });
    }

    // Menangani tombol back jika drawer terbuka
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
