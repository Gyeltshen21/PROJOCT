package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URLEncoder;

public class AdminPDFViewActivity extends AppCompatActivity {

    WebView AdminPDFView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_p_d_f_view);

        AdminPDFView = (WebView) findViewById(R.id.AdminPDFview);
        AdminPDFView.getSettings().setJavaScriptEnabled(true);

        String AdminPDFName = getIntent().getStringExtra("AdminPDFName");
        String AdminPDFImage = getIntent().getStringExtra("AdminPDFImage");

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(AdminPDFName);
        progressDialog.setMessage("Opening....");

        AdminPDFView.setWebViewClient(new WebViewClient(){
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
            url = URLEncoder.encode(AdminPDFImage,"UTF-8");
        }
        catch(Exception e){ }

        AdminPDFView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
    }
}