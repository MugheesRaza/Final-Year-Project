package com.example.mughees.chat_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mughees.chat_app.MessageActivity;
import com.example.mughees.chat_app.Model.User;
import com.example.mughees.chat_app.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private Context mContax;
    private List<User> mUser;
    private boolean ischat;

    public UserAdapter(Context context,List<User> uploads,boolean ischat){
        this.mContax = context;
        this.mUser = uploads;
        this.ischat = ischat;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContax).inflate(R.layout.user_items,viewGroup,false);
        return new UserAdapter.ViewHolder(view);
    }
    

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final User user = mUser.get(i);
        viewHolder.username.setText(user.getUser_name());
        if (user.getImageUrl().equals("default")){
            viewHolder.user_profile.setImageResource(R.drawable.profile_image);

        }
        else
            Glide.with(mContax).load(user.getImageUrl()).into(viewHolder.user_profile);
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
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContax, MessageActivity.class);
                intent.putExtra("userId",user.getId());
                mContax.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView user_profile;
        public ImageView img_on,img_off;
        public ViewHolder(View itemView){
            super(itemView);
            username = itemView.findViewById(R.id.username);
            user_profile = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
        }
    }
}
