package com.example.oss;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WalkRecordAdapter extends RecyclerView.Adapter<WalkRecordAdapter.WalkLogViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(WalkRecordWithKey walkLog);
    }
    private OnItemClickListener listener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private List<WalkRecordWithKey> logList;

    public WalkRecordAdapter(List<WalkRecordWithKey> logList) {
        this.logList = logList;
    }

    public void setItems(List<WalkRecordWithKey> logs) {
        this.logList = logs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WalkLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_walk_record, parent, false);
        return new WalkLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalkLogViewHolder holder, int position) {
        WalkRecordWithKey item = logList.get(position); // 🔑 key 포함 객체
        WalkRecord record = item.getRecord();


        holder.timeText.setText(record.getDuration());

        String content = String.format("걸음수: %,d  거리: %.2fkm", record.getSteps(), record.getDistance());
        holder.contentText.setText(content);
        // 클릭 이벤트 연결
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });

    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public static class WalkLogViewHolder extends RecyclerView.ViewHolder {
        TextView timeText, contentText;
        ImageView imageView;

        public WalkLogViewHolder(@NonNull View itemView) {
            super(itemView);
            timeText = itemView.findViewById(R.id.text_walk_time);
            contentText = itemView.findViewById(R.id.text_walk_content);
            imageView = itemView.findViewById(R.id.image_walk);
        }
    }
}

