package com.example.shuttletimesport;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class KelolaJadwalActivity extends AppCompatActivity {

    private static final String TAG = "KelolaJadwalActivity";

    private Button btnPilihTanggal;
    private Button btnKembali; // ✅ Tambahan tombol kembali
    private TextView tvTanggalTerpilih;
    private RecyclerView rvLapanganJadwal;

    private KelolaJadwalAdapter adapter;
    private List<KelolaJadwalAdapter.LapanganWithJadwal> lapanganList = new ArrayList<>();

    private String tanggalFilter = null;
    private static final String URL = "https://shuttletime.my.id/get_lapangan.php";

    private LinearLayout emptyState;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelola_jadwal);

        // ✅ Inisialisasi UI
        btnPilihTanggal = findViewById(R.id.btnPilihTanggal);
        btnKembali = findViewById(R.id.btnKembali); // ✅
        tvTanggalTerpilih = findViewById(R.id.tvTanggalTerpilih);
        rvLapanganJadwal = findViewById(R.id.rvLapanganJadwal);
        emptyState = findViewById(R.id.emptyState);
        progressBar = findViewById(R.id.progressBar);

        // ✅ Aksi tombol kembali
        btnKembali.setOnClickListener(v -> onBackPressed());

        adapter = new KelolaJadwalAdapter(lapanganList);
        rvLapanganJadwal.setLayoutManager(new LinearLayoutManager(this));
        rvLapanganJadwal.setAdapter(adapter);

        btnPilihTanggal.setOnClickListener(v -> showDatePickerDialog());

        fetchData();
    }

    private void showDatePickerDialog() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    tanggalFilter = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    tvTanggalTerpilih.setText("Tanggal: " + tanggalFilter);
                    fetchData();
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show();
    }

    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);
        emptyState.setVisibility(View.GONE);
        rvLapanganJadwal.setVisibility(View.GONE);

        String urlWithParam = URL;
        if (tanggalFilter != null) {
            urlWithParam += "?tanggal=" + tanggalFilter;
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, urlWithParam, null,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        String status = response.optString("status");
                        if (!"success".equals(status)) {
                            showEmptyState("Gagal mengambil data");
                            return;
                        }

                        lapanganList.clear();
                        JSONArray lapanganArr = response.getJSONArray("lapangan");

                        if (lapanganArr.length() == 0) {
                            showEmptyState("Tidak ada jadwal tersedia");
                            return;
                        }

                        for (int i = 0; i < lapanganArr.length(); i++) {
                            JSONObject lapObj = lapanganArr.getJSONObject(i);
                            String namaLapangan = lapObj.getString("nama_lapangan");
                            JSONArray jadwalArr = lapObj.getJSONArray("jadwal");

                            List<KelolaJadwalAdapter.Booking> jadwalList = new ArrayList<>();
                            for (int j = 0; j < jadwalArr.length(); j++) {
                                JSONObject jadwalObj = jadwalArr.getJSONObject(j);
                                String tanggal = jadwalObj.getString("tanggal");
                                String jamMulai = jadwalObj.getString("jam_mulai");
                                String jamSelesai = jadwalObj.getString("jam_selesai");
                                String nama = jadwalObj.getString("nama");

                                KelolaJadwalAdapter.Booking booking = new KelolaJadwalAdapter.Booking(
                                        tanggal, jamMulai, jamSelesai, nama);
                                jadwalList.add(booking);
                            }

                            lapanganList.add(new KelolaJadwalAdapter.LapanganWithJadwal(namaLapangan, jadwalList));
                        }

                        rvLapanganJadwal.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        Log.e(TAG, "Error parsing response", e);
                        showEmptyState("Gagal memproses data");
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Volley error", error);
                    showEmptyState("Gagal terhubung ke server");
                });

        queue.add(req);
    }

    private void showEmptyState(String message) {
        emptyState.setVisibility(View.VISIBLE);
        rvLapanganJadwal.setVisibility(View.GONE);
        TextView emptyText = emptyState.findViewById(R.id.emptyText);
        emptyText.setText(message);
    }
}
