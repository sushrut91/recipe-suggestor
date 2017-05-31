package com.example.sushrut.recipedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RecipeCall extends AppCompatActivity {
    public static final String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients";
    private EditText ingridientTxt;
    private Button callAPIBtn;
    private TextView recipeTxt;
    private static final String RECIPE_API_KEY="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_call);
        ingridientTxt = (EditText)findViewById(R.id.ingridientTxt);
        callAPIBtn =(Button)findViewById(R.id.callApiBtn);
        recipeTxt = (TextView)findViewById(R.id.recipeTxtView);

        callAPIBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(RecipeCall.this);
                String url ="https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?"+recipeTxt.getText();

// Request a string response from the provided URL.
                final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Display the first 500 characters of the response string.
                                //recipeTxt.setText("Response is: "+ response.substring(0,500));
                                try{
                                    JSONObject obj = new JSONObject(response);
                                    recipeTxt.setText(obj.getJSONArray("results").getJSONObject(0).getString("title"));
                                }
                                catch(JSONException ex){
                                    recipeTxt.setText(ex.getMessage());
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        recipeTxt.setText("That didn't work!");
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
            }
        });
    }
}
