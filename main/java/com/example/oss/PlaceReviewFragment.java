package com.example.oss;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class PlaceReviewFragment extends Fragment {

    private TextView textPlaceTitle;
    private RecyclerView recyclerReviews;
    private String placeId;
    private String placeName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_reviews, container, false);

        textPlaceTitle = view.findViewById(R.id.text_place_title);
        recyclerReviews = view.findViewById(R.id.recycler_reviews);

        // 인자로 전달된 장소 정보 가져오기
        Bundle args = getArguments();
        if (args != null) {
            placeId = args.getString("placeId", "unknown_place");
            placeName = args.getString("placeName", "장소 이름 없음");
        }

        // 장소 이름 상단 타이틀에 표시
        textPlaceTitle.setText(placeName);

        // 이후 Firestore에서 해당 장소 리뷰 로딩 가능
        loadReviews(placeId);

        return view;
    }

    private void loadReviews(String placeId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<Review> reviewList = new ArrayList<>();
        ReviewAdapter adapter = new ReviewAdapter(reviewList);
        recyclerReviews.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerReviews.setAdapter(adapter);

        db.collection("places")
                .document(placeId)
                .collection("reviews")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    reviewList.clear();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        Review review = doc.toObject(Review.class);
                        if (review != null) {
                            reviewList.add(review);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }

}

