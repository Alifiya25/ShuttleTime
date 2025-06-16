package com.example.shuttletimesport;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TambahLapanganActivity extends AppCompatActivity {

    EditText etNamaLapangan, etHargaPerJam, etFasilitas;
    Spinner spinnerJenis;
    ImageView ivGambar;
    Button btnPilihGambar, btnSimpan;
    Bitmap bitmap;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String UPLOAD_URL = "https://shuttletime.my.id/tambah_lapangan.php"; // ✅ Ganti IP sesuai lokal

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_lapangan);

        etNamaLapangan = findViewById(R.id.etNamaLapangan);
        etHargaPerJam = findViewById(R.id.etHargaPerJam);
        etFasilitas = findViewById(R.id.etFasilitas);
        spinnerJenis = findViewById(R.id.spinnerJenis);
        ivGambar = findViewById(R.id.ivGambar);
        btnPilihGambar = findViewById(R.id.btnPilihGambar);
        btnSimpan = findViewById(R.id.btnSimpan);

        // Isi dropdown spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.jenis_lapangan, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJenis.setAdapter(adapter);

        btnPilihGambar.setOnClickListener(v -> chooseImage());

        btnSimpan.setOnClickListener(v -> uploadData());

        ImageView backButton = findViewById(R.id.btnBack);
        backButton.setOnClickListener(v -> finish());
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Pilih Gambar"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivGambar.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadData() {
        final String namaLapangan = etNamaLapangan.getText().toString().trim();
        final String jenis = spinnerJenis.getSelectedItem().toString();
        final String hargaPerJam = etHargaPerJam.getText().toString().trim();
        final String fasilitas = etFasilitas.getText().toString().trim();
        final String encodedImage = getStringImage(bitmap);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Menyimpan...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                response -> {
                    progressDialog.dismiss();
                    Toast.makeText(TambahLapanganActivity.this, response, Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(TambahLapanganActivity.this, "Gagal: " + error.toString(), Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_lapangan", namaLapangan);
                params.put("jenis", jenis);
                params.put("harga_per_jam", hargaPerJam);
                params.put("fasilitas", fasilitas);
                params.put("gambar", encodedImage);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    public String getStringImage(Bitmap bmp) {
        if (bmp == null) return "";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}
