package com.example.sushrut.recipedemo;

import java.util.HashMap;
import java.util.Date;

/**
 * Created by Sushrut on 8/14/2017.
 */

public class VisualIngredient {

    private String userSuggestedName;
    private String googleSuggestedName;
    private String dominantColor;
    private String googleDominantColor;
    private String contourShape;
    private int shapeVertices;
    private String cuisine;
    private int noOfContours;
    private HashMap<String,Float> googleSuggestions;
    private float redVal;
    private Float googleRedVal;
    private float greenVal;
    private Float googleGreenVal;
    private float blueVal;
    private Float googleBlueVal;
    private int useFrequencyRating;

    public VisualIngredient(Float googleRedVal, Float googleGreenVal, Float googleBlueVal,
                            HashMap<String,Float> googleSuggestions){
        this.googleRedVal = googleRedVal;
        this.googleGreenVal = googleGreenVal;
        this.googleBlueVal = googleBlueVal;
        this.googleSuggestions = googleSuggestions;
    }

    public String getGoogleSuggestedName() {
        return googleSuggestedName;
    }

    public void setGoogleSuggestedName(String googleSuggestedName) {
        this.googleSuggestedName = googleSuggestedName;
    }

    public String getUserSuggestedName() {
        return userSuggestedName;
    }

    public void setUserSuggestedName(String userSuggestedName) {
        this.userSuggestedName = userSuggestedName;
    }

    public String getDominantColor() {
        return dominantColor;
    }

    public void setDominantColor(String dominantColor) {
        this.dominantColor = dominantColor;
    }

    public String getContourShape() {
        return contourShape;
    }

    public void setContourShape(String contourShape) {
        this.contourShape = contourShape;
    }

    public int getShapeVertices() {
        return shapeVertices;
    }

    public void setShapeVertices(int shapeVertices) {
        this.shapeVertices = shapeVertices;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public int getNoOfContours() {
        return noOfContours;
    }

    public void setNoOfContours(int noOfContours) {
        this.noOfContours = noOfContours;
    }

    public HashMap<String, Float> getGoogleSuggestions() {
        return googleSuggestions;
    }

    public void setGoogleSuggestions(HashMap<String, Float> googleSuggestions) {
        this.googleSuggestions = googleSuggestions;
    }

    public float getRedVal() {
        return redVal;
    }

    public void setRedVal(float redVal) {
        this.redVal = redVal;
    }

    public float getGreenVal() {
        return greenVal;
    }

    public void setGreenVal(float greenVal) {
        this.greenVal = greenVal;
    }

    public float getBlueVal() {
        return blueVal;
    }

    public void setBlueVal(float blueVal) {
        this.blueVal = blueVal;
    }

    public int getUseFrequencyRating() {
        return useFrequencyRating;
    }

    public void setUseFrequencyRating(int useFrequencyRating) {
        this.useFrequencyRating = useFrequencyRating;
    }

    public String getGoogleDominantColor() {
        return googleDominantColor;
    }

    public void setGoogleDominantColor(String googleDominantColor) {
        this.googleDominantColor = googleDominantColor;
    }

    public float getGoogleRedVal() {
        return googleRedVal;
    }

    public void setGoogleRedVal(float googleRedVal) {
        this.googleRedVal = googleRedVal;
    }

    public float getGoogleGreenVal() {
        return googleGreenVal;
    }

    public void setGoogleGreenVal(float googleGreenVal) {
        this.googleGreenVal = googleGreenVal;
    }

    public float getGoogleBlueVal() {
        return googleBlueVal;
    }

    public void setGoogleBlueVal(float googleBlueVal) {
        this.googleBlueVal = googleBlueVal;
    }

    @Override
    public String toString() {
        return "VisualIngredient{" +
                "userSuggestedName='" + userSuggestedName + '\'' +
                "GoogleSuggestedName='" + googleSuggestedName + '\'' +
                ", dominantColor='" + dominantColor + '\'' +
                ", contourShape='" + contourShape + '\'' +
                '}';
    }
}
