package com.gcit.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class loadingDailogue {
    Activity activity;
    AlertDialog dialog;

    loadingDailogue(Activity myActivity){
      activity = myActivity;
    }

    void startLoadingDialogue(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progressbar, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();;
    }

    void dismiss(){
        dialog.dismiss();
    }
}
