package com.xiaomei.passportphoto.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xiaomei.passportphoto.logic.ImageRequest;

public class UrlDrawable extends Drawable implements Runnable{

    String mUrl;
    Bitmap mBitmap,mDefaultBitmap;
    int mState=0;
    Handler mHandler;
    ImageRequest mIR;
    Paint mPaint;

    public static UrlDrawable getDrawable(String url){
        return new UrlDrawable(url);
    }
    public UrlDrawable(String url){
        mUrl = url;
        mHandler = new Handler();
        mIR = new ImageRequest(url,mHandler,this);
        mIR.doRequest();
        mPaint = new Paint();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if(mState == 0){
            canvas.drawColor(Color.CYAN);
        }else{
            canvas.drawBitmap(mBitmap,0,0,mPaint);
        }
    }

    @Override
    public void setAlpha(int i) {

    }


    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    public void run(){
        mBitmap = mIR.mBitmap;
        mState = 1;
        invalidateSelf();
    }
}
