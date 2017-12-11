package com.example.sushrut.recipedemo.HelperClasses;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sushrut.recipedemo.Models.RecipeModel;
import com.example.sushrut.recipedemo.R;

import java.net.URI;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by Sushrut on 8/19/2017.
 */

public class RecipeListAdapter extends ArrayAdapter<RecipeModel> {
    private List<RecipeModel> recipes = null;
    public RecipeListAdapter(Context context, int resource, int resourceTxt, List<RecipeModel> objects){
        super(context,resource,resourceTxt,objects);
        recipes = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem,parent,false);
            RecipeModel recipe = recipes.get(position);

            TextView nameText = (TextView)convertView.findViewById(R.id.recipeNameTxt);
            nameText.setText(recipe.getRecipeTitle().toString());

            ImageView recipeImgView = (ImageView)convertView.findViewById(R.id.recipeImg);
            new ImageLoadTask(recipe.getRecipeImage(), recipeImgView).execute();
            //recipeImgView.setImageURI();
            //Drawable recipeImgDrawable = recipe.getRecipeImage();
            //recipeImgView.setImageDrawable(recipeImgDrawable); recipeImgView.setu
        }

        return super.getView(position, convertView, parent);

    }
}
