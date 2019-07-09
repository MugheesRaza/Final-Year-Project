package com.example.mughees.chat_app.Admin_Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mughees.chat_app.MessageActivity;
import com.example.mughees.chat_app.Model.User;
import com.example.mughees.chat_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapterAdmin extends RecyclerView.Adapter<UserAdapterAdmin.ViewHolder> {
    private Context mContax;
    private List<User> mUser;
    private boolean ischat;

    public UserAdapterAdmin(Context context, List<User> uploads, boolean ischat){
        this.mContax = context;
        this.mUser = uploads;
        this.ischat = ischat;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContax).inflate(R.layout.user_items,viewGroup,false);
        return new UserAdapterAdmin.ViewHolder(view);
    }
    

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final User user = mUser.get(i);
        viewHolder.username.setText(user.getUser_name());
        if (user.getImageUrl().equals("default")){
            viewHolder.user_profile.setImageResource(R.drawable.profile_image);
        }
        else {
            Glide.with(mContax).load(user.getImageUrl()).into(viewHolder.user_profile);
        }
//        viewHolder.relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                final AlertDialog.Builder builder = new AlertDialog.Builder(mContax);
//                builder.setMessage("Are You sure you want to delete");
//                builder.setCancelable(true);
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        viewHolder.reference.orderByChild("id").equalTo(user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
//                                    snapshot.getRef().removeValue();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//                    }
//                });
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//                return true;
//            }
//        });
        if (ischat){
            if (user.getStatus().equals("Online")){
                viewHolder.img_on.setVisibility(View.VISIBLE);
                viewHolder.img_off.setVisibility(View.GONE);
            }
            else {
                viewHolder.img_on.setVisibility(View.GONE);
                viewHolder.img_off.setVisibility(View.VISIBLE);
            }
        }
        else {
            viewHolder.img_on.setVisibility(View.GONE);
            viewHolder.img_off.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView user_profile;
        public ImageView img_on,img_off;
        public RelativeLayout relativeLayout;
        DatabaseReference reference;
        public ViewHolder(View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.username);
            user_profile = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            relativeLayout = itemView.findViewById(R.id.relative_layou);
            reference = FirebaseDatabase.getInstance().getReference("User");
        }
    }
}
