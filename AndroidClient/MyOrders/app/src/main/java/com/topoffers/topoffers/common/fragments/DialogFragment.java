package com.topoffers.topoffers.common.fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.topoffers.topoffers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DialogFragment extends Fragment {
    private Context context;
    private String message;
    private int visibleSeconds;
    private Toast toast;

    public DialogFragment() {
        // Required empty public constructor
    }

    public static DialogFragment create(Context context, String message, int visibleSeconds) {
        DialogFragment dialogFragment = new DialogFragment();
        dialogFragment.setContext(context);
        dialogFragment.setMessage(message);
        return dialogFragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setVisibleSeconds(int visibleSeconds) {
        this.visibleSeconds = visibleSeconds;
    }

    public void show() {
        if (this.toast == null) {
            int toastVisibleTime = Toast.LENGTH_SHORT;
            if (visibleSeconds > 0) {
                toastVisibleTime = Toast.LENGTH_LONG;
            }
            this.toast = Toast.makeText(context, message , toastVisibleTime);
        }

        this.toast.show();
    }

    public void hide() {
        if (this.toast != null) {
            this.toast.cancel();
        }
    }

}
