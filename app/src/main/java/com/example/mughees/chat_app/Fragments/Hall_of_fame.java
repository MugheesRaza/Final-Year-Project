package com.example.mughees.chat_app.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mughees.chat_app.Adapter.fame_Adapter;
import com.example.mughees.chat_app.Model.fame;
import com.example.mughees.chat_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Hall_of_fame extends Fragment {
    RecyclerView recyclerView;
    private List<fame> mfame;
    private fame_Adapter adapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hall_of_fame,container,false);
        recyclerView  = v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mfame = new ArrayList<>();
        readFamousUser();
        return v;
    }
    private void readFamousUser() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Famous Students");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mfame.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    fame fame = snapshot.getValue(fame.class);
                    mfame.add(fame);
                }
                adapter = new fame_Adapter(getContext(),mfame);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
