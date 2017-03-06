package com.topoffers.topoffers.common.services;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Simeon on 5.3.2017 г..
 */

public class GetApplicationDescriptionFileService extends IntentService {
    public GetApplicationDescriptionFileService() {
        this("DEFAULT_NAME");
    }

    public GetApplicationDescriptionFileService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Observable.just(5)
            .subscribe(new Consumer<Integer>() {
                @Override
                public void accept(Integer integer) throws Exception {
                    new DownloadFileFromURL().execute("http://192.168.0.103:8000/static/top-offers-description.pdf");
                }
            });
    }

    private class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... fileUrls) {
            int count;
            try {
                String descriptionFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/top-offers-description.pdf";
                File file = new File(descriptionFilePath);
                if (!file.exists()) {
                    URL url = new URL(fileUrls[0]);
                    URLConnection confection = url.openConnection();
                    confection.connect();

                    InputStream input = new BufferedInputStream(url.openStream(), 8192);
                    OutputStream output = new FileOutputStream(descriptionFilePath);

                    byte data[] = new byte[1024];
                    long total = 0;

                    while ((count = input.read(data)) != -1) {
                        total += count;
                        // writing data to file
                        output.write(data, 0, count);
                    }

                    // flushing output
                    output.flush();

                    // closing streams
                    output.close();
                    input.close();
                }
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }

            return null;
        }
    }
}
