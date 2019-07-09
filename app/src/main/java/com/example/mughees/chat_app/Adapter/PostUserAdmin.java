package com.example.mughees.chat_app.Adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mughees.chat_app.Model.post;
import com.example.mughees.chat_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PostUserAdmin extends RecyclerView.Adapter<PostUserAdmin.ViewHolder>{
    private Context mContax;
    private List<post> mPost;

    public PostUserAdmin(Context mContax, List<post> mPost) {
        this.mContax = mContax;
        this.mPost = mPost;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContax).inflate(R.layout.potsitems,viewGroup,false);
        return new PostUserAdmin.ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final post post = mPost.get(i);
        viewHolder.title.setText(post.getTitle());
        viewHolder.description.setText(post.getDescription());
    }
    @Override
    public int getItemCount() {
        return mPost.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView title;
        public TextView description;
        public LinearLayout linearLayout;
        public DatabaseReference reference;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_dec);
            linearLayout = itemView.findViewById(R.id.linear_layout);
            reference = FirebaseDatabase.getInstance().getReference("Announcements");
        }
    }
}
