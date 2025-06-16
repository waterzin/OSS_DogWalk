package com.example.oss;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class WalkDetailFragment extends Fragment {

    private TextView textDateTime, textDuration, textSteps, textDistance, textMemo;
    private ImageButton buttonDelete;
    private ImageView iconEditMemo;

    private WalkRecord walkRecord;
    private String recordKey;

    public WalkDetailFragment() {} // 기본 생성자

    public static WalkDetailFragment newInstance(WalkRecord record, String key) {
        WalkDetailFragment fragment = new WalkDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("walkRecord", record);
        args.putString("recordKey", key);
        fragment.setArguments(args);
        return fragment;

    }

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
        View view = inflater.inflate(R.layout.activity_walk_detail, container, false);

        // 뷰 연결
        textDateTime = view.findViewById(R.id.text_date_time);
        textDuration = view.findViewById(R.id.text_duration);
        textSteps = view.findViewById(R.id.text_steps);
        textDistance = view.findViewById(R.id.text_distance);
        textMemo = view.findViewById(R.id.text_memo);
        buttonDelete = view.findViewById(R.id.button_delete);
        iconEditMemo = view.findViewById(R.id.icon_edit_memo);

        // 데이터 받기
        if (getArguments() != null) {
            walkRecord = (WalkRecord) getArguments().getSerializable("walkRecord");
            recordKey = getArguments().getString("recordKey");

            if (walkRecord != null) {
                // 데이터 바인딩
                textDateTime.setText(walkRecord.getDate());
                textDuration.setText(walkRecord.getDuration());
                textSteps.setText(String.format("%,d", walkRecord.getSteps()));
                textDistance.setText(String.format("%.1fkm", walkRecord.getDistance()));
                textMemo.setText(walkRecord.getMemo());
            }
        }

        // 삭제 버튼
        buttonDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle("삭제")
                    .setMessage("이 산책 기록을 정말 삭제할까요?")
                    .setPositiveButton("삭제", (dialog, which) -> {
                        FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(FirebaseAuth.getInstance().getUid())
                                .collection("walks")
                                .document(recordKey)
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getContext(), "삭제 완료", Toast.LENGTH_SHORT).show();

                                    // 이전 화면으로 돌아가기
                                    requireActivity().getSupportFragmentManager().popBackStack();
                                });
                    })
                    .setNegativeButton("취소", null)
                    .show();

        });

        iconEditMemo.setOnClickListener(v -> {
            final EditText input = new EditText(getContext());
            input.setText(textMemo.getText());

            new AlertDialog.Builder(getContext())
                    .setTitle("메모 수정")
                    .setView(input)
                    .setPositiveButton("저장", (dialog, which) -> {
                        String newMemo = input.getText().toString();
                        textMemo.setText(newMemo);

                        // Firestore로 메모 업데이트
                        FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(FirebaseAuth.getInstance().getUid())
                                .collection("walks")
                                .document(recordKey)
                                .update("memo", newMemo)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(getContext(), "메모가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "수정 실패: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    })
                    .setNegativeButton("취소", null)
                    .show();
        });


        return view;
    }
}
