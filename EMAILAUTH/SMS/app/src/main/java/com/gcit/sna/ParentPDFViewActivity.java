package com.gcit.sna;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URLEncoder;

public class ParentPDFViewActivity extends AppCompatActivity {

    WebView ParentPDFView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_p_d_f_view);

        ParentPDFView = (WebView) findViewById(R.id.ParentPDFview);
        ParentPDFView.getSettings().setJavaScriptEnabled(true);

        String ParentPDFName = getIntent().getStringExtra("ParentPDFName");
        String ParentPDFImage = getIntent().getStringExtra("ParentPDFImage");

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(ParentPDFName);
        progressDialog.setMessage("Opening....");

        ParentPDFView.setWebViewClient(new WebViewClient(){
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
            url = URLEncoder.encode(ParentPDFImage,"UTF-8");
        }
        catch(Exception e){ }

        ParentPDFView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
    }
}