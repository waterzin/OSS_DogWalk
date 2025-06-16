package com.example.oss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class WalkFinishActivity extends AppCompatActivity {

    private WalkRecord walkRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_finish);

        walkRecord = (WalkRecord) getIntent().getSerializableExtra("walkRecord");

        TextView textDate = findViewById(R.id.text_date);
        MaterialButton btnWrite = findViewById(R.id.btnWrite);
        MaterialButton btnSave = findViewById(R.id.btnSave);
        TextView textSkip = findViewById(R.id.textSkip);

        if (walkRecord != null) {
            textDate.setText(walkRecord.getDate());
        }

        btnWrite.setOnClickListener(v -> {
            Intent intent = new Intent(this, WriteRecordActivity.class);
            intent.putExtra("walkRecord", walkRecord);
            startActivity(intent);
        });

        btnSave.setOnClickListener(v -> {
            saveToDatabase(walkRecord);
        });

        textSkip.setOnClickListener(v -> {
            goToHome();
        });
    }

    private void saveToDatabase(WalkRecord record) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String uid = user.getUid();

        firestore.collection("users")
                .document(uid)
                .collection("walks")
                .add(record)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "산책 기록이 저장되었습니다!", Toast.LENGTH_SHORT).show();
                    goToHome();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "저장 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void goToHome() {
        SharedPreferences prefs = getSharedPreferences("WALK_PREFS", MODE_PRIVATE);
        prefs.edit().putBoolean("isWalking", false).apply();

        Intent intent = new Intent(WalkFinishActivity.this, MenuActivity.class);
        intent.putExtra("target", "home");
        intent.putExtra("home_inner", "start");
        startActivity(intent);
        finish();
    }
}

