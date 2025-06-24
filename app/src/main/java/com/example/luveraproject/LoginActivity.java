package com.example.luveraproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        DatabaseHelper db;
        db = new DatabaseHelper(this);
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            EditText etEmail = findViewById(R.id.etEmail);
            EditText etPassword = findViewById(R.id.etPassword);
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (db.checkUser(email, password)) {
                String username = db.getUsernameByEmail(email);

                SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("username", username);
                editor.apply();

                startActivity(new Intent(this, HomeActivity.class));
                finish();
            }
            else {
                Toast.makeText(this, "Login gagal. Periksa kembali data Anda.", Toast.LENGTH_SHORT).show();
            }
        });

        TextView tvRegister = findViewById(R.id.tvRegister);
        SpannableString ss = new SpannableString("Don't have an account? Register Now");

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                finish();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(android.R.color.holo_red_light));
                ds.setUnderlineText(true);
            }
        };

        ss.setSpan(clickableSpan, 23, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvRegister.setText(ss);
        tvRegister.setMovementMethod(LinkMovementMethod.getInstance());
        tvRegister.setHighlightColor(Color.TRANSPARENT);
    }
}