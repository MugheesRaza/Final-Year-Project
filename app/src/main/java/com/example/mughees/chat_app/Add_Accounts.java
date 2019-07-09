package com.example.mughees.chat_app;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mughees.chat_app.Admin_Fragments.Admin_MainActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Add_Accounts extends AppCompatActivity {

EditText userMail,rollno,username,phone_number;
NiceSpinner year,programe;
FloatingActionButton addAccounts;
DatabaseReference reference;
String registeration;
RadioGroup status_group;
RadioButton status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__accounts);
        userMail = findViewById(R.id.user_email);

        year = (NiceSpinner) findViewById(R.id.year);
        List<String> dataset = new LinkedList<>(Arrays.asList("FA14", "SP14", "FA15","SP15", "FA16", "SP16", "FA17", "SP17"));
        year.attachDataSource(dataset);

        programe = (NiceSpinner) findViewById(R.id.programe);
        List<String> dataset2 = new LinkedList<>(Arrays.asList("BAF", "BBA", "BCE", "BCS", "BEE", "BSC", "BTN"));
        programe.attachDataSource(dataset2);

        rollno = (EditText) findViewById(R.id.roll);
        username = findViewById(R.id.username);
        addAccounts = findViewById(R.id.add_account);
        phone_number = findViewById(R.id.userPhone);
        status_group = findViewById(R.id.radioGroup1);

        addAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUserAccounts();
            }
        });
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void addUserAccounts() {
        if (rollno.getText().toString().equals("")|| rollno.getText().length()<=2 || rollno.getText().length()>3){
            Toast.makeText(getApplicationContext(),"Please enter the 3 digit roll number",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(username.getText().toString())||TextUtils.isEmpty(userMail.getText().toString())
                ||TextUtils.isEmpty(phone_number.getText().toString())){
            Toast.makeText(getApplicationContext(),"Please enter all fields ",Toast.LENGTH_SHORT).show();

        }
        else if (!isEmailValid(userMail.getText().toString())){
            userMail.setError("Please enter valid email address");
            userMail.requestFocus();
            return;

        }
        else if (username.getText().toString().length()>15){
            username.setError("Name length is too long");
            username.requestFocus();
            return;
        }
        else if (phone_number.getText().toString().length()>11 || phone_number.getText().toString().length()<11){
            phone_number.setError("Invalid Phone number");
            phone_number.requestFocus();
            return;
        }
        else
            {
                int getid = status_group.getCheckedRadioButtonId();
                status = findViewById(getid);
            registeration = year.getText().toString() + "-" + programe.getText().toString() + "-" + rollno.getText().toString();
            reference = FirebaseDatabase.getInstance().getReference("Alumni");
            DatabaseReference databaseReference = reference.push();
            databaseReference.child("REG").setValue(registeration);
            databaseReference.child("Name").setValue(username.getText().toString());
            databaseReference.child("Gmail").setValue(userMail.getText().toString());
            databaseReference.child("PhoneNumber").setValue(phone_number.getText().toString());
            databaseReference.child("status").setValue(status.getText());
            databaseReference.child("Image").setValue("default");
            Toast.makeText(getApplicationContext(),"Data Added",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Add_Accounts.this, Admin_MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }

    }
}
