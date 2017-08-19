package com.example.sushrut.recipedemo.HelperClasses;

import com.example.sushrut.recipedemo.Models.RecipeModel;
import com.example.sushrut.recipedemo.NetworkCommunications.InternetDataManager;
import com.example.sushrut.recipedemo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public List<RecipeModel> getRecipesByName (String searchString) throws JSONException{
        List<RecipeModel> recipeList = new ArrayList<>();
        JSONArray receivedArray = idm.getJSONFromRecipeApi(searchString, findByRecipeNameURL);
        for(int i = 0 ; i<receivedArray.length();i++){
           recipeList.add( new RecipeModel(
                Integer.parseInt(receivedArray.getJSONObject(i).get("id").toString()),
                    receivedArray.getJSONObject(i).get("title").toString()
            ));
        }
        return  recipeList;
    }

    public  List<RecipeModel> getRecipesByIngredients(String ingredients) throws JSONException{
        List<RecipeModel> recipeList = new ArrayList<>();
        JSONArray receivedArray = idm.getJSONFromRecipeApi(ingredients, findByRecipeNameURL);
        for(int i = 0 ; i<receivedArray.length();i++){
            recipeList.add( new RecipeModel(
                    Integer.parseInt(receivedArray.getJSONObject(i).get("id").toString()),
                    receivedArray.getJSONObject(i).get("title").toString()
            ));
        }
        return  recipeList;
    }
}
