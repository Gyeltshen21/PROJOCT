package com.gcit.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URLEncoder;

public class TeacherPDFViewActivity extends AppCompatActivity {

    WebView TeacherPDFView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_p_d_f_view);

        TeacherPDFView = (WebView) findViewById(R.id.TeacherPDFview);
        TeacherPDFView.getSettings().setJavaScriptEnabled(true);

        String TeacherPDFName = getIntent().getStringExtra("TeacherPDFName");
        String TeacherPDFImage = getIntent().getStringExtra("TeacherPDFImage");

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(TeacherPDFName);
        progressDialog.setMessage("Opening....");

        TeacherPDFView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressDialog.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }
        });
        String url = "";
        try{
            url = URLEncoder.encode(TeacherPDFImage,"UTF-8");
        }
        catch(Exception e){ }

        TeacherPDFView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
    }
}