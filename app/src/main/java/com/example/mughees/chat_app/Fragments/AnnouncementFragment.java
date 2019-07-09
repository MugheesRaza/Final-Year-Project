package com.example.mughees.chat_app.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mughees.chat_app.Adapter.PostUserAdmin;
import com.example.mughees.chat_app.Admin_Fragments.PostAdapterAdmin;
import com.example.mughees.chat_app.MainActivity;
import com.example.mughees.chat_app.Model.post;
import com.example.mughees.chat_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnnouncementFragment extends Fragment {

    DatabaseReference refrence;
    RecyclerView recyclerView;
    private PostUserAdmin postUserAdmin;
    private List<post> mpost;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Announvement Fragment");
        View view = inflater.inflate(R.layout.fragment_announcement,container,false);
        recyclerView  = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mpost = new ArrayList<>();
        readPost();
        return  view;
    }
    private void readPost() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Announcements");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mpost.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    post post = snapshot.getValue(post.class);
                    mpost.add(post);
                }
                postUserAdmin = new PostUserAdmin(getContext(),mpost);
                recyclerView.setAdapter(postUserAdmin);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
