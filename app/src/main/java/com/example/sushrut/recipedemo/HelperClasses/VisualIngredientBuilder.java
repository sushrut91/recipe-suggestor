package com.example.sushrut.recipedemo.HelperClasses;

import com.example.sushrut.recipedemo.CameraImage;
import com.example.sushrut.recipedemo.GoogleCloudVision;
import com.example.sushrut.recipedemo.ImageProcessor;
import com.example.sushrut.recipedemo.Models.GoogleImage;
import com.example.sushrut.recipedemo.VisualIngredient;

/**
 * Created by Sushrut on 8/15/2017.
 */

public class VisualIngredientBuilder {
    //void changes to VisualIngredient In Future
    public VisualIngredient CreateVisualIngredient(ImageProcessor ip, GoogleCloudVision gcv
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
}
