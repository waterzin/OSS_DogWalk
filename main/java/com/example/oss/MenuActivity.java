package com.example.oss;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MenuActivity extends AppCompatActivity {

    private FragmentManager fm;
    private Fragment walkFrag = null;
    private HomemenuFragment homemenuFrag;
    private MapFragment mapFrag;
    private ProfileFragment profileFrag;
    private Fragment activeFragment; // 현재 보이는 프래그먼트

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setItemIconTintList(null);

        fm = getSupportFragmentManager();

        homemenuFrag = new HomemenuFragment();
        mapFrag = new MapFragment();
        profileFrag = new ProfileFragment();

        // 한 번에 add + hide + show
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.menu_frame, profileFrag, "PROFILE").hide(profileFrag);
        ft.add(R.id.menu_frame, mapFrag, "MAP").hide(mapFrag);
        ft.add(R.id.menu_frame, homemenuFrag, "HOME");
        ft.commit();

        activeFragment = homemenuFrag;

        // 탭 선택 시 프래그먼트 전환
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_map) {
                switchFragment(mapFrag);
                return true;
            } else if (id == R.id.action_profile) {
                switchFragment(profileFrag);
                return true;
            } else if (id == R.id.action_home) {
                switchFragment(homemenuFrag);
                return true;
            }
            return false;
        });

        // 기본 탭 선택
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }


    private void switchFragment(Fragment targetFragment) {
        if (activeFragment != targetFragment) {
            FragmentTransaction ft = fm.beginTransaction();

            // add()된 적 없는 프래그먼트는 먼저 add()
            if (!targetFragment.isAdded()) {
                ft.add(R.id.menu_frame, targetFragment);
            }

            ft.hide(activeFragment)
                    .show(targetFragment)
                    .commit();

            activeFragment = targetFragment;
        }
    }

    public void setActiveFragment(Fragment fragment) {
        this.activeFragment = fragment;
        if (fragment instanceof WalkStartFragment) {
            walkFrag = fragment; // 산책 중인 상태 저장
        }
    }

    public Fragment getActiveFragment() {
        return activeFragment;
    }
    public void showActivityList() {
        FragmentTransaction ft = fm.beginTransaction();

        Fragment activityFrag = fm.findFragmentByTag("ACTIVITY");
        if (activityFrag == null) {
            activityFrag = new MyActivityFragment();
            ft.add(R.id.menu_frame, activityFrag, "ACTIVITY");
        }

        ft.hide(activeFragment);  // 기존 탭 숨기고
        ft.show(activityFrag);    // 새로운 전체 화면 보여줌
        ft.addToBackStack(null);  // 뒤로가기 가능하게

        ft.commit();
        activeFragment = activityFrag; // 현재 활성 프래그먼트 갱신!
    }



}
