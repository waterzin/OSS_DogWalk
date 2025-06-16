package com.example.oss;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<WalkRecordWithKey> walkLogs = new ArrayList<>();
    private WalkRecordAdapter adapter;
    private Calendar selectedCalendar;
    private TextView textSelectedDate;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 뒤로가기 콜백 등록
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //뒤로가기 눌렀을 때 calendar 탭으로 이동
                HomemenuFragment home = new HomemenuFragment();
                Bundle args = new Bundle();
                args.putString("inner", "calendar");
                home.setArguments(args);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.menu_frame, home)
                        .commit();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // 날짜 초기화
        selectedCalendar = Calendar.getInstance();

        // 날짜 텍스트뷰 연결 (updateDateText보다 먼저 해야 함)
        textSelectedDate = view.findViewById(R.id.text_selected_date);

        // 날짜 표시
        updateDateText();

        // 날짜 선택 다이얼로그
        textSelectedDate.setOnClickListener(v -> showDatePickerDialog());

        // RecyclerView 설정
        recyclerView = view.findViewById(R.id.recycler_walk_logs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new WalkRecordAdapter(walkLogs);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(item -> {
            WalkRecord record = item.getRecord();
            String key = item.getKey();

            WalkDetailFragment fragment = WalkDetailFragment.newInstance(record, key);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.menu_frame, fragment)
                    .addToBackStack(null)
                    .commit();
        });


        // 초기 날짜에 해당하는 기록 불러오기
        loadWalkRecordsForDate(selectedCalendar);

        return view;
    }

    private void showDatePickerDialog() {
        int year = selectedCalendar.get(Calendar.YEAR);
        int month = selectedCalendar.get(Calendar.MONTH);
        int day = selectedCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(
                requireContext(),
                (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDayOfMonth);
                    updateDateText();
                    loadWalkRecordsForDate(selectedCalendar);
                },
                year, month, day
        );

        dialog.show();
    }

    private void updateDateText() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA);
        String formattedDate = sdf.format(selectedCalendar.getTime());
        textSelectedDate.setText(formattedDate);
    }

    private void loadWalkRecordsForDate(Calendar date) {
        walkLogs.clear();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.e("Firestore", "현재 로그인되어 있지 않음! Firestore read 실패할 수 있음!");
            return;
        }

        // 날짜 포맷: WalkRecord.date와 반드시 동일하게 맞춰야 함
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일", Locale.getDefault());
        String selectedDate = sdf.format(date.getTime());

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document(uid)
                .collection("walks")
                .whereEqualTo("date", selectedDate)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        WalkRecord record = doc.toObject(WalkRecord.class);
                        String key=doc.getId();
                        if (record != null) {
                            if (record != null) {
                                walkLogs.add(new WalkRecordWithKey(record, key)); // 변환 없이 바로 담기
                            }
                        }
                    }
                    adapter.setItems(walkLogs); // 리스트 교체

                })
                .addOnFailureListener(e -> {
                    Log.e("CalendarFragment", "불러오기 실패: " + e.getMessage());
                });
    }



}
