package com.example.mughees.chat_app.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mughees.chat_app.Model.User;
import com.example.mughees.chat_app.PhoneVerfication;
import com.example.mughees.chat_app.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.suke.widget.SwitchButton;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment {
    CircleImageView image_profile;
    TextView username, userphone, edite;
    EditText phone_number;
    DatabaseReference reference;
    FirebaseUser fuser;
    ImageButton uploadimage;
    EditText code;
    EditText number;
    String phoneN,stateofswirch;
    StorageReference storageReference;
    Button btn_done;
    private static final int IMAGE_REQUEST = 71;
    private Uri imageUri;
    FirebaseUser user;
    String state = null;
    String phone, new_phonenumber;
    FirebaseAuth myAuth;
    private StorageTask uploadTask;
    com.suke.widget.SwitchButton switchButton, public_button;
    private ProgressBar mprogressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        image_profile = view.findViewById(R.id.profile_image);
        uploadimage = view.findViewById(R.id.imageuplaod);
        myAuth = FirebaseAuth.getInstance();
//        edite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(getContext(), PhoneVerfication.class);
////                startActivity(intent);
//
//                Enter_Phone_number();
//            }
//        });
        storageReference = FirebaseStorage.getInstance().getReference("Pictures");

        user = FirebaseAuth.getInstance().getCurrentUser();
        username = view.findViewById(R.id.username);
//        phone_number = view.findViewById(R.id.userPhone);
        userphone = view.findViewById(R.id.user_phone_number);
//        btn_done = view.findViewById(R.id.btn_add);
//
        public_button = view.findViewById(R.id.public_private);
//        switchButton = view.findViewById(R.id.switch_button);
        public_button.toggle();     //switch state
        public_button.toggle(true);//switch without animation
        public_button.setShadowEffect(true);//disable shadow effect
        public_button.setEnableEffect(true);//disable the switch animation
//

        reference = FirebaseDatabase.getInstance().getReference("User");
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                phone = dataSnapshot.child(fuser.getUid()).child("Phone Number").getValue().toString();
                userphone.setText(phone);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        public_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (public_button.isChecked()) {
                    reference = FirebaseDatabase.getInstance().getReference("User").child(fuser.getUid());
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Phone status", "public");
                    reference.updateChildren(hashMap);
                    Toast.makeText(getContext(), "You changed your phone status now everyone can make calls to you", Toast.LENGTH_LONG).show();
                } else {
                    reference = FirebaseDatabase.getInstance().getReference("User").child(fuser.getUid());
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Phone status", "private");
                    reference.updateChildren(hashMap);
                    Toast.makeText(getContext(), "You change your phone status public to privte", Toast.LENGTH_SHORT).show();

                }
            }
        });
//
//        btn_done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                addPhonetoFirebase();
//            }
//        });
//
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("User").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUser_name());

                if (user.getImageUrl().equals("default")) {
                    image_profile.setImageResource(R.drawable.profile_image);
                } else {
                    if (getActivity() == null)
                        return;
                    Glide.with(getActivity()).load(user.getImageUrl()).into(image_profile);
                    findphonestatus();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        uploadimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), IMAGE_REQUEST);

            }
        });


        return view;
    }

    private void findphonestatus() {

        reference = FirebaseDatabase.getInstance().getReference("User").child(fuser.getUid());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               String state = dataSnapshot.child("Phone status").getValue().toString();
                if (state.equals("private"))
                {
                    public_button.setChecked(false);
                }
                if (state.equals("public"))
                    public_button.setChecked(true);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    private void changePhone() {
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.enter_number,null);
//        final EditText number = view.findViewById(R.id.new_number);
//        Button phone_ok  = view.findViewById(R.id.ok);
//        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setView(view);
//
//        phone_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(number.getText().toString().equals("")){
//                    Toast.makeText(getContext(),"Please Enter the phone number",Toast.LENGTH_LONG).show();
//                }
//                else if (number.getText().toString().length() < 11||number.getText().toString().length()>11){
//                    Toast.makeText(getContext(),"Please enter valid phone number",Toast.LENGTH_LONG).show();
//                }
//                else {
//                    new_phonenumber = number.getText().toString();
//                    sendVerificationCode();
//                    View view  = LayoutInflater.from(getContext()).inflate(R.layout.enter_phone_code,null);
//                   EditText code = view.findViewById(R.id.new_code);
//
//                   matchcode = code.getText().toString();
//                   Button code_ok = view.findViewById(R.id.new_phone);
//                   code_ok.setOnClickListener(new View.OnClickListener() {
//                       @Override
//                       public void onClick(View v) {
//                           checkPhoneVerfication();
//                       }
//                   });
//                   AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
//                   builder1.setView(view);
//                    AlertDialog alertDialog = builder1.create();
//                    alertDialog.show();
//                }
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }

