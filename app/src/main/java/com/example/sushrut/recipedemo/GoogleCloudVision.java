package com.example.sushrut.recipedemo;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import com.example.sushrut.recipedemo.Models.GoogleImage;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Sushrut on 8/6/2017.
 */

public class GoogleCloudVision extends AsyncTask<Void, Void, GoogleImage>{

    private static String CLOUD_VISION_API_KEY = null;
    private static final String TAG = "GoogleCloudVision";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private String packageName = null;
    private PackageManager packageManager = null;
    private String activitySimpleName = null;
    private Bitmap bmp = null;
    //Responses contain details from Cloud Vision
    private List<AnnotateImageResponse> responses = null;

    public GoogleCloudVision(String packageName, PackageManager packageManager, String activitySimpleName, Bitmap bmp){
        this.packageName = packageName;
        this.packageManager = packageManager;
        this.activitySimpleName = activitySimpleName;
        this.bmp = bmp;
        this.CLOUD_VISION_API_KEY = BuildConfig.GOOGLE_CV_KEY;
    }

    public GoogleImage uploadImageToGoogleCloud(Uri uri, ContentResolver contentResolver) throws IOException {
        if (uri != null) {
                // scale the image to save on bandwidth
                 bmp = ImageProcessor.scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(contentResolver, uri),
                                1200);

                return getGoogleVisualIngredients(bmp);
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            return  null;
        }
    }
    // Get Visual Ingredient flourished with google data
    private GoogleImage getGoogleVisualIngredients(Bitmap bmp) throws IOException
    {
        Log.d(TAG,"Getting visual ingridients.");
        return doInBackground();
    }
    //public List<AnnotateImageResponse> getResponsesList(){
     //   return this.responses;
    //}

    @Override
    protected GoogleImage doInBackground(Void ... params) {
        GoogleImage gi = new GoogleImage();
        try {
            HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            VisionRequestInitializer requestInitializer =
                    new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                        /**
                         * We override this so we can inject important identifying fields into the HTTP
                         * headers. This enables use of a restricted cloud platform API key.
                         */
                        @Override
                        protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                                throws IOException {
                            super.initializeVisionRequest(visionRequest);

                            visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                            String sig = PackageManagerUtils.getSignature(packageManager, packageName);

                            visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                        }
                    };

            Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
            builder.setVisionRequestInitializer(requestInitializer);

            Vision vision = builder.build();

            BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                    new BatchAnnotateImagesRequest();
            batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                // Add the image
                Image base64EncodedImage = new Image();
                // Convert the bitmap to a JPEG
                // Just in case it's a format that Android understands but Cloud Vision
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                // Base64 encode the JPEG
                base64EncodedImage.encodeContent(imageBytes);
                annotateImageRequest.setImage(base64EncodedImage);

                // add the features we want
                annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                    Feature labelDetection = new Feature();
                    Feature textDetection = new Feature();
                    labelDetection.setType("LABEL_DETECTION");
                    textDetection.setType("TEXT_DETECTION");
                    labelDetection.setMaxResults(10);
                    add(labelDetection);
                    add(textDetection);
                }});

                // Add the list of one thing to the request
                add(annotateImageRequest);
            }});

            Vision.Images.Annotate annotateRequest =
                    vision.images().annotate(batchAnnotateImagesRequest);
            // Due to a bug: requests to Vision API containing large images fail when GZipped.
            annotateRequest.setDisableGZipContent(true);
            Log.d(activitySimpleName, "created Cloud Vision request object, sending request");

            BatchAnnotateImagesResponse response = annotateRequest.execute();
            //return convertResponseToString(response);
            //Added to check. Uncomment the above line to make things work
            //List<AnnotateImageResponse> responses = response.getResponses();
            responses = response.getResponses();

                    for (AnnotateImageResponse res : responses) {
                        // For full list of available annotations, see http://g.co/cloud/vision/docs
                        DominantColorsAnnotation colors = res.getImagePropertiesAnnotation().getDominantColors();
                        for (ColorInfo color : colors.getColors()) {
                            gi.setRedVal(color.getColor().getRed());
                            gi.setGreenVal(color.getColor().getGreen());
                            gi.setBlueVal(color.getColor().getBlue());
                            gi.setDominantColor(color.getColor().toString());
                        }
                    }

             gi.setCloudVisionSuggestions(getRecognizedObjects(response));

        } catch (GoogleJsonResponseException e) {
            Log.d(activitySimpleName, "failed to make API request because " + e.getContent());
        } catch (IOException e) {
            Log.d(activitySimpleName, "failed to make API request because of other IOException " +
                    e.getMessage());
        }

        //Create customized object
        return gi;
    }

    private HashMap<String,Float> getRecognizedObjects(BatchAnnotateImagesResponse response) {
        HashMap<String,Float> recoginizedObjects = new HashMap<String,Float>();
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                //message += String.format(Locale.US, "%.3f: %s", label.getScore(), label.getDescription());
                //message += "\n";
                recoginizedObjects.put(label.getDescription(),label.getScore());
            }
        }
        return recoginizedObjects;
    }
}
