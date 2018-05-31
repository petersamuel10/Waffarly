package com.example.jesus.waffarly;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Clothes extends Fragment {
    private View view;
    private RecyclerView ClothesRecyclerView;
    MainRecyclerView mainRecyclerView = new MainRecyclerView();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view=inflater.inflate(R.layout.clothes, container, false);
        ClothesRecyclerView = view.findViewById(R.id.clothesRecyclerView);
        ClothesRecyclerView.setHasFixedSize(true);
        ClothesRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        ClothesRecyclerView.setAdapter(mainRecyclerView.getRecycler(container.getContext()));

    return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mainRecyclerView.getData("Clothes",getActivity());
    }
}
