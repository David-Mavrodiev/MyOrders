package com.topoffers.topoffers.common.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.topoffers.data.base.IImageData;
import com.topoffers.data.services.ImagesHttpData;
import com.topoffers.topoffers.R;
import com.topoffers.topoffers.common.helpers.Utils;
import com.topoffers.topoffers.common.models.Product;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private IImageData imageData;

    public ProductListAdapter(Context context, IImageData imageData) {
        super(context, -1);
        this.imageData = imageData;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            view = inflater.inflate(R.layout.item_product, parent, false);
        }

        Product currentProduct = this.getItem(position);

        // Set title
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_product_title);
        tvTitle.setText(currentProduct.getTitle());

        // Set price
        TextView tvPrice = (TextView) view.findViewById(R.id.tv_product_price);
        tvPrice.setText(Utils.convertDoublePriceToStringPriceWithTag(currentProduct.getPrice()));

        // Set seller username
        TextView tvSellerUsername = (TextView) view.findViewById(R.id.tv_product_seller_username);
        tvSellerUsername.setText(String.format("by %s", currentProduct.getSellerUsername()));

        // Set date added
        TextView tvDateAdded = (TextView) view.findViewById(R.id.tv_product_dateAdded);
        tvDateAdded.setText(String.format("Date added %s", Utils.convertDateToString(currentProduct.getDateAdded())));

        // Set image
        final ImageView ivImage = (ImageView) view.findViewById(R.id.iv_product_image);
        if (currentProduct.getImageIdentifier() != null) {
            imageData.getImage(currentProduct.getImageIdentifier())
                    .subscribe(new Consumer<Bitmap>() {
                        @Override
                        public void accept(Bitmap bitmap) throws Exception {
                            ivImage.setImageBitmap(bitmap);
                        }
                    });
        }

        return view;
    }
}
