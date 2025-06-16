package com.example.oss;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyFavoriteListFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyFavoriteAdapter adapter;
    private List<FavoritePlace> favoriteList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyFavoriteAdapter(getContext(), favoriteList);
        recyclerView.setAdapter(adapter);

        loadFavorites();

        return view;
    }

    private void loadFavorites() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;

        String uid = user.getUid();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("favorites")
                .get()
                .addOnSuccessListener(querySnapshots -> {
                    favoriteList.clear();
                    for (DocumentSnapshot doc : querySnapshots.getDocuments()) {
                        FavoritePlace place = doc.toObject(FavoritePlace.class);
                        if (place != null) {
                            favoriteList.add(place);
                        }
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}
