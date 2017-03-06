package com.topoffers.topoffers.buyer.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.topoffers.data.base.IData;
import com.topoffers.data.models.Headers;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.common.helpers.AuthenticationHelpers;
import com.topoffers.topoffers.common.models.AuthenticationCookie;
import com.topoffers.topoffers.common.models.Product;
import com.topoffers.topoffers.common.models.ProductsCart;

import java.util.ArrayList;
import java.util.Arrays;

import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyerProductDetailsExtraFragment extends Fragment {
    private static final String ARG_EXTRA_CART_KEY = "cart-key";
    private static final String ARG_EXTRA_ID_KEY = "id-key";
    private static final String ARG_EXTRA_DATA_KEY = "data-key";
    private static final String ARG_EXTRA_COOKIE_KEY = "cookie-key";

    private View root;
    private IData<Product> productData;
    private Product product;

    public BuyerProductDetailsExtraFragment() {
        // Required empty public constructor
    }

    public static BuyerProductDetailsExtraFragment createFragment(ProductsCart cart, int productId, IData<Product> data, AuthenticationCookie cookie) {
        BuyerProductDetailsExtraFragment buyerExtraFragment = new BuyerProductDetailsExtraFragment();
        Bundle args = new Bundle();

        buyerExtraFragment.setProductData(data);
        args.putSerializable(ARG_EXTRA_CART_KEY, cart);
        args.putInt(ARG_EXTRA_ID_KEY, productId);
        args.putSerializable(ARG_EXTRA_COOKIE_KEY, cookie);
        buyerExtraFragment.setArguments(args);

        return buyerExtraFragment;
    }

    public void setProductData(IData<Product> data){
        this.productData = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_buyer_product_details_extra, container, false);
        this.setupAddToCart();
        return root;
    }

    public void setupAddToCart(){
        final ProductsCart cart = (ProductsCart)this.getArguments().getSerializable(ARG_EXTRA_CART_KEY);
        final int productId = this.getArguments().getInt(ARG_EXTRA_ID_KEY);
        final AuthenticationCookie cookie = (AuthenticationCookie) this.getArguments().getSerializable(ARG_EXTRA_COOKIE_KEY);
        Button btn = (Button) this.root.findViewById(R.id.btn_add_to_cart);
        final Headers headers = AuthenticationHelpers.getAuthenticationHeaders(cookie);

        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                productData.getById(productId, headers)
                        .subscribe(new Consumer<Product>() {
                            @Override
                            public void accept(Product product) throws Exception {
                                cart.AddProduct(product);
                                Toast.makeText(getActivity(), "Successfully added to cart!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
