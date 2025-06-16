package com.example.oss;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MyActivityFragment extends Fragment {

    private LinearLayout btnReviewList, btnFavoriteList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myactivity, container, false);

        btnReviewList = view.findViewById(R.id.btn_review_list);
        btnFavoriteList = view.findViewById(R.id.btn_favorite_list);

        btnReviewList.setOnClickListener(v -> {
            MyReviewListFragment reviewFragment = new MyReviewListFragment();

            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.add(R.id.menu_frame, reviewFragment, "REVIEW");
            ft.hide(this); // 현재 화면 숨기기
            ft.addToBackStack(null);
            ft.commit();

            if (getActivity() instanceof MenuActivity) {
                ((MenuActivity) getActivity()).setActiveFragment(reviewFragment);
            }
        });

        btnFavoriteList.setOnClickListener(v -> {
            MyFavoriteListFragment favoriteFragment = new MyFavoriteListFragment();

            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.add(R.id.menu_frame, favoriteFragment, "FAVORITE");
            ft.hide(this); // 현재 화면 숨기기
            ft.addToBackStack(null);
            ft.commit();

            if (getActivity() instanceof MenuActivity) {
                ((MenuActivity) getActivity()).setActiveFragment(favoriteFragment);
            }
        });

        return view;
    }
}



