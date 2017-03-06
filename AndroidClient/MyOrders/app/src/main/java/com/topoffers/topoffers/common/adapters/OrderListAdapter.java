package com.topoffers.topoffers.common.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
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
import com.topoffers.topoffers.common.models.Order;

import java.text.DecimalFormat;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

public class OrderListAdapter extends ArrayAdapter<Order> {
    private IImageData imageData;

    public OrderListAdapter(Context context, IImageData imageData) {
        super(context, -1);
        this.imageData = imageData;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(this.getContext());
            view = inflater.inflate(R.layout.item_order, parent, false);
        }

        Order currentOrder = this.getItem(position);

        // Set title
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_order_title);
        tvTitle.setText(currentOrder.getProductTitle());

        // Set quantity and single price
        TextView tvQuantityAndSinglePrice = (TextView) view.findViewById(R.id.tv_order_quantity_and_single_price);
        String quantityAndSinglePrice = String.format("%s x %s", currentOrder.getQuantity(), Utils.convertDoublePriceToStringPriceWithTag(currentOrder.getSinglePrice(), "Single price"));
        tvQuantityAndSinglePrice.setText(quantityAndSinglePrice);

        TextView tvTotalPrice = (TextView) view.findViewById(R.id.tv_order_total_price);
        tvTotalPrice.setText(Utils.convertDoublePriceToStringPriceWithTag(currentOrder.getTotalPrice(), "Total price"));

        TextView tvOrderedDate = (TextView) view.findViewById(R.id.tv_order__list_dateOrdered);
        String status = Utils.convertDateToStringWithTime(currentOrder.getDateOrdered());
        tvOrderedDate.setText(Utils.buildDetailsString("Date ordered", status));

        // Set image
        final ImageView ivImage = (ImageView) view.findViewById(R.id.iv_order_image);
        if (currentOrder.getProductImageIdentifier() != null) {
            imageData.getImage(currentOrder.getProductImageIdentifier())
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
