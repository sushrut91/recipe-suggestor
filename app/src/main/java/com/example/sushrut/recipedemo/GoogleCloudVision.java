package com.example.sushrut.recipedemo;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by Sushrut on 8/6/2017.
 */

public class GoogleCloudVision {
    public static final String CLOUD_VISION_API_KEY = "####";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private String packageName = null;
    private PackageManager packageManager = null;
    private String activitySimpleName = null;
    //Responses contain details from Cloud Vision
    private List<AnnotateImageResponse> responses = null;
    private HashMap<String,Float> recoginizedObjects = null;

    public GoogleCloudVision(String packageName, PackageManager packageManager, String activitySimpleName){
        this.packageName = packageName;
        this.packageManager = packageManager;
        this.activitySimpleName = activitySimpleName;
    }

    public List<AnnotateImageResponse> getResponsesList(){
        return this.responses;
    }
    public void callCloudVision(final Bitmap bitmap) throws IOException {
        // Do the real work in an async task, because we need to use the network anyway
        AsyncTask<Object, Void, HashMap<String, Float>> execute = new AsyncTask<Object, Void, HashMap<String, Float>>() {
            @Override
            protected HashMap<String, Float> doInBackground(Object... params) {
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
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
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
                    /*for (AnnotateImageResponse res : responses) {

                        // For full list of available annotations, see http://g.co/cloud/vision/docs
                        DominantColorsAnnotation colors = res.getImagePropertiesAnnotation().getDominantColors();
                        for (ColorInfo color : colors.getColors()) {
                            out.printf(
                                    "fraction: %f\nr: %f, g: %f, b: %f\n",
                                    color.getPixelFraction(),
                                    color.getColor().getRed(),
                                    color.getColor().getGreen(),
                                    color.getColor().getBlue());

                        }
                    }*/
                    return GetRecoginzedObjects(response);
                } catch (GoogleJsonResponseException e) {
                    Log.d(activitySimpleName, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    Log.d(activitySimpleName, "failed to make API request because of other IOException " +
                            e.getMessage());
                }
                return null;
            }

            /*protected void onPostExecute(String result) {
                //searchResultTxt.setText(result);
                return result;
            }*/
        }.execute();
    }
    private HashMap<String,Float> GetRecoginzedObjects(BatchAnnotateImagesResponse response) {
        recoginizedObjects = new HashMap<String,Float>();
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
