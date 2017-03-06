package com.topoffers.topoffers.common.helpers;

import android.content.Context;
import android.content.Intent;

import com.topoffers.topoffers.buyer.activities.BuyerProductsListActivity;
import com.topoffers.topoffers.common.models.AuthenticationCookie;
import com.topoffers.topoffers.seller.activities.SellerOrderHistoryListActivity;
import com.topoffers.topoffers.seller.activities.SellerProductsListActivity;

import java.util.Objects;

public class RedirectHelpers {
    public static Intent baseRedirect(Context context, AuthenticationCookie cookie) {
        Intent intent;
        if (cookie != null) {
            if (Objects.equals(cookie.getRole(), "seller")) {
                intent = new Intent(context, SellerProductsListActivity.class);
            } else {
                intent = new Intent(context, BuyerProductsListActivity.class);
            }
        } else {
            intent = null;
        }

        return intent;
    }
}
