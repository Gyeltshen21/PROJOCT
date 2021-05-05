package com.gcit.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminPDF extends RecyclerView.Adapter<AdminPDF.AdminPDFHolder> {
    private Context mContext;
    private List<AdminProfilePhoto> adminProfilePhotos;

    public AdminPDF(Context context, List<AdminProfilePhoto>adminProfilePhotos){
        mContext = context;
        this.adminProfilePhotos = adminProfilePhotos;
    }
    @Override
    public AdminPDFHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.pdf_item,parent,false);
        return new AdminPDFHolder(v);
    }

    @Override
    public void onBindViewHolder(AdminPDFHolder holder, int position) {
        AdminProfilePhoto adminProfilePhoto = adminProfilePhotos.get(position);
        Picasso.with(mContext)
                .load(adminProfilePhoto.getAdminImageUrl())
                .fit()
                .centerCrop()
                .into(holder.pdfView);

    }

    @Override
    public int getItemCount() {
        return adminProfilePhotos.size();
    }

    public class AdminPDFHolder extends RecyclerView.ViewHolder {
        public TextView pdfname;
        public ImageView pdfView;
        public AdminPDFHolder(View itemView) {
            super(itemView);

            pdfname = itemView.findViewById(R.id.AdminPDFTitle);
            pdfView = itemView.findViewById(R.id.AdminPDF);
        }
    }
}
