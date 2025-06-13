package com.example.shuttletimesport;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText usernameInput, passwordInput;
    Button loginButton;
    String url = "http://10.0.2.2/shuttletime_api/login.php"; // Sesuaikan IP jika pakai HP asli


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        // ⬇️ Ini bagian yang diperbaiki
        TextView registerLink = findViewById(R.id.registerLink);
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Login Button logic
        loginButton.setOnClickListener(v -> {
            String user = usernameInput.getText().toString().trim();
            String pass = passwordInput.getText().toString().trim();

            if (!user.isEmpty() && !pass.isEmpty()) {
                login(user, pass);
            } else {
                Toast.makeText(this, "Lengkapi semua data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void login(String username, String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        Log.d("LOGIN_RESPONSE", response); // Debug log isi response

                        JSONObject obj = new JSONObject(response);

                        boolean isSuccess = obj.optBoolean("success", false);

                        if (isSuccess && obj.has("user")) {
                            JSONObject user = obj.getJSONObject("user");

                            String role = user.optString("role", "").toLowerCase().trim();
                            Log.d("ROLE_CHECK", "Role: " + role); // Tambahkan log untuk role

                            Toast.makeText(this, "Login berhasil sebagai " + role, Toast.LENGTH_SHORT).show();

                            // Redirect berdasarkan role
                            switch (role) {
                                case "admin":
                                    startActivity(new Intent(this, DashboardAdminActivity.class));
                                    break;
                                case "customer":
                                    startActivity(new Intent(this, DashboardCustomerActivity.class));
                                    break;
                                default:
                                    Toast.makeText(this, "Role tidak dikenali: " + role, Toast.LENGTH_LONG).show();
                                    return;
                            }

                            finish(); // Akhiri login activity

                        } else {
                            String msg = obj.optString("message", "Login gagal");
                            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Log.e("JSON_ERROR", "Gagal parsing JSON", e);
                        Toast.makeText(this, "Kesalahan parsing data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("LOGIN_ERROR", "Koneksi error: ", error);
                    Toast.makeText(this, "Terjadi kesalahan koneksi: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
