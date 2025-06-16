package com.example.oss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Iterator;
import java.util.List;

public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.ReviewViewHolder> {

    private List<Review> reviewList;
    private Context context;
    private OnDeleteClickListener deleteClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClick(String reviewId);
    }

    public MyReviewAdapter(Context context, List<Review> reviewList, OnDeleteClickListener listener) {
        this.context = context;
        this.reviewList = reviewList;
        this.deleteClickListener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);

        holder.ratingBar.setRating((float)review.getRating());
        holder.textContent.setText(review.getContent());

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteClickListener != null) {
                deleteClickListener.onDeleteClick(review.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        RatingBar ratingBar;
        TextView textContent;
        ImageView deleteButton;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            textContent = itemView.findViewById(R.id.text_content);
            deleteButton = itemView.findViewById(R.id.button_delete);
        }
    }

    public void removeItem(String reviewId) {
        Iterator<Review> iterator = reviewList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId().equals(reviewId)) {
                iterator.remove();
                notifyDataSetChanged();
                break;
            }
        }
    }
}

