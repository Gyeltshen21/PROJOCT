package com.gcit.sna;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class AdminNotificationParentAdapter extends FirebaseRecyclerAdapter<AdminNotificationHelperClass, AdminNotificationParentAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdminNotificationParentAdapter(@NonNull FirebaseRecyclerOptions<AdminNotificationHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myViewHolder holder, final int position, @NonNull final AdminNotificationHelperClass adminNotificationHelperClass) {
        holder.AdminNotificationSender.setText("From: " +adminNotificationHelperClass.getSenderName());
        holder.AdminNotificationDate.setText("Date: " +adminNotificationHelperClass.getDate());
        holder.AdminNotificationTime.setText("Time: " +adminNotificationHelperClass.getTime());
        holder.AdminNotificationMessage.setText(adminNotificationHelperClass.getMessage());

        holder.AdminEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.AdminNotificationSender.getContext())
                        .setContentHolder(new ViewHolder(R.layout.admin_notification_dialog_content))
                        .setExpanded(true, 1500)
                        .create();

                View adminNotificationView = dialogPlus.getHolderView();
                EditText AdminNotificationSenderName = adminNotificationView.findViewById(R.id.AdminSenderName);
                EditText AdminNotificationMessage = adminNotificationView.findViewById(R.id.AdminMessage);
                Button AdminNotificationBtn = adminNotificationView.findViewById(R.id.AdminBtn);
                AdminNotificationSenderName.setText(adminNotificationHelperClass.getSenderName());
                AdminNotificationMessage.setText(adminNotificationHelperClass.getMessage());
                dialogPlus.show();

                AdminNotificationBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("senderName", AdminNotificationSenderName.getText().toString().trim());
                        map.put("message", AdminNotificationMessage.getText().toString().trim());

                        FirebaseDatabase.getInstance().getReference().child("ParentNews").child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });

                    }
                });
            }
        });

        holder.AdminDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.AdminNotificationSender.getContext());
                builder.setTitle("Delete");
                builder.setMessage("Are sure you want to delete?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("ParentNews").child(getRef(position).getKey()).removeValue();

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_notification_parent_single_view,parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView AdminNotificationSender, AdminNotificationDate, AdminNotificationTime, AdminNotificationMessage;
        ImageView AdminEdit, AdminDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            AdminNotificationSender = itemView.findViewById(R.id.AdminNotificationSenderName);
            AdminNotificationDate = itemView.findViewById(R.id.AdminNotificationDate);
            AdminNotificationTime = itemView.findViewById(R.id.AdminNotificationTime);
            AdminNotificationMessage = itemView.findViewById(R.id.AdminNotificationMessage);
            AdminEdit = itemView.findViewById(R.id.AdminEdit);
            AdminDelete = itemView.findViewById(R.id.AdminDelete);
        }
    }
}



