package com.example.sushrut.recipedemo;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.sushrut.recipedemo.HelperClasses.RecipeListAdapter;
import com.example.sushrut.recipedemo.HelperClasses.RecipeService;
import com.example.sushrut.recipedemo.Models.RecipeModel;
import com.example.sushrut.recipedemo.NetworkCommunications.InternetDataManager;

import org.json.JSONException;

import java.util.List;


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
                        InternetDataManager(new BuildConfig()));

                ListView recipeListView = (ListView) findViewById(R.id.recipeListView);
                try
                {
                    List<RecipeModel> recipes = new RecipeService(new InternetDataManager(new BuildConfig()))
                            .getRecipesByName(recipeSearchTxt.getText().toString());
                    RecipeListAdapter rla = new RecipeListAdapter(getApplicationContext(),R.layout.listitem,recipes);
                }catch (JSONException je){
                    Log.d(TAG, je.getMessage());
                }

            }
        });
    }
}
