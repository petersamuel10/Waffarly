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



public class Mobile extends Fragment {
    View view;
    private RecyclerView MobileRecyclerView;
    MainRecyclerView m = new MainRecyclerView();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.laptop, container, false);

        MobileRecyclerView = (RecyclerView)view.findViewById(R.id.mobileRecyclerView);
        MobileRecyclerView.setHasFixedSize(true);
        MobileRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        MobileRecyclerView.setAdapter(m.getRecycler(container.getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        m.getData("Mobile",getActivity());
    }
}
