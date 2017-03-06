package com.topoffers.topoffers.common.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.orm.SugarContext;
import com.orm.SugarRecord;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.common.fragments.DrawerFragment;
import com.topoffers.topoffers.common.models.DrawerItemInfo;
import com.topoffers.topoffers.seller.activities.SellerProductsListActivity;

import java.util.ArrayList;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.init();
    }

    protected void init() {
        SugarContext.init(this); // required by the library
    }
}
