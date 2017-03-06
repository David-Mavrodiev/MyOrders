package com.topoffers.topoffers.common.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.common.fragments.DrawerFragment;
import com.topoffers.topoffers.common.helpers.AuthenticationHelpers;
import com.topoffers.topoffers.common.models.AuthenticationCookie;
import com.topoffers.topoffers.common.models.DrawerItemInfo;
import com.topoffers.topoffers.common.models.LoginResult;
import com.topoffers.topoffers.seller.activities.SellerProductsListActivity;

import java.util.ArrayList;

public abstract class BaseAuthenticatedActivity extends BaseActivity {
    protected AuthenticationCookie authenticationCookie;
    protected LoginResult loginResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.checkAuthentication();
    }

    private void checkAuthentication() {
        authenticationCookie = AuthenticationHelpers.getAuthenticationCookie();
        Intent intent = AuthenticationHelpers.checkAuthentication(this, authenticationCookie);
        if (intent != null) {
            this.startActivity(intent);
        } else {
            loginResult = AuthenticationHelpers.getLoginResult();
        }
    }
}
