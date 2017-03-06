package com.topoffers.topoffers.seller.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.topoffers.data.base.IData;
import com.topoffers.data.base.IImageData;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.TopOffersApplication;
import com.topoffers.topoffers.common.activities.BaseAuthenticatedActivity;
import com.topoffers.topoffers.common.fragments.DrawerFragment;
import com.topoffers.topoffers.common.fragments.OrdersListFragment;
import com.topoffers.topoffers.common.models.DrawerItemInfo;
import com.topoffers.topoffers.common.models.Order;
import com.topoffers.topoffers.profile.MyProfileActivity;

import java.util.ArrayList;

import javax.inject.Inject;

public class SellerOrderHistoryListActivity extends BaseAuthenticatedActivity {
    public static final String INTENT_ORDER_PRODUCT_ID = "intent_order_product_id";

    @Inject
    public IData<Order> orderData;

    @Inject
    public IImageData imageData;

    private int queryProductId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_order_history);

        Intent intent = this.getIntent();
        queryProductId = intent.getIntExtra(INTENT_ORDER_PRODUCT_ID, 0);

        this.initTitle();
        this.initOrdersListFragment();
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
                                    intent = new Intent(SellerOrderHistoryListActivity.this, SellerProductsListActivity.class);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    intent = new Intent(SellerOrderHistoryListActivity.this, MyProfileActivity.class);
                                    startActivity(intent);
                                    break;
                                case 3:
                                    intent = new Intent(SellerOrderHistoryListActivity.this, UpdateProductActivity.class);
                                    startActivity(intent);
                                    break;
                                case 4:
                                    intent = new Intent(SellerOrderHistoryListActivity.this, SellerOrderHistoryListActivity.class);
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

    private void initTitle() {
        TextView tvTitle = (TextView) this.findViewById(R.id.tv_seller_orders_list_title);
        String title = "";
        if (this.queryProductId > 0) {
            title = String.format("Product orders history", this.loginResult.getFirstName());
        } else {
            title = String.format("%s's orders history", this.loginResult.getFirstName());
        }
        tvTitle.setText(title);
    }

    private void initOrdersListFragment() {
        OrdersListFragment ordersListFragment = OrdersListFragment.create(this.queryProductId, this.orderData, this.imageData, this.authenticationCookie, SellerOrderHistoryDetailsActivity.class);
        this.getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragment_seller_orders_list, ordersListFragment)
            .commit();

    }
}
