package com.topoffers.topoffers.common.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.topoffers.data.base.IData;
import com.topoffers.data.base.IImageData;
import com.topoffers.data.models.Headers;
import com.topoffers.data.models.QueryParam;
import com.topoffers.data.models.QueryParams;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.common.adapters.OrderListAdapter;
import com.topoffers.topoffers.common.helpers.AuthenticationHelpers;
import com.topoffers.topoffers.common.helpers.Utils;
import com.topoffers.topoffers.common.models.AuthenticationCookie;
import com.topoffers.topoffers.common.models.Order;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersListFragment extends Fragment {
    private View root;
    private int queryProductId;
    private IData<Order> orderData;
    private IImageData imageData;
    private AuthenticationCookie cookie;
    private Class classToNavigateOnItemClick;
    private ArrayList<Order> mainOrders;
    
    public OrdersListFragment() {
        // Required empty public constructor
        mainOrders = new ArrayList<>();
    }

    public static OrdersListFragment create(int queryProductId, IData<Order> orderData, IImageData imageData, AuthenticationCookie cookie, Class classToNavigateOnItemClick) {
        OrdersListFragment ordersListFragment = new OrdersListFragment();
        ordersListFragment.setQueryProductId(queryProductId);
        ordersListFragment.setOrderData(orderData);
        ordersListFragment.setImageData(imageData);
        ordersListFragment.setCookie(cookie);
        ordersListFragment.setClassToNavigateOnItemClick(classToNavigateOnItemClick);
        return ordersListFragment;
    };

    public void setQueryProductId(int queryProductId) {
        this.queryProductId = queryProductId;
    }

    public void setOrderData(IData<Order> orderData) {
        this.orderData = orderData;
    }

    public void setImageData(IImageData imageData) {
        this.imageData = imageData;
    }

    public void setCookie(AuthenticationCookie cookie) {
        this.cookie = cookie;
    }

    public void setClassToNavigateOnItemClick(Class classToNavigateOnItemClick) {
        this.classToNavigateOnItemClick = classToNavigateOnItemClick;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_orders_list, container, false);
        this.initOrdersList();
        return root;
    }

    private void initOrdersList() {
        ListView lvOrders = (ListView) root.findViewById(R.id.lv_orders);
        ArrayAdapter<Order> ordersAdapter = new OrderListAdapter(this.getContext(), this.imageData);

        lvOrders.setAdapter(ordersAdapter);

        final Context context = this.getContext();
        lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order clickedOrder = mainOrders.get(position);

                Intent intent = new Intent(context, classToNavigateOnItemClick);
                intent.putExtra(OrderDetailsFragment.INTENT_ORDER_KEY, clickedOrder.getId());
                context.startActivity(intent);
            }
        });

        lvOrders.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Order clickedOrder = mainOrders.get(position);
                (DialogFragment.create(context, Utils.buildDetailsString("Status", clickedOrder.getStatus()), 1)).show();
                return true;
            }
        });

        // Perform HTTP Request
        this.loadOrders(ordersAdapter);
    }

    private void loadOrders(final ArrayAdapter<Order> ordersAdapter) {
        final LoadingFragment loadingFragment = LoadingFragment.create(this.getContext(), "Preparing orders...");
        loadingFragment.show();

        Headers headers = AuthenticationHelpers.getAuthenticationHeaders(this.cookie);
        Consumer<Order[]> orderDataCallback = new Consumer<Order[]>() {
            @Override
            public void accept(Order[] orders) throws Exception {
                mainOrders = new ArrayList<Order>(Arrays.asList(orders));

                if (orders.length > 0) {
                    TextView tvNoOrders = (TextView) root.findViewById(R.id.tv_no_products_added);
                    tvNoOrders.setVisibility(View.GONE);
                }

                ordersAdapter.clear();
                ordersAdapter.addAll(orders);

                loadingFragment.hide();
            }
        };

        if (this.queryProductId > 0) {
            QueryParam queryParam = new QueryParam("productId", String.valueOf(this.queryProductId));
            QueryParams queryParams = new QueryParams(new ArrayList<>(Arrays.asList(queryParam)));

            orderData.getAllWithQueryParams(queryParams, headers)
                    .subscribe(orderDataCallback);
        } else {
            orderData.getAll(headers)
                .subscribe(orderDataCallback);
        }
    }

}
