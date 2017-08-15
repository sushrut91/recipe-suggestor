package com.example.sushrut.recipedemo.NetworkCommunications;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sushrut.recipedemo.BuildConfig;
import com.example.sushrut.recipedemo.RecipeCall;
import com.example.sushrut.recipedemo.VisualIngredient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sushrut on 8/15/2017.
 */

public class InternetDataManager {
    private final String TAG = "InternetDataManager";
    private String RECIPE_API_KEY = null;
    private Context context = null;
    private JSONArray responseJsonArray = null;
    public String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients";

    public InternetDataManager(BuildConfig config){
        this.RECIPE_API_KEY = config.RECIPE_API_KEY;
    }

    public JSONArray getDetectedIngredients(VisualIngredient vi)
    {
        return null;
    }

    public  void sendVisualIngredient(VisualIngredient vi)
    {

    }

    public JSONArray getRecipesByIngredient(String ingredients)
    {
        url = url +"?"+ ingredients.trim();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
// Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, this.url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //recipeTxt.setText("Response is: "+ response.substring(0,500));
                        try{
                            JSONObject obj = new JSONObject(response);
                            responseJsonArray = obj.getJSONArray("results");
                            //recipeTxt.setText(obj.getJSONArray("results").getJSONObject(0).getString("title"));
                        }
                        catch(JSONException ex){
                            Log.d(TAG, "onResponse: " +ex.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                responseJsonArray.put(error);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Map<String,String> params =  super.getHeaders();
                Map<String,String> params =  new HashMap<>();
                if(params==null)params = new HashMap<>();
                params.put("X-Mashape-Authorization", RECIPE_API_KEY);
                //..add other headers
                return params;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);

        return responseJsonArray;
    }
}
