package com.example.mughees.chat_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mughees.chat_app.Admin_Fragments.Admin_MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class newAdmin extends AppCompatActivity {
    EditText key,password,new_key,new_password;
    Button btn_admin_new;
    DatabaseReference reference;
    String oldkey,oldpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_admin);
        key = findViewById(R.id.admin_key);
        password = findViewById(R.id.admin_pass);
        new_key = findViewById(R.id.admin_new_key);
        new_password = findViewById(R.id.admin_new_pass);
        btn_admin_new = findViewById(R.id.login);
        reference = FirebaseDatabase.getInstance().getReference("Admin");
        checkoldAdmin();
        btn_admin_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkoldAdmin();
                checkValidAdmin();
            }
        });

    }

    private void checkoldAdmin() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    oldkey = snapshot.child("key").getValue().toString();
                    oldpass = snapshot.child("password").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void checkValidAdmin() {
        if (key.getText().toString().equals("")||password.getText().toString().equals("")||new_key.getText().toString().equals("")||new_password.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter all fieldas",Toast.LENGTH_LONG).show();
        }
        else if(oldkey.equals(key.getText().toString())&&oldpass.equals(password.getText().toString())) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                            String newK = new_key.getText().toString();
                            String newP = new_password.getText().toString();
                            DatabaseReference dbrefrence= FirebaseDatabase.getInstance().getReference("Admin").child(snapshot.getKey());
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("key", newK);
                            result.put("password", newP);
                            dbrefrence.updateChildren(result);
                        Intent intent = new Intent(getApplicationContext(), Admin_MainActivity.class);
                        startActivity(intent);
                        finish();
                            Toast.makeText(getApplicationContext(),"Admin key and password changed successfully",Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(),"please enter correct old key and password",Toast.LENGTH_SHORT).show();
        }
    }
}
