package com.example.sushrut.recipedemo.Models;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by Sushrut on 8/16/2017.
 */

public class VisualIngredientViewModel {
    private String IngredientName;
    private String cuisene;
    private Bitmap bmp;
    private int useFrequency;
    private String packageName;
    private String activitySimpleName;
    private PackageManager pm;

    public ContentResolver getContentResolver() {
        return contentResolver;
    }

    private ContentResolver contentResolver;
    private Uri imgUri;
    private Context appContext;

    public VisualIngredientViewModel(Bitmap bmp, String packageName,
                                     PackageManager pm, String activitySimpleName,
                                     Uri imgUri, Context appContext, ContentResolver contentResolver){
        this.bmp = bmp;
        this.packageName = packageName;
        this.pm = pm;
        this.activitySimpleName = activitySimpleName;
        this.imgUri = imgUri;
        this.appContext = appContext;
        this.contentResolver = contentResolver;
    }

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }

    public Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getActivitySimpleName() {
        return activitySimpleName;
    }

    public void setActivitySimpleName(String activitySimpleName) {
        this.activitySimpleName = activitySimpleName;
    }

    public PackageManager getPm() {
        return pm;
    }

    public void setPm(PackageManager pm) {
        this.pm = pm;
    }


    public String getIngredientName() {
        return IngredientName;
    }

    public void setIngredientName(String ingredientName) {
        IngredientName = ingredientName;
    }

    public String getCuisene() {
        return cuisene;
    }

    public void setCuisene(String cuisene) {
        this.cuisene = cuisene;
    }

    public Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public int getUseFrequency() {
        return useFrequency;
    }

    public void setUseFrequency(int useFrequency) {
        this.useFrequency = useFrequency;
    }
}
