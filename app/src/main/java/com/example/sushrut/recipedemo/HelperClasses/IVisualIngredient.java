package com.example.sushrut.recipedemo.HelperClasses;

import com.example.sushrut.recipedemo.NetworkCommunications.InternetDataManager;
import com.example.sushrut.recipedemo.VisualIngredient;
import com.example.sushrut.recipedemo.Models.VisualIngredientViewModel;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by Sushrut on 8/16/2017.
 * Client Interface Specification. Every service which will interface with client must implement this interface.
 */

public interface IVisualIngredient {
    public void createVisualIngredient(VisualIngredientViewModel vivm)throws URISyntaxException, Exception;
    public List<VisualIngredient> getVisualIngredients(VisualIngredientViewModel vivm);
    public JSONObject sendVisualIngredient(VisualIngredientViewModel vivm);
}
