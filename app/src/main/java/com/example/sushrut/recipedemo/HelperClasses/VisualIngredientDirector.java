package com.example.sushrut.recipedemo.HelperClasses;

import android.content.Context;
import android.util.Log;

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
import java.util.concurrent.ExecutionException;

/**
 * Created by Sushrut on 8/15/2017.
 */

public class VisualIngredientDirector {
    private InternetDataManager idm = null;
    private VisualIngredientBuilder builder = null;
    private ImageProcessor ip = null;
    private GoogleCloudVision gcv = null;
    private Context context = null;
    private final String TAG="VID";

    public VisualIngredientDirector(VisualIngredientViewModel vivm, Context context){
        idm = new InternetDataManager(new BuildConfig(),context);
        builder = new VisualIngredientBuilder();
        ip = new ImageProcessor();
        gcv = new GoogleCloudVision(vivm.getPackageName(),vivm.getPm(),
                vivm.getActivitySimpleName(),vivm.getBmp());
    }

    public void createVisualIngredient(VisualIngredientViewModel vivm) throws URISyntaxException,IOException,
            ExecutionException,InterruptedException,JSONException
    {
        VisualIngredient vi = null;
        CameraImage ci = new CameraImage(vivm.getAppContext(),vivm.getImgUri(),vivm.getBmp());
        Log.d(TAG, "CameraImg created");
        ci.setUserSuggestedName(vivm.getIngredientName());
        ci.setBitmap(vivm.getBmp());
        ci.setCusiene(vivm.getCuisine());
        ip.setCameraImage(ci);
        ci.setDominantColor(ip.getDominantColorInCameraImg());
        ip.setCameraImage(ci);
        ip.setAverageColors();
        Log.d(TAG, "Avg colours set");
        GoogleImage gi = gcv.uploadCloudImg();
        Log.d(TAG, "Got google img from cloud");
        //Wait for google to do its processing
        /*while(true){
            Log.d(TAG, "In loop");
            if(gi.getCloudVisionSuggestions() != null)
                break;
        }*/
        vi = builder.BuildVisualIngredient(ip,gcv,ci,gi);
        Log.d(TAG,"Sending data");
        sendVisualIngredient(vi);
    }

    private void sendVisualIngredient(VisualIngredient vi) throws JSONException{
        JSONObject json = builder.BuildVisualIngredientJSON(vi);
        Log.d(TAG, "sendVisualIngredient: " +json.toString());
        idm.sendJSONToServer(json,"saveIngredient");
    }
}
