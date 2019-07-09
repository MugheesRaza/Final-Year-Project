package com.example.mughees.chat_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class addFamousStudents extends AppCompatActivity {

    CircleImageView image;
    ImageButton imagechange;
    EditText name,job,dec,city,gpa,year_of_grad;
    FloatingActionButton addfamus_students;
    private static final int IMAGE_REQUEST = 71;
    private Uri imageUri;
    String imagurl;
    StorageReference storageReference;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_famous_students);
        image = findViewById(R.id.student_image);
        imagechange = findViewById(R.id.imagestudent);
        job = findViewById(R.id.userjob);
        dec = findViewById(R.id.user_job_dec);
        city = findViewById(R.id.user_city);
        name = findViewById(R.id.username);
        gpa = findViewById(R.id.user_gpa);
        year_of_grad = findViewById(R.id.user_gradu_year);
        addfamus_students = findViewById(R.id.add_account);
        storageReference = FirebaseStorage.getInstance().getReference("Pictures");
        addfamus_students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRecord();
            }
        });

        imagechange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), IMAGE_REQUEST);
            }
        });
    }

    private void addRecord() {
        if(job.getText().toString().equals("")||dec.getText().toString().equals("")||city.getText().toString().equals("")||name.getText().toString().equals("")||
                gpa.getText().toString().equals("")||year_of_grad.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter all fields",Toast.LENGTH_SHORT).show();
        }
        if (gpa.getText().toString().length()>4){

            gpa.setError("Invalid gpa");
            gpa.requestFocus();
            return;

        }
        if (year_of_grad.getText().toString().length()>4){
            year_of_grad.setError("Invalid year");
            year_of_grad.requestFocus();
            return;
        }
        else {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Famous Students");
            DatabaseReference firebase = reference.push();
            firebase.child("name").setValue(name.getText().toString());
            firebase.child("job").setValue(job.getText().toString());
            firebase.child("jobdescription").setValue(dec.getText().toString());
            firebase.child("city").setValue(city.getText().toString());
            firebase.child("gpa").setValue(gpa.getText().toString());
            firebase.child("year").setValue(year_of_grad.getText().toString());
            if (imagurl!=null){
                firebase.child("image").setValue(imagurl);
                uploadImage();
            }
            else {
                firebase.child("image").setValue("default");
            }

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
//                && data != null && data.getData() != null )
//        {
//            imageUri = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(addFamousStudents.this.getContentResolver(), imageUri);
//                image.setImageBitmap(bitmap);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
//        }
        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri);
                image.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(getApplicationContext(),"permission denied",Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadImage() {
        if (imageUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(addFamousStudents.this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            imagurl = taskSnapshot.getDownloadUrl().toString();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }
}
