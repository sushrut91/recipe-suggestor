package com.example.sushrut.recipedemo.HelperClasses;

import com.example.sushrut.recipedemo.Models.VisualIngredientViewModel;
import com.example.sushrut.recipedemo.NetworkCommunications.InternetDataManager;
import com.example.sushrut.recipedemo.VisualIngredient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Sushrut on 8/15/2017.
 */

public class VisualIngredientService implements IVisualIngredient {
    private  VisualIngredientDirector director = null;

    public  VisualIngredientService( VisualIngredientDirector director){
        this.director = director;
    }
    public void createVisualIngredient(VisualIngredientViewModel vivm) throws URISyntaxException, Exception{
        VisualIngredient vi = director.createVisualIngredient(vivm);
        if(vi!=null)
            director.sendVisualIngredient(vi);
        else
            throw new Exception("Error creation VisualIngredient");
    }
    public List<VisualIngredient> getVisualIngredients(VisualIngredientViewModel vivm)
    {
        return null;
    }

    public JSONObject sendVisualIngredient(VisualIngredientViewModel vivm)
    {
        return null;
    }

}
