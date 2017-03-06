package com.topoffers.topoffers.common.fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.topoffers.data.base.IData;
import com.topoffers.data.base.IImageData;
import com.topoffers.data.models.Headers;
import com.topoffers.data.services.ImagesHttpData;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.common.helpers.AuthenticationHelpers;
import com.topoffers.topoffers.common.helpers.Utils;
import com.topoffers.topoffers.common.models.AuthenticationCookie;
import com.topoffers.topoffers.common.models.Product;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class ProductDetailsFragment extends Fragment {
    public static final String INTENT_PRODUCT_KEY = "intent_product_key";

    private View root;
    private IData<Product> productData;
    private IImageData imageData;
    private AuthenticationCookie cookie;
    private Product mainProduct;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    public static ProductDetailsFragment create(int productId, IData<Product> productData, IImageData imageData, AuthenticationCookie cookie) {
        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();

        productDetailsFragment.setProductData(productData);
        productDetailsFragment.setImageData(imageData);
        productDetailsFragment.setCookie(cookie);

        Bundle args = new Bundle();
        args.putSerializable(INTENT_PRODUCT_KEY, productId);
        productDetailsFragment.setArguments(args);

        return productDetailsFragment;
    }

    public void setProductData(IData<Product> productData) {
        this.productData = productData;
    }

    public void setImageData(IImageData imageData) {
        this.imageData = imageData;
    }

    public void setCookie(AuthenticationCookie cookie) {
        this.cookie = cookie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_product_details, container, false);
        this.initProduct();
        return  root;
    }

    private void initProduct() {
        final Context context = this.getContext();
        Headers headers = AuthenticationHelpers.getAuthenticationHeaders(this.cookie);

        Bundle arguments = this.getArguments();
        final int productId = (int) arguments.getSerializable(INTENT_PRODUCT_KEY);

        final LoadingFragment loadingFragment = LoadingFragment.create(this.getContext(), "Preparing product data...");
        loadingFragment.show();

        productData.getById(productId, headers)
            .subscribe(new Consumer<Product>() {
                @Override
                public void accept(Product product) throws Exception {
                    mainProduct = product;

                    // Set title
                    TextView tvTitle = (TextView) root.findViewById(R.id.tv_product_details_title);
                    tvTitle.setText(product.getTitle());

                    // Set price
                    TextView tvPrice = (TextView) root.findViewById(R.id.tv_product_details_price);
                    tvPrice.setText(Utils.convertDoublePriceToStringPriceWithTag(product.getPrice()));

                    // Set quantity
                    TextView tvQuantity = (TextView) root.findViewById(R.id.tv_product_details_quantity);
                    tvQuantity.setText(String.format("Quantity: %s", product.getQuantity()));

                    // Set seller username
                    TextView tvSellerUsername = (TextView) root.findViewById(R.id.tv_product_details_seller_username);
                    tvSellerUsername.setText(String.format("Seller username: %s", product.getSellerUsername()));

                    // Set seller full name
                    TextView tvSellerFullname = (TextView) root.findViewById(R.id.tv_product_details_seller_fullName);
                    tvSellerFullname.setText(String.format("Seller username: %s %s", product.getSellerFirstName(), product.getSellerLastName()));

                    // Set date added
                    TextView tvDateAdded = (TextView) root.findViewById(R.id.tv_product_details_dateAdded);
                    tvDateAdded.setText(String.format("Date added: %s", Utils.convertDateToStringWithTime(product.getDateAdded())));

                    // Set description
                    TextView tvDescription = (TextView) root.findViewById(R.id.tv_product_details_description);
                    tvDescription.setText(String.format("Description: %s", product.getDescription()));

                    // Set image
                    final ImageView ivImage = (ImageView) root.findViewById(R.id.iv_product_details_image);
                    if (product.getImageIdentifier() != null) {
                        imageData.getImage(product.getImageIdentifier())
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

}
