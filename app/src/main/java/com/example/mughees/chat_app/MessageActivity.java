package com.example.mughees.chat_app;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mughees.chat_app.Adapter.MessageAdapter;
import com.example.mughees.chat_app.Model.Chat;
import com.example.mughees.chat_app.Model.User;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission.CALL_PHONE;

public class MessageActivity extends AppCompatActivity {

    CircleImageView profile_image;
    TextView username;
    FirebaseUser fuser;
    Intent intent;
    DatabaseReference reference;
    TextView text_send,studet_status;
    ImageButton btn_send,btn_Call;
    MessageAdapter messageAdapter;
    List<Chat> mChat;
    String userId , phoneNumberOfOthe,phoneStatus;
    RecyclerView recyclerView;
    ValueEventListener valueEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MessageActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        profile_image = findViewById(R.id.profile_image);
        username  = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        btn_Call = findViewById(R.id.btn_call);
        studet_status = findViewById(R.id.userstatus);
        text_send = findViewById(R.id.text_send);
        intent = getIntent();
        userId = intent.getStringExtra("userId");
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String masg = text_send.getText().toString();
                if (!masg.equals("")){
                    send_message(fuser.getUid(),userId,masg);
                }
                else {
                    Toast.makeText(getApplicationContext(),"You can't send the empty massege",Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });
        btn_Call.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                MakeCall();
            }
        });


        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUser_name());
                if (user.getImageUrl().equals("default")){
                    profile_image.setImageResource(R.drawable.profile_image);

                }
                else
                {
                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(profile_image);
                }
                receiveMasg(fuser.getUid(),userId,user.getImageUrl());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("User").child(userId).child("student status");
        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                studet_status.setText("("+dataSnapshot.getValue().toString()+")");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        seenMessage(userId);
    }



    private void MakeCall() {  
       DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
       reference.child(userId).child("Phone status").addValueEventListener(new ValueEventListener() {

           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

               if (dataSnapshot.getValue()!=null){
                  phoneStatus= dataSnapshot.getValue().toString();
                   if (phoneStatus.equals("public")){
                       make_a_call();
                   }
                   else {
                           Toast.makeText(getApplicationContext(),"This user sets his phone number as private ",Toast.LENGTH_LONG).show();
                   }
               }
               else
                   Toast.makeText(getApplicationContext(),"null",Toast.LENGTH_LONG).show();
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });


    }

    private void make_a_call() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(userId).child("Phone Number").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    phoneNumberOfOthe = dataSnapshot.getValue().toString();
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:"+phoneNumberOfOthe));
                    if (ContextCompat.checkSelfPermission(getApplicationContext(),CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                        startActivity(i);
                    }
                    else {
                        requestPermissions(new String[]{CALL_PHONE},1);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void seenMessage(final String userid){
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat  = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String , Object> hashMap = new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot.getRef().updateChildren(hashMap);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    private void send_message(String sender,String receiver,String message){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen",false);

        databaseReference.child("Chats").push().setValue(hashMap);
        final DatabaseReference chatref = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid())
                .child(userId);
        chatref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    chatref.child("id").setValue(userId);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void receiveMasg(final String my_id, final String user_id, final String imageUrl){
        mChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mChat.clear();
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if(chat.getReceiver().equals(my_id) && chat.getSender().equals(user_id)||
                            chat.getReceiver().equals(user_id) && chat.getSender().equals(my_id)){
                        mChat.add(chat);
                    }

                    messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imageUrl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("User").child(fuser.getUid());
        HashMap<String ,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("Online");

    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(valueEventListener);
        status("Offline");
    }


}
