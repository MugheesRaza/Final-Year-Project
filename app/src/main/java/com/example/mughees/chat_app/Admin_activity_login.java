package com.example.mughees.chat_app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mughees.chat_app.Admin_Fragments.Admin_MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin_activity_login extends AppCompatActivity {

    EditText key;
    EditText password;
    TextView change_admin;
    DatabaseReference reference;
    FloatingActionButton btnadd;
    connectionDetector cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        cd =  new connectionDetector(Admin_activity_login.this);
        if(cd.isConnected()){

        }
        else {
            android.app.AlertDialog.Builder a_builder = new android.app.AlertDialog.Builder(Admin_activity_login.this);
            a_builder.setMessage("You need to have Mobile Data or wifi to access this.Press Ok to Exit.")
                    .setCancelable(false)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Admin_activity_login.this.finish();

                        }
                    });
            android.app.AlertDialog alert = a_builder.create();
            alert.setTitle("Alert !!!");
            alert.show();
        }
        key = findViewById(R.id.admin_key);
        password = findViewById(R.id.admin_pass);
        change_admin = findViewById(R.id.changekey);
        btnadd = findViewById(R.id.add_account);
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAdmin();
            }
        });
        change_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),newAdmin.class);
                startActivity(intent);

            }
        });

    }

    private void checkAdmin() {
        if (key.getText().toString().equals("")||password.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_SHORT).show();
        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(Admin_activity_login.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            reference = FirebaseDatabase.getInstance().getReference("Admin");
//            DatabaseReference data = reference.push();
//            data.child("key").setValue("12345");
//            data.child("password").setValue("12345");
//        reference.orderByChild("key").equalTo(key.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
//                    String pass = snapshot.child("password").getValue().toString();
//                    String ky = snapshot.child("key").getValue().toString();
//                    if (password.getText().toString().equals(pass)&&key.getText().toString().equals(ky)){
//                        Intent intent = new Intent(getApplicationContext(), Admin_MainActivity.class);
//                        intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        progressDialog.dismiss();
//                        finish();
//                    }
//                    else {
//                        Toast.makeText(getApplicationContext(),"please enter correct key and password",Toast.LENGTH_LONG).show();
//                        progressDialog.dismiss();
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getApplicationContext(),databaseError.getMessage().toString(),Toast.LENGTH_LONG).show();
//            }
//        });
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                        String pass = snapshot.child("password").getValue().toString();
                        String ky = snapshot.child("key").getValue().toString();
                        if (password.getText().toString().equals(pass)&&key.getText().toString().equals(ky)){
                            Intent intent = new Intent(getApplicationContext(), Admin_MainActivity.class);
                            intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TASK|intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"please enter correct key and password",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
}
