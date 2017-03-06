package com.topoffers.topoffers.seller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.topoffers.data.base.IData;
import com.topoffers.data.base.IImageData;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.TopOffersApplication;
import com.topoffers.topoffers.common.activities.BaseAuthenticatedActivity;
import com.topoffers.topoffers.common.fragments.DrawerFragment;
import com.topoffers.topoffers.common.models.DrawerItemInfo;
import com.topoffers.topoffers.common.models.Product;
import com.topoffers.topoffers.profile.MyProfileActivity;
import com.topoffers.topoffers.seller.fragments.UpdateProductFragment;

import java.util.ArrayList;

import javax.inject.Inject;

public class UpdateProductActivity extends BaseAuthenticatedActivity {
    public static final String INTENT_UPDATE_PRODUCT_ID = "intent_update_product_id";

    @Inject
    public IData<Product> productData;

    @Inject
    public IImageData imageData;

    private int productId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);

        Intent intent = this.getIntent();
        productId = intent.getIntExtra(INTENT_UPDATE_PRODUCT_ID, 0);

        this.setProductFragment();
        this.setupDrawer();
    }

    @Override
    protected void init() {
        super.init();
        ((TopOffersApplication) getApplication()).getComponent().inject(this);
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
                                    intent = new Intent(UpdateProductActivity.this, SellerProductsListActivity.class);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    intent = new Intent(UpdateProductActivity.this, MyProfileActivity.class);
                                    startActivity(intent);
                                    break;
                                case 3:
                                    intent = new Intent(UpdateProductActivity.this, UpdateProductActivity.class);
                                    startActivity(intent);
                                    break;
                                case 4:
                                    intent = new Intent(UpdateProductActivity.this, SellerOrderHistoryListActivity.class);
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

    private void setProductFragment() {
        UpdateProductFragment updateProductFragment = UpdateProductFragment.create(this.productData, this.imageData, this.authenticationCookie, productId);
        this.getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_update_product, updateProductFragment)
            .commit();
    }
}
