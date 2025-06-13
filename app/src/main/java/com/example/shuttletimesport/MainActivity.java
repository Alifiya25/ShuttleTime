package com.example.shuttletimesport;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private ImageView logo;
    private TextView selamatDatang;
    private TextView slogan;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logo);
        selamatDatang = findViewById(R.id.SelamatDatang);
        slogan = findViewById(R.id.slogan);
        startButton = findViewById(R.id.startButton);

        // Sembunyikan semua dulu
        logo.setVisibility(View.INVISIBLE);
        selamatDatang.setVisibility(View.INVISIBLE);
        slogan.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.INVISIBLE);

        // Tampilkan animasi setelah 2 detik
        new Handler().postDelayed(() -> {
            logo.setVisibility(View.VISIBLE);
            TranslateAnimation animLogo = new TranslateAnimation(0, 0, 0, -300);
            animLogo.setDuration(1000);
            animLogo.setFillAfter(true);
            logo.startAnimation(animLogo);

            new Handler().postDelayed(() -> {
                selamatDatang.setVisibility(View.VISIBLE);
                TranslateAnimation animSelamatDatang = new TranslateAnimation(500, 0, 0, 0);
                animSelamatDatang.setDuration(500);
                selamatDatang.startAnimation(animSelamatDatang);

                new Handler().postDelayed(() -> {
                    slogan.setVisibility(View.VISIBLE);
                    TranslateAnimation animSlogan = new TranslateAnimation(-500, 0, 0, 0);
                    animSlogan.setDuration(500);
                    slogan.startAnimation(animSlogan);

                    startButton.setVisibility(View.VISIBLE);
                    startButton.setOnClickListener(v -> {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    });

                }, 500);
            }, 1000);
        }, 2000);
    }
}
