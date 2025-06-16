package com.example.oss;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReviewWriteFragment extends Fragment {

    private RatingBar ratingBar;
    private EditText editDetail;
    private TextView textSubmit;
    private float rating = 0;


    private String placeId = "yuuniversity";
    private String placeName = "영남대";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_write, container, false);


        Bundle args = getArguments();
        if (args != null) {
            placeId = args.getString("placeId", "unknown_place");
            placeName = args.getString("placeName", "알 수 없는 장소");
        }

        ratingBar = view.findViewById(R.id.ratingBar);
        editDetail = view.findViewById(R.id.edit_detail);
        textSubmit = view.findViewById(R.id.text_submit);

        // 별점 리스너
        ratingBar.setOnRatingBarChangeListener((bar, newRating, fromUser) -> rating = newRating);

        // 등록 버튼
        textSubmit.setOnClickListener(v -> {
            String reviewText = editDetail.getText().toString().trim();
            if (rating == 0 || reviewText.isEmpty()) {
                Toast.makeText(getContext(), "별점과 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            saveReviewToFirestore(rating, reviewText);
        });

        return view;
    }

    private void saveReviewToFirestore(float rating, String content) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(getContext(), "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = user.getUid();
        long timestamp = System.currentTimeMillis();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Firestore에서 사용자 이름 조회
        db.collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (!documentSnapshot.exists()) {
                        Toast.makeText(getContext(), "사용자 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String userName = documentSnapshot.getString("username");

                    // 리뷰 데이터 구성
                    Map<String, Object> reviewData = new HashMap<>();
                    reviewData.put("userId", uid);
                    reviewData.put("userName", userName);
                    reviewData.put("placeId", placeId);
                    reviewData.put("placeName", placeName);
                    reviewData.put("rating", rating);
                    reviewData.put("content", content);
                    reviewData.put("timestamp", timestamp);

                    // 1. 내 리뷰 저장
                    db.collection("users")
                            .document(uid)
                            .collection("reviews")
                            .add(reviewData);

                    // 2. 장소 리뷰 저장
                    db.collection("places")
                            .document(placeId)
                            .collection("reviews")
                            .add(reviewData)
                            .addOnSuccessListener(doc -> {
                                Toast.makeText(getContext(), "리뷰가 등록되었습니다!", Toast.LENGTH_SHORT).show();
                                requireActivity().getSupportFragmentManager().popBackStack(); // 작성 완료 후 뒤로가기
                            })
                            .addOnFailureListener(e ->
                                    Toast.makeText(getContext(), "저장 중 오류 발생: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "사용자 이름 조회 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }

}


