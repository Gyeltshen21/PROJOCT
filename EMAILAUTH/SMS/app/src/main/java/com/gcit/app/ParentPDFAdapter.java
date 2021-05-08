package com.gcit.app;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ParentPDFAdapter extends FirebaseRecyclerAdapter<TeacherPDFHelperClass, ParentPDFAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ParentPDFAdapter(@NonNull FirebaseRecyclerOptions<TeacherPDFHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myViewHolder holder, final int position, @NonNull final TeacherPDFHelperClass teacherPDFHelperClass) {
        holder.ParentPDFName.setText("Name: " +teacherPDFHelperClass.getName());
        holder.ParentPDFImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.ParentPDFImage.getContext(),ParentPDFViewActivity.class);
                intent.putExtra("ParentPDFName",teacherPDFHelperClass.getName());
                intent.putExtra("ParentPDFImage",teacherPDFHelperClass.getUrl());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.ParentPDFImage.getContext().startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_singlerow,parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ImageView ParentPDFImage;
        TextView ParentPDFName;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            ParentPDFImage = itemView.findViewById(R.id.ParentPDF);
            ParentPDFName = itemView.findViewById(R.id.ParentPDFName);
        }
    }
}

