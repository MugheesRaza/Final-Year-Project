package com.example.mughees.chat_app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class RollNumberRegisteration extends AppCompatActivity {
    NiceSpinner year, programe;
    EditText rollno;
    EditText user_password;
    FloatingActionButton floatingActionButton;
    String registeration,gmail_of_user,phonenumber,name,stusent_status;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    connectionDetector cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_number_registeration);
        cd = new connectionDetector(RollNumberRegisteration.this);
        if (cd.isConnected()){

        }
        else{
            android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(RollNumberRegisteration.this);
            a_builder.setMessage("You need to have Mobile Data or wifi to access this.Press Ok to Exit  !!!")
                    .setCancelable(false)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RollNumberRegisteration.this.finish();

                        }
                    });
            android.app.AlertDialog alert = a_builder.create();
            alert.setTitle("Alert !!!");
            alert.show();
        }

        floatingActionButton = findViewById(R.id.add_anous);
        reference  = FirebaseDatabase.getInstance().getReference("Alumni");
        mAuth = FirebaseAuth.getInstance();

        year = (NiceSpinner) findViewById(R.id.year);
        List<String> dataset = new LinkedList<>(Arrays.asList("FA14", "SP14", "FA15","SP15", "FA16", "SP16", "FA17", "SP17"));
        year.attachDataSource(dataset);

        programe = (NiceSpinner) findViewById(R.id.programe);
        List<String> dataset2 = new LinkedList<>(Arrays.asList("BAF", "BBA", "BCE", "BCS","BSE","BEE", "BSC", "BTN"));
        programe.attachDataSource(dataset2);

        rollno = (EditText) findViewById(R.id.roll);



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Showdata();
            }
        });

    }

    private void Showdata() {
        registeration =year.getText().toString()+"-"+programe.getText().toString()+"-"+rollno.getText().toString();
            if (rollno.getText().toString().equals("") ){
                Toast.makeText(getApplicationContext(),"Please enter the 3 digit roll number",Toast.LENGTH_SHORT).show();
            }
            else if (rollno.getText().length() == 3){
                final ProgressDialog pd = new ProgressDialog(RollNumberRegisteration.this);
                pd.setMessage("Loading...");
                pd.setCancelable(false);

                reference.orderByChild("REG").equalTo(registeration).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            pd.show();

                            gmail_of_user = snapshot.child("Gmail").getValue().toString();
                            phonenumber  = snapshot.child("PhoneNumber").getValue().toString();
                            name = snapshot.child("Name").getValue().toString();
                            stusent_status = snapshot.child("status").getValue().toString();
                            View view = LayoutInflater.from(RollNumberRegisteration.this).inflate(R.layout.login_form, null);
                            user_password = view.findViewById(R.id.email_password);
                            TextView user_gmail = view.findViewById(R.id.gmail);
                            FloatingActionButton floatingActionButton = view.findViewById(R.id.add_anous);
                            AlertDialog.Builder builder = new AlertDialog.Builder(RollNumberRegisteration.this);
                            builder.setMessage("Authentication...");
                            builder.setView(view);
                            user_gmail.setText(gmail_of_user);

                                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                            go_to_Main();
                                    }
                                });


                            AlertDialog alertDialog = builder.create();
                            pd.dismiss();
                            alertDialog.show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });



            }
            else
                Toast.makeText(getApplicationContext(),"Please enter 3 digit roll number",Toast.LENGTH_SHORT).show();
        }

    private void go_to_Main() {
        if (user_password.getText().toString().equals("")||user_password.getText().toString().length()<=5){
            Toast.makeText(getApplicationContext(),"Please enter the greater than 5 digit password",Toast.LENGTH_SHORT).show();
        }
        else {

            final ProgressDialog pd = new ProgressDialog(RollNumberRegisteration.this);
            pd.setCancelable(false);
            pd.setMessage("Loading...");
            pd.show();

            mAuth.fetchProvidersForEmail(gmail_of_user).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                    boolean check = !task.getResult().getProviders().isEmpty();
                    if(!check){

                        String passwrod  = user_password.getText().toString();
                        mAuth.createUserWithEmailAndPassword(gmail_of_user,passwrod).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(),"Please go to this email and verify ",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                pd.dismiss();
                                                Toast.makeText(getApplicationContext(),"Unable to send the email verification code ",Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });
                                }
//                                else {
//                                    pd.dismiss();
//
//                                    FirebaseAuth.getInstance().getCurrentUser()
//                                            .reload()
//                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//
//                                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                                                    if (user.isEmailVerified()){
//
//                                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//                                                        assert firebaseUser != null;
//                                                        String userID = firebaseUser.getUid();
//                                                        reference = FirebaseDatabase.getInstance().getReference("User").child(userID);
//                                                        HashMap<String ,String> hashMap = new HashMap<>();
//                                                        hashMap.put("id",userID);
//                                                        hashMap.put("User_name",name);
//                                                        hashMap.put("imageUrl","default");
//                                                        hashMap.put("status","Offline");
//                                                        hashMap.put("Phone Number",phonenumber);
//                                                        hashMap.put("Phone status","private");
//                                                        hashMap.put("student status",stusent_status);
//                                                        hashMap.put("search",name.toLowerCase());
//                                                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                            @Override
//                                                            public void onComplete(@NonNull Task<Void> task) {
//                                                                if (task.isSuccessful()){
//                                                                    Intent i  = new Intent(getApplicationContext(),MainActivity.class);
//                                                                    i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_NEW_TASK);
//                                                                    startActivity(i);
//                                                                    finish();
//                                                                }
//                                                            }
//                                                        });
//                                                    }
//                                                    else
//                                                        Toast.makeText(getApplicationContext(),"Please go to the email and verify this email ",Toast.LENGTH_SHORT).show();
//                                                }
//                                            });
//                                }
                            }
                        });
                    }
                    else {
                        pd.dismiss();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user == null){
                            Toast.makeText(getApplicationContext(),"You account is already approved please go to the login page",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            FirebaseAuth.getInstance().getCurrentUser()
                                    .reload()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                            if (user.isEmailVerified()){
                                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                                assert firebaseUser != null;
                                                String userID = firebaseUser.getUid();
                                                reference = FirebaseDatabase.getInstance().getReference("User").child(userID);
                                                HashMap<String ,String> hashMap = new HashMap<>();
                                                hashMap.put("id",userID);
                                                hashMap.put("User_name",name);
                                                hashMap.put("imageUrl","default");
                                                hashMap.put("status","Offline");
                                                hashMap.put("Phone Number",phonenumber);
                                                hashMap.put("Phone status","private");
                                                hashMap.put("student status",stusent_status);
                                                hashMap.put("search",name.toLowerCase());
                                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Intent i  = new Intent(getApplicationContext(),MainActivity.class);
                                                            i.addFlags(i.FLAG_ACTIVITY_CLEAR_TASK|i.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(i);
                                                            finish();
                                                        }
                                                    }
                                                });
                                            }
                                            else
                                                Toast.makeText(getApplicationContext(),"Please go to this email and verify. ",Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }

                    }
                }
            });


        }
    }


}
