package com.topoffers.topoffers.seller.helpers;

import android.content.Context;
import android.widget.Toast;

import java.io.File;

import pl.aprilapps.easyphotopicker.EasyImage;

/**
 * Created by Simeon on 27.2.2017 г..
 */

public class ImageHandler implements EasyImage.Callbacks {
    private Context context;
    private File imageFile;

    public ImageHandler(Context context) {
        this.context = context;
    }

    public File getImageFile() {
        return imageFile;
    }

    @Override
    public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
        try {
            throw e;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
        this.imageFile = imageFile;
    }

    @Override
    public void onCanceled(EasyImage.ImageSource source, int type) {
        this.imageFile = null;
    }
}
