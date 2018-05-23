package com.example.jesus.waffarly;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

/**
 * Created by jesus on 5/19/2018.
 */

public class MainRecyclerView {

    private ArrayList<RecyclerModel> List;
    private RecyclerViewAdapter RecyclerAdapter;
    private FirebaseFirestore firestore;
    RecyclerModel model;

    public RecyclerViewAdapter getRecycler(Context context)
    {
        List  = new ArrayList<>();
        firestore = FirebaseFirestore.getInstance();
        RecyclerAdapter = new RecyclerViewAdapter(context,List);

        return RecyclerAdapter;
    }
    public void getData(String collection, FragmentActivity activity)
    {
        List.clear();
        firestore.collection(collection).addSnapshotListener(activity, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                // check if new account is saved in the firebase collection
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                    if (doc.getType() == DocumentChange.Type.ADDED)
                    {

                        model = doc.getDocument().toObject(RecyclerModel.class);
                        List.add(model);
                        RecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }

}
