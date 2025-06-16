package com.example.shuttletimesport;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookingActivity extends AppCompatActivity {

    private static final String TAG = "BookingActivity";

    private EditText etNama, etTanggal, etJamMulai, etJamSelesai;
    private TextView tvLapangan, tvHarga;
    private Button btnBooking, btnKembali;

    private static final String URL = "https://shuttletime.my.id/booking.php";


    private int idLapangan;
    private int idUser = -1;
    private double hargaPerJam = 0;

    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        initializeViews();
        setupBackButton();
        loadUserData();
        loadLapanganData();
        setupDatePicker();
        setupTimePickers();
        setupBookingButton();
    }

    private void initializeViews() {
        etNama = findViewById(R.id.etNama);
        etTanggal = findViewById(R.id.etTanggal);
        etJamMulai = findViewById(R.id.etJamMulai);
        etJamSelesai = findViewById(R.id.etJamSelesai);
        tvLapangan = findViewById(R.id.tvLapangan);
        tvHarga = findViewById(R.id.tvHarga);
        btnBooking = findViewById(R.id.btnBooking);
        btnKembali = findViewById(R.id.btnKembali);
    }

    private void setupBackButton() {
        btnKembali.setOnClickListener(v -> finish());
    }

    private void loadUserData() {
        // Ambil data user dari Intent atau SharedPreferences
        String namaUser = getIntent().getStringExtra("nama_user");
        idUser = getIntent().getIntExtra("id_user", -1);

        if ((namaUser == null || namaUser.isEmpty()) || idUser == -1) {
            SharedPreferences sp = getSharedPreferences("user_pref", MODE_PRIVATE);
            if (namaUser == null || namaUser.isEmpty())
                namaUser = sp.getString("nama_lengkap", "");
            if (idUser == -1)
                idUser = sp.getInt("id_user", -1);
        }

        if (!namaUser.isEmpty() && idUser != -1) {
            etNama.setText(namaUser);
            etNama.setEnabled(false);
            Log.d(TAG, "User loaded: " + namaUser + " (ID: " + idUser + ")");
        } else {
            Toast.makeText(this, "Data pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "User data not found");
        }
    }

    private void loadLapanganData() {
        Lapangan lapangan = (Lapangan) getIntent().getSerializableExtra("lapangan");
        if (lapangan == null) {
            Toast.makeText(this, "Data lapangan tidak valid", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Lapangan data null");
            finish();
            return;
        }

        idLapangan = lapangan.getId();
        hargaPerJam = lapangan.getHarga();
        tvLapangan.setText(lapangan.getNama());
        Log.d(TAG, "Lapangan loaded: " + lapangan.getNama() + " (ID: " + idLapangan + "), harga: " + hargaPerJam);
    }

    private void setupDatePicker() {
        etTanggal.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog dpd = new DatePickerDialog(
                    BookingActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        // Format tanggal yyyy-MM-dd
                        String tgl = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        etTanggal.setText(tgl);
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH));
            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // Tidak bisa pilih tanggal sebelum hari ini
            dpd.show();
        });
    }

    private void setupTimePickers() {
        etJamMulai.setOnClickListener(v -> showTimePicker(true));
        etJamSelesai.setOnClickListener(v -> showTimePicker(false));
    }

    private void showTimePicker(boolean isStartTime) {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = new TimePickerDialog(
                BookingActivity.this,
                (view, hourOfDay, minute) -> {
                    String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    if (isStartTime) {
                        etJamMulai.setText(time);
                    } else {
                        etJamSelesai.setText(time);
                    }
                    updateHarga();
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true);
        tpd.show();
    }

    private void updateHarga() {
        try {
            String mulai = etJamMulai.getText().toString();
            String selesai = etJamSelesai.getText().toString();

            if (mulai.isEmpty() || selesai.isEmpty()) {
                tvHarga.setText("Rp 0");
                return;
            }

            Date start = timeFormat.parse(mulai);
            Date end = timeFormat.parse(selesai);

            if (start == null || end == null || !end.after(start)) {
                tvHarga.setText("Rp 0");
                return;
            }

            // Hitung durasi dalam jam (desimal)
            long diffMs = end.getTime() - start.getTime();
            double durasiJam = diffMs / (1000.0 * 60 * 60);

            if (durasiJam <= 0) {
                tvHarga.setText("Rp 0");
                return;
            }

            double totalHarga = durasiJam * hargaPerJam;
            tvHarga.setText(String.format(Locale.getDefault(), "Rp %,.0f", totalHarga));

        } catch (ParseException e) {
            Log.e(TAG, "Gagal parsing waktu untuk harga", e);
            tvHarga.setText("Rp 0");
        }
    }

    private void setupBookingButton() {
        btnBooking.setOnClickListener(v -> {
            if (validateForm()) {
                bookingLapangan();
            }
        });
    }

    private boolean validateForm() {
        String nama = etNama.getText().toString().trim();
        String tanggal = etTanggal.getText().toString().trim();
        String jamMulai = etJamMulai.getText().toString().trim();
        String jamSelesai = etJamSelesai.getText().toString().trim();

        if (nama.isEmpty() || tanggal.isEmpty() || jamMulai.isEmpty() || jamSelesai.isEmpty()) {
            Toast.makeText(this, "Harap lengkapi semua data", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            Date start = timeFormat.parse(jamMulai);
            Date end = timeFormat.parse(jamSelesai);

            if (start == null || end == null) {
                Toast.makeText(this, "Format jam tidak valid", Toast.LENGTH_SHORT).show();
                return false;
            }

            // Jam operasional 08:00 - 22:00
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(start);
            int startHour = calStart.get(Calendar.HOUR_OF_DAY);
            int startMinute = calStart.get(Calendar.MINUTE);

            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(end);
            int endHour = calEnd.get(Calendar.HOUR_OF_DAY);
            int endMinute = calEnd.get(Calendar.MINUTE);

            if (startHour < 8 || (endHour > 22 || (endHour == 22 && endMinute > 0))) {
                Toast.makeText(this, "Jam operasional 08:00 - 22:00", Toast.LENGTH_LONG).show();
                return false;
            }

            if (!end.after(start)) {
                Toast.makeText(this, "Jam selesai harus setelah jam mulai", Toast.LENGTH_LONG).show();
                return false;
            }

        } catch (ParseException e) {
            Toast.makeText(this, "Format jam tidak valid", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void bookingLapangan() {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest req = new StringRequest(Request.Method.POST, URL,
                response -> {
                    Log.d(TAG, "Response booking: " + response);
                    Toast.makeText(this, "Booking berhasil", Toast.LENGTH_LONG).show();
                    finish();
                },
                error -> {
                    Log.e(TAG, "Error booking", error);
                    Toast.makeText(this, "Gagal melakukan booking", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama", etNama.getText().toString());
                params.put("tanggal", etTanggal.getText().toString());
                params.put("jam_mulai", etJamMulai.getText().toString());
                params.put("jam_selesai", etJamSelesai.getText().toString());
                params.put("id_lapangan", String.valueOf(idLapangan));
                params.put("id_user", String.valueOf(idUser));
                params.put("total_harga", String.valueOf(calculateTotalHarga()));

                Log.d(TAG, "Booking params: " + params);
                return params;
            }
        };

        queue.add(req);
    }

    private double calculateTotalHarga() {
        try {
            String jamMulai = etJamMulai.getText().toString();
            String jamSelesai = etJamSelesai.getText().toString();

            Date start = timeFormat.parse(jamMulai);
            Date end = timeFormat.parse(jamSelesai);

            if (start != null && end != null && end.after(start)) {
                long diffMillis = end.getTime() - start.getTime();
                double durasiJam = diffMillis / (1000.0 * 60 * 60);
                return durasiJam * hargaPerJam;
            }
            return 0;
        } catch (ParseException e) {
            return 0;
        }
    }
}
