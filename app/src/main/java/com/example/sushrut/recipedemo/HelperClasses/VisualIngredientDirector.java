package com.example.sushrut.recipedemo.HelperClasses;

import android.content.Context;

import com.example.sushrut.recipedemo.BuildConfig;
import com.example.sushrut.recipedemo.CameraImage;
import com.example.sushrut.recipedemo.GoogleCloudVision;
import com.example.sushrut.recipedemo.ImageProcessor;
import com.example.sushrut.recipedemo.Models.GoogleImage;
import com.example.sushrut.recipedemo.Models.VisualIngredientViewModel;
import com.example.sushrut.recipedemo.NetworkCommunications.InternetDataManager;
import com.example.sushrut.recipedemo.VisualIngredient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * Created by Sushrut on 8/15/2017.
 */

public class VisualIngredientDirector {
    private InternetDataManager idm = null;
    private VisualIngredientBuilder builder = null;
    private ImageProcessor ip = null;
    private GoogleCloudVision gcv = null;
    private Context context = null;

    public VisualIngredientDirector(VisualIngredientViewModel vivm, Context context){
        idm = new InternetDataManager(new BuildConfig(),context);
        builder = new VisualIngredientBuilder();
        ip = new ImageProcessor();
        gcv = new GoogleCloudVision(vivm.getPackageName(),vivm.getPm(),
                vivm.getActivitySimpleName(),vivm.getBmp());
    }

    public VisualIngredient createVisualIngredient(VisualIngredientViewModel vivm) throws URISyntaxException,IOException{
        VisualIngredient vi = null;
        CameraImage ci = new CameraImage(vivm.getAppContext(),vivm.getImgUri(),vivm.getBmp());
        GoogleImage gi = gcv.uploadImageToGoogleCloud(vivm.getImgUri(),vivm.getContentResolver());
        ip.setCameraImage(ci);
        ip.setAverageColors();
        vi = builder.BuildVisualIngredient(ip,gcv,ci,gi);
        return  vi;
    }

    public void sendVisualIngredient(VisualIngredient vi) throws JSONException{
        JSONObject json = builder.BuildVisualIngredientJSON(vi);
        HashMap<String,String> headers = new HashMap<String,String>();
        headers.put("Content-Type","application/json");
        idm.sendJSONToServer(json,"saveIngredient",headers);
    }
}
