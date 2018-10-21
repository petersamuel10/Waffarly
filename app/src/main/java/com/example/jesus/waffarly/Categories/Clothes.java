package com.example.jesus.waffarly.Categories;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jesus.waffarly.Model.Offer;
import com.example.jesus.waffarly.R;
import com.example.jesus.waffarly.ViewHolder.OfferViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Clothes extends Fragment {

    private View view;
    public RecyclerView ClothesRecyclerView;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    public RecyclerView.LayoutManager layoutManager;

    com.example.jesus.waffarly.Adapter.Adapter adapter;

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.clothes, container, false);
        ClothesRecyclerView = view.findViewById(R.id.clothesRecyclerView);

        ClothesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Offer");

        adapter = new com.example.jesus.waffarly.Adapter.Adapter(Offer.class,
                R.layout.item,
                OfferViewHolder.class,
                databaseReference.orderByChild("categoryId").equalTo("Clothes"));

        adapter.notifyDataSetChanged();
        ClothesRecyclerView.setAdapter(adapter);
        return view;
    }
}

