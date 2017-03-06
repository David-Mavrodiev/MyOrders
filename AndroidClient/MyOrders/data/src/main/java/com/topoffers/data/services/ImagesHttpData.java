package com.topoffers.data.services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.topoffers.data.base.IImageData;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImagesHttpData implements IImageData {
    private String url;
    private final OkHttpClient httpClient;

    public ImagesHttpData(String url) {
        this.url = url;
        this.httpClient = new OkHttpClient();
        int seconds = 1500;
        httpClient.connectTimeoutMillis();
    }

    public Observable<Bitmap> getImage(final String imageFilename) {
        return Observable
            .create(new ObservableOnSubscribe<Bitmap>() {
                @Override
                public void subscribe(ObservableEmitter<Bitmap> e) throws Exception {
                    String text = url + imageFilename;
                    Log.d("ImageGet", text);
                    Request request = new Request.Builder()
                            .url(url + imageFilename)
                            .build();

                    Response response = httpClient.newCall(request).execute();
                    InputStream resultStream = response.body().byteStream();

                    Bitmap bitmap = BitmapFactory.decodeStream(resultStream);
                    e.onNext(bitmap);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
}
