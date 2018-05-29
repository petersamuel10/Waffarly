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

public class SuperMarket extends Fragment {
    private View view;
    private RecyclerView Super_MarketRecyclerView;
    MainRecyclerView m = new MainRecyclerView();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.laptop, container, false);

        Super_MarketRecyclerView = view.findViewById(R.id.superMarketRecyclerView);
        Super_MarketRecyclerView.setHasFixedSize(true);
        Super_MarketRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        Super_MarketRecyclerView.setAdapter(m.getRecycler(container.getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        m.getData("SuperMarket",getActivity());
    }
}
