package com.example.sushrut.recipedemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.sushrut.recipedemo.HelperClasses.VisualIngredientDirector;
import com.example.sushrut.recipedemo.Models.VisualIngredientViewModel;

import org.opencv.android.OpenCVLoader;


public class MainActivity extends AppCompatActivity {
    public static final String TAG=MainActivity.class.getSimpleName();
    private static final int PICK_IMAGE_ACTIVITY = 1;
    private static final int CAMERA_ACTIVITY = 2;

    static {
        if(!OpenCVLoader.initDebug()){
            Log.d(TAG, "OpenCV not loaded");
        } else {
            Log.d(TAG, "OpenCV loaded");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton choiceFab = (FloatingActionButton)findViewById(R.id.choiceActionBtn);
        choiceFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select a photo"),PICK_IMAGE_ACTIVITY);
            }
        });

        FloatingActionButton cameraFab = (FloatingActionButton)findViewById(R.id.cameraActionButton);
        cameraFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startCamera();
            }
        });

        FloatingActionButton recipeBrowseFab = (FloatingActionButton)findViewById(R.id.recipeBrowseBtn);
        recipeBrowseFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent recipeIntent = new Intent(MainActivity.this,RecipeCall.class);
                MainActivity.this.startActivity(recipeIntent);
            }
        });

        FloatingActionButton addToLibFab = (FloatingActionButton)findViewById(R.id.addToLibraryBtn);
        addToLibFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent ingredientIntent = new Intent(MainActivity.this,IngredientLibraryActivity.class);
                MainActivity.this.startActivity(ingredientIntent);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri tempUri = null;
        Bitmap capturedImg = null;
        try{
            if (requestCode == PICK_IMAGE_ACTIVITY  && resultCode == RESULT_OK && data != null) {
                if (PermissionUtils.requestPermission(
                        this,
                        PICK_IMAGE_ACTIVITY,
                        Manifest.permission.READ_EXTERNAL_STORAGE) &&
                        PermissionUtils.requestPermission(
                                this,
                                PICK_IMAGE_ACTIVITY,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    tempUri = data.getData();
                    capturedImg = (Bitmap)data.getExtras().get("data");
                }
            }
            else if ( requestCode == CAMERA_ACTIVITY && resultCode == RESULT_OK && data != null){
                tempUri = data.getData();
                if (PermissionUtils.requestPermission(
                        this,
                        CAMERA_ACTIVITY,
                        Manifest.permission.READ_EXTERNAL_STORAGE) &&
                        PermissionUtils.requestPermission(
                                this,
                                CAMERA_ACTIVITY,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    capturedImg = (Bitmap)data.getExtras().get("data");
                }
            }

            //Create and send visual ingredient
            VisualIngredientViewModel vivm = new VisualIngredientViewModel(capturedImg, getPackageName(),
                    getPackageManager(), TAG, tempUri, getApplicationContext(),getContentResolver());
            VisualIngredientDirector vid = new VisualIngredientDirector(vivm,getApplicationContext());
            VisualIngredient vi = vid.createVisualIngredient(vivm);
            vid.sendVisualIngredient(vi);

        }catch(Exception ex){
            Log.d(TAG, "Error: " + ex.getMessage());
        }
    }

    private void startCamera()
    {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_ACTIVITY,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_ACTIVITY);
        }
    }
}
