package com.topoffers.topoffers.common.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.topoffers.data.base.IData;
import com.topoffers.data.base.IImageData;
import com.topoffers.data.models.Headers;
import com.topoffers.data.services.ImagesHttpData;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.common.contracts.IHandleOrderStatusChange;
import com.topoffers.topoffers.common.helpers.AuthenticationHelpers;
import com.topoffers.topoffers.common.helpers.Utils;
import com.topoffers.topoffers.common.models.AuthenticationCookie;
import com.topoffers.topoffers.common.models.Order;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class OrderDetailsFragment extends Fragment implements IHandleOrderStatusChange {
    public static final String INTENT_ORDER_KEY = "intent_order_key";

    @Inject
    public IImageData imageHttpData;

    private View root;
    private IData<Order> orderData;
    private AuthenticationCookie cookie;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }

    public static OrderDetailsFragment create(int orderId, IData<Order> orderData, IImageData imageData, AuthenticationCookie cookie) {
        OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();

        orderDetailsFragment.setOrderData(orderData);
        orderDetailsFragment.setImageHttpData(imageData);
        orderDetailsFragment.setCookie(cookie);

        Bundle args = new Bundle();
        args.putSerializable(INTENT_ORDER_KEY, orderId);
        orderDetailsFragment.setArguments(args);

        return orderDetailsFragment;
    }

    public void setOrderData(IData<Order> orderData) {
        this.orderData = orderData;
    }

    public void setImageHttpData(IImageData imageHttpData) {
        this.imageHttpData = imageHttpData;
    }

    public void setCookie(AuthenticationCookie cookie) {
        this.cookie = cookie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_order_details, container, false);
        this.initOrder();
        return root;
    }

    private void initOrder() {
        final Context context = this.getContext();
        Headers headers = AuthenticationHelpers.getAuthenticationHeaders(this.cookie);

        Bundle arguments = this.getArguments();
        final int orderId = (int) arguments.getSerializable(INTENT_ORDER_KEY);

        final LoadingFragment loadingFragment = LoadingFragment.create(this.getContext(), "Preparing order data...");
        loadingFragment.show();

        orderData.getById(orderId, headers)
            .subscribe(new Consumer<Order>() {
                @Override
                public void accept(Order order) throws Exception {
                    // Set title
                    TextView tvProductTitle = (TextView) root.findViewById(R.id.tv_order_details_product_title);
                    tvProductTitle.setText(order.getProductTitle());

                    // Set quantity
                    TextView tvQuantity = (TextView) root.findViewById(R.id.tv_order_details_quantity);
                    tvQuantity.setText(Utils.buildDetailsString("Quantity", String.valueOf(order.getQuantity())));

                    // Set single price
                    TextView tvSinglePrice = (TextView) root.findViewById(R.id.tv_order_details_single_price);
                    tvSinglePrice.setText(Utils.convertDoublePriceToStringPriceWithTag(order.getSinglePrice(), "Single price"));

                    // Set total price
                    TextView tvTotalPrice = (TextView) root.findViewById(R.id.tv_order_details_total_price);
                    tvTotalPrice.setText(Utils.convertDoublePriceToStringPriceWithTag(order.getTotalPrice(), "Total price"));

                    // Set date ordered
                    TextView tvDadeOrdered = (TextView) root.findViewById(R.id.tv_order_details_dateOrdered);
                    String dateOrderedAsString = Utils.convertDateToStringWithTime(order.getDateOrdered());
                    tvDadeOrdered.setText(Utils.buildDetailsString("Date ordered", dateOrderedAsString));

                    // Set status
                    TextView tvStatus = (TextView) root.findViewById(R.id.tv_order_details_status);
                    tvStatus.setText(Utils.buildDetailsString("Status", order.getStatus().toUpperCase()));
                    tvStatus.setTextColor(Color.parseColor(Utils.getOrderStatusColor(order.getStatus())));

                    // Set address
                    TextView tvAddress = (TextView) root.findViewById(R.id.tv_order_details_address);
                    tvAddress.setText(Utils.buildDetailsString("Delivery address", order.getDeliveryAddress()));

                    // Set buyer fullname
                    TextView tvBuyerFullname = (TextView) root.findViewById(R.id.tv_order_details_buyer_fullname);
                    tvBuyerFullname.setText(String.format("Buyer name: %s %s", order.getBuyerFirstName(), order.getBuyerLastName()));

                    // Set buyer username
                    TextView tvBuyerUsername = (TextView) root.findViewById(R.id.tv_order_details_buyer_username);
                    tvBuyerUsername.setText(Utils.buildDetailsString("Username", order.getBuyerUsername()));

                    // Set buyer phone
                    TextView tvBuyerPhone = (TextView) root.findViewById(R.id.tv_order_details_buyer_phone);
                    tvBuyerPhone.setText(Utils.buildDetailsString("Phone", order.getBuyerPhone()));

                    // Set buyer address
                    TextView tvBuyerAddress = (TextView) root.findViewById(R.id.tv_order_details_buyer_address);
                    tvBuyerAddress.setText(Utils.buildDetailsString("Address", order.getBuyerAddress()));

                    // Set seller fullname
                    TextView tvSellerFullname = (TextView) root.findViewById(R.id.tv_order_details_seller_fullname);
                    tvSellerFullname.setText(String.format("Buyer name: %s %s", order.getProductSellerFirstName(), order.getProductSellerLastName()));

                    // Set seller username
                    TextView tvSellerUsername = (TextView) root.findViewById(R.id.tv_order_details_seller_username);
                    tvSellerUsername.setText(Utils.buildDetailsString("Username", order.getProductSellerUsername()));

                    // Set seller phone
                    TextView tvSellerPhone = (TextView) root.findViewById(R.id.tv_order_details_seller_phone);
                    tvSellerPhone.setText(Utils.buildDetailsString("Phone", order.getProductSellerPhone()));

                    // Set seller address
                    TextView tvSellerAddress = (TextView) root.findViewById(R.id.tv_order_details_seller_address);
                    tvSellerAddress.setText(Utils.buildDetailsString("Address", order.getProductSellerAddress()));

                    initExtras(order.getStatus());

                    // Set image
                    final ImageView ivImage = (ImageView) root.findViewById(R.id.iv_order_details_image);
                    if (order.getProductImageIdentifier() != null) {
                        imageHttpData.getImage(order.getProductImageIdentifier())
                            .subscribe(new Consumer<Bitmap>() {
                                @Override
                                public void accept(Bitmap bitmap) throws Exception {
                                    ivImage.setImageBitmap(bitmap);
                                    loadingFragment.hide();
                                }
                            });
                    }
                }
            });
    }

    private void initExtras(String status) {
        Button actionButton1 = (Button) root.findViewById(R.id.btn_order_history_mark_1);
        Button actionButton2 = (Button) root.findViewById(R.id.btn_order_history_mark_2);
        ArrayList<Button> actionButtons = new ArrayList<>(Arrays.asList(actionButton1, actionButton2));

        Bundle arguments = this.getArguments();
        int orderId = (int) arguments.getSerializable(INTENT_ORDER_KEY);
        Headers headers = AuthenticationHelpers.getAuthenticationHeaders(this.cookie);

        if (((cookie.getRole()).equals("seller")) && (status.equals("pending"))) {
            actionButton1.setText("Mark as send");
            actionButton2.setText("Mark as rejected");

            actionButton1.setOnClickListener(new OnActionButtonClickListener(this.getContext(), orderId, headers, "send", this));
            actionButton2.setOnClickListener(new OnActionButtonClickListener(this.getContext(), orderId, headers, "rejected", this));
        } else if ((cookie.getRole().equals("buyers")) && (status.equals("send"))) {
            actionButton1.setText("Mark as received");
            actionButton2.setText("Mark as not received");

            actionButton1.setOnClickListener(new OnActionButtonClickListener(this.getContext(), orderId, headers, "received", this));
            actionButton2.setOnClickListener(new OnActionButtonClickListener(this.getContext(), orderId, headers, "notReceived", this));
        } else {
            hideExtraActionButtons(actionButtons);
        }
    }

    private void hideExtraActionButtons(List<Button> actionButtons) {
        for (Button actionButton : actionButtons) {
            actionButton.setVisibility(View.GONE);
        }
    }

    private class OnActionButtonClickListener implements View.OnClickListener {
        private Context context;
        private int orderId;
        private Headers headers;
        private String newStatus;
        IHandleOrderStatusChange changeStatusHandler;

        public OnActionButtonClickListener(Context context, int orderId, Headers headers, String newStatus, IHandleOrderStatusChange changeStatusHandler) {
            this.context = context;
            this.orderId = orderId;
            this.headers = headers;
            this.newStatus = newStatus;
            this.changeStatusHandler = changeStatusHandler;
        }

        @Override
        public void onClick(View v) {
            final LoadingFragment loadingFragment = LoadingFragment.create(context, "Updating status information...");
            loadingFragment.show();

            Order updatedStatusOrder = new Order(newStatus);
            orderData.edit(orderId, updatedStatusOrder, headers)
                .subscribe(new Consumer<Order>() {
                    @Override
                    public void accept(Order order) throws Exception {
                        loadingFragment.hide();
                        String notifyUserMessage = "";
                        if (order.getId() == orderId) {
                            notifyUserMessage = "Status changed to " + newStatus;
                            changeStatusHandler.handleStatusChange(newStatus);
                        } else {
                            notifyUserMessage = "Unable to update status";
                        }

                        ((DialogFragment.create(context, notifyUserMessage, 1))).show();
                    }
                });
        }
    }

    @Override
    public void handleStatusChange(String newStatus) {
        // Remove action buttons
        ((Button) root.findViewById(R.id.btn_order_history_mark_1)).setVisibility(View.GONE);
        ((Button) root.findViewById(R.id.btn_order_history_mark_2)).setVisibility(View.GONE);

        // Update status Text view
        TextView tvOrderStatus = (TextView) root.findViewById(R.id.tv_order_details_status);
        tvOrderStatus.setText(Utils.buildDetailsString("Status", newStatus.toUpperCase()));
        tvOrderStatus.setTextColor(Color.parseColor(Utils.getOrderStatusColor(newStatus)));
    }
}
