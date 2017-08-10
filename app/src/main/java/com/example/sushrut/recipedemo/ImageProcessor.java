package com.example.sushrut.recipedemo;
import android.graphics.Bitmap;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.opencv.android.Utils;
/**
 * Created by Sushrut on 8/7/2017.
 */

public class ImageProcessor {
    public ImageProcessor() {
    }

    public Bitmap ApplyImageFilters(String filePath) throws InterruptedException {
        Mat image = Imgcodecs.imread(filePath, Imgcodecs.IMREAD_COLOR);
        int width = image.width();
        int height = image.height();
        Bitmap bmp = null;
        if (width > 0 && height > 0 && !image.empty()) {
            bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2GRAY);
            Imgproc.GaussianBlur(image, image, new Size(5, 5), 0);
            Imgproc.threshold(image, image, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
            Utils.matToBitmap(image, bmp);

        }
        return bmp;
    }
}
