package com.github.client.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import com.github.client.api.model.ItemDetail;
import com.github.client.common.ItemAdapter;

import java.util.List;

/**
 * Created by Sam on 2/19/17.
 * Utility for displaying dialogs/Alerts/progress
 */
public class DialogUtils {

    private static ProgressDialog progress;

    public static void showAlert(Context context, String message) {
        showAlert(context, "", message);
    }

    public static void showAlert(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title)
                .setNeutralButton("Ok", null)
                .create()
                .show();
    }

    public static void showListDialog(Context context, String title, List<ItemDetail> items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setNeutralButton("Ok", null)
                .setAdapter(new ItemAdapter(context, items), null)
                .create()
                .show();
    }

    private static void showProgress(Context context, String message) {
        progress = new ProgressDialog(context);
        progress.setIndeterminate(true);
        progress.setMessage(message);
        progress.show();
    }

    public static void showProgress(Context context, @StringRes int messageResId) {
        showProgress(context, context.getString(messageResId));
    }

    public static void dismissProgress() {
        progress.dismiss();
    }
}
