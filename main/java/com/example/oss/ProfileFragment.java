package com.example.oss;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private TextView tvName;
    private LinearLayout btnEditProfile;
    private LinearLayout btnMyActivity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false); // XML 이름 변경

        tvName = view.findViewById(R.id.tv_name);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile); //수정 버튼 연결
        btnMyActivity=view.findViewById(R.id.btn_myActivity);
        loadUserName();

        // 수정 버튼 클릭 시 EditProfileFragment로 이동
        btnEditProfile.setOnClickListener(v -> {
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.menu_frame, new EditProfileFragment())
                    .addToBackStack(null)
                    .commit();
        });
        btnMyActivity.setOnClickListener(v -> {
            if (getActivity() instanceof MenuActivity) {
                ((MenuActivity) getActivity()).showActivityList();
            }
        });


        return view;
    }

    private void loadUserName() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userName = documentSnapshot.getString("username");
                        tvName.setText(userName != null ? userName : "(이름 없음)");
                    }
                })
                .addOnFailureListener(e -> {
                    tvName.setText("(이름 불러오기 실패)");
                });
    }
}

