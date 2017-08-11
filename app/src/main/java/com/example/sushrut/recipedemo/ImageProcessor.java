package com.example.sushrut.recipedemo;
import android.graphics.Bitmap;
import android.os.Debug;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sushrut on 8/7/2017.
 */

public class ImageProcessor {
    public ImageProcessor() {
    }

    public void GetContourMatPointList(String filePath){
        Mat image = ApplyImageFilters(filePath);
        Mat tempImage = image.clone();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Imgproc.findContours(tempImage,contours,tempImage,Imgproc.RETR_EXTERNAL,Imgproc.CHAIN_APPROX_SIMPLE);
        System.out.println("Contour size:" +contours.size());
        //return contours;
    }

    private Mat ApplyImageFilters(String filePath){
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
