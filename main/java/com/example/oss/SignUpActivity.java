package com.example.oss;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailEdit;
    private EditText passwordEdit;
    private EditText nameEdit;
    private Button registerButton;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initView();
        registerUser();
    }

    private void initView() {
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        emailEdit = findViewById(R.id.emailEditText);
        nameEdit = findViewById(R.id.nameEditText);
        passwordEdit = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
    }

    private void registerUser() {
        registerButton.setOnClickListener(v -> {
            String email = emailEdit.getText().toString();
            String password = passwordEdit.getText().toString();
            String name = nameEdit.getText().toString();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
                Toast.makeText(SignUpActivity.this, "모든 필드를 입력하세요", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SignUpActivity.this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            if (firebaseUser != null) {
                                String uid = firebaseUser.getUid();
                                saveUserData(uid, name, email);
                            }
                            Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            navigateToLogin();
                        } else {
                            Toast.makeText(SignUpActivity.this, "회원가입 실패: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void saveUserData(String uid, String name, String email) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", name);
        user.put("email", email);

        firestore.collection("users").document(uid)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d("SignUpActivity", "사용자 정보 저장 완료 (uid: " + uid + ")");
                })
                .addOnFailureListener(e -> {
                    Log.e("SignUpActivity", "문서 추가 오류", e);
                });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}

