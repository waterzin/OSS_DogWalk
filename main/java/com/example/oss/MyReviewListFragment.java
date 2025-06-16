package com.example.oss;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyReviewListFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyReviewAdapter adapter;
    private List<Review> reviewList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myreview_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_my_reviews);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyReviewAdapter(getContext(), reviewList, this::deleteReview);
        recyclerView.setAdapter(adapter);

        loadMyReviews();

        return view;
    }

    private void loadMyReviews() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("reviews")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    reviewList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Review review = doc.toObject(Review.class);
                        if (review != null) {
                            review.setId(doc.getId()); // 🔸 문서 ID 저장
                            reviewList.add(review);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void deleteReview(String reviewId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("reviews")
                .document(reviewId)
                .delete()
                .addOnSuccessListener(unused -> {
                    adapter.removeItem(reviewId);
                    Toast.makeText(getContext(), "리뷰가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "삭제 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}

