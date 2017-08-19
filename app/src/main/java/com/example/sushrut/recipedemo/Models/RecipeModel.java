package com.example.sushrut.recipedemo.Models;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Sushrut on 8/19/2017.
 */

public class RecipeModel {

    private int recipeId;
    private String recipeTitle;
    private Drawable recipeImage;

    public RecipeModel(int recipeId, String recipeTitle){
        this.recipeId = recipeId;
        this.recipeTitle = recipeTitle;
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

    public Drawable getRecipeImage() {
        return recipeImage;
    }

    public void setRecipeImage(Drawable recipeImage) {
        this.recipeImage = recipeImage;
    }

}
