package com.topoffers.topoffers.buyer.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.topoffers.data.base.IData;
import com.topoffers.data.base.IImageData;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.TopOffersApplication;
import com.topoffers.topoffers.common.activities.BaseAuthenticatedActivity;
import com.topoffers.topoffers.common.fragments.DrawerFragment;
import com.topoffers.topoffers.common.fragments.OrderDetailsFragment;
import com.topoffers.topoffers.common.models.DrawerItemInfo;
import com.topoffers.topoffers.common.models.Order;
import com.topoffers.topoffers.profile.MyProfileActivity;
import com.topoffers.topoffers.seller.activities.SellerOrderHistoryListActivity;
import com.topoffers.topoffers.seller.activities.SellerProductsListActivity;
import com.topoffers.topoffers.seller.activities.UpdateProductActivity;

import java.util.ArrayList;

import javax.inject.Inject;

public class BuyerOrderHistoryDetailsActivity extends BaseAuthenticatedActivity {
    @Inject
    public IData<Order> orderData;

    @Inject
    public IImageData imageData;

    private int orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_order_history_details);

        Intent intent = this.getIntent();
        orderId = intent.getIntExtra(OrderDetailsFragment.INTENT_ORDER_KEY, 0);

        this.initProductDetailsFragment();
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
                                    intent = new Intent(BuyerOrderHistoryDetailsActivity.this, BuyerProductsListActivity.class);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    intent = new Intent(BuyerOrderHistoryDetailsActivity.this, MyProfileActivity.class);
                                    startActivity(intent);
                                    break;
                                case 3:
                                    intent = new Intent(BuyerOrderHistoryDetailsActivity.this, BuyerProductsCart.class);
                                    startActivity(intent);
                                    break;
                                case 4:
                                    intent = new Intent(BuyerOrderHistoryDetailsActivity.this, BuyerOrderHistoryListActivity.class);
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

    private void initProductDetailsFragment() {
        OrderDetailsFragment orderDetailsFragment = OrderDetailsFragment.create(orderId, this.orderData, this.imageData, this.authenticationCookie);
        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_seller_order_details, orderDetailsFragment)
                .commit();
    }
}
