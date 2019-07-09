package com.example.mughees.chat_app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class PhoneVerfication extends AppCompatActivity {

    EditText phoneNumber,phonecode;
    Button getCode,entercode;
    String codesend,id;
    PhoneAuthProvider.ForceResendingToken token;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verfication);
        mAuth = FirebaseAuth.getInstance();
        phoneNumber = findViewById(R.id.phone);
        getCode = findViewById(R.id.getcode);
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationcode();
//                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.enter_phone_code,null);
//                phonecode = view.findViewById(R.id.new_code);
//                entercode = view.findViewById(R.id.new_phone);
//                AlertDialog.Builder builder = new AlertDialog.Builder(PhoneVerfication.this);
//                builder.setView(view);
//                entercode.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        matchVerficationcode();
//                    }
//                });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
            }
        });

    }

    private void matchVerficationcode() {
        String code  = phonecode.getText().toString();
        if (code.isEmpty()){
            phonecode.setError("Please enter code");
            phonecode.requestFocus();
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codesend,code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener( this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                             id = mAuth.getCurrentUser().getUid();
                            deleteID();
                            Toast.makeText(getApplicationContext(),"Verification successful",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),"Incorrect Verification Code",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
    private void deleteID() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child(id);
       reference.orderByChild("status").equalTo("Online").addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                   snapshot.getRef().removeValue();
               }
           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });


    }


    private void sendVerificationcode() {
        String number  = phoneNumber.getText().toString();
        if (number.isEmpty()){
            phoneNumber.setError("Phone Number is Required");
            phoneNumber.requestFocus();
            return;
        }
         if (number.length()<10){
            phoneNumber.setError("Please enter a valid phone number");
            phoneNumber.requestFocus();
            return;
         }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBacks);
        View view = LayoutInflater.from(PhoneVerfication.this).inflate(R.layout.enter_phone_code,null);
        phonecode = view.findViewById(R.id.new_code);
        entercode = view.findViewById(R.id.new_phone);
        AlertDialog.Builder builder = new AlertDialog.Builder(PhoneVerfication.this);
        builder.setView(view);
        entercode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchVerficationcode();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codesend = s;
            token = forceResendingToken;
        }
    };
}
