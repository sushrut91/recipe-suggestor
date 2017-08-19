package com.example.sushrut.recipedemo;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sushrut.recipedemo.HelperClasses.RecipeService;
import com.example.sushrut.recipedemo.NetworkCommunications.InternetDataManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecipeCall extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_call);

        FloatingActionButton searchRecipeBtn = (FloatingActionButton) findViewById(R.id.searchRecipeBtn);
        searchRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText recipeSearchTxt = (EditText)findViewById(R.id.recipeTxtView);
                RecipeService rs = new RecipeService(new
                        InternetDataManager(new BuildConfig()));

                ListView recipeListView = (ListView) findViewById(R.id.recipeListView);
          
            }
        });
    }
}
