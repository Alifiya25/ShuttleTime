package com.example.shuttletimesport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

        // ✅ CEK APAKAH USER SUDAH LOGIN
        SharedPreferences sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE);
        int userId = sharedPref.getInt("id_user", -1);
        String role = sharedPref.getString("role", null);

        Log.d("DASHBOARD_PREF", "userId: " + userId + ", role: " + role); // 👈 Tambahan debug

        // ❗Jika belum login atau bukan customer, kembalikan ke LoginActivity
        if (userId == -1 || role == null || !role.equalsIgnoreCase("customer")) {
            Toast.makeText(this, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show();

            // Untuk jaga-jaga, hapus juga session yang salah
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.apply();

            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // ✅ Inisialisasi view
        topAppBar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        // ✅ Atur toolbar sebagai ActionBar
        setSupportActionBar(topAppBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // ✅ Tombol hamburger untuk membuka drawer
        topAppBar.setNavigationOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // ✅ Listener untuk item navigasi drawer
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Toast.makeText(DashboardCustomerActivity.this, "Beranda", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.nav_logout) {
                    Toast.makeText(DashboardCustomerActivity.this, "Keluar dari aplikasi", Toast.LENGTH_SHORT).show();

                    // ✅ Hapus data login dari SharedPreferences
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.clear();
                    editor.apply();

                    // ✅ Kembali ke LoginActivity dan bersihkan back stack
                    Intent intent = new Intent(DashboardCustomerActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                // Tutup drawer setelah klik
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // ✅ Inisialisasi card menu
        cardLapangan = findViewById(R.id.cardLapangan);
        cardBooking = findViewById(R.id.cardBooking);
        cardHistory = findViewById(R.id.cardStatusBooking);

        // ✅ Navigasi ke halaman lain sesuai card
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

    // ✅ Menangani tombol back jika drawer terbuka
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
