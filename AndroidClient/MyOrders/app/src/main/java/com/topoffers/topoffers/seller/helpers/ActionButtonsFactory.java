package com.topoffers.topoffers.seller.helpers;

import android.content.Context;
import android.content.Intent;
import android.view.View;


public class ActionButtonsFactory {
    public static View.OnClickListener getActionButtonWithId(final Context context, final Class activityToStart, final String extraIntentKey, final int id) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, activityToStart);
                intent.putExtra(extraIntentKey, id);
                context.startActivity(intent);
            }
        };
    }
}
