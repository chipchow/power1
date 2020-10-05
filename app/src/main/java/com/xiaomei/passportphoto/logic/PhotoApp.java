package com.xiaomei.passportphoto.logic;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.xiaomei.passportphoto.utils.MyConstant;

import org.opencv.android.OpenCVLoader;
import org.opencv.objdetect.CascadeClassifier;

import static com.xiaomei.passportphoto.utils.MyConstant.TAG;

public class PhotoApp extends Application {
    private static Context mContext;

    static {
        if (OpenCVLoader.initDebug()) {
            Log.d(TAG, "OpenCV Succesfully loaded!");
        } else {
            Log.d(TAG, "OpenCV not loaded");
        }
    }

    public static CascadeClassifier mCascadeClassifier;
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        mCascadeClassifier = MyConstant.Cascade_Setting_Eye(this);
    }

    public static Context getAppContext(){
        return mContext;
    }
}
