package com.gcit.sna;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class AdminParentAdapter extends FirebaseRecyclerAdapter<ParentHelperClass, AdminParentAdapter.myviewholder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AdminParentAdapter(@NonNull FirebaseRecyclerOptions<ParentHelperClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder, final int position, @NonNull final ParentHelperClass parentHelperClass) {
        holder.adminName.setText("Name :" +parentHelperClass.getName());
        holder.adminEmployeeID.setText("Student Code :" +parentHelperClass.getStdCode());
        holder.adminEmail.setText("Email :" +parentHelperClass.getEmail());
        holder.adminPhoneNo.setText("Phone Number :" +parentHelperClass.getPhone());
        holder.adminEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.adminName.getContext()).
                        setContentHolder(new ViewHolder(R.layout.admin_dialog_content))
                        .setExpanded(true,1100)
                        .create();

                View adminView = dialogPlus.getHolderView();
                EditText adminName = adminView.findViewById(R.id.AdminParentName);
                EditText adminEmployeeID = adminView.findViewById(R.id.AdminParentStdCode);
                EditText adminEmail = adminView.findViewById(R.id.AdminParentEmail);
                EditText adminPhoneNo = adminView.findViewById(R.id.AdminParentPhoneNo);
                Button adminBtn = adminView.findViewById(R.id.AdminParentBtn);

                //Display in editText
                adminName.setText(parentHelperClass.getName());
                adminEmployeeID.setText(parentHelperClass.getStdCode());
                adminEmail.setText(parentHelperClass.getEmail());
                adminPhoneNo.setText(parentHelperClass.getPhone());
                dialogPlus.show();

                //Update btn
                adminBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map = new HashMap<>();
                        map.put("name",adminName.getText().toString().trim());
                        map.put("employeeID",adminEmployeeID.getText().toString().trim());
                        map.put("email",adminEmail.getText().toString().trim());
                        map.put("phone",adminPhoneNo.getText().toString().trim());
                        FirebaseDatabase.getInstance().getReference().child("parent").child(getRef(position).getKey()).updateChildren(map)
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

        holder.adminDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.adminName.getContext());
                builder.setTitle("Delete");
                builder.setMessage("Are sure you want to delete?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("parent").child(getRef(position).getKey()).removeValue();
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
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parentsinglerow,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView adminName, adminEmployeeID, adminEmail, adminPhoneNo;
        ImageView adminEdit, adminDelete;
        Button adminBtn;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            adminName = (TextView)itemView.findViewById(R.id.AdminParentName);
            adminEmployeeID = (TextView)itemView.findViewById(R.id.AdminParentStdCode);
            adminEmail = (TextView)itemView.findViewById(R.id.AdminParentEmail);
            adminPhoneNo = (TextView)itemView.findViewById(R.id.AdminParentPhoneNo);
            adminEdit = (ImageView)itemView.findViewById(R.id.AdminEdit);
            adminDelete = (ImageView)itemView.findViewById(R.id.AdminDelete);
            adminBtn = (Button) itemView.findViewById(R.id.AdminParentBtn);
        }
    }
}

