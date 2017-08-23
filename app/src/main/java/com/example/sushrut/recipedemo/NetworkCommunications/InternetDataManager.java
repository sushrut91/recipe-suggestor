package com.example.sushrut.recipedemo.NetworkCommunications;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sushrut.recipedemo.BuildConfig;
import com.example.sushrut.recipedemo.Models.RecipeModel;
import com.example.sushrut.recipedemo.RecipeCall;
import com.example.sushrut.recipedemo.VisualIngredient;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by Sushrut on 8/15/2017.
 */

public class InternetDataManager {
    private final String TAG = "InternetDataManager";
    private String SERVER_API_KEY = null;
    private Context context = null;
    private JSONArray responseJsonArray = null;
    public String SERVER_URL = null;


    public InternetDataManager(BuildConfig config, Context context){
        this.SERVER_API_KEY = config.SERVER_API_KEY;
        this.SERVER_URL = config.SERVER_URL;
        this.context = context;
    }

    public void sendJSONToServer(JSONObject vi) throws JSONException{
        RequestQueue queue = Volley.newRequestQueue(context);
// Request a string response from the provided URL.
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, SERVER_URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display the first 500 characters of the response string.
                        //recipeTxt.setText("Response is: "+ response.substring(0,500));
                        try{
                            Toast.makeText(context,response.getString(""),Toast.LENGTH_LONG);
                        }
                        catch(Exception ex){
                            Log.d(TAG, "onResponse: " +ex.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Map<String,String> params =  super.getHeaders();
                Map<String,String> params =  new HashMap<>();
                if(params==null)params = new HashMap<>();
                params.put("X-Mashape-Authorization", SERVER_API_KEY);
                //..add other headers
                return params;
            }
        };
// Add the request to the RequestQueue.
        queue.add(jsonRequest);
    }

}
