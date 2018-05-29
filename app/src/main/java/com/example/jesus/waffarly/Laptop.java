package com.example.jesus.waffarly;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Laptop extends Fragment {

    private View view;
    private RecyclerView laptopRecyclerView;
    MainRecyclerView m = new MainRecyclerView();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.laptop, container, false);
        laptopRecyclerView = view.findViewById(R.id.laptopRecyclerView);
        laptopRecyclerView.setHasFixedSize(true);
        laptopRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        laptopRecyclerView.setAdapter(m.getRecycler(container.getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        m.getData("Laptop",getActivity());
    }
}
