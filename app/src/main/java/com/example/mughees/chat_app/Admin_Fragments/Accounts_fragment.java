package com.example.mughees.chat_app.Admin_Fragments;

import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.mughees.chat_app.Adapter.UserAdapter;
import com.example.mughees.chat_app.Add_Accounts;
import com.example.mughees.chat_app.MainActivity;
import com.example.mughees.chat_app.Model.User;
import com.example.mughees.chat_app.Model.account;
import com.example.mughees.chat_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class Accounts_fragment extends Fragment {
    FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private AccountAdapterAdmin userAdapter;
    private List<account> mAccount;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_accounts,container,false);
        floatingActionButton = view.findViewById(R.id.add_anous);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAccount = new ArrayList<>();

        readUSer();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),Add_Accounts.class);
                startActivity(i);

            }
        });
        return view;
    }

    private void readUSer() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Alumni");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mAccount.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    account ac = snapshot.getValue(account.class);
                    mAccount.add(ac);
                }
                userAdapter = new AccountAdapterAdmin(getContext(),mAccount);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
