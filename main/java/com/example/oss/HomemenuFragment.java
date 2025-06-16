package com.example.oss;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class HomemenuFragment extends Fragment {
    private View view;
    private TabLayout tabLayout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homemenu, container, false);
        tabLayout = view.findViewById(R.id.tabs);
        Bundle args = getArguments();
        String innerTarget = args != null ? args.getString("inner") : null;
        if ("calendar".equals(innerTarget)) {
            // calendar 탭으로 강제 이동
            tabLayout.selectTab(tabLayout.getTabAt(1));
            CalendarFragment calendarFragment = new CalendarFragment();
            loadFragment(calendarFragment);


            if (getActivity() instanceof MenuActivity) {
                ((MenuActivity) getActivity()).setActiveFragment(calendarFragment);
            }

        }

        else {
            // 2. 그 외엔 isWalking 기반으로 선택
            SharedPreferences prefs = requireActivity().getSharedPreferences("WALK_PREFS", Context.MODE_PRIVATE);
            boolean isWalking = prefs.getBoolean("isWalking", false);

            if (isWalking) {
                loadFragment(new WalkStartFragment());
                tabLayout.selectTab(tabLayout.getTabAt(0)); // 탭 UI도 맞춰줌
            } else {
                loadFragment(new WalkHomeFragment());
            }
        }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selected = null;
                SharedPreferences prefs = requireActivity().getSharedPreferences("WALK_PREFS", Context.MODE_PRIVATE);
                boolean isWalking = prefs.getBoolean("isWalking", false);
                switch (tab.getPosition()) {
                    case 0:
                        selected = isWalking ? new WalkStartFragment() : new WalkHomeFragment();
                        break;
                    case 1:
                        selected = new CalendarFragment();
                        break;
                }

                if (selected != null) loadFragment(selected);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return view;
    }

    private void loadFragment(Fragment fragment){
        getChildFragmentManager().beginTransaction().replace(R.id.tabContent,fragment).commit();
    }
}