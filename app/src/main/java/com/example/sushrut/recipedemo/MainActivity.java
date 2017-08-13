package com.example.sushrut.recipedemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.AnnotateImageResponse;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.ColorInfo;
import com.google.api.services.vision.v1.model.DominantColorsAnnotation;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import org.opencv.android.OpenCVLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.lang.System.out;


public class MainActivity extends AppCompatActivity {
    public static final String TAG=MainActivity.class.getSimpleName();

    public static final int HOME_SCREEN_ACTIVITY = 1;
    public static final int CAMERA_ACTIVITY = 2;
    public static final int RECIPE_SEARCH_ACTIVITY = 3;

    private ImageView mainImageView;
    private TextView searchResultTxt;


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

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.choiceActionBtn);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select a photo"),HOME_SCREEN_ACTIVITY);
            }
        });

        FloatingActionButton cameraFab = (FloatingActionButton)findViewById(R.id.cameraActionButton);
        cameraFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startCamera();
                mainImageView = (ImageView)findViewById(R.id.main_image);
                searchResultTxt = (TextView)findViewById(R.id.searchResults);
            }
        });

        FloatingActionButton recipeSearchBtn = (FloatingActionButton)findViewById(R.id.recipeSearchBtn);
        recipeSearchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent recipeIntent = new Intent(MainActivity.this,RecipeCall.class);
                MainActivity.this.startActivity(recipeIntent);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == HOME_SCREEN_ACTIVITY  && resultCode == RESULT_OK && data != null) {
            Uri tempUri = data.getData();
            try{
                if (PermissionUtils.requestPermission(
                        this,
                        HOME_SCREEN_ACTIVITY,
                        Manifest.permission.READ_EXTERNAL_STORAGE) &&
                        PermissionUtils.requestPermission(
                                this,
                                HOME_SCREEN_ACTIVITY,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    ImageProcessor ip = new ImageProcessor();
                    String filePath = CommonUtils.getFilePathFromURI(getApplicationContext(),tempUri);
                    ip.DetectShapes(filePath);
                    Toast.makeText(this,"Shapes detected",Toast.LENGTH_LONG);
                    ip.uploadImageToCloud(tempUri,getContentResolver(),getPackageName(),getPackageManager());
                }
            }catch(Exception se){
                Log.d(TAG, se.getMessage());
            }
        }
        else if ( requestCode == CAMERA_ACTIVITY && resultCode == RESULT_OK && data != null){
            //uploadImageFromCamera(data);
            Uri tempUri = data.getData();
            try{
                if (PermissionUtils.requestPermission(
                        this,
                        CAMERA_ACTIVITY,
                        Manifest.permission.READ_EXTERNAL_STORAGE) &&
                        PermissionUtils.requestPermission(
                                this,
                                CAMERA_ACTIVITY,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    ImageProcessor ip = new ImageProcessor();
                    Bitmap capturedImg = (Bitmap)data.getExtras().get("data");
                    CommonUtils.CompressBitmap(capturedImg);
                    String filePath = CommonUtils.getFilePathFromURI(getApplicationContext(),tempUri);
                    String shape = ip.DetectShapes(filePath);
                    Toast.makeText(this,"Shape detected" + shape,Toast.LENGTH_LONG).show();
                    CommonUtils.DeleteCameraImage(getApplicationContext(),tempUri);
                }
            }catch(Exception se){
                Log.d(TAG, se.getMessage());
            }
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
