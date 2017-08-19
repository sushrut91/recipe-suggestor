package com.example.sushrut.recipedemo.HelperClasses;

import com.example.sushrut.recipedemo.NetworkCommunications.InternetDataManager;
import com.example.sushrut.recipedemo.R;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Sushrut on 8/15/2017.
 */

public class RecipeService {

    private InternetDataManager idm = null;
    private String ingredientList = null;
    private String findByIngredientsURL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?";
    private String findByRecipeNameURL = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?query=";

    public  RecipeService(InternetDataManager idm){
        this.idm = idm;
    }

    public JSONArray getRecipesByName (String searchString){
        return idm.getJSONFromRecipeApi(searchString, findByRecipeNameURL);
    }

    public JSONArray getRecipesByIngredients(String ingredients){
        return idm.getJSONFromRecipeApi(ingredients,findByIngredientsURL);
    }
}
