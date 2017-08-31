package com.example.sushrut.recipedemo;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.sushrut.recipedemo.HelperClasses.VisualIngredientDirector;
import com.example.sushrut.recipedemo.Models.VisualIngredientViewModel;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import static com.example.sushrut.recipedemo.ImageProcessor.scaleBitmapDown;

public class IngredientLibraryActivity extends AppCompatActivity {
    private static final String TAG=IngredientLibraryActivity.class.getSimpleName();
    private Uri imageUri = null;
    private Bitmap bmpImg = null;
    private static final int PICK_IMAGE_ACTIVITY = 1;
    private static final int CAMERA_ACTIVITY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_library);

        Button cameraBtn = (Button)findViewById(R.id.cameraBtn);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCamera();
            }
        });

        Button pickImgBtn = (Button)findViewById(R.id.pickImgBtn);
        pickImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select a photo"),PICK_IMAGE_ACTIVITY);
            }
        });


        Button saveBtn = (Button)findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ImageView img = (ImageView)findViewById(R.id.ingredientImgView);
                    if(img.getDrawable() != null && imageUri != null){
                        VisualIngredientViewModel vivm = new VisualIngredientViewModel(bmpImg, getPackageName(),
                                getPackageManager(), TAG, imageUri, getApplicationContext(),getContentResolver());
                        VisualIngredientDirector vid = new VisualIngredientDirector(vivm,getApplicationContext());
                        VisualIngredient vi = vid.createVisualIngredient(vivm);
                        //vid.sendVisualIngredient(vi);

                    }else{
                        Snackbar.make(v,"Please pick/capture an image and retry",Snackbar.LENGTH_SHORT);
                    }

                }catch (IOException io){
                    Log.d(TAG, "onClick: " + io.getMessage());
                }
                catch (ExecutionException ee){
                    Log.d(TAG, "onClick: " + ee.getMessage());
                }
                catch (InterruptedException ie){
                    Log.d(TAG, "onClick: " + ie.getMessage());
                }
                catch (URISyntaxException use){
                    Log.d(TAG, "onClick: " +use.getMessage());
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView ingredientImgView = (ImageView) findViewById(R.id.ingredientImgView);
        if (requestCode == PICK_IMAGE_ACTIVITY  && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try{
                bmpImg =(Bitmap)data.getExtras().get("data");
                bmpImg = scaleBitmapDown(MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri), 1200);
                if (PermissionUtils.requestPermission(
                        this,
                        PICK_IMAGE_ACTIVITY,
                        Manifest.permission.READ_EXTERNAL_STORAGE) &&
                        PermissionUtils.requestPermission(
                                this,
                                PICK_IMAGE_ACTIVITY,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ingredientImgView.setImageBitmap(bmpImg);
                }
            }catch(Exception se){
                Log.d(TAG, se.getMessage());
            }
        }
        else if ( requestCode == CAMERA_ACTIVITY && resultCode == RESULT_OK && data != null){
            //uploadImageFromCamera(data);
            imageUri = data.getData();
            bmpImg = (Bitmap) data.getExtras().get("data");
            try{
                bmpImg = scaleBitmapDown(MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri), 1200);
                if (PermissionUtils.requestPermission(
                        this,
                        CAMERA_ACTIVITY,
                        Manifest.permission.READ_EXTERNAL_STORAGE) &&
                        PermissionUtils.requestPermission(
                                this,
                                CAMERA_ACTIVITY,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    //Bitmap capturedImg = (Bitmap)data.getExtras().get("data");
                    ingredientImgView.setImageBitmap(bmpImg);
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
