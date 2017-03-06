package com.topoffers.topoffers.buyer.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.topoffers.data.base.IData;
import com.topoffers.data.base.IImageData;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.TopOffersApplication;
import com.topoffers.topoffers.common.activities.BaseAuthenticatedActivity;
import com.topoffers.topoffers.common.fragments.DrawerFragment;
import com.topoffers.topoffers.common.fragments.LogoutFragment;
import com.topoffers.topoffers.common.fragments.ProductsListFragment;
import com.topoffers.topoffers.common.helpers.AuthenticationHelpers;
import com.topoffers.topoffers.common.models.AuthenticationCookie;
import com.topoffers.topoffers.common.models.DrawerItemInfo;
import com.topoffers.topoffers.common.models.Product;
import com.topoffers.topoffers.profile.MyProfileActivity;
import com.topoffers.topoffers.seller.activities.SellerOrderHistoryDetailsActivity;
import com.topoffers.topoffers.seller.activities.SellerOrderHistoryListActivity;
import com.topoffers.topoffers.seller.activities.SellerProductsListActivity;
import com.topoffers.topoffers.seller.activities.UpdateProductActivity;

import java.util.ArrayList;

import javax.inject.Inject;

public class BuyerProductsListActivity extends BaseAuthenticatedActivity {

    @Inject
    public IData<Product> productData;

    @Inject
    public IImageData imageHttpData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_products_list);

        this.initProductsListFragment();
        this.initLogoutFragment();
        this.setupDrawer();
        this.setupFab();
    }

    @Override
    protected void init() {
        super.init();
        ((TopOffersApplication) getApplication()).getComponent().inject(this);
    }

    private void setupFab(){
        FloatingActionButton btn = (FloatingActionButton) this.findViewById(R.id.fab);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyerProductsListActivity.this, BuyerProductsCart.class);
                startActivity(intent);
            }
        });
    }

    private void initProductsListFragment() {
        ProductsListFragment productListFragment = ProductsListFragment.create(this.productData, this.imageHttpData, this.authenticationCookie, BuyerProductDetailsActivity.class);
        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_seller_products_list, productListFragment)
                .commit();
    }

    protected void setupDrawer() {
        View drawerContainer = this.findViewById(R.id.container_drawer);
        if (drawerContainer != null) {
            ArrayList<DrawerItemInfo> items = new ArrayList<>();

            items.add(new DrawerItemInfo(1, "Products"));
            items.add(new DrawerItemInfo(2, "My Profile"));
            items.add(new DrawerItemInfo(3, "Cart"));
            items.add(new DrawerItemInfo(4, "My Orders"));

            Fragment drawerFragment =
                    DrawerFragment.createFragment(items, super.loginResult, new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            Intent intent;
                            switch ((int) drawerItem.getIdentifier()) {
                                case 1:
                                    intent = new Intent(BuyerProductsListActivity.this, BuyerProductsListActivity.class);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    intent = new Intent(BuyerProductsListActivity.this, MyProfileActivity.class);
                                    startActivity(intent);
                                    break;
                                case 3:
                                    intent = new Intent(BuyerProductsListActivity.this, BuyerProductsCart.class);
                                    startActivity(intent);
                                    break;
                                case 4:
                                    intent = new Intent(BuyerProductsListActivity.this, BuyerOrderHistoryListActivity.class);
                                    startActivity(intent);
                                    break;
                            }

                            return true;
                        }
                    });

            this.getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container_drawer, drawerFragment)
                    .commit();
        }
    }

    private void initLogoutFragment() {
        LogoutFragment logoutFragment = LogoutFragment.createFragment();
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.logout_container_fragment, logoutFragment)
                .commit();
    }
}
