package com.topoffers.topoffers.buyer.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.topoffers.data.base.IData;
import com.topoffers.data.base.IImageData;
import com.topoffers.data.models.Headers;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.TopOffersApplication;
import com.topoffers.topoffers.common.activities.BaseAuthenticatedActivity;
import com.topoffers.topoffers.common.adapters.ProductListAdapter;
import com.topoffers.topoffers.common.fragments.ConfirmDialogFragment;
import com.topoffers.topoffers.common.fragments.DrawerFragment;
import com.topoffers.topoffers.common.fragments.LoadingFragment;
import com.topoffers.topoffers.common.fragments.ProductDetailsFragment;
import com.topoffers.topoffers.common.helpers.AuthenticationHelpers;
import com.topoffers.topoffers.common.helpers.GPSTracker;
import com.topoffers.topoffers.common.helpers.OnSwipeTouchListener;
import com.topoffers.topoffers.common.models.AuthenticationCookie;
import com.topoffers.topoffers.common.models.DrawerItemInfo;
import com.topoffers.topoffers.common.models.LoginResult;
import com.topoffers.topoffers.common.models.Order;
import com.topoffers.topoffers.common.models.Product;
import com.topoffers.topoffers.common.models.ProductsCart;
import com.topoffers.topoffers.profile.MyProfileActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class BuyerProductsCart extends BaseAuthenticatedActivity {
    @Inject
    public ProductsCart cart;

    @Inject
    public IData<Order> orderData;

    @Inject
    public IData<Product> productData;

    @Inject
    public IImageData imageHttpData;

    private ArrayList<Product> mainProducts;
    Headers headers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_products_cart);
        this.setupDrawer();
        this.initProductsList();
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
                                    intent = new Intent(BuyerProductsCart.this, BuyerProductsListActivity.class);
                                    startActivity(intent);
                                    break;
                                case 2:
                                    intent = new Intent(BuyerProductsCart.this, MyProfileActivity.class);
                                    startActivity(intent);
                                    break;
                                case 3:
                                    intent = new Intent(BuyerProductsCart.this, BuyerProductsCart.class);
                                    startActivity(intent);
                                    break;
                                case 4:
                                    intent = new Intent(BuyerProductsCart.this, BuyerOrderHistoryListActivity.class);
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

    @Override
    protected void init() {
        super.init();
        ((TopOffersApplication) getApplication()).getComponent().inject(this);
    }

    private void initProductsList() {
        mainProducts = new ArrayList<>();
        final ListView lvProducts = (ListView) this.findViewById(R.id.lv_cart_products);
        final ArrayAdapter<Product> productsAdapter = new ProductListAdapter(this, this.imageHttpData);

        lvProducts.setAdapter(productsAdapter);

        final Context context = this;

        lvProducts.setOnTouchListener(new OnSwipeTouchListener(this, lvProducts) {
            public void onSwipeLeft(final int position) {
                final Product swipedProduct = mainProducts.get(position);
                DialogInterface.OnClickListener onClickYesListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        productData.delete(swipedProduct.getId(), headers)
                                .subscribe(new Consumer<Product>() {
                                    @Override
                                    public void accept(Product product) throws Exception {
                                        productsAdapter.remove(mainProducts.get(position));
                                        productsAdapter.notifyDataSetChanged();
                                        mainProducts.remove(position);
                                    }
                                });
                    }};

                (ConfirmDialogFragment.create(
                        context,
                        "Delete " + swipedProduct.getTitle(),
                        "Are you sure you want to delete " + swipedProduct.getTitle(),
                        onClickYesListener))
                        .show();
            }
        });

        // Perform HTTP Request
        this.loadProducts(productsAdapter);
        this.setupRestoreButton(productsAdapter);
        this.setupOrder(productsAdapter);
    }

    private void setupRestoreButton(final ArrayAdapter<Product> productsAdapter){
        final Button restore = (Button) this.findViewById(R.id.restore_btn);
        final TextView price = (TextView) this.findViewById(R.id.tv_products_cart_price);
        final TextView noItems = (TextView) this.findViewById(R.id.tv_no_products_added_cart);
        final Button order = (Button) this.findViewById(R.id.order_btn);
        restore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                productsAdapter.clear();
                cart.RestoreCart();
                restore.setVisibility(View.INVISIBLE);
                price.setVisibility(View.INVISIBLE);
                noItems.setVisibility(View.VISIBLE);
                order.setVisibility(View.INVISIBLE);
                Toast.makeText(v.getContext(), "Successfully restored products!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupOrder(final ArrayAdapter<Product> productsAdapter){
        Button order = (Button) this.findViewById(R.id.order_btn);
        final AuthenticationCookie cookie = this.authenticationCookie;
        final Headers headers = AuthenticationHelpers.getAuthenticationHeaders(cookie);
        final LoginResult loginResult = super.loginResult;

        final Button btn = (Button) this.findViewById(R.id.restore_btn);
        final TextView vi = (TextView) this.findViewById(R.id.tv_products_cart_price);
        final TextView vi2 = (TextView) this.findViewById(R.id.tv_no_products_added_cart);
        final Button btn2 = (Button) this.findViewById(R.id.order_btn);

        order.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                ArrayList<Product> products = cart.getProducts();

                HashMap<Integer, Order> idToProduct = new HashMap<Integer, Order>();
                GPSTracker gps = new GPSTracker(BuyerProductsCart.this);

                if(gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    gps.showSettingsAlert();
                }

                for(int i = 0; i < products.size(); i++){
                    if(!idToProduct.containsKey(products.get(i).getId())){
                        Product product = products.get(i);
                        Order order = new Order(0, product.getId(), 1, product.getPrice(), product.getPrice(), gps.getLatitude() + ";" + gps.getLongitude(), new Date(), "pending", product.getTitle(), product.getImageIdentifier(),
                                product.getDescription(), product.getSellerFirstName(), product.getSellerLastName(), "N/A", "N/A",
                                product.getSellerUsername(), loginResult.getFirstName(), loginResult.getLastName(), loginResult.getAddress(), loginResult.getPhone(),
                                loginResult.getUsername());
                        idToProduct.put(products.get(i).getId(), order);
                    }
                    else {
                        Order order = idToProduct.get(products.get(i).getId());
                        order.setQuantity(order.getQuantity() + 1);
                    }
                }

                for(int i = 0; i < idToProduct.size(); i++){
                    Order sendOrder = (Order) idToProduct.values().toArray()[i];
                    orderData.add(sendOrder, headers).subscribe(new Consumer<Order>() {
                        @Override
                        public void accept(Order o) throws Exception {

                        }
                    });
                }
                productsAdapter.clear();
                cart.RestoreCart();
                btn.setVisibility(View.INVISIBLE);
                vi.setVisibility(View.INVISIBLE);
                vi2.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.INVISIBLE);
                Toast.makeText(v.getContext(), "Successfully ordered products!",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadProducts(final ArrayAdapter<Product> productsAdapter) {
        if(cart.getProducts().size() > 0){
            TextView view = (TextView) this.findViewById(R.id.tv_no_products_added_cart);
            view.setVisibility(View.INVISIBLE);
            TextView allPrice = (TextView) this.findViewById(R.id.tv_products_cart_price);
            allPrice.setText("Summary price: " + Double.toString(cart.getAllPrice()));
        }else {
            TextView view = (TextView) this.findViewById(R.id.tv_products_cart_price);
            view.setVisibility(View.INVISIBLE);
            Button restore = (Button) this.findViewById(R.id.restore_btn);
            restore.setVisibility(View.INVISIBLE);
            Button order = (Button) this.findViewById(R.id.order_btn);
            order.setVisibility(View.INVISIBLE);
        }
        productsAdapter.addAll(cart.getProducts());
    }
}
