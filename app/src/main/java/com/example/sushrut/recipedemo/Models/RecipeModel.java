package com.example.sushrut.recipedemo.Models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.net.URI;

/**
 * Created by Sushrut on 8/19/2017.
 */

public class RecipeModel {

    private int recipeId;
    private String recipeTitle;
    private String cookingTime;


    private String recipeImage;

    public RecipeModel(int recipeId, String recipeTitle, String cookingTime){
        this.recipeId = recipeId;
        this.recipeTitle = recipeTitle;
        this.cookingTime = cookingTime;
    }

    public String getCookingTime() {
        return cookingTime;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        this.recipeImage = recipeImage;
    }

    @Override
    public String toString() {
        return this.getRecipeTitle() + "\n" + "Made in " +this.getCookingTime() +" minutes";
    }
}
