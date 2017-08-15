package com.example.sushrut.recipedemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.net.URISyntaxException;

/**
 * Created by Sushrut on 8/6/2017.
 */

public class CameraImage {
    private Uri imageUri = null;
    private Bitmap bitmap = null;
    int redVal;
    int greenVal;
    int blueVal;
    Context context = null;
    String filePath = null;

    public CameraImage(Context context ,Uri imageUri) throws URISyntaxException {
        this.context = context;
        this.imageUri = imageUri;
        this.filePath = CommonUtils.getFilePathFromURI(context,imageUri);
    }

    public CameraImage(Context context,Uri imageUri ,Bitmap capturedImage) throws URISyntaxException {
        this.context = context;
        this.imageUri = imageUri;
        this.filePath = CommonUtils.getFilePathFromURI(context,imageUri);
        this.bitmap = CommonUtils.CompressBitmap(capturedImage);
    }

    public Context getContext() {
        return context;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getRedVal() {
        return redVal;
    }

    public void setRedVal(int redVal) {
        this.redVal = redVal;
    }

    public int getGreenVal() {
        return greenVal;
    }

    public void setGreenVal(int greenVal) {
        this.greenVal = greenVal;
    }

    public int getBlueVal() {
        return blueVal;
    }

    public void setBlueVal(int blueVal) {
        this.blueVal = blueVal;
    }

    public String getFilePath() {
        return filePath;
    }
}
