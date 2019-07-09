package com.example.mughees.chat_app.Admin_Fragments;

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

import com.example.mughees.chat_app.Model.post;
import com.example.mughees.chat_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;


public class PostFragmentAdmin extends Fragment {
    FloatingActionButton floatingActionButton;
    DatabaseReference refrence;
    RecyclerView recyclerView;
    private PostAdapterAdmin postAdapterAdmin;
    private List<post> mpost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_annousment,container,false);

        recyclerView  = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mpost = new ArrayList<>();

        readPost();

        floatingActionButton = view.findViewById(R.id.add_anous);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writePost();
            }
        });
        return view;
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
                postAdapterAdmin = new PostAdapterAdmin(getContext(),mpost);
                recyclerView.setAdapter(postAdapterAdmin);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void writePost() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.write_post,null);



        final EditText title = view.findViewById(R.id.title);
        final EditText description = view.findViewById(R.id.deccript);

        FloatingActionButton floatingActionButton = view.findViewById(R.id.add_anous);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Create Announcement");
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(title.getText().toString().equals("")||description.getText().toString().equals("")){
                    Toast.makeText(getContext(),"Please enter all fields.",Toast.LENGTH_SHORT).show();
                }
                if (title.getText().toString().length()>15){
                    title.setError("Title length must be less than 15 characters");
                    title.requestFocus();
                    return;
                }
                else {
                    refrence = FirebaseDatabase.getInstance().getReference("Announcements");
                    DatabaseReference firebase  = refrence.push();
                    firebase.child("title").setValue(title.getText().toString());
                    firebase.child("description").setValue(description.getText().toString());
                    alertDialog.dismiss();
                }
            }
        });
    }


}
