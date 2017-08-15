package com.example.sushrut.recipedemo.HelperClasses;

import com.example.sushrut.recipedemo.NetworkCommunications.InternetDataManager;

/**
 * Created by Sushrut on 8/15/2017.
 */

public class RecipeService {

    private InternetDataManager idm = null;
    private String ingredientList = null;

    public  RecipeService(InternetDataManager idm){
        this.idm = idm;
    }
}
