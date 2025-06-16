package com.example.oss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyFavoriteAdapter extends RecyclerView.Adapter<MyFavoriteAdapter.FavoriteViewHolder> {

    private List<FavoritePlace> favoriteList;
    private Context context;

    public MyFavoriteAdapter(Context context, List<FavoritePlace> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        FavoritePlace place = favoriteList.get(position);
        holder.textPlaceName.setText(place.getPlaceName());
        holder.textPlaceAddress.setText(place.getAddress());
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView textPlaceName, textPlaceAddress;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            textPlaceName = itemView.findViewById(R.id.text_place_name);
            textPlaceAddress = itemView.findViewById(R.id.text_place_address);
        }
    }
}
