package com.example.sushrut.recipedemo.HelperClasses;

import com.example.sushrut.recipedemo.Models.RecipeModel;
import com.example.sushrut.recipedemo.NetworkCommunications.InternetDataManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sushrut on 8/15/2017.
 */

public class RecipeService {

    private final String TAG ="RecipeService";
    private InternetDataManager idm = null;
    private String ingredientList = null;
    private String findByIngredientsURL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?";
    private String findByRecipeNameURL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?query=";

    public String getFindByRecipeNameURL() {
        return findByRecipeNameURL;
    }


    public  RecipeService(InternetDataManager idm){
        this.idm = idm;
    }
}
