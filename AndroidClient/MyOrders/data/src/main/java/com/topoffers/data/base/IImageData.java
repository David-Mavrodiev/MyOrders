package com.topoffers.data.base;

import android.graphics.Bitmap;

import io.reactivex.Observable;

/**
 * Created by Simeon on 28.2.2017 г..
 */

public interface IImageData {
    Observable<Bitmap> getImage(String url);
}
