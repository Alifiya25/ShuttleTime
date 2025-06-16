package com.example.shuttletimesport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private EditText usernameInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi input & tombol
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginButton);
        TextView registerLink = findViewById(R.id.registerLink);

        // Arahkan ke halaman register jika klik "Belum punya akun?"
        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        // Tombol login ditekan
        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            // Validasi input kosong
            if (TextUtils.isEmpty(username)) {
                usernameInput.setError("Email atau Username harus diisi");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                passwordInput.setError("Password harus diisi");
                return;
            }

            loginUser(username, password);
        });
    }

    // Fungsi untuk login ke server
    private void loginUser(String username, String password) {
        String url = "https://shuttletime.my.id/login.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Raw Response: " + response); // Cek respons mentah

                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        // Validasi jika format response tidak memiliki key 'success'
                        if (!jsonResponse.has("success")) {
                            Toast.makeText(this, "Format response tidak valid", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (jsonResponse.getBoolean("success")) {
                            JSONObject user = jsonResponse.getJSONObject("user");

                            // Simpan data ke SharedPreferences
                            SharedPreferences sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("id_user", user.getInt("id_user"));
                            editor.putString("nama_lengkap", user.optString("nama_lengkap", user.getString("username")));
                            editor.putString("role", user.getString("role"));
                            editor.putBoolean("is_logged_in", true); // Tandai sudah login
                            editor.apply();

                            // Debug log isi SharedPreferences
                            Log.d(TAG, "Sukses login. id_user: " + user.getInt("id_user") + ", role: " + user.getString("role"));

                            // Redirect ke dashboard sesuai role
                            redirectUser(user.getString("role"));

                        } else {
                            String errorMsg = jsonResponse.has("message")
                                    ? jsonResponse.getString("message") : "Login gagal";
                            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Error: " + e.getMessage());
                        Toast.makeText(this, "Data tidak valid: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Network Error: " + error.getMessage());
                    Toast.makeText(this, "Gagal terhubung ke server: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                // Parameter yang dikirim ke server
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        // Kirim request
        Volley.newRequestQueue(this).add(request);
    }

    // Redirect ke halaman dashboard sesuai role
    private void redirectUser(String role) {
        Intent intent;

        switch (role.toLowerCase()) {
            case "admin":
                intent = new Intent(this, DashboardAdminActivity.class);
                break;

            case "customer":
                intent = new Intent(this, DashboardCustomerActivity.class);
                break;

            default:
                // Jika role tidak dikenali
                Toast.makeText(this, "Role tidak dikenali: " + role, Toast.LENGTH_SHORT).show();
                return;
        }

        startActivity(intent);
        finish(); // Selesai login, jangan kembali ke sini lagi
    }
}
