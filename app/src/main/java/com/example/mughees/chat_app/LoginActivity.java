package com.example.mughees.chat_app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button btn_login;
    FirebaseAuth auth;
    TextView forgot_password;
    connectionDetector cd;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        cd = new connectionDetector(LoginActivity.this);
        if (cd.isConnected()){

        }
        else {
            AlertDialog.Builder a_builder = new AlertDialog.Builder(LoginActivity.this);
            a_builder.setMessage("You need to have Mobile Data or wifi to access this.Press Ok to Exit  !!!")
                    .setCancelable(false)
                    .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           LoginActivity.this.finish();

                        }
                    });
            AlertDialog alert = a_builder.create();
            alert.setTitle("Alert !!!");
            alert.show();
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ResetPasswordActivity.class));

            }
        });


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_pasword = password.getText().toString();
                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_pasword)) {
                    Toast.makeText(getApplicationContext(), "All fields are required ", Toast.LENGTH_SHORT).show();
                }
                else if (!isEmailValid(txt_email)){
                    email.setError("Invalid Email");
                    email.requestFocus();
                    return;
                }
                else if (txt_pasword.length()<6){
                    Toast.makeText(getApplicationContext(),"Please enter the greator then 5 digit password ",Toast.LENGTH_SHORT).show();
                }
                else {

                    final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                    pd.setCancelable(false);
                    pd.setMessage("Loading...");
                    pd.show();
                    auth.signInWithEmailAndPassword(txt_email, txt_pasword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                //check if user is null
                                if(firebaseUser!=null){
                                    if (firebaseUser.isEmailVerified()){
//                                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
//                                        startActivity(i);
//                                        finish();
                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        i.setFlags(i.FLAG_ACTIVITY_CLEAR_TASK | i.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        pd.dismiss();
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(LoginActivity.this,"Please varify your email first ",Toast.LENGTH_SHORT).show();
                                    }

                                }

                            } else {
                                pd.dismiss();
                                Toast.makeText(getApplicationContext(), "Please create an account first", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    boolean isEmailValid(CharSequence email) {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();

    }
}
