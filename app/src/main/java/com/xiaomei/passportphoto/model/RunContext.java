package com.xiaomei.passportphoto.model;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;

import com.xiaomei.passportphoto.utils.BitmapUtils;

public class RunContext {
    public String mVer;
    public String mModel;
    public int mOS;
    public User mUser;
    public String mSession;
    public long mUpdateSessionTime;
    public Bitmap mBitmap;
    public PhotoSpec mSpec;
    private static RunContext mInstance = null;
    public RunContext(){
        mModel = Build.MODEL;
        mVer = BitmapUtils.getAPKVer();
        mOS = Build.VERSION.SDK_INT;
    }

    public static RunContext getInstance(){
        if(mInstance == null){
            mInstance = new RunContext();
        }
        return mInstance;
    }

    public void setUser(User user){
        mUser = user;
    }

    public User getUser(){
        return mUser;
    }
    public void setSession(String session){
        mSession = session;
        mUpdateSessionTime = System.currentTimeMillis();
    }
}
