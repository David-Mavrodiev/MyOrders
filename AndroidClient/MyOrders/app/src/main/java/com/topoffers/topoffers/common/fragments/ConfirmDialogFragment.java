package com.topoffers.topoffers.common.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

/**
 * Created by Simeon on 1.3.2017 г..
 */

public class ConfirmDialogFragment {
    private Context context;
    private String title;
    private String message;
    private DialogInterface.OnClickListener onClickListener;
    private AlertDialog.Builder alertDialogBuilder;

    public ConfirmDialogFragment() {
        // Required empty public constructor
    }

    public static ConfirmDialogFragment create(Context context, String title, String message, DialogInterface.OnClickListener onClickListener) {
        ConfirmDialogFragment confirmDialogFragment = new ConfirmDialogFragment();
        confirmDialogFragment.setContext(context);
        confirmDialogFragment.setTitle(title);
        confirmDialogFragment.setMessage(message);
        confirmDialogFragment.setOnClickListener(onClickListener);
        return confirmDialogFragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void show() {
        if (this.alertDialogBuilder == null) {
            this.alertDialogBuilder = new AlertDialog.Builder(context)
                .setTitle(this.title)
                .setMessage(this.message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, this.onClickListener)
                .setNegativeButton(android.R.string.no, null);
        }

        this.alertDialogBuilder.show();
    }
}
