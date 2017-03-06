package com.topoffers.topoffers.common.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.topoffers.data.base.IData;
import com.topoffers.data.base.IImageData;
import com.topoffers.data.models.Headers;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.common.adapters.ProductListAdapter;
import com.topoffers.topoffers.common.helpers.AuthenticationHelpers;
import com.topoffers.topoffers.common.helpers.OnSwipeTouchListener;
import com.topoffers.topoffers.common.models.AuthenticationCookie;
import com.topoffers.topoffers.common.models.Product;

import java.security.DigestException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsListFragment extends Fragment {
    private View root;
    private IData<Product> productData;
    private IImageData imageHttpData;
    private AuthenticationCookie cookie;
    private Class classToNavigateOnItemClick;
    private ArrayList<Product> mainProducts;
    Headers headers;

    public ProductsListFragment() {
        // Required empty public constructor
        mainProducts = new ArrayList<>();
    }

    public static ProductsListFragment create(IData<Product> productData, IImageData imageHttpData, AuthenticationCookie cookie, Class classToNavigateOnItemClick) {
        ProductsListFragment productsListFragment = new ProductsListFragment();
        productsListFragment.setProductData(productData);
        productsListFragment.setImageHttpData(imageHttpData);
        productsListFragment.setCookie(cookie);
        productsListFragment.setClassToNavigateOnItemClick(classToNavigateOnItemClick);
        return productsListFragment;
    };

    public void setProductData(IData<Product> productData) {
        this.productData = productData;
    }

    public void setImageHttpData(IImageData imageHttpData) {
        this.imageHttpData = imageHttpData;
    }

    public void setCookie(AuthenticationCookie cookie) {
        this.cookie = cookie;
    }

    public void setClassToNavigateOnItemClick(Class classToNavigateOnItemClick) {
        this.classToNavigateOnItemClick = classToNavigateOnItemClick;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_products_list, container, false);
        headers = AuthenticationHelpers.getAuthenticationHeaders(this.cookie);
        this.initProductsList();
        return root;
    }

    private void initProductsList() {
        final ListView lvProducts = (ListView) root.findViewById(R.id.lv_products);
        final ArrayAdapter<Product> productsAdapter = new ProductListAdapter(this.getContext(), this.imageHttpData);

        lvProducts.setAdapter(productsAdapter);

        final Context context = this.getContext();
        lvProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product clickedProduct = mainProducts.get(position);

                Intent intent = new Intent(context, classToNavigateOnItemClick);
                intent.putExtra(ProductDetailsFragment.INTENT_PRODUCT_KEY, clickedProduct.getId());
                context.startActivity(intent);
            }
        });

        lvProducts.setOnTouchListener(new OnSwipeTouchListener(getContext(), lvProducts) {
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
    }

    private void loadProducts(final ArrayAdapter<Product> productsAdapter) {
        final LoadingFragment loadingFragment = LoadingFragment.create(this.getContext(), "Preparing products...");
        loadingFragment.show();

        productData.getAll(headers)
            .subscribe(new Consumer<Product[]>() {
                @Override
                public void accept(Product[] products) throws Exception {
                    mainProducts = new ArrayList<Product>(Arrays.asList(products));

                    if (products.length > 0) {
                        TextView tvNoProductsAdded = (TextView) root.findViewById(R.id.tv_no_products_added);
                        tvNoProductsAdded.setVisibility(View.GONE);
                    }

                    productsAdapter.clear();
                    productsAdapter.addAll(products);

                    loadingFragment.hide();
                }
            });
    }
}
