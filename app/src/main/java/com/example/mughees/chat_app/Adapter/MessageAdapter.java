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

import com.bumptech.glide.Glide;
import com.example.mughees.chat_app.MessageActivity;
import com.example.mughees.chat_app.Model.Chat;
import com.example.mughees.chat_app.Model.User;
import com.example.mughees.chat_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context mContax;
    private List<Chat> mChat;
    private String imageUrl;
    public static final int MSG_TYPE_RIGHT = 0;
    public static final int MASG_TYPE_LEFT = 1;
    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

    public MessageAdapter(Context context,List<Chat> chat,String image){
        this.mContax = context;
        this.mChat = chat;
        this.imageUrl = image;

    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContax).inflate(R.layout.chat_item_right,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(mContax).inflate(R.layout.chat_item_left,viewGroup,false);
            return new MessageAdapter.ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder viewHolder, int i) {

        Chat chat = mChat.get(i);
        viewHolder.show_message.setText(chat.getMessage());
        if (imageUrl.equals("default")){
            viewHolder.user_profile.setImageResource(R.mipmap.ic_launcher);

        }
        else {
            Glide.with(mContax).load(imageUrl).into(viewHolder.user_profile);
        }
        if (i == mChat.size()-1){
            if (chat.isIsseen()){
                viewHolder.text_seen.setText("seen");
            }else
                viewHolder.text_seen.setText("Delivered");
        }
        else {
            viewHolder.text_seen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView user_profile;
        public TextView text_seen;
        public ViewHolder(View itemView){
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            user_profile = itemView.findViewById(R.id.profile_image);
            text_seen = itemView.findViewById(R.id.txt_seen);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mChat.get(position).getSender().equals(fuser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else
            return MASG_TYPE_LEFT;
    }
}


