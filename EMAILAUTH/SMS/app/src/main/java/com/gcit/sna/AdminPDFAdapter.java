package com.gcit.sna;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class AdminPDFAdapter extends FirebaseRecyclerAdapter<TeacherPDFHelperClass, AdminPDFAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdminPDFAdapter(@NonNull FirebaseRecyclerOptions<TeacherPDFHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myViewHolder holder, final int position, @NonNull final TeacherPDFHelperClass teacherPDFHelperClass) {
        holder.AdminPDFName.setText("Name: " +teacherPDFHelperClass.getName());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.AdminPDFImage.getContext(),AdminPDFViewActivity.class);
                intent.putExtra("AdminPDFName",teacherPDFHelperClass.getName());
                intent.putExtra("AdminPDFImage",teacherPDFHelperClass.getUrl());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.AdminPDFImage.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_pdfsinglerow,parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ImageView AdminPDFImage;
        TextView AdminPDFName;
        LinearLayout linearLayout;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.pdf);
            AdminPDFImage = itemView.findViewById(R.id.AdminPDF);
            AdminPDFName = itemView.findViewById(R.id.AdminPDFName);
        }
    }
}

