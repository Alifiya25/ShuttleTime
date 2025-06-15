package com.example.shuttletimesport;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditLapanganActivity extends AppCompatActivity {

    TextInputEditText etEditNama, etEditHarga, etEditFasilitas;
    Spinner spinnerEditJenis;
    ImageView ivEditGambar;
    Button btnPilihGambar, btnSimpan;

    int idLapangan;
    String nama, jenis, fasilitas, gambar;
    int harga;

    Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_lapangan);

        etEditNama = findViewById(R.id.etEditNamaLapangan);
        etEditHarga = findViewById(R.id.etEditHargaPerJam);
        etEditFasilitas = findViewById(R.id.etEditFasilitas);
        spinnerEditJenis = findViewById(R.id.spinnerEditJenis);
        ivEditGambar = findViewById(R.id.ivEditGambar);
        btnPilihGambar = findViewById(R.id.btnEditPilihGambar);
        btnSimpan = findViewById(R.id.btnEditSimpan);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.jenis_lapangan, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEditJenis.setAdapter(adapter);

        Intent intent = getIntent();
        idLapangan = intent.getIntExtra("id", -1);
        nama = intent.getStringExtra("nama");
        jenis = intent.getStringExtra("jenis");
        harga = intent.getIntExtra("harga", 0);
        fasilitas = intent.getStringExtra("fasilitas");
        gambar = intent.getStringExtra("gambar");

        if (idLapangan == -1) {
            Toast.makeText(this, "Data tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etEditNama.setText(nama);
        etEditHarga.setText(String.valueOf(harga));
        etEditFasilitas.setText(fasilitas);
        Glide.with(this)
                .load("http://shuttletime.rf.gd/uploads/" + gambar)
                .into(ivEditGambar);

        int indexJenis = adapter.getPosition(jenis);
        spinnerEditJenis.setSelection(indexJenis);

        btnPilihGambar.setOnClickListener(v -> {
            Intent pickIntent = new Intent(Intent.ACTION_GET_CONTENT);
            pickIntent.setType("image/*");
            startActivityForResult(pickIntent, 100);
        });

        btnSimpan.setOnClickListener(v -> {
            String namaBaru = etEditNama.getText().toString().trim();
            String hargaBaruStr = etEditHarga.getText().toString().trim();
            String fasilitasBaru = etEditFasilitas.getText().toString().trim();
            String jenisBaru = spinnerEditJenis.getSelectedItem().toString();

            if (TextUtils.isEmpty(namaBaru) || TextUtils.isEmpty(hargaBaruStr) || TextUtils.isEmpty(fasilitasBaru)) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            int hargaBaru = Integer.parseInt(hargaBaruStr);

            updateLapanganToServer(idLapangan, namaBaru, jenisBaru, hargaBaru, fasilitasBaru, gambar);
        });


        findViewById(R.id.btnEditBack).setOnClickListener(view -> finish());
    }

    private void updateLapanganToServer(int id, String nama, String jenis, int harga, String fasilitas, String gambar) {
        String url = "http://shuttletime.rf.gd/update_lapangan.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        boolean success = json.getBoolean("success");

                        if (success) {
                            Toast.makeText(this, "Update berhasil!", Toast.LENGTH_SHORT).show();

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("id", id);
                            resultIntent.putExtra("nama", nama);
                            resultIntent.putExtra("jenis", jenis);
                            resultIntent.putExtra("harga", harga);
                            resultIntent.putExtra("fasilitas", fasilitas);
                            resultIntent.putExtra("gambar", gambar);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        } else {
                            Toast.makeText(this, "Gagal update: " + json.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, "JSON Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> Toast.makeText(this, "Volley Error: " + error.getMessage(), Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("nama", nama);
                params.put("jenis", jenis);
                params.put("harga", String.valueOf(harga));
                params.put("fasilitas", fasilitas);
                params.put("gambar", gambar);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            ivEditGambar.setImageURI(selectedImageUri);
            // Tambahkan upload ke server jika kamu ingin mengupdate gambar juga
        }
    }
}
