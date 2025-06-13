package com.example.shuttletimesport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardAdminActivity extends AppCompatActivity {

    LinearLayout cardKelolaLapangan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        // Hubungkan dengan ID di layout
        cardKelolaLapangan = findViewById(R.id.cardKelolaLapangan);

        // Aksi ketika diklik
        cardKelolaLapangan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardAdminActivity.this, DaftarLapanganActivity.class);
                startActivity(intent);
            }
        });
    }
}
