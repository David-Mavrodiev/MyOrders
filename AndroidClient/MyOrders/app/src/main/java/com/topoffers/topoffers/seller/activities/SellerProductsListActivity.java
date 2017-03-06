package com.topoffers.topoffers.seller.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
import com.topoffers.topoffers.common.models.DrawerItemInfo;
import com.topoffers.topoffers.common.models.Product;
import com.topoffers.topoffers.profile.MyProfileActivity;

import java.util.ArrayList;

import javax.inject.Inject;

public class SellerProductsListActivity extends BaseAuthenticatedActivity {

    @Inject
    public IData<Product> productData;

    @Inject
    public IImageData imageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_products_list);

        this.initTitle();
        this.initProductsListFragment();
        this.initLogoutFragment();
        this.addTempButtonListener();
        this.setupDrawer();
    }

    @Override
    protected void init() {
        super.init();
        ((TopOffersApplication) getApplication()).getComponent().inject(this);
    }

    private void initTitle() {
        TextView tvTitle = (TextView) this.findViewById(R.id.tv_seller_products_list_title);
        String title = String.format("%s's products for sale", this.loginResult.getFirstName());
        tvTitle.setText(title);
    }

    private void initProductsListFragment() {
        ProductsListFragment productListFragment = ProductsListFragment.create(this.productData, this.imageData, this.authenticationCookie, SellerProductDetailsActivity.class);
        this.getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_seller_products_list, productListFragment)
            .commit();
    }

    private void initLogoutFragment() {
        LogoutFragment logoutFragment = LogoutFragment.createFragment();
        this.getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.logout_container_fragment, logoutFragment)
            .commit();
    }

    protected void setupDrawer() {
        View drawerContainer = this.findViewById(R.id.container_drawer);
        if (drawerContainer != null) {
            ArrayList<DrawerItemInfo> items = new ArrayList<>();

            items.add(new DrawerItemInfo(1, "My Products"));
            items.add(new DrawerItemInfo(2, "My Profile"));
            items.add(new DrawerItemInfo(3, "Add Product"));
            items.add(new DrawerItemInfo(4, "Orders"));

            Fragment drawerFragment =
                    DrawerFragment.createFragment(items, super.loginResult, new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            Intent intent;
                            switch ((int) drawerItem.getIdentifier()) {
                                case 1:
                                    intent = new Intent(SellerProductsListActivity.this, SellerProductsListActivity.class);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    intent = new Intent(SellerProductsListActivity.this, MyProfileActivity.class);
                                    startActivity(intent);
                                    break;
                                case 3:
                                    intent = new Intent(SellerProductsListActivity.this, UpdateProductActivity.class);
                                    startActivity(intent);
                                    break;
                                case 4:
                                    intent = new Intent(SellerProductsListActivity.this, SellerOrderHistoryListActivity.class);
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

    private void addTempButtonListener() {
        final Context context = this;
        Button btnCreateNewProduct = (Button) this.findViewById(R.id.btn_temp_add_new_product);

        btnCreateNewProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateProductActivity.class);
                context.startActivity(intent);
            }
        });

        // Orders list navigation
        Button btnOrdersList = (Button) this.findViewById(R.id.temp_btn_orders);
        btnOrdersList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SellerOrderHistoryListActivity.class);
                context.startActivity(intent);
            }
        });

        // My profile navigation
        Button btnMyProfile = (Button) this.findViewById(R.id.temp_btn_my_profile);
        btnMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyProfileActivity.class);
                context.startActivity(intent);
            }
        });
    }
}
