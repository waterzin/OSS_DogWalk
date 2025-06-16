package com.example.oss;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WalkHomeFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_walkstart_home,container,false);
        TextView dateTextView = view.findViewById(R.id.text_date);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN);
        String today = dateFormat.format(new Date());

        dateTextView.setText(today);

        Button startWalkButton = view.findViewById(R.id.button_start_walk);
        startWalkButton.setOnClickListener(v -> {
            MenuActivity activity = (MenuActivity) getActivity();
            FragmentManager fm = activity.getSupportFragmentManager();

            Fragment walkFrag = fm.findFragmentByTag("WALK");
            if (walkFrag == null) {
                walkFrag = new WalkStartFragment();
                fm.beginTransaction()
                        .add(R.id.menu_frame, walkFrag, "WALK")
                        .hide(activity.getActiveFragment())
                        .show(walkFrag)
                        .commit();
            } else {
                fm.beginTransaction()
                        .hide(activity.getActiveFragment())
                        .show(walkFrag)
                        .commit();
            }

            activity.setActiveFragment(walkFrag);
        });



        return view;
    }
}