//    private void checkPhoneVerfication() {
//        String codenumber  = code.getText().toString();
//        if (codenumber.isEmpty()){
//            code.setError("Please enter code");
//            code.requestFocus();
//            return;
//        }
//        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(code_send, codenumber);
//        signInWithPhoneAuthCredential(credential);
//    }

//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
//        myAuth.signInWithCredential(credential)
//                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getContext(), "Match", Toast.LENGTH_SHORT).show();
//                        } else {
//                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
//                                Toast.makeText(getContext(), "Not Match", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    }
//                });
//    }

//    private void sendVerificationCode() {
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phoneN,
//                60,
//                TimeUnit.SECONDS,
//                (Activity) getContext(),
//                mCallBack);
//    }
//
//    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//        @Override
//        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//
//
//        }
//
//        @Override
//        public void onVerificationFailed(FirebaseException e) {
//
//
//        }
//
//        @Override
//        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//            super.onCodeSent(s, forceResendingToken);
//            code_send = s;
//        }
//    };


    //    private void addPhonetoFirebase() {
//        if (phone_number.getText().equals("")){
//            Toast.makeText(getContext(),"Please enter the phone number",Toast.LENGTH_SHORT).show();
//
//        }
//        else {
//            reference = FirebaseDatabase.getInstance().getReference("Phone numbers");
//            switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
//                    reference = FirebaseDatabase.getInstance().getReference("Phone Numbers");
//                    DatabaseReference firebaseref = reference.push();
//                    if(switchButton.isChecked()){
//                        firebaseref.child("status").setValue("private");
//                        Toast.makeText(getContext(),"on",Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        Toast.makeText(getContext(),"off",Toast.LENGTH_SHORT).show();
//                        firebaseref.child("status").setValue("public");
//
//                    }
//
//                }
//            });
//        }
//
//    }
//

    private void uploadImage() {


//
        if (imageUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            String imagurl = taskSnapshot.getDownloadUrl().toString();
                            reference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());
                            reference.child("imageUrl").setValue(imagurl);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
//            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
//                @Override
//                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                    if (!task.isSuccessful())
//                    {
//                        throw task.getException();
//                    }
//                    return fileReference.getDownloadUrl();
//
//                }
//            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                @Override
//                public void onComplete(@NonNull Task<Uri> task) {
//                    if (task.isSuccessful()){
//                        Uri downloadUri = task.getResult();
//                        String mUri = downloadUri.toString();
//                        reference = FirebaseDatabase.getInstance().getReference("User").child(fuser.getUid());
//                        HashMap<String,Object> map = new HashMap<>();
//                        map.put("imageUrl",mUri);
//                        reference.updateChildren(map);
//                        pd.dismiss();
//                    }
//                    else {
//                        Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
//                        pd.dismiss();
//                    }
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
//                    pd.dismiss();
//                }
//            });
//
//        }else {
//            Toast.makeText(getContext(),"No Image Selected",Toast.LENGTH_SHORT).show();
//        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){
//            imageUri = data.getData();
//            Picasso.with(getContext()).load(imageUri).into(image_profile);
////            image_profile.setImageURI(imageUri);
////            if (uploadTask != null && uploadTask.isInProgress()){
////                Toast.makeText(getContext(),"Upload in Progress",Toast.LENGTH_SHORT).show();
////            }else {
////                uploadImage();
////            }
//        }
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                image_profile.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public void Enter_Phone_number() {
//        View view = LayoutInflater.from(getContext()).inflate(R.layout.enter_number, null);
//        number = view.findViewById(R.id.new_number);
//        Button ok = view.findViewById(R.id.ok);
//        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setView(view);
//        builder.show();
//
//        ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                phoneN = number.getText().toString();
//                if (phoneN.isEmpty()) {
//                    number.setError("Phone Number is Required");
//                    number.requestFocus();
//                    return;
//                }
//                if (phoneN.length() < 10 || phoneN.length() > 11) {
//                    number.setError("Please enter a valid phone number");
//                    number.requestFocus();
//                    return;
//                } else {
//                    sendVerificationCode();
//                    View view1 = LayoutInflater.from(getContext()).inflate(R.layout.enter_phone_code, null);
//                    code = view1.findViewById(R.id.new_code);
//                    Button btn_done = view1.findViewById(R.id.new_phone);
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
//                    builder1.setView(view1);
//                    builder1.show();
//                    btn_done.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            checkPhoneVerfication();
//
//                        }
//                    });
//                }
//            }
//        });
//    }
}
