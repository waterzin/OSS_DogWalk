package com.example.oss;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class WalkStartFragment extends Fragment {

    private TextView textDate, textTime, textSteps, textDistance;
    private Button buttonEndWalk;

    private long startTimeMillis;
    private Handler handler = new Handler();
    private int stepCount = 0;

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            long currentTimeMillis = System.currentTimeMillis();
            long elapsed = currentTimeMillis - startTimeMillis;

            int seconds = (int) (elapsed / 1000);
            int minutes = seconds / 60;
            int remainingSeconds = seconds % 60;

            String timeText = String.format(Locale.getDefault(), "%02d:%02d", minutes, remainingSeconds);
            textTime.setText(timeText);

            stepCount++;
            textSteps.setText(String.format(Locale.getDefault(), "%,d", stepCount));

            double distanceMeters = stepCount * 0.7;
            double distanceKm = distanceMeters / 1000.0;
            textDistance.setText(String.format(Locale.getDefault(), "%.2fkm", distanceKm));

            handler.postDelayed(this, 1000);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_walk_in_progress, container, false);

        textDate = view.findViewById(R.id.text_date);
        textTime = view.findViewById(R.id.text_time);
        textSteps = view.findViewById(R.id.text_steps);
        textDistance = view.findViewById(R.id.text_distance);
        buttonEndWalk = view.findViewById(R.id.button_end_walk);

        // 날짜 표시
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.KOREA);
        textDate.setText(dateFormat.format(calendar.getTime()));


        startTimeMillis = System.currentTimeMillis();

        buttonEndWalk.setOnClickListener(v -> {
            handler.removeCallbacks(updateRunnable); // 타이머 정지
            Toast.makeText(getContext(), "산책 종료!", Toast.LENGTH_SHORT).show();

            // 날짜, 시간, 거리 텍스트뷰에서 가져오기
            String dateString = textDate.getText().toString();
            String timeString = textTime.getText().toString();
            String distanceText = textDistance.getText().toString().replace("km", "");
            double distanceKm = Double.parseDouble(distanceText);

            // WalkRecord 생성
            WalkRecord record = new WalkRecord(dateString, timeString, distanceKm, stepCount);

            // 다음 화면으로 이동
            Intent intent = new Intent(getActivity(), WalkFinishActivity.class);
            intent.putExtra("walkRecord", record);
            startActivity(intent);
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.removeCallbacks(updateRunnable); // 혹시 실행 중이던 루프 제거
        handler.post(updateRunnable);            // 하나만 시작
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(updateRunnable); // 프래그먼트 숨겨질 때 정지
    }
}

