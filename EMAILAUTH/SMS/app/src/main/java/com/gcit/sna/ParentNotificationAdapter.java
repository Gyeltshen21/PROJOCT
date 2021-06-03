package com.gcit.sna;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class ParentNotificationAdapter extends FirebaseRecyclerAdapter<AdminNotificationHelperClass, ParentNotificationAdapter.myViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ParentNotificationAdapter(@NonNull FirebaseRecyclerOptions<AdminNotificationHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myViewHolder holder, final int position, @NonNull final AdminNotificationHelperClass adminNotificationHelperClass) {
        holder.ParentNotificationSender.setText("From: " +adminNotificationHelperClass.getSenderName());
        holder.ParentNotificationDate.setText("Date: " +adminNotificationHelperClass.getDate());
        holder.ParentNotificationTime.setText("Time: " +adminNotificationHelperClass.getTime());
        holder.ParentNotificationMessage.setText("Message: " +adminNotificationHelperClass.getMessage());
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_notification_single_row,parent,false);
        return new myViewHolder(view);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView ParentNotificationSender, ParentNotificationDate, ParentNotificationTime, ParentNotificationMessage;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            ParentNotificationSender = itemView.findViewById(R.id.ParentNotificationSenderName);
            ParentNotificationDate = itemView.findViewById(R.id.ParentNotificationDate);
            ParentNotificationTime = itemView.findViewById(R.id.ParentNotificationTime);
            ParentNotificationMessage = itemView.findViewById(R.id.ParentNotificationMessage);
        }
    }
}



