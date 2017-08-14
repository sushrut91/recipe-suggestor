package com.example.sushrut.recipedemo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
    private Context context = null;
    private String filePath = null;
    private Uri imageUri;
    private Bitmap capturedImage = null;

    public ImageProcessor(Context context ,Uri imageUri) throws URISyntaxException {
        this.context = context;
        this.imageUri = imageUri;
        this.filePath = CommonUtils.getFilePathFromURI(context,imageUri);
    }
    public ImageProcessor(Context context,Uri imageUri ,Bitmap capturedImage) throws URISyntaxException {
        this.context = context;
        this.imageUri = imageUri;
        this.filePath = CommonUtils.getFilePathFromURI(context,imageUri);
        this.capturedImage = CommonUtils.CompressBitmap(capturedImage);
    }
    public String DetectShapes(){
        String detectedShape = null;
        List<MatOfPoint> contourList = GetContourMatPointList();
        List<MatOfPoint2f> contourList2f = new ArrayList<MatOfPoint2f>();
        if(contourList != null){
            for(MatOfPoint c : contourList)
            {
                MatOfPoint2f point = new MatOfPoint2f(c.toArray());
                contourList2f.add(point);
            }
            for(MatOfPoint2f mop :contourList2f ){
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
        CommonUtils.DeleteCameraImage(context,imageUri);
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
        Mat image = Imgcodecs.imread(filePath, Imgcodecs.IMREAD_COLOR);
        int width = image.width();
        int height = image.height();
        if (width > 0 && height > 0 && !image.empty()) {
            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
            Imgproc.GaussianBlur(image, image, new Size(5, 5), 0);
            Imgproc.threshold(image, image, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
        }
        return image;
    }

}
