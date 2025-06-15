package com.example.shuttletimesport;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class DashboardAdminActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;

    LinearLayout cardKelolaLapangan, cardKelolaBooking, cardKelolaJadwal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        drawerLayout = findViewById(R.id.admin_drawer_layout);
        toolbar = findViewById(R.id.admin_toolbar);
        navigationView = findViewById(R.id.admin_navigation_view);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        cardKelolaLapangan = findViewById(R.id.cardKelolaLapangan);
        cardKelolaBooking = findViewById(R.id.cardKelolaBooking);
        cardKelolaJadwal = findViewById(R.id.cardKelolaJadwal);  // Tambahan id cardKelolaJadwal

        cardKelolaLapangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardAdminActivity.this, DaftarLapanganActivity.class);
                startActivity(intent);
            }
        });

        cardKelolaBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardAdminActivity.this, StatusBookingActivity.class);
                intent.putExtra("isAdmin", true); // Kirim status admin
                startActivity(intent);
            }
        });


        cardKelolaJadwal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardAdminActivity.this, KelolaJadwalActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(DashboardAdminActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
