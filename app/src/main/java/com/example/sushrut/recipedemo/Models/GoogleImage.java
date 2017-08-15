package com.example.sushrut.recipedemo.Models;

import java.util.HashMap;

/**
 * Created by Sushrut on 8/15/2017.
 */

public class GoogleImage {
    private Float redVal;
    private Float greenVal;
    private Float blueVal;
    private HashMap<String,Float> cloudVisionSuggestions;

    public String getGoogleSuggestedName() {
        return googleSuggestedName;
    }

    public void setGoogleSuggestedName(String googleSuggestedName) {
        this.googleSuggestedName = googleSuggestedName;
    }

    private String googleSuggestedName;

    public String getDominantColor() {
        return dominantColor;
    }

    public void setDominantColor(String dominantColor) {
        this.dominantColor = dominantColor;
    }

    String dominantColor = null;

    public Float getRedVal() {
        return redVal;
    }

    public void setRedVal(Float redVal) {
        this.redVal = redVal;
    }

    public Float getGreenVal() {
        return greenVal;
    }

    public void setGreenVal(Float greenVal) {
        this.greenVal = greenVal;
    }

    public Float getBlueVal() {
        return blueVal;
    }

    public void setBlueVal(Float blueVal) {
        this.blueVal = blueVal;
    }

    public HashMap<String, Float> getCloudVisionSuggestions() {
        return cloudVisionSuggestions;
    }

    public void setCloudVisionSuggestions(HashMap<String, Float> cloudVisionSuggestions) {
        this.cloudVisionSuggestions = cloudVisionSuggestions;
    }
}
