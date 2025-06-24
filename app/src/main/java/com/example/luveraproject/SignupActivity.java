package com.example.luveraproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    EditText etUsername, etEmail, etNomorHP, etPassword;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Inisialisasi views
        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etNomorHP = findViewById(R.id.etNomorHP);
        etPassword = findViewById(R.id.etPassword);
        dbHelper = new DatabaseHelper(this);

        Button btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String noHP = etNomorHP.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || noHP.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Harap isi semua field", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean inserted = dbHelper.insertUser(username, email, noHP , password);
            if (inserted) {
                Toast.makeText(this, "Register Berhasil!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                Toast.makeText(this, "Gagal register", Toast.LENGTH_SHORT).show();
            }
        });

        // TextView login
        TextView tvLogin = findViewById(R.id.tvLogin);
        SpannableString ss = new SpannableString("Already have an account? Log in");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(android.R.color.holo_red_light));
                ds.setUnderlineText(true);
            }
        };

        ss.setSpan(clickableSpan, 25, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLogin.setText(ss);
        tvLogin.setMovementMethod(LinkMovementMethod.getInstance());
        tvLogin.setHighlightColor(Color.TRANSPARENT);
    }
}
