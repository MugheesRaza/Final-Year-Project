package com.example.mughees.chat_app.Admin_Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mughees.chat_app.Model.account;
import com.example.mughees.chat_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountAdapterAdmin extends RecyclerView.Adapter<AccountAdapterAdmin.ViewHolder>{

    private Context mContext;
    private List<account> maccount;

    public AccountAdapterAdmin(Context mContext, List<account> maccount) {
        this.mContext = mContext;
        this.maccount = maccount;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.account_items,null);
        return new AccountAdapterAdmin.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final account ac = maccount.get(i);
        viewHolder.username.setText(ac.getName());
        viewHolder.status.setText(ac.getStatus());
        viewHolder.reg.setText(ac.getREG());
        viewHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are You sure you want to delete");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        viewHolder.reference.orderByChild("REG").equalTo(ac.getREG()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            }
        });


    }

    @Override
    public int getItemCount() {
        return maccount.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public TextView reg;
        public TextView status;
        public CircleImageView profile_image;
        public LinearLayout linearLayout;
        public DatabaseReference reference;
       public ViewHolder(@NonNull View itemView) {
           super(itemView);

           profile_image = itemView.findViewById(R.id.profile_image);
           username = itemView.findViewById(R.id.username);
           reg = itemView.findViewById(R.id.user_reg);
           status = itemView.findViewById(R.id.status_phone);
           linearLayout = itemView.findViewById(R.id.linear_layout);
           reference = FirebaseDatabase.getInstance().getReference("Alumni");
       }
   }
}
