package com.example.mughees.chat_app.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mughees.chat_app.Model.fame;
import com.example.mughees.chat_app.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class fame_Adapter extends RecyclerView.Adapter<fame_Adapter.ViewHolder> {
    private Context mContax;
    private List<fame> fames;

    public fame_Adapter(Context mContax, List<fame> fames) {
        this.mContax = mContax;
        this.fames = fames;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContax).inflate(R.layout.famous_student_items,viewGroup,false);
        return new fame_Adapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final fame user_fame = fames.get(i);
        viewHolder.name.setText(user_fame.getName());
        viewHolder.job.setText(user_fame.getJob());
        viewHolder.job_dec.setText(user_fame.getJobdescription());
        if (user_fame.getImage().equals("default")){

        }
        else {
            Glide.with(mContax).load(user_fame.getImage()).into(viewHolder.imageofstudent);
        }

        viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(mContax).inflate(R.layout.halloffame,null);
                TextView name,job,job_dec,gpa,city,year;
                CircleImageView image;
                name = view.findViewById(R.id.namestudent);
                job = view.findViewById(R.id.jobstudent);
                job_dec = view.findViewById(R.id.studentjobdec);
                gpa = view.findViewById(R.id.student_CGPA);
                city = view.findViewById(R.id.studentcity);
                year = view.findViewById(R.id.studentgraduation);
                image = view.findViewById(R.id.student_image);
                AlertDialog.Builder builder = new AlertDialog.Builder(mContax);
                builder.setView(view);

                name.setText(user_fame.getName());
                job.setText(user_fame.getJob());
                job_dec.setText(user_fame.getJobdescription());
                gpa.setText(user_fame.getGpa());
                city.setText(user_fame.getCity());
                year.setText(user_fame.getYear());

                    Glide.with(mContax).load(user_fame.getImage()).into(image);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return fames.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView pic;
        TextView name,job,job_dec;
        public RelativeLayout relativeLayout;
        public DatabaseReference reference;
        CircleImageView imageofstudent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.famous_pic);
            name = itemView.findViewById(R.id.item_user_name);
            job = itemView.findViewById(R.id.famous_userjob);
            job_dec = itemView.findViewById(R.id.famous_userjobdec);
            relativeLayout = itemView.findViewById(R.id.relative_layou);
            imageofstudent = itemView.findViewById(R.id.famous_pic);
            reference = FirebaseDatabase.getInstance().getReference("Famous Students");


        }

    }
}
