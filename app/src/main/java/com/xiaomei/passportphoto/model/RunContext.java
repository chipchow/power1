package com.xiaomei.passportphoto.model;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;

import com.xiaomei.passportphoto.logic.PhotoApp;
import com.xiaomei.passportphoto.utils.BitmapUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RunContext {
    public String mVer;
    public String mModel;
    public int mOS;
    public User mUser;
    public String mSession;
    public int mCost;
    public long mUpdateSessionTime;
    public Bitmap mBitmap;
    public PhotoSpec mSpec;
    public int mSpecType;
    public int mBackground;
    private static RunContext mInstance = null;
    public RunContext(){
        mModel = Build.MODEL;
        mVer = BitmapUtils.getAPKVer();
        mOS = Build.VERSION.SDK_INT;
        restore(PhotoApp.getAppContext());
        if(mUser == null){
            mUser = new User();
            mUser.mUserID="123456789";
        }
        mBackground = 1;
        mSpecType = 0;
    }

    public static RunContext getInstance(){
        if(mInstance == null){
            mInstance = new RunContext();
        }
        return mInstance;
    }

    public User getUser(){
        return mUser;
    }
    public void setSession(String session){
        mSession = session;
        mUpdateSessionTime = System.currentTimeMillis();
    }

    public void save(Context context){
        File file = new File(context.getFilesDir()+File.separator+"user");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(fos);
                oos.writeObject(mUser);            //写入对象
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    oos.close();
                } catch (IOException e) {
                    System.out.println("oos关闭失败："+e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("找不到文件："+e.getMessage());
        } finally{
            try {
                fos.close();
            } catch (IOException e) {
                System.out.println("fos关闭失败："+e.getMessage());
            }
        }

    }

    public void restore(Context context) {
        File file = new File(context.getFilesDir() + File.separator + "user");
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                ObjectInputStream ois = null;
                try {
                    ois = new ObjectInputStream(fis);
                    try {
                        mUser = (User) ois.readObject();   //读出对象
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        ois.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
