package com.gcit.sna;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class TeacherNotificationAdapter extends FirebaseRecyclerAdapter<AdminNotificationHelperClass, TeacherNotificationAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public TeacherNotificationAdapter(@NonNull FirebaseRecyclerOptions<AdminNotificationHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myViewHolder holder, final int position, @NonNull final AdminNotificationHelperClass adminNotificationHelperClass) {
        holder.TeacherNotificationSender.setText("From: " +adminNotificationHelperClass.getSenderName());
        holder.TeacherNotificationDate.setText("Date: " +adminNotificationHelperClass.getDate());
        holder.TeacherNotificationTime.setText("Time: " +adminNotificationHelperClass.getTime());
        holder.TeacherNotificationMessage.setText("Message: " +adminNotificationHelperClass.getMessage());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_notification_single_row,parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView TeacherNotificationSender, TeacherNotificationDate, TeacherNotificationTime, TeacherNotificationMessage;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            TeacherNotificationSender = itemView.findViewById(R.id.TeacherNotificationSenderName);
            TeacherNotificationDate = itemView.findViewById(R.id.TeacherNotificationDate);
            TeacherNotificationTime = itemView.findViewById(R.id.TeacherNotificationTime);
            TeacherNotificationMessage = itemView.findViewById(R.id.TeacherNotificationMessage);
        }
    }
}


