package com.example.sushrut.recipedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sushrut.recipedemo.HelperClasses.RecipeListAdapter;
import com.example.sushrut.recipedemo.HelperClasses.RecipeService;
import com.example.sushrut.recipedemo.Models.RecipeModel;
import com.example.sushrut.recipedemo.NetworkCommunications.InternetDataManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class RecipeCall extends AppCompatActivity {
    private static final String TAG ="RecipeSearch";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_call);

        FloatingActionButton searchRecipeBtn = (FloatingActionButton) findViewById(R.id.searchRecipeBtn);
        searchRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText recipeSearchTxt = (EditText)findViewById(R.id.recipeSearchTxt);
                RecipeService rs = new RecipeService(new
                        InternetDataManager(new BuildConfig(),getApplicationContext()));
                try
                {
                    getJSONFromRecipeApi(recipeSearchTxt.getText().toString(),rs.getFindByRecipeNameURL(),rs);
                }catch (JSONException je){
                    Log.d(TAG, je.getMessage());
                }

            }
        });
    }

    private void getJSONFromRecipeApi(String params, String url, RecipeService rs) throws JSONException{
        final List<RecipeModel> recipes = new ArrayList<RecipeModel>();
        final RecipeListAdapter rla = new RecipeListAdapter(getApplicationContext(),
                R.layout.listitem,R.id.recipeNameTxt,recipes);
        recipes.clear();
        rla.notifyDataSetChanged();
        params = params.trim();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
// Request a string response from the provided URL.
        final JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response){
                    try{
                        JSONArray resultArray = response.getJSONArray("results");
                        System.out.println("Entered onResponse");
                        // Display the first 500 characters of the response string.
                        //recipeTxt.setText("Response is: "+ response.substring(0,500));

                            for(int i=0;i<resultArray.length();i++) {
                                // Get current json object
                                JSONObject recipe = resultArray.getJSONObject(i);
                                int id = Integer.parseInt(recipe.getString("id"));
                                String title = recipe.getString("title");
                                String img = "https://spoonacular.com/recipeImages/"+recipe.getString("imageUrls");
                                Log.d(TAG, "onResponse: Image Got url" + img);
                                String cookingTime = recipe.getString("readyInMinutes");
                                Drawable dimg = new BitmapDrawable(img);
                                RecipeModel rm = new RecipeModel(id,title,cookingTime);
                                rm.setRecipeImage(dimg);
                                recipes.add(rm);
                            }
                        //new ArrayAdapter<String>(this, R.layout.a_layout_file,
                          //      R.id.the_id_of_a_textview_from_the_layout, this.file)

                            ListView recipeListView = (ListView) findViewById(R.id.recipeListView);
                            recipeListView.setAdapter(rla);
                                //recipeTxt.setText(obj.getJSONArray("results").getJSONObject(0).getString("title"));
                        }
                    catch(JSONException ex){
                           Log.d(TAG, "onResponse: " +ex.getMessage());
                    }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error" +error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                //Map<String,String> params =  super.getHeaders();
                Map<String,String> params =  new HashMap<>();
                if(params==null)params = new HashMap<>();
                params.put("X-Mashape-Authorization",BuildConfig.RECIPE_API_KEY );
                //..add other headers
                return params;
            }
        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private static Drawable drawableFromUrl(String url) throws IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(x);
    }
}
