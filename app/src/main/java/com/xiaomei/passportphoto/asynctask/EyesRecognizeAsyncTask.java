package com.xiaomei.passportphoto.asynctask;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.xiaomei.passportphoto.logic.PhotoApp;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class EyesRecognizeAsyncTask extends AsyncTask<Bitmap, Void, org.opencv.core.Point[]> {
    private ProgressDialog dialog;
    private Context context;


    public EyesRecognizeAsyncTask(Context context) {
        dialog = new ProgressDialog(context);
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage("识别中");
        dialog.setCancelable(false);
        dialog.show();
        Log.d("Today", "Show progress");
    }

    @Override
    protected org.opencv.core.Point[] doInBackground(Bitmap... bitmaps) {
        Point[] points = new Point[2];
        Mat myImageMat = new Mat();
        MatOfRect eyes = new MatOfRect();
        Utils.bitmapToMat(bitmaps[0], myImageMat);
        Imgproc.cvtColor(myImageMat, myImageMat, Imgproc.COLOR_RGBA2GRAY);
        PhotoApp.mCascadeClassifier.detectMultiScale(myImageMat, eyes, 1.1, 15, 10, new Size(5, 5), new Size());
        org.opencv.core.Rect[] eyesArray = eyes.toArray();

        if (eyesArray.length == 2) {
            Log.d("Today", "eyes.length = 2");
            if(eyesArray[0].tl().x < eyesArray[1].tl().x) {
                points[0] = new Point((eyesArray[0].tl().x + eyesArray[0].br().x) / 2, (eyesArray[0].tl().y + eyesArray[0].br().y) / 2);
                points[1] = new Point((eyesArray[1].tl().x + eyesArray[1].br().x) / 2, (eyesArray[1].tl().y + eyesArray[1].br().y) / 2);
            }else{
                points[1] = new Point((eyesArray[0].tl().x + eyesArray[0].br().x) / 2, (eyesArray[0].tl().y + eyesArray[0].br().y) / 2);
                points[0] = new Point((eyesArray[1].tl().x + eyesArray[1].br().x) / 2, (eyesArray[1].tl().y + eyesArray[1].br().y) / 2);
            }
        } else {
            points[0] = new org.opencv.core.Point(-1,-1);
            points[1] = new org.opencv.core.Point(-1, -1);
        }
        return points;
    }

    @Override
    protected void onPostExecute(Point[] points) {
        super.onPostExecute(points);
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
