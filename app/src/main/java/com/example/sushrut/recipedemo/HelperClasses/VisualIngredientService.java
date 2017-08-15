package com.example.sushrut.recipedemo.HelperClasses;

import com.example.sushrut.recipedemo.NetworkCommunications.InternetDataManager;
import com.example.sushrut.recipedemo.VisualIngredient;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Sushrut on 8/15/2017.
 */

public class VisualIngredientService {
    private  InternetDataManager idm = null;
    public  VisualIngredientService(InternetDataManager idm){
        this.idm = idm;
    }
    public List<VisualIngredient> getVisualIngredients(VisualIngredient vi)
    {
        return null;
    }

    public  void sendVisualIngredient(VisualIngredient vi)
    {

    }

}
