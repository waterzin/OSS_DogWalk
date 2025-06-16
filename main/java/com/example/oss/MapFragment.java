package com.example.oss;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment {

    private Button buttonWriteReview;
    private Button buttonViewReview;
    private ImageView bookmarkIcon;
    private boolean isBookmarked = false;
    private String placeId = "yuuniversity";
    private String placeName = "영남대";
    private String placeAddress="경상북도 경산시 대학로 280";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        bookmarkIcon = view.findViewById(R.id.ic_bookmark);
        buttonWriteReview = view.findViewById(R.id.button_write_review);

        buttonWriteReview.setOnClickListener(v -> openReviewWriteFragment());
        buttonViewReview = view.findViewById(R.id.button_view_reviews);
        checkBookmarkState();
        bookmarkIcon.setOnClickListener(v -> {
            if (isBookmarked) {
                removeBookmark();
            } else {
                addBookmark();
            }
        });
        buttonViewReview.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("placeId", placeId);      // 장소 고유 ID
            bundle.putString("placeName", placeName);       // 장소 이름
            bundle.putString("placeAddress",placeAddress);



            PlaceReviewFragment fragment = new PlaceReviewFragment();
            fragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.menu_frame, fragment)  // 실제 FrameLayout ID로 바꿔줘
                    .addToBackStack(null)
                    .commit();
            if (getActivity() instanceof MenuActivity) {
                ((MenuActivity) getActivity()).setActiveFragment(fragment);
            }
        });


        return view;
    }
    private void checkBookmarkState() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .collection("favorites")
                .document(placeId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        isBookmarked = true;
                        bookmarkIcon.setImageResource(R.drawable.ic_bookmark_filled);
                    } else {
                        isBookmarked = false;
                        bookmarkIcon.setImageResource(R.drawable.ic_bookmark_outline);
                    }
                });
    }

    private void addBookmark() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        Map<String, Object> data = new HashMap<>();
        data.put("placeName", placeName);
        data.put("address", placeAddress);

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .collection("favorites")
                .document(placeId)
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    isBookmarked = true;
                    bookmarkIcon.setImageResource(R.drawable.ic_bookmark_filled);
                });
    }

    private void removeBookmark() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.getUid())
                .collection("favorites")
                .document(placeId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    isBookmarked = false;
                    bookmarkIcon.setImageResource(R.drawable.ic_bookmark_outline);
                });
    }


    private void openReviewWriteFragment() {
        // 예: 현재 선택된 장소 정보
        String placeId = "yuuniversity";
        String placeName = "영남대";

        // 전달할 정보 묶기
        Bundle bundle = new Bundle();
        bundle.putString("placeId", placeId);
        bundle.putString("placeName", placeName);

        // 프래그먼트 생성 및 인자 설정
        ReviewWriteFragment reviewFragment = new ReviewWriteFragment();
        reviewFragment.setArguments(bundle);

        // 프래그먼트 이동
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_frame, reviewFragment)  // 실제 컨테이너 ID에 맞게 수정
                .addToBackStack(null)
                .commit();
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).setActiveFragment(reviewFragment);
        }

    }
    @Override
    public void onResume() {
        super.onResume();

        // 화면에 다시 보일 때 activeFragment를 갱신
        if (getActivity() instanceof MenuActivity) {
            ((MenuActivity) getActivity()).setActiveFragment(this);
        }
    }

}
