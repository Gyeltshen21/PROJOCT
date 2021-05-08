package com.gcit.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class TeacherPDFAdapter extends FirebaseRecyclerAdapter<TeacherPDFHelperClass, TeacherPDFAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TeacherPDFAdapter(@NonNull FirebaseRecyclerOptions<TeacherPDFHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myViewHolder holder, final int position, @NonNull final TeacherPDFHelperClass teacherPDFHelperClass) {
        holder.TeacherPDFName.setText("Name: " +teacherPDFHelperClass.getName());
        holder.TeacherPDFImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.TeacherPDFImage.getContext(),TeacherPDFViewActivity.class);
                intent.putExtra("TeacherPDFName",teacherPDFHelperClass.getName());
                intent.putExtra("TeacherPDFImage",teacherPDFHelperClass.getUrl());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.TeacherPDFImage.getContext().startActivity(intent);
            }
        });

        holder.TeacherPDFEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.TeacherPDFName.getContext())
                        .setContentHolder(new ViewHolder(R.layout.teacher_pdf_dialog_content))
                        .setExpanded(true,700)
                        .create();
                //Reference
                View teacherPdfView = dialogPlus.getHolderView();
                EditText teacherPDFName = teacherPdfView.findViewById(R.id.TeacherPDFName);
                Button teacherPDFUpdate = teacherPdfView.findViewById(R.id.TeacherPDFBtn);

                //Displaying
                teacherPDFName.setText(teacherPDFHelperClass.getName());
                dialogPlus.show();

                teacherPDFUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("name", teacherPDFName.getText().toString().trim());
                        FirebaseDatabase.getInstance().getReference().child("TeacherPDFResult").child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialogPlus.dismiss();
                            }
                        });
                    }
                });
            }
        });
        holder.TeacherPDFDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.TeacherPDFName.getContext());
                builder.setTitle("Delete");
                builder.setMessage("Are sure you want to delete?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("TeacherPDFResult").child(getRef(position).getKey()).removeValue();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacherpdfsinglerow,parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ImageView TeacherPDFImage, TeacherPDFEdit, TeacherPDFDelete;
        TextView TeacherPDFName;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            TeacherPDFImage = itemView.findViewById(R.id.TeacherPDF);
            TeacherPDFName = itemView.findViewById(R.id.TeacherPDFName);
            TeacherPDFEdit = itemView.findViewById(R.id.TeacherPDFEdit);
            TeacherPDFDelete = itemView.findViewById(R.id.TeacherPDFDelete);
        }
    }
}
