package com.example.shuttletimesport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils; // Tambahkan import ini
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

        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginButton);
        TextView registerLink = findViewById(R.id.registerLink);

        // Pindah ke halaman register
        registerLink.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        // Tombol login
        loginButton.setOnClickListener(v -> {
            String username = usernameInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Username dan password harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(username, password);
        });
    }

    private void loginUser(String username, String password) {
        String url = "http://192.168.1.9/shuttletime_api/login.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d(TAG, "Raw Response: " + response); // Debugging
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        if (!jsonResponse.has("success")) {
                            Toast.makeText(this, "Format response tidak valid", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (jsonResponse.getBoolean("success")) {
                            JSONObject user = jsonResponse.getJSONObject("user");

                            // Simpan data user dengan pengecekan field
                            SharedPreferences sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("id_user", user.getInt("id_user"));

                            // Gunakan username jika nama_lengkap tidak ada
                            String nama = user.has("nama_lengkap") ? user.getString("nama_lengkap") : user.getString("username");
                            editor.putString("nama_lengkap", nama);

                            editor.putString("role", user.getString("role"));
                            editor.apply();

                            // Debug saved data
                            Log.d(TAG, "Saved user data - ID: " + user.getInt("id_user") +
                                    ", Name: " + nama + ", Role: " + user.getString("role"));

                            redirectUser(user.getString("role"));
                        } else {
                            String errorMsg = jsonResponse.has("message") ?
                                    jsonResponse.getString("message") : "Login gagal";
                            Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Error: " + e.getMessage());
                        Toast.makeText(this, "Format data tidak valid: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Network Error: " + error.getMessage());
                    Toast.makeText(this, "Gagal terhubung ke server: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

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
                Toast.makeText(this, "Role tidak dikenali", Toast.LENGTH_SHORT).show();
                return;
        }
        startActivity(intent);
        finish();
    }
}