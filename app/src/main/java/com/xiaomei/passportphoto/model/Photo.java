package com.xiaomei.passportphoto.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.xiaomei.passportphoto.utils.BitmapUtils;

import java.io.Serializable;

public class Photo implements Serializable {
    public String mPID;
    public String mThumbnailPath;
    public String mPostPath;
    public int mSpecType;
    public int mBackGround;
    public PhotoSpec mSpec;
    public String mOrderNo;
    public float mCost;
    public boolean mPaid;
    public boolean isLocal;
    public String mNetForUpload_PhotoThumbnail;
    public String mNetForMatting_PhotoOrigin;
    public String mNetForUpload_PhotoPost;
    public byte[] mNetForMatting_PhotoMat;
    public Photo(){

    }

    public Photo(String pid, int specType){
        mCost = 1;
        mPID = pid;
        mOrderNo = pid;
        mPaid = false;
        mSpecType = specType;
    }

    public void setmPaid(boolean paid){
        mPaid = paid;
    }

    public void setmBackGround(int bg){
        mBackGround = bg;
    }

    public String getSpecString(){
        String spec="",bg="";
        switch (mSpecType){
            case 0:
                spec="一寸";
                break;
            case 1:
                spec="小一寸";
                break;
            case 2:
                spec="二寸";
                break;
            case 3:
                spec="小二寸";
                break;
            case 4:
                spec="大一寸";
                break;
            case 5:
                spec="其他";
                break;
            default:
                break;
        }
        switch (mBackGround){
            case 0:
                bg="红底";
                break;
            case 1:
                bg="白底";
                break;
            case 2:
                bg="蓝底";
                break;
            default:
                break;
        }
        return spec+"("+bg+")";
    }

    public String fixUrl(){
        return null;
    }

    @Override
    public String toString() {
        return mPID;
    }

}
