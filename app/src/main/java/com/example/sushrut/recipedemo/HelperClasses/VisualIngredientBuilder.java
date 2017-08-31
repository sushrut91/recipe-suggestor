package com.example.sushrut.recipedemo.HelperClasses;

import com.example.sushrut.recipedemo.CameraImage;
import com.example.sushrut.recipedemo.GoogleCloudVision;
import com.example.sushrut.recipedemo.ImageProcessor;
import com.example.sushrut.recipedemo.Models.GoogleImage;
import com.example.sushrut.recipedemo.VisualIngredient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sushrut on 8/15/2017.
 */

public class VisualIngredientBuilder {
    //void changes to VisualIngredient In Future
    public VisualIngredient BuildVisualIngredient(ImageProcessor ip, GoogleCloudVision gcv
            , CameraImage ci, GoogleImage gi){


        VisualIngredient vi = new VisualIngredient();
        // Set CameraImage properties first
        vi.setRedVal(ci.getRedVal());
        vi.setGreenVal(ci.getGreenVal());
        vi.setBlueVal(ci.getBlueVal());
        vi.setContourShape(ip.DetectShapes());
        vi.setNoOfContours(ci.getNoOfContours());
        vi.setShapeVertices(ci.getShapeVertices());

        // Set GoogleImage properties
        vi.setGoogleRedVal(gi.getRedVal());
        vi.setGoogleGreenVal(gi.getGreenVal());
        vi.setGoogleBlueVal(gi.getBlueVal());
        vi.setGoogleSuggestions(gi.getCloudVisionSuggestions());
        vi.setGoogleSuggestedName(gi.getGoogleSuggestedName());

        //Set User provided properties
        vi.setUserSuggestedName(ci.getUserSuggestedName());
        vi.setUseFrequencyRating(ci.getUserSuggestedUseFrequency());
        vi.setCuisine(ci.getCusiene());
        return  vi;
    }

    public JSONObject BuildVisualIngredientJSON(VisualIngredient vi) throws JSONException{
        JSONObject json = new JSONObject();
        json.put("user_suggested_name",vi.getUserSuggestedName());
        json.put("dominant_color",vi.getDominantColor());
        json.put("contour_shape",vi.getContourShape());
        json.put("shape_vertices",vi.getShapeVertices());
        json.put("cusine",vi.getCuisine());
        json.put("no_of_contours",vi.getNoOfContours());
        json.put("red_value",vi.getRedVal());
        json.put("green_value",vi.getGreenVal());
        json.put("blue_value",vi.getBlueVal());
        json.put("use_frequency",vi.getUseFrequencyRating());
        json.put("google_suggested_name",vi.getGoogleSuggestedName());
        json.put("google_dominant_color",vi.getGoogleDominantColor());
        json.put("google_red_value",vi.getGoogleRedVal());
        json.put("google_green_value",vi.getGoogleGreenVal());
        json.put("google_blue_value",vi.getGoogleBlueVal());
        json.put("google_suggestions",vi.getGoogleSuggestions().toString());
        return json;
    }
}
