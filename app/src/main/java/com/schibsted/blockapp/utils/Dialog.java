package com.schibsted.blockapp.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.schibsted.blockapp.R;
import com.schibsted.blockapp.adapter.ItemAdapter;
import com.schibsted.blockapp.model.ItemDetail;

import java.util.List;

/**
 * Created by Sam on 2/19/17.
 * Utility for displaying dialogs/Alerts/progress
 */
public class Dialog {

    static ProgressDialog progress;

    public static void showAlert(Context context, String message){
        showAlert(context, "", message);
    }

    public static void showAlert(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setNeutralButton("Ok",null)
                .create()
                .show();
    }

    public static void showListDialog(Context context, String title, List<ItemDetail> items){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setNeutralButton("Ok",null)
                .setAdapter(new ItemAdapter(context, items), null)
                .create()
                .show();
    }

    public static void showProgress(Context context, String message){
        progress = new ProgressDialog(context);
        progress.setIndeterminate(true);
        progress.setMessage(message);
        progress.show();
    }

    public static void dismissProgress(){
        progress.dismiss();
    }
}
