package com.topoffers.topoffers.seller.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.topoffers.data.base.IData;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.common.fragments.DialogFragment;
import com.topoffers.topoffers.common.helpers.AuthenticationHelpers;
import com.topoffers.topoffers.common.models.AuthenticationCookie;
import com.topoffers.topoffers.common.models.Product;
import com.topoffers.topoffers.seller.activities.SellerOrderHistoryListActivity;
import com.topoffers.topoffers.seller.activities.SellerProductsListActivity;
import com.topoffers.topoffers.seller.activities.UpdateProductActivity;
import com.topoffers.topoffers.seller.helpers.ActionButtonsFactory;

import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellerProductDetailsExtraFragment extends Fragment {
    private static final String BUNDLE_ARGUMENTS_PRODUCT_KEY = "bundle_arguments_product_key";

    private View root;
    private IData<Product> productData;
    private AuthenticationCookie cookie;

    public SellerProductDetailsExtraFragment() {
        // Required empty public constructor
    }

    public static SellerProductDetailsExtraFragment create(int productId, IData<Product> productData, AuthenticationCookie cookie) {
        SellerProductDetailsExtraFragment sellerProductDetailsExtraFragment = new SellerProductDetailsExtraFragment();

        sellerProductDetailsExtraFragment.setProductData(productData);
        sellerProductDetailsExtraFragment.setCookie(cookie);

        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_ARGUMENTS_PRODUCT_KEY, productId);
        sellerProductDetailsExtraFragment.setArguments(args);

        return sellerProductDetailsExtraFragment;
    }

    public void setProductData(IData<Product> productData) {
        this.productData = productData;
    }

    public void setCookie(AuthenticationCookie cookie) {
        this.cookie = cookie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_seller_product_details_extra, container, false);
        this.addEditButtonListener();
        return  root;
    }

    private void addEditButtonListener() {
        final Context context = this.getContext();
        Bundle arguments = this.getArguments();
        final int productId = (int) arguments.getSerializable(BUNDLE_ARGUMENTS_PRODUCT_KEY);

        Button btnEditProduct =  (Button) root.findViewById(R.id.btn_edit_product);
        btnEditProduct.setOnClickListener(ActionButtonsFactory.getActionButtonWithId(context, UpdateProductActivity.class, UpdateProductActivity.INTENT_UPDATE_PRODUCT_ID, productId));

        Button btnOrderHistoryProduct =  (Button) root.findViewById(R.id.btn_order_history_product);
        btnOrderHistoryProduct.setOnClickListener(ActionButtonsFactory.getActionButtonWithId(context, SellerOrderHistoryListActivity.class, SellerOrderHistoryListActivity.INTENT_ORDER_PRODUCT_ID, productId));

        Button btnDeleteProduct =  (Button) root.findViewById(R.id.btn_delete_product);
        btnDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productData.delete(productId, AuthenticationHelpers.getAuthenticationHeaders(cookie))
                    .subscribe(new Consumer<Product>() {
                        @Override
                        public void accept(Product product) throws Exception {
                            if (product.getId() == productId) {
                                Intent intent = new Intent(context, SellerProductsListActivity.class);
                                context.startActivity(intent);
                            } else {
                                (DialogFragment.create(context, "Error while deleting", 1)).show();
                            }
                        }
                    });
            }
        });
    }

}
