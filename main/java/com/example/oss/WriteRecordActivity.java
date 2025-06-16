package com.example.oss;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WriteRecordActivity extends AppCompatActivity {

    private TextView textDate, textDuration, textSteps, textDistance;
    private EditText editReview;
    private WalkRecord walkRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_record);

        // UI 연결
        textDate = findViewById(R.id.text_date);
        textDuration = findViewById(R.id.text_duration);
        textSteps = findViewById(R.id.text_steps);
        textDistance = findViewById(R.id.text_distance);
        editReview = findViewById(R.id.edit_record);
        TextView textSave = findViewById(R.id.text_save); // 저장 버튼 역할

        // WalkRecord 받아오기
        walkRecord = (WalkRecord) getIntent().getSerializableExtra("walkRecord");

        if (walkRecord != null) {
            textDate.setText(walkRecord.getDate());
            textDuration.setText(walkRecord.getDuration());
            textSteps.setText(String.format("%,d", walkRecord.getSteps()));
            textDistance.setText(String.format("%.1fkm", walkRecord.getDistance()));
        }

        textSave.setOnClickListener(v -> {
            String review = editReview.getText().toString().trim();
            if (review.isEmpty()) {
                Toast.makeText(this, "내용을 입력해주세요!", Toast.LENGTH_SHORT).show();
                return;
            }


            saveToDatabase(review);
        });
    }

    private void saveToDatabase(String reviewContent) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String uid = user.getUid();

        Map<String, Object> data = new HashMap<>();
        data.put("date", walkRecord.getDate());
        data.put("duration", walkRecord.getDuration());
        data.put("steps", walkRecord.getSteps());
        data.put("distance", walkRecord.getDistance());
        data.put("review", reviewContent);
        data.put("timestamp", System.currentTimeMillis());

        db.collection("users")
                .document(uid)
                .collection("walks")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "기록이 저장되었습니다!", Toast.LENGTH_SHORT).show();

                    // 홈 탭을 지정해서 MenuActivity로 이동
                    Intent intent = new Intent(WriteRecordActivity.this, MenuActivity.class);
                    intent.putExtra("target", "home");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); // 현재 액티비티 종료
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "저장에 실패했습니다: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );

    }

}
