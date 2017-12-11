package com.example.sushrut.recipedemo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used for image processing , shape detectition and calling Google Cloud Vision
 * Created by Sushrut on 8/7/2017.
 */

public class ImageProcessor {
    private static final String TAG = "ImageProcessor";
    private CameraImage ci = null;

    public ImageProcessor(){}
    public void setCameraImage(CameraImage ci){
        this.ci = ci;
    }
    public String DetectShapes(){
        String detectedShape = null;
        List<MatOfPoint> contourList = GetContourMatPointList();
        ci.setNoOfContours(contourList.size());
        List<MatOfPoint2f> contourList2f = new ArrayList<MatOfPoint2f>();
        if(contourList != null){
            for(MatOfPoint c : contourList)
            {
                MatOfPoint2f point = new MatOfPoint2f(c.toArray());
                contourList2f.add(point);
            }
            for(MatOfPoint2f mop :contourList2f ){
                //set shape vertices count
                ci.setShapeVertices(mop.size().height);
                double arcLen = Imgproc.arcLength(mop,true);
                Imgproc.approxPolyDP(mop,mop,0.04 * arcLen,true);
                //1x4 format
                if(mop.size().height == 3) {
                    detectedShape = "Triangle";
                }else if(mop.size().height == 4){
                    detectedShape = "Rectangle";
                }else if(mop.size().height == 5){
                    detectedShape = "Pentagon";
                }else{
                    detectedShape = "Circle";
                }
            }
        }
        CommonUtils.DeleteCameraImage(ci.getContext(),ci.getImageUri());
        return detectedShape;
    }
    private List<MatOfPoint> GetContourMatPointList(){
        Mat image = ApplyImageFilters();
        Mat tempImage = image.clone();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(tempImage,contours,tempImage,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        System.out.println("Contour size:" +contours.size());
        return contours;
    }

    private Mat ApplyImageFilters(){
        Mat image = Imgcodecs.imread(ci.getFilePath(), Imgcodecs.IMREAD_COLOR);
        int width = image.width();
        int height = image.height();
        if (width > 0 && height > 0 && !image.empty()) {
            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
            Imgproc.GaussianBlur(image, image, new Size(5, 5), 0);
            Imgproc.threshold(image, image, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        }
        return image;
    }

    public String getDominantColorInCameraImg(){
        Bitmap bitmap = ci.getBitmap(); //assign your bitmap here
        int redColors = 0;
        int greenColors = 0;
        int blueColors = 0;

        for (int y = 0; y < bitmap.getHeight(); y++)
        {
            for (int x = 0; x < bitmap.getWidth(); x++)
            {
                int c = bitmap.getPixel(x, y);
                redColors += Color.red(c);
                greenColors += Color.green(c);
                blueColors += Color.blue(c);
            }
        }
        // calculate average of bitmap r,g,b values
        int red = redColors;
        int green = greenColors;
        int blue = blueColors;
        String dominantColor = null;
        ci.setRedVal(red);
        ci.setGreenVal(green);
        ci.setBlueVal(blue);

        if(red > green && red > blue)
            dominantColor = "Red";
        else if (green > red && green > blue)
            dominantColor = "Green";
        else
            dominantColor = "Blue";

        return  dominantColor;
    }

    //Used in Google Cloud API
    public static Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }
}
