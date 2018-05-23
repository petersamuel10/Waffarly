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

    View view;
 //   private ArrayList<RecyclerModel> laptopList;
    private RecyclerView laptopRecyclerView;
  //  private RecyclerViewAdapter RecyclerAdapter;
  //  private FirebaseFirestore firestore;
    MainRecyclerView m = new MainRecyclerView();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.laptop, container, false);
        laptopRecyclerView = (RecyclerView)view.findViewById(R.id.laptopRecyclerView);
      //  laptopList  = new ArrayList<>();
      //  firestore = FirebaseFirestore.getInstance();
       // RecyclerAdapter = new RecyclerViewAdapter(container.getContext(),laptopList);
        laptopRecyclerView.setHasFixedSize(true);
        laptopRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        laptopRecyclerView.setAdapter(m.getRecycler(container.getContext()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        m.getData("Laptop",getActivity());
        // clear the list to prevent repeated users
        // and get all users for first time
       // laptopList.clear();

        /*
        firestore.collection("Laptop").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                // check if new account is saved in the firebase collection
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                    if (doc.getType() == DocumentChange.Type.ADDED)
                    {
                        RecyclerModel model = doc.getDocument().toObject(RecyclerModel.class);
                        laptopList.add(model);
                        RecyclerAdapter.notifyDataSetChanged();
                    }
                }
                }
        });
        */
    }
}
