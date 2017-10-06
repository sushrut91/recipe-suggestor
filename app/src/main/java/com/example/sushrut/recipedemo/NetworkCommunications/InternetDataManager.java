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

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    public AppServerResponse sendJSONToServer(JSONObject vi, String methodName) {
        //Add server_api key for authorization
        String requestBody = vi.toString();
        appServerResponse = new AppServerResponse();

        HttpURLConnection urlConnection=null;
        try
        {
            URL url = new URL(SERVER_URL+methodName);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setUseCaches(false);
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            urlConnection.setRequestProperty("Content-Type","application/json");
            urlConnection.setRequestProperty("x-access-token","");
            urlConnection.connect();
            OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
            out.write(vi.toString());
            out.close();

            int HttpResult =urlConnection.getResponseCode();
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    appServerResponse.setMessage(line);
                    appServerResponse.setHttpStatusCode(200);
                }
                br.close();
            }else{
                System.out.println(urlConnection.getResponseMessage());
            }
        }catch(MalformedURLException mue){
            appServerResponse.setHttpStatusCode(404);
            appServerResponse.setMessage(mue.getMessage());
            Log.d(TAG, "sendJSONToServer: " +mue.getMessage());
        }catch(IOException ioe){
            appServerResponse.setHttpStatusCode(404);
            appServerResponse.setMessage(ioe.getMessage());
            Log.d(TAG, "sendJSONToServer: " +ioe.getMessage());
        }
        return appServerResponse;
    }

}
