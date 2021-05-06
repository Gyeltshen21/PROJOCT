 package com.gcit.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

 public class TeacherProfileActivity extends AppCompatActivity {
     
    private ImageView TeacherProfilePic;
    private Context context = TeacherProfileActivity.this;
    private static final int PICK_Teacher_IMAGE_REQUEST = 1;
    private Uri TeacherImageUri;
    private ProgressBar TeacherProgressBar;
    private DatabaseReference databaseReference;
    private TextView TeacherHeaderName, TeacherName, TeacherEmployeeID, TeacherEmail, TeacherPhoneNo;
    String sCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile);

        TeacherProfilePic = (ImageView) findViewById(R.id.TeacherProfilePic);
        TeacherHeaderName = (TextView) findViewById(R.id.TeacherHeaderName1);
        TeacherName = (TextView) findViewById(R.id.TeacherName1);
        TeacherEmployeeID = (TextView) findViewById(R.id.TeacherEmployeeID1);
        TeacherEmail = (TextView) findViewById(R.id.TeacherEmail1);
        TeacherPhoneNo = (TextView) findViewById(R.id.TeacherPhoneNo1);
        TeacherProgressBar = (ProgressBar) findViewById(R.id.TeacherProgressBar);
        Intent intent = getIntent();
        String employeeID = intent.getStringExtra("employeeID");
        sCode = employeeID;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("employee");
        Query checkUser = databaseReference.orderByChild("employeeID").equalTo(employeeID);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    TeacherEmployeeID.setError(null);
                    TeacherEmployeeID.setEnabled(false);
                    String nameDB = snapshot.child(employeeID).child("name").getValue(String.class);
                    String emailDB = snapshot.child(employeeID).child("email").getValue(String.class);
                    String phoneDB = snapshot.child(employeeID).child("phone").getValue(String.class);
                    String employeeIDDB = snapshot.child(employeeID).child("employeeID").getValue(String.class);
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference("employee/"+sCode+"/image.jpg");
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.with(context).load(uri).into(TeacherProfilePic);
                        }
                    });
                    TeacherHeaderName.setText(nameDB);
                    TeacherName.setText(nameDB);
                    TeacherEmployeeID.setText(employeeIDDB);
                    TeacherEmail.setText(emailDB);
                    TeacherPhoneNo.setText(phoneDB);
                }
            }
            //Data fetch error
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Back button to home
    public void GoBackToTeacherHome(View view) {
        Intent intent = new Intent(getApplicationContext(),TeacherHomeActivity.class);
        intent.putExtra("employeeID",sCode);
        startActivity(intent);
    }

     //Button to Choose photo
     public void ChooseTeacherProfilePhoto(View view) {
         openTeacherFileChooser();
     }
     //Method to choose file
     private void openTeacherFileChooser() {
         Intent intent = new Intent();
         intent.setType("image/*");
         intent.setAction(Intent.ACTION_GET_CONTENT);
         startActivityForResult(intent,PICK_Teacher_IMAGE_REQUEST);
     }

     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if(requestCode == PICK_Teacher_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
             TeacherImageUri = data.getData();
             Picasso.with(this).load(TeacherImageUri).into(TeacherProfilePic);
         }
     }

     //Button to Upload photo
     public void UploadTeacherProfilePhoto(View view) {
         UploadFile();
     }

     private String getFileExtension(Uri uri){
         ContentResolver cR = getContentResolver();
         MimeTypeMap mime = MimeTypeMap.getSingleton();
         return mime.getExtensionFromMimeType(cR.getType(uri));
     }
     private void UploadFile() {
         if(TeacherImageUri != null){
             final StorageReference fileReference = FirebaseStorage.getInstance().getReference("employee/"+sCode+"/image.jpg");
             fileReference.putFile(TeacherImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                     Handler handler = new Handler();
                     handler.postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             TeacherProgressBar.setProgress(0);
                         }
                     },500);
                     Toast.makeText(getApplicationContext(),"Uploaded Successfully", Toast.LENGTH_LONG).show();
//                    TeacherProfilePhoto upload = new TeacherProfilePhoto(taskSnapshot.getUploadSessionUri().toString());
//                    String TeacherUploadId = databaseReference.push().getKey();
//                    databaseReference.child(sCode).child("image").setValue(upload);
                 }
             }).addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     Toast.makeText(getApplicationContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
                 }
             }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                     double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                     TeacherProgressBar.setProgress((int) progress);
                 }
             });
         }
         else{
             Toast.makeText(getApplicationContext(),"No file selected",Toast.LENGTH_SHORT).show();
         }
     }
}