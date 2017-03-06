package com.topoffers.topoffers;

import android.content.Intent;
import android.os.Bundle;

import com.topoffers.topoffers.common.activities.BaseAuthenticatedActivity;
import com.topoffers.topoffers.common.helpers.RedirectHelpers;

public class MainActivity extends BaseAuthenticatedActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.redirect();
    }

    private void redirect() {
        Intent intent = RedirectHelpers.baseRedirect(this, this.authenticationCookie);
        if (intent != null) {
            this.startActivity(intent);
        }
    }
}
