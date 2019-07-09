package com.example.mughees.chat_app.Admin_Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mughees.chat_app.Adapter.fame_Adapter;
import com.example.mughees.chat_app.Model.fame;
import com.example.mughees.chat_app.R;
import com.example.mughees.chat_app.addFamousStudents;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Admin_Hallof_fame extends Fragment {
    FloatingActionButton btn_add_famoususer;
    private RecyclerView recyclerView;
    private List<fame> mfame;
    private fame_AdapterAdmin adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin__hallof_fame,container,false);
        recyclerView  = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mfame = new ArrayList<>();
        readFamousUser();

        btn_add_famoususer = view.findViewById(R.id.add_fame);
        btn_add_famoususer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFamousUser();
            }
        });
        return view;
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
                adapter = new fame_AdapterAdmin(getContext(),mfame);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addFamousUser() {
        Intent intent = new Intent(getContext(),addFamousStudents.class);
        startActivity(intent);
    }
}
