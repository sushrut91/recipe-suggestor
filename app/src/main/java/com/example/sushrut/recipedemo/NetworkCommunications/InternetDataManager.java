package com.example.sushrut.recipedemo.NetworkCommunications;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
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

import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
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
    private AppServerResponse appServerResponse = null;

    public InternetDataManager(BuildConfig config, Context context){
        this.SERVER_API_KEY = config.SERVER_API_KEY;
        this.SERVER_URL = config.SERVER_URL;
        this.context = context;
    }

    public AppServerResponse sendJSONToServer(JSONObject vi, String methodName) throws JSONException,InterruptedException{
        //Add server_api key for authorization
        String requestBody = vi.toString();
        appServerResponse = new AppServerResponse();

        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest JOPR = new JsonObjectRequest(Request.Method.POST, SERVER_URL + methodName,requestBody,
                new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response){
                try {
                    Log.d(TAG, "onResponse: "+response.toString(4));
                    appServerResponse.setMessage(response.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error.toString());
                appServerResponse.setHttpStatusCode(404);
                appServerResponse.setMessage(error.getMessage());
                return;
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Content-Type", "application/json");
                headers.put("x-access-token",SERVER_API_KEY);
                return headers;
            }


            /*@Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    Log.d(TAG, "getBody: " +uee.getMessage());
                    return null;
                }
            }*/
        };
        JOPR.setRetryPolicy(new DefaultRetryPolicy (7000,  1, 1f ));
        queue.add(JOPR);
        Thread.sleep(10000);
        return appServerResponse;
    }

}
