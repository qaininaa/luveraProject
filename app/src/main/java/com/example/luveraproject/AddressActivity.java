package com.example.luveraproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddressActivity extends AppCompatActivity {

    private EditText editAddress;
    private Button btnSave;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        editAddress = findViewById(R.id.editAddress);
        btnSave = findViewById(R.id.btnSaveAddress);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        userRef.child("address").get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                editAddress.setText(snapshot.getValue(String.class));
            }
        });


        btnSave.setOnClickListener(v -> {
            String newAddress = editAddress.getText().toString().trim();
            if (newAddress.isEmpty()) {
                Toast.makeText(this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                userRef.child("address").setValue(newAddress)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(this, "Address saved to database", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}